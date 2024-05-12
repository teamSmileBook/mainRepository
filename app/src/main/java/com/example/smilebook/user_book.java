package com.example.smilebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.GridAdapter;

import java.util.ArrayList;

public class user_book extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;

    protected void onCreate(Bundle savedInstanceStace){
        super.onCreate(savedInstanceStace);
        setContentView(R.layout.user_book);

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.user_book_recycler_view); //사용할 리사이클러뷰 id
        gridAdapter = new GridAdapter(new ArrayList<>(), this); //사용할 어댑터
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰랑 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)
    }
}
