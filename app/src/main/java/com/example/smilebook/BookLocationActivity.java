package com.example.smilebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.databinding.BookLocationBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookLocationDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookLocationActivity extends AppCompatActivity {

    private ApiService apiService;
    private CustomImageView imageView;
    private Long bookId;
    private BookLocationBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_location);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_location);
        // TextView의 text 설정
        binding.setTitleText("도서 정보");
        toolbarTitleBinding = binding.toolbar;

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        Button back = findViewById(R.id.back);
        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

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
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(BookLocationActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(BookLocationActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(BookLocationActivity.this, UserBook.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(BookLocationActivity.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(BookLocationActivity.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(BookLocationActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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
