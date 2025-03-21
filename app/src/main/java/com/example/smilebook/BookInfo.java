package com.example.smilebook;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.smilebook.admin.UserAdminModeSwitch;
import com.example.smilebook.alarm.UserAlarm;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.BookInfoBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ReservationDTO;
import com.example.smilebook.model.ReservationResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//도서의 상태에 따라 UI를 업데이트하고 예약 기능을 구현
public class BookInfo extends AppCompatActivity {

    // 도서 정보를 요청할 기본 URL
    private static final String BASE_URL = "http://3.39.9.175:8080/";

    // API 서비스 인터페이스
    private ApiService apiService;

    //도서 정보를 표시할 뷰 요소들
    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookStatus;
    private TextView bookDescription;
    private Button reservation;

    // 예약 상태와 예약자 ID를 저장할 변수
    private boolean isReserved = false;
    private MemberDTO reservedBy;

    //데이터 바인딩을 위한 변수
    private BookInfoBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    //현재 도서의 상태를 저장할 변수
    private String currentBookStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 레이아웃, 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_info);
        binding.setTitleText("도서 정보");
        toolbarTitleBinding = binding.toolbar;

        //뷰 요소 초기화
        bookCover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.book_author);
        bookStatus = findViewById(R.id.book_status);
        bookDescription = findViewById(R.id.book_description);
        Button location = findViewById(R.id.location);
        Button back = findViewById(R.id.back);
        reservation = findViewById(R.id.reservation);


        //more 클릭 시 팝업 표시
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //뒤로가기 버튼 클릭 시 현재 액티비티 종료
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

        //"위치 확인" 버튼 클릭 시 도서 위치 조회 액티비티로 이동
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookInfo.this, BookLocationActivity.class);
                intent.putExtra("bookId", bookId);
                startActivity(intent);
            }
        });

        // "도서 예약" 버튼 클릭 시 도서 예약, "예약 취소" 버튼 클릭 시 도서 예약 취소
        binding.reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자의 로그인 상태 확인(로그인 시에만 도서 예약이 가능)
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String memberId = prefs.getString("memberId", "");

                if (!memberId.isEmpty()) {
                    // 도서 예약 요청
                    Call<ReservationResponseDTO> call = apiService.reserveBook(new ReservationDTO(memberId, bookId));
                    call.enqueue(new Callback<ReservationResponseDTO>() {
                        @Override
                        public void onResponse(Call<ReservationResponseDTO> call, Response<ReservationResponseDTO> response) {
                            // 예약 요청 응답 처리
                            if (response.isSuccessful() && response.body() != null) {
                                ReservationResponseDTO responseBody = response.body();
                                if (responseBody.isSuccess()) {
                                    isReserved = !isReserved; // 예약 상태 업데이트
                                    currentBookStatus = isReserved ? "예약 중" : "대출 가능"; // 도서 상태 업데이트
                                    updateBookStatus(); // 도서 상태 업데이트
                                    updateReservationButton(); // 버튼 상태 업데이트
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isReserved", isReserved); // 예약 상태 저장
                                    editor.apply();

                                    //예약 성공 메시지 출력
                                    if (isReserved) {
                                        Toast.makeText(BookInfo.this, "도서가 예약되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(BookInfo.this, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // 예약 실패 시 메시지 출력
                                    Toast.makeText(BookInfo.this, responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //서버 응답 실패 시 메시지 출력
                                Toast.makeText(BookInfo.this, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReservationResponseDTO> call, Throwable t) {
                            // 네트워크 요청 실패 시 메시지 출력
                            Log.e("error", "네트워크 요청 실패", t);
                            Toast.makeText(BookInfo.this, "네트워크 요청 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //로그인 요청 메시지 출력
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

        // Retrofit을 사용해 도서 정보 요청
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // bookId에 해당하는 도서 정보 요청
        Call<BookDTO> call = apiService.getBookById(bookId);
        call.enqueue(new Callback<BookDTO>() {
            @Override
            public void onResponse(Call<BookDTO> call, Response<BookDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //도서 정보를 성공적으로 받아올 시 UI 업데이트
                    BookDTO book = response.body();
                    Log.d("BookInfo", "서버 응답 성공");
                    // 도서 정보 설정
                    setBookInfo(book);
                } else {
                    // 도서 정보 요청 실패 시 메시지 출력
                    Log.e("BookInfo", "서버 응답 실패");
                    Toast.makeText(BookInfo.this, "도서 정보를 불러 오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                //네트워크 요청 실패 시 로그 출력
                Log.e("error", "네트워크 요청 실패", t);
            }
        });
    }

    // 도서 정보 설정
    private void setBookInfo(BookDTO book) {
        // 로그인 상태 확인
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        //도서 정보 UI 업데이트
        Glide.with(BookInfo.this)
                .load(book.getCoverUrl())
                .into(bookCover);
        bookTitle.setText(book.getBookTitle());
        bookAuthor.setText(book.getAuthor());
        currentBookStatus = book.getBookStatus(); // 현재 도서 상태 저장
        bookDescription.setText(book.getDescription());
        String reservedBy = (book.getMember() != null) ? book.getMember().getMemberId() : null; // 예약자 ID 저장

        // 도서 상태에 따라 예약 상태 설정
        isReserved = book.getBookStatus().equals("예약 중") && memberId != null && memberId.equals(reservedBy);

        // 예약 상태 업데이트
        updateReservationStatus();

        // 도서 상태 업데이트
        updateBookStatus();

        // 버튼 상태 업데이트
        updateReservationButton();
    }

    // 예약 상태 저장
    private void updateReservationStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isReserved", isReserved); // 예약 상태 저장
        editor.apply();
    }

    // 도서 상태 업데이트
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

    // 예약 버튼 상태 업데이트
    private void updateReservationButton() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        if (currentBookStatus.equals("대출 중")) {
            reservation.setText("예약 불가");
            reservation.setTextColor(Color.RED);
            reservation.setEnabled(false); // 버튼 비활성화
        } else if (currentBookStatus.equals("예약 중")) {
            if (memberId != null && memberId.equals(reservedBy)) {
                reservation.setText("예약 취소");
                reservation.setTextColor(Color.RED);
                reservation.setEnabled(true); // 버튼 활성화
            } else {
                reservation.setText("예약 불가");
                reservation.setTextColor(Color.RED);
                reservation.setEnabled(false); // 버튼 비활성화
            }
        } else {
            reservation.setText("도서 예약");
            reservation.setTextColor(Color.BLACK);
            reservation.setEnabled(true); // 버튼 활성화
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
                    startActivity(new Intent(BookInfo.this, MyBookList.class));
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