package com.example.smilebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.IDNA;
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
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.BookInfoBinding;
import com.example.smilebook.databinding.SearchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.ReservationDTO;
import com.example.smilebook.model.ReservationResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookInfo extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookStatus;
    private TextView bookDescription;
    private Button reservation;
    private boolean isReserved = false; // 예약 상태를 저장할 변수
    private BookInfoBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    private String currentBookStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_info);
        // TextView의 text 설정
        binding.setTitleText("도서 정보");
        toolbarTitleBinding = binding.toolbar;

        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookStatus = findViewById(R.id.book_status);
        bookDescription = findViewById(R.id.book_description);
        Button location = findViewById(R.id.location);
        Button back = findViewById(R.id.back);
        reservation = findViewById(R.id.reservation);


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

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookInfo.this, BookLocationActivity.class);
                intent.putExtra("bookId", bookId);
                startActivity(intent);
            }
        });

        binding.reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String memberId = prefs.getString("memberId", "");

                if (!memberId.isEmpty()) {
                    Call<ReservationResponseDTO> call = apiService.reserveBook(new ReservationDTO(memberId, bookId));
                    call.enqueue(new Callback<ReservationResponseDTO>() {
                        @Override
                        public void onResponse(Call<ReservationResponseDTO> call, Response<ReservationResponseDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ReservationResponseDTO responseBody = response.body();
                                if (responseBody.isSuccess()) {
                                    isReserved = !isReserved; // 예약 상태 업데이트
                                    updateReservationButton(); // 버튼 상태 업데이트
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isReserved", isReserved); // 예약 상태 저장
                                    editor.apply();

                                    if (isReserved) {
                                        Toast.makeText(BookInfo.this, "도서가 예약되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BookInfo.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // 예약 실패 시 메시지 표시
                                    Toast.makeText(BookInfo.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(BookInfo.this, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReservationResponseDTO> call, Throwable t) {
                            Log.e("error", "네트워크 요청 실패", t);
                            Toast.makeText(BookInfo.this, "네트워크 요청 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(BookInfo.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    Log.d("BookInfo", "서버 응답 성공");
                    Glide.with(BookInfo.this)
                            .load(book.getCoverUrl())
                            .into(bookCover);
                    bookTitle.setText(book.getBookTitle());
                    bookAuthor.setText(book.getAuthor());
                    currentBookStatus = book.getBookStatus(); // 현재 도서 상태 저장
                    bookDescription.setText(book.getDescription());

                    updateBookStatus(); // 도서 상태 업데이트

                    // 저장된 예약 상태 가져오기
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    isReserved = prefs.getBoolean("isReserved", false);
                    updateReservationButton(); // 버튼 상태 업데이트

                } else {
                    Log.e("BookInfo", "서버 응답 실패");
                    Toast.makeText(BookInfo.this, "도서 정보를 불러 오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
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
        } else {
            binding.bookStatus.setTextColor(Color.parseColor("#009000"));
        }
    }

    // 예약 버튼 상태 업데이트 메소드
    private void updateReservationButton() {
        if (currentBookStatus.equals("대출 중")) {
            reservation.setText("예약 불가");
            reservation.setTextColor(Color.RED);
        } else if (currentBookStatus.equals("예약 중")) {
            reservation.setText("예약 취소");
            reservation.setTextColor(Color.RED);
        } else {
            reservation.setText("도서 예약");
            reservation.setTextColor(Color.BLACK);
        }
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
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(BookInfo.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(BookInfo.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(BookInfo.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        // SharedPreferences를 사용하여 "memberId" 값을 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("memberId", null);

        // memberId가 null이면 로그인 버튼 텍스트 설정
        MenuItem logOutMenuItem = popupMenu.getMenu().findItem(R.id.user_logOutBtn);
        if (memberId == null) {
            logOutMenuItem.setTitle("로그인");
        } else {
            logOutMenuItem.setTitle("로그아웃");
        }

        popupMenu.show();

    }
}