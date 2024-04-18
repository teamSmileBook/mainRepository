package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListAll extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        //텍스트뷰 세팅
        TextView category = (TextView) findViewById(R.id.categoryTextView);
        category.setText("전체 도서");

//        //스피너 설정 - 나영 (레이아웃 파일에 simple_spinner_item이 없어서 튕기길래 잠깐 주석)
//        String[] items = {"전체", "가나다순", "대출가능순"};
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner spinner = findViewById(R.id.filter);
//        spinner.setAdapter(spinnerAdapter);


        recyclerView = findViewById(R.id.recycler_view); //사용할 리사이클러뷰 id(=book_list 내 리사이클러뷰 id)
        gridAdapter = new GridAdapter(new ArrayList<>(), this); //사용할 어댑터
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰랑 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)

        // Retrofit을 사용하여 서버에 HTTP 요청을 보냄
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.9.175:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService 인터페이스 구현체 생성
        ApiService apiService = retrofit.create(ApiService.class);

        //전체 도서 정보 가져오기 요청
        Call<List<GridBookListData>> call = apiService.getAllBooks();

        call.enqueue(new Callback<List<GridBookListData>>() {
            @Override
            public void onResponse(Call<List<GridBookListData>> call, Response<List<GridBookListData>> response) {
                if (response.isSuccessful()) {
                    Log.d("BookListAll", "서버 응답 성공");

                    //응답 받은 데이터들을 GridBookListData 형태로 리스트 생성 -> 어댑터에 데이터 전송
                    List<GridBookListData> bookList = response.body();
                    gridAdapter.setData(bookList);
                } else {
                    // 서버 응답에 실패한 경우
                    Log.e("BookListAll", "서버 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<List<GridBookListData>> call, Throwable t) {
                // 네트워크 요청 실패
                Log.e("BookListAll", "네트워크 요청 실패", t);
            }
        });


//        ImageButton bookCover = (ImageButton) findViewById(R.id.bookCover);
//
//        bookCover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(BookListAll.this, BookInfo.class);
//                startActivity(intent);
//            }
//        });
    }
    //하트 누르면 찜으로 변경 - 나영
    public void onHeartClicked(View view) {
        ImageButton heartButton = (ImageButton) view;
        if (heartButton.getBackground().getConstantState().equals
                (getResources().getDrawable(R.drawable.heart).getConstantState())) {
            heartButton.setBackgroundResource(R.drawable.empty_heart);
        } else {
            heartButton.setBackgroundResource(R.drawable.heart);
        }
    }
}