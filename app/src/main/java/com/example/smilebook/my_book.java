package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class my_book extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book);

        Button location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // book_title TextView에서 텍스트 가져오기
                TextView bookTitleTextView = findViewById(R.id.book_title);
                String bookTitle = bookTitleTextView.getText().toString();

                // Intent를 통해 BookLocationActivity로 이동하고 book_title 전달
                Intent intent = new Intent(my_book.this, BookLocationActivity.class);
                intent.putExtra("book_title", bookTitle);
                startActivity(intent);
            }
        });
    }
}