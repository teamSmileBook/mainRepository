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

// 관리자가 도서의 정보를 확인하고 필요한 경우 수정할 수 있음.
public class AdminBookEdit extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/"; // 서버의 기본 URL
    private ApiService apiService; // Retrofit을 사용하여 API와 통신하기 위한 ApiService 객체
    private ImageView bookCover; // 도서 표지 이미지
    private TextView bookTitle; // 도서 제목
    private TextView bookAuthor; // 저자
    private TextView bookStatus; // 도서 상태
    private TextView bookDescription; // 책 소개
    private BookEditBinding binding; // 데이터 바인딩을 위한 객체
    private ToolbarTitleBinding toolbarTitleBinding; // 툴바 데이터 바인딩을 위한 객체

    private String currentBookStatus;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.book_edit);

        //데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_edit);
        binding.setTitleText("도서 정보");
        toolbarTitleBinding = binding.toolbar;

        // UI 요소 초기화
        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookStatus = findViewById(R.id.book_status);
        bookDescription = findViewById(R.id.book_description);
        Button back = findViewById(R.id.back);

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 뒤로가기 버튼 클릭 시 액티비티 종료
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

                    // Glide 라이브러리를 사용하여 도서 표지 이미지 표시
                    Glide.with(AdminBookEdit.this)
                            .load(book.getCoverUrl())
                            .into(bookCover);

                    // 도서 정보 UI에 설정
                    bookTitle.setText(book.getBookTitle());
                    bookAuthor.setText(book.getAuthor());
                    currentBookStatus = book.getBookStatus(); // 현재 도서 상태 저장
                    bookDescription.setText(book.getDescription());

                    // 도서 상태 업데이트
                    updateBookStatus();

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

    // 현재 도서 상태를 UI에 설정하는 메서드
    private void updateBookStatus() {

        binding.bookStatus.setText(currentBookStatus);

        // 대출 중 또는 예약 중인 경우 빨간색으로 표시
        if (currentBookStatus.equals("대출 중") || currentBookStatus.equals("예약 중")) {
            binding.bookStatus.setTextColor(Color.RED);
        }
        // 그 외의 경우 초록색으로 표시
        else {
            binding.bookStatus.setTextColor(Color.parseColor("#009000"));
        }
    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    // 도서 등록 화면으로 이동
                    startActivity(new Intent(AdminBookEdit.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    // 사용자 목록 화면으로 이동
                    startActivity(new Intent(AdminBookEdit.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    // 메인 화면으로 이동
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
