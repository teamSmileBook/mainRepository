package com.example.smilebook.bookManage;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.smilebook.userManage.UserList;
import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.BookEditBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminBookEdit extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookStatus;
    private TextView bookDescription;
    private BookEditBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    private String currentBookStatus;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.book_edit);

        //데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_edit);
        binding.setTitleText("도서 정보");
        toolbarTitleBinding = binding.toolbar;

        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookStatus = findViewById(R.id.book_status);
        bookDescription = findViewById(R.id.book_description);
        Button back = findViewById(R.id.back);

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 인텐트로부터 bookId 전달 받음
        Intent intent = getIntent();
        Long bookId = intent.getLongExtra("bookId", -1L);

        // 도서 정보 불러오기
        fetchBookInfo();

    }

    // 도서 정보 불러오기
    public void fetchBookInfo() {
        // 인텐트로부터 bookId 전달 받음
        Intent intent = getIntent();
        Long bookId = intent.getLongExtra("bookId", -1L);

        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // bookId에 해당하는 도서 정보 가져오기 요청
        Call<BookDTO> call = apiService.getBookById(bookId);
        call.enqueue(new Callback<BookDTO>() {
            @Override
            public void onResponse(Call<BookDTO> call, Response<BookDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookDTO book = response.body();
                    Log.d("AdminBookEdit", "서버 응답 성공");
                    Log.d("glide","coverURL: "+book.getCoverUrl());
                    Glide.with(AdminBookEdit.this)
                            .load(book.getCoverUrl())
                            .into(bookCover);
                    bookTitle.setText(book.getBookTitle());
                    bookAuthor.setText(book.getAuthor());
                    currentBookStatus = book.getBookStatus(); // 현재 도서 상태 저장
                    bookDescription.setText(book.getDescription());

                    updateBookStatus(); // 도서 상태 업데이트

                } else {
                    Log.e("BookInfo", "서버 응답 실패");
                    Toast.makeText(AdminBookEdit.this, "도서 정보를 불러 오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                Log.e("error", "네트워크 요청 실패", t);
            }
        });
    }

    private void updateBookStatus() {
        // 현재 도서 상태를 UI에 설정
        binding.bookStatus.setText(currentBookStatus);
        if (currentBookStatus.equals("대출 중") || currentBookStatus.equals("예약 중")) {
            binding.bookStatus.setTextColor(Color.RED);
        }
        else {
            binding.bookStatus.setTextColor(Color.parseColor("#009000"));
        }
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminBookEdit.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(AdminBookEdit.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(AdminBookEdit.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }
}
