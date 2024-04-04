package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class book_list extends AppCompatActivity {
    public book_list() {
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.book_list);

        LinearLayout book_list1 = findViewById(R.id.book_list1);
        LinearLayout book_list2 = findViewById(R.id.book_list2);
        LinearLayout book_list3 = findViewById(R.id.book_list3);
        LinearLayout book_list4 = findViewById(R.id.book_list4);
        Button filter = findViewById(R.id.filter);

        book_list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });
        book_list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });
        book_list3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, book_list_filter.class);
            }
        });




    }
}
