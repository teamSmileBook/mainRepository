package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.smilebook.ItemData.ImageLoader;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserSearchBinding;
import com.example.smilebook.model.BookDTO;

import org.w3c.dom.Text;

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
        ImageView bookCover = findViewById(R.id.book_cover);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView bookStatus = findViewById(R.id.book_status);
        TextView bookDescription = findViewById(R.id.book_description);

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });



        // 인텐트로부터 bookId 전달 받음
        Intent intent = getIntent();
        Long bookId = intent.getLongExtra("bookId", -1L); //기본값:-1, 만약 bookId가 전달되지 않은 경우를 나타냄

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // book_title TextView에서 텍스트 가져오기
                TextView bookTitleTextView = findViewById(R.id.book_title);
                String bookTitle = bookTitleTextView.getText().toString();

                // Intent를 통해 BookLocationActivity로 이동하고 bookId 전달
                Intent intent = new Intent(BookInfo.this, BookLocationActivity.class);
                intent.putExtra("bookId", bookId); // bookId 추가
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

                    //book 앤티티 정보를 받아와서 UI에 적용하는 코드

                    //Glide: 이미지 로딩 라이브러리
                    Glide.with(BookInfo.this)
                            .load(book.getCoverUrl()) //book.getCoverUrl()로부터 얻은 이미지 경로를 load 하겠다.
                            .into(bookCover); //bookCover(ImageView)로
                    bookTitle.setText(book.getBookTitle()); //제목
                    bookAuthor.setText(book.getAuthor()); //저자
                    bookStatus.setText(book.getBookStatus()); //도서 상태
                    bookDescription.setText(book.getDescription()); //책 소개

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

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(BookInfo.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(BookInfo.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(BookInfo.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(BookInfo.this, book_list.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(BookInfo.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // 로그아웃은 동작 해줘야함
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }
}