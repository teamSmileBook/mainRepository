package com.example.smilebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.MyBookExtensionBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.ReservationDTO;
import com.example.smilebook.model.ReservationResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyBookExtension extends AppCompatActivity {
    private MyBookExtensionBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookStatus;
    private TextView dateTime;
    private TextView bookDescription;
    private Button extention;
    private boolean isReserved = false; // 예약 상태를 저장할 변수
    private String currentBookStatus;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.my_book_extension);
        // TextView의 text 설정
        binding.setTitleText("내 도서");
        toolbarTitleBinding = binding.toolbar;

        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookStatus = findViewById(R.id.book_status);
        dateTime = findViewById(R.id.datetime);
        bookDescription = findViewById(R.id.book_description);
        extention = findViewById(R.id.extension_btn);

        //뒤로가기
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

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
                    Log.d("MyBookExtention", "fatchBookInfo() 서버 응답 성공");
                    Glide.with(MyBookExtension.this)
                            .load(book.getCoverUrl())
                            .into(bookCover);
                    bookTitle.setText(book.getBookTitle());
                    bookAuthor.setText(book.getAuthor());
                    currentBookStatus = book.getBookStatus(); // 현재 도서 상태 저장
                    bookDescription.setText(book.getDescription());

                    // 대출 중인 도서일 경우에만 dueDate 표시
                    if (currentBookStatus.equals("대출 중")) {
                        // dueDate를 "yyyy-MM-dd HH:mm:ss" 형식으로 가정하고 연, 월, 일만 추출
                        String[] dateTimeParts = book.getDueDate().split("T")[0].split("-"); //spoit("T") : time 제거
                        String year = dateTimeParts[0];
                        String month = dateTimeParts[1];
                        String day = dateTimeParts[2];

                        // 연, 월, 일을 UI에 표시
                        String formattedDueDate = "반납기한 : " + year + "년 " + month + "월 " + day + "일";
                        dateTime.setText(formattedDueDate);

                        // 대출 중인 경우에는 대출 연장 버튼 표시
                        extention.setVisibility(View.VISIBLE);
                        extention.setText("대출 연장");

                        // 대출 연장 버튼 클릭 이벤트 처리
                        extention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                extendLoan(bookId);
                            }
                        });
                    } else if (currentBookStatus.equals("예약 중")) {
                        // 예약 중인 경우에는 예약 취소 버튼 표시
                        extention.setVisibility(View.VISIBLE);
                        extention.setText("예약 취소");
                        // 예약 취소 버튼 클릭 이벤트 처리
                        extention.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 예약 취소
                                cancelReservation(bookId);
                            }
                        });
                        // 반납 기한을 표시하지 않음
                        dateTime.setVisibility(View.GONE);
                    }

                    updateBookStatus(); // 도서 상태 업데이트

                } else {
                    Log.e("MyBookExtention", "fetchBookInfo() 서버 응답 실패");
                    Toast.makeText(MyBookExtension.this, "도서 정보를 불러 오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                Log.e("MyBookExtention", "네트워크 요청 실패", t);
            }
        });
    }

    // 대출 연장 다이얼로그
    private void extendLoan(Long bookId) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("대출을 연장하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 대출 연장 요청 보내기
                        sendExtendLoanRequest(bookId);
                    }
                })
                .setNegativeButton("아니오", null) // 사용자가 아니오를 선택하면 아무 동작도 하지 않음
                .show();
    }
    // 대출 연장 메서드
    private void sendExtendLoanRequest(Long bookId) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        if (!memberId.isEmpty()) {
            // 연장 요청을 서버에 보냄
            Call<Void> call = apiService.extendLoan(bookId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // 연장 성공 시 메시지 표시
                        Toast.makeText(MyBookExtension.this, "대출이 연장되었습니다.", Toast.LENGTH_SHORT).show();
                        // 연장 후 도서 정보 다시 불러오기
                        fetchBookInfo();
                    } else {
                        // 연장 실패 시 메시지 표시
                        Toast.makeText(MyBookExtension.this, "대출 연장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // 연장 요청 실패 시 메시지 표시
                    Toast.makeText(MyBookExtension.this, "네트워크 요청 실패", Toast.LENGTH_SHORT).show();
                    Log.e("MyBookExtension", "sendExtendLoanRequest() 네트워크 요청 실패", t);
                }
            });
        } else {
            Toast.makeText(MyBookExtension.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 도서 예약 취소 메서드
    private void cancelReservation(Long bookId) {
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
                            currentBookStatus = isReserved ? "예약 중" : "대출 가능"; // 도서 상태 업데이트
                            updateBookStatus(); // 도서 상태 업데이트
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isReserved", isReserved); // 예약 상태 저장
                            editor.apply();

                            Toast.makeText(MyBookExtension.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 예약 취소 실패 시 메시지 표시
                            Toast.makeText(MyBookExtension.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyBookExtension.this, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservationResponseDTO> call, Throwable t) {
                    Log.e("error", "네트워크 요청 실패", t);
                    Toast.makeText(MyBookExtension.this, "네트워크 요청 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MyBookExtension.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBookStatus() {
        // 현재 도서 상태를 UI에 설정
        binding.bookStatus.setText(currentBookStatus);
        if (currentBookStatus.equals("대출 중")) {
            binding.bookStatus.setTextColor(Color.RED);
        } else if(currentBookStatus.equals("예약 중")) {
            binding.bookStatus.setTextColor(Color.parseColor("#DA9D00"));
        } else {
            binding.bookStatus.setTextColor(Color.parseColor("#009000"));
        }
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(MyBookExtension.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(MyBookExtension.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(MyBookExtension.this, UserBook.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(MyBookExtension.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(MyBookExtension.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(MyBookExtension.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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
