package com.example.smilebook;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.BookLocationDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookLocationActivity extends AppCompatActivity {

    private ApiService apiService;
    private CustomImageView imageView;
    private Long bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_location);

        imageView = findViewById(R.id.imageView36);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookId = extras.getLong("bookId");
        }

        // Retrofit 객체 생성
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        Call<BookLocationDTO> call = apiService.getBookLocationById(bookId);
        call.enqueue(new Callback<BookLocationDTO>() {
            @Override
            public void onResponse(Call<BookLocationDTO> call, Response<BookLocationDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookLocationDTO book = response.body();

                    float circleX = book.getXCoordinate();
                    float circleY = book.getYCoordinate();
                    imageView.setCirclePosition(circleX, circleY);

                    // 카테고리와 층 정보를 텍스트뷰에 설정
                    TextView categoryTextView = findViewById(R.id.category);
                    TextView floorTextView = findViewById(R.id.floor);
                    categoryTextView.setText(book.getCategory());
                    floorTextView.setText(book.getFloor());
                }
            }

            @Override
            public void onFailure(Call<BookLocationDTO> call, Throwable t) {
                // 오류 처리
            }
        });

        Button back = findViewById(R.id.back);
        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }
}
