package com.example.smilebook;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class BookListFiction extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //타이틀 안 보이게

        // 카테고리 이름을 인텐트에서 받아와 텍스트뷰에 표시 및 카테고리 값 저장
        String category = getIntent().getStringExtra("category");
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText(category);

//        //스피너 설정 - 나영 (레이아웃 파일에 simple_spinner_item이 없어서 튕기길래 잠깐 주석)
//        String[] items = {"전체", "가나다순", "대출가능순"};
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner spinner = findViewById(R.id.filter);
//        spinner.setAdapter(spinnerAdapter);

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view); //사용할 리사이클러뷰 id(=book_list 내 리사이클러뷰 id)
        gridAdapter = new GridAdapter(new ArrayList<>(), this); //사용할 어댑터
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰랑 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)

        // Retrofit을 사용하여 서버에서 카테고리별 도서 목록을 가져옴
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.9.175:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<GridBookListData>> call = apiService.getBooksByCategory(category); //서버에게 카테고리 전달하여 도서 목록 요청
        call.enqueue(new Callback<List<GridBookListData>>() {
            @Override
            public void onResponse(Call<List<GridBookListData>> call, Response<List<GridBookListData>> response) {
                if (response.isSuccessful()) {
                    List<GridBookListData> bookList = response.body();
                    gridAdapter.setData(bookList);
                } else {
                    Log.e("BookListFiction","서버 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<List<GridBookListData>> call, Throwable t) {
                Log.e("BookListFiction","네트워크 요청 실패");
            }
        });

    }
}
