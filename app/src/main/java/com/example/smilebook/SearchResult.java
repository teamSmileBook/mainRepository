package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;

import java.util.List;

public class SearchResult extends AppCompatActivity {

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

        //메인으로
        Button home_btn = (Button) findViewById(R.id.icons8_smile);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResult.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText("검색 결과");

//        //스피너 설정 - 나영 (레이아웃 파일에 simple_spinner_item이 없어서 튕기길래 잠깐 주석)
//        String[] items = {"전체", "가나다순", "대출가능순"};s
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner spinner = findViewById(R.id.filter);
//        spinner.setAdapter(spinnerAdapter);

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 검색 결과 데이터를 가져옴
        List <GridBookListData> bookList = (List <GridBookListData>) getIntent().getSerializableExtra("bookList");

        // 어댑터 설정
        if (bookList != null) { //검색 결과가 비어있지 않으면 데이터 표시
            Log.d("SearchResult","검색 결과 불러오기 성공");
            gridAdapter = new GridAdapter(bookList, this);
            recyclerView.setAdapter(gridAdapter);
        } else {
            Log.e("SearchResult","검색 결과 불러오기 실패");
            Toast.makeText(SearchResult.this, "검색 결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    //툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}