package com.example.smilebook;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.BookLocationResponse;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookLocationActivity extends AppCompatActivity {

    private TextView floorTextView;
    private TextView categoryTextView;
    private CustomImageView imageView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_location);

        floorTextView = findViewById(R.id.floor);
        categoryTextView = findViewById(R.id.category);
        imageView = findViewById(R.id.imageView36);

        // Retrofit을 사용하여 API 서비스 인스턴스 가져오기
        Retrofit retrofit = RetrofitClient.getInstance();
        apiService = retrofit.create(ApiService.class);

        // Intent로 전달받은 book_title 읽어오기
        String bookTitle = getIntent().getStringExtra("book_title");

        // API 호출하여 데이터 받아오기
        getBookLocation(bookTitle);
    }

    private void getBookLocation(String bookTitle) {
        // 서버에 책 제목으로 요청을 보내기
        Call<BookLocationResponse> call = apiService.getBookLocation(bookTitle);
        call.enqueue(new Callback<BookLocationResponse>() {
            @Override
            public void onResponse(@NotNull Call<BookLocationResponse> call, @NotNull Response<BookLocationResponse> response) {
                if (response.isSuccessful()) {
                    BookLocationResponse data = response.body();
                    if (data != null) {
                        // 받은 데이터로 화면 업데이트
                        floorTextView.setText(String.valueOf(data.getFloor()));
                        categoryTextView.setText(data.getCategory());
                        float circleX = data.getXCoordinate();
                        float circleY = data.getYCoordinate();
                        imageView.setCirclePosition(circleX, circleY);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookLocationResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
