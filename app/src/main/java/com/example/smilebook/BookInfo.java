package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.model.BookDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookInfo extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);

        Button location = findViewById(R.id.location);
        TextView bookInfoTitle = findViewById(R.id.book_title);

        // 인텐트로부터 bookId 전달 받음
        Intent intent = getIntent();
        Long bookId = intent.getLongExtra("bookId", -1L); //기본값:-1, 만약 bookId가 전달되지 않은 경우를 나타냄

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // book_title TextView에서 텍스트 가져오기
                TextView bookTitleTextView = findViewById(R.id.book_title);
                String bookTitle = bookTitleTextView.getText().toString();

                // Intent를 통해 BookLocationActivity로 이동하고 book_title 전달
                Intent intent = new Intent(BookInfo.this, BookLocationActivity.class);
                intent.putExtra("book_title", bookTitle);
                startActivity(intent);
            }
        });

        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService 인터페이스 구현체 생성
        apiService = retrofit.create(ApiService.class);

        // bookId에 해당하는 도서 정보 가져오기 요청 (bookId를 long 타입으로 변환하여 전달)
        Call<BookDTO> call = apiService.getBookById(bookId);

        call.enqueue(new Callback<BookDTO>() {
            @Override
            public void onResponse(Call<BookDTO> call, Response<BookDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookDTO book = response.body();
                    Log.d("BookInfo","서버 응답 성공");

                    String bookTitle = book.getBookTitle(); // 도서 제목 가져오기
                    bookInfoTitle.setText(book.getBookTitle());

                } else {
                    Log.e("BookInfo","서버 응답 실패");
                    Toast.makeText(BookInfo.this, "도서 정보를 불러 오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                Log.e("error", "네트워크 요청 실패", t);
            }
        });
    }
}