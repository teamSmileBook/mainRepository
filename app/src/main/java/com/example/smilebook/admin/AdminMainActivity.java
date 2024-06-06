package com.example.smilebook.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.smilebook.bookManage.BookRegistration;
import com.example.smilebook.MainActivity;
import com.example.smilebook.userManage.UserList;
import com.example.smilebook.R;
import com.example.smilebook.search.SearchActivity;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminMainActivity extends AppCompatActivity {

    private ImageView allImageView1, allImageView2, allImageView3, allImageView4; //전체 ImageView
    private ImageView eduImageView1, eduImageView2, eduImageView3, eduImageView4; //교육 ImageView
    private ImageView ficImageView1, ficImageView2, ficImageView3, ficImageView4; //소설 ImageView
    private ImageView toonImageView1, toonImageView2, toonImageView3, toonImageView4; //만화 ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        //메인화면 카테고리별 도서 ImageView
        allImageView1 = findViewById(R.id.all_book_1);
        allImageView2 = findViewById(R.id.all_book_2);
        allImageView3 = findViewById(R.id.all_book_3);
        allImageView4 = findViewById(R.id.all_book_4);

        eduImageView1 = findViewById(R.id.edu_book_1);
        eduImageView2 = findViewById(R.id.edu_book_2);
        eduImageView3 = findViewById(R.id.edu_book_3);
        eduImageView4 = findViewById(R.id.edu_book_4);

        ficImageView1 = findViewById(R.id.fiction_book_1);
        ficImageView2 = findViewById(R.id.fiction_book_2);
        ficImageView3 = findViewById(R.id.fiction_book_3);
        ficImageView4 = findViewById(R.id.fiction_book_4);

        toonImageView1 = findViewById(R.id.cartoon_book_1);
        toonImageView2 = findViewById(R.id.cartoon_book_2);
        toonImageView3 = findViewById(R.id.cartoon_book_3);
        toonImageView4 = findViewById(R.id.cartoon_book_4);

        // 이미지 로드
        String eduImageName1 = "9791158742188.jpg";
        String eduImageName2 = "9788996100768.jpg";
        String eduImageName3 = "9788950968113.jpg";
        String eduImageName4 = "9788972995678.jpg";
        loadImage(eduImageName1, eduImageView1);
        loadImage(eduImageName2, eduImageView2);
        loadImage(eduImageName3, eduImageView3);
        loadImage(eduImageName4, eduImageView4);

        String ficImageName1 = "9788998441012.jpg";
        String ficImageName2 = "9791130646381.jpg";
        String ficImageName3 = "9791161571188.jpg";
        String ficImageName4 = "9788937461033.jpg";
        loadImage(ficImageName1, ficImageView1);
        loadImage(ficImageName2, ficImageView2);
        loadImage(ficImageName3, ficImageView3);
        loadImage(ficImageName4, ficImageView4);

        String toonImageName1 = "9791198527288.jpg";
        String toonImageName2 = "9791191884333.jpg";
        String toonImageName3 = "9791170626503.jpg";
        String toonImageName4 = "9791172034085.jpg";
        loadImage(toonImageName1, toonImageView1);
        loadImage(toonImageName2, toonImageView2);
        loadImage(toonImageName3, toonImageView3);
        loadImage(toonImageName4, toonImageView4);

        //인기 도서 book_list 이동
        Button bestArrow = (Button) findViewById(R.id.best_arrow_btn); //오른쪽 더보기 버튼 book_list 이동
        bestArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminBookListAll.class);
                intent.putExtra("category","인기 도서");
                startActivity(intent);
            }
        });

        //교육 book_list
        Button eduArrow = (Button) findViewById(R.id.edu_arrow_btn);
        eduArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminBookListActivity.class);
                intent.putExtra("category","교육");
                startActivity(intent);
            }
        });

        //소설 book_list
        Button ficArrow = (Button) findViewById(R.id.fiction_arrow_btn);
        ficArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminBookListActivity.class);
                intent.putExtra("category","소설");
                startActivity(intent);
            }
        });

        //만화 book_list
        Button toonArrow = (Button) findViewById(R.id. cartoon_arrow_btn);
        toonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminBookListActivity.class);
                intent.putExtra("category","만화");
                startActivity(intent);
            }
        });

        //검색 아이템 화면 인텐트
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, SearchActivity.class));
            }
        });

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

    }

    public static void loadImage(String imageName, ImageView imageView) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 이미지 가져오는 API 호출
        Call<ResponseBody> call = apiService.getImage(imageName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 이미지 로드
                    Log.d("ImageLoader", "이미지 로드 응답 성공");
                    try {
                        // 이미지를 바이트 배열로 읽어옴
                        byte[] imageBytes = response.body().bytes();
                        // 바이트 배열로 Glide에 이미지 로드
                        Glide.with(imageView.getContext())
                                .load(imageBytes)
                                .into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 예외 처리
                        Log.e("ImageLoader", "이미지 로드 실패: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ImageLoader", "서버 응답 실패");
            }
        });
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminMainActivity.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(AdminMainActivity.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(AdminMainActivity.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }

}

