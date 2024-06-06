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

public class MyBookList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyBookItemAdapter bookItemAdapter;
    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private String memberId;

    private boolean  isAllUserVisible  = true;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book);

        //전체도서 버튼에 클릭리스너 설정
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
    @Override
    protected void onResume() {
        super.onResume();
        fetchBooks(); // onResume()이 호출될 때마다 책 목록을 다시 불러옴
    }

    private void fetchBooks() {
        // Retrofit을 사용하여 서버에서 데이터 가져오기
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
                Log.e("UserBook","네트워크 오류 : "+ t);
            }
        });
    }
}
