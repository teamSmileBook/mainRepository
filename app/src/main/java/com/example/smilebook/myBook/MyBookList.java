package com.example.smilebook.myBook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.R;
import com.example.smilebook.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 회원이 대출 중이거나 예약 중인 도서를 목록으로 표시
public class MyBookList extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/"; // 서버의 기본 URL
    private RecyclerView recyclerView; // 도서 목록을 표시하는 RecyclerView
    private MyBookItemAdapter bookItemAdapter; // RecyclerView에 데이터를 설정하는 어댑터
    private String memberId; // 현재 사용자의 회원 ID
    private boolean  isAllUserVisible;  // 반납 상태를 저장하는 변수 (true : 전체 도서 목록 표시, false : 반납되지 않은 도서 목록 표시)

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book);

        // 전체 도서 및 반납 미완료 버튼에 대한 클릭 리스너 설정
        Button all_book_btn = findViewById(R.id.all_book_btn);
        Button no_return_btn = findViewById(R.id.no_return_btn);
        View all_book_view = findViewById(R.id.all_book_view);
        View no_return_view = findViewById(R.id.no_return_view);

        all_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllUserVisible) {
                    all_book_view.setVisibility(View.VISIBLE);
                    no_return_view.setVisibility(View.GONE);
                    isAllUserVisible = true;
                }
            }
        });

        //반납 미완료 버튼에 클릭 리스너 설정
        no_return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllUserVisible) {
                    all_book_view.setVisibility(View.GONE);
                    no_return_view.setVisibility(View.VISIBLE);
                    isAllUserVisible = false;
                    no_return_view.setBackgroundResource(R.drawable.user_btn);
                }
            }
        });


        // SharedPreferences를 사용하여 memberId 값을 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        memberId = sharedPreferences.getString("memberId", null);

        // memberId가 null이면 처리
        if (memberId == null) {
            Toast.makeText(this, "회원 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.user_book_recycler_view); //사용할 리사이클러뷰 id(=book_list 내 리사이클러뷰 id)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)
    }

    // 액티비티가 다시 활성화될 때 호출되는 메서드
    @Override
    protected void onResume() {
        super.onResume();
        fetchBooks(); // 사용자의 도서 목록을 다시 불러옴
    }

    // 서버에서 도서 목록을 가져오는 메서드
    private void fetchBooks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<MyBookItemData>> call = apiService.getAllBooksForMember(memberId);

        call.enqueue(new Callback<List<MyBookItemData>>() {
            @Override
            public void onResponse(Call<List<MyBookItemData>> call, Response<List<MyBookItemData>> response) {
                if (response.isSuccessful()) {
                    List<MyBookItemData> bookItems = response.body();
                    Log.d("user_book","BookItemData"+bookItems);
                    if (bookItems != null && !bookItems.isEmpty()) {
                        // 가져온 데이터를 어댑터에 설정하여 리사이클러뷰에 표시
                        ArrayList<MyBookItemData> bookItemsArrayList = new ArrayList<>(bookItems);
                        Log.d("user_book","BookItemData : "+bookItemsArrayList);
                        bookItemAdapter = new MyBookItemAdapter(MyBookList.this, bookItemsArrayList);
                        recyclerView.setAdapter(bookItemAdapter);
                    } else {
                        Toast.makeText(MyBookList.this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyBookList.this, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MyBookItemData>> call, Throwable t) {
                Toast.makeText(MyBookList.this, "네트워크 오류: ", Toast.LENGTH_SHORT).show();
                Log.e("UserBook","네트워크 오류 : " + t);
            }
        });
    }
}
