package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;

    private ImageView allImageView1, allImageView2, allImageView3, allImageView4; //전체 ImageView
    private ImageView eduImageView1, eduImageView2, eduImageView3, eduImageView4; //교육 ImageView
    private ImageView ficImageView1, ficImageView2, ficImageView3, ficImageView4; //소설 ImageView
    private ImageView toonImageView1, toonImageView2, toonImageView3, toonImageView4; //만화 ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        Button loginbtn = (Button) findViewById(R.id.rectangle_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //툴바 사용 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false); //타이틀 안 보이게

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

        // 로그인 성공 시 rectangle_login 숨김
        boolean hideButton = getIntent().getBooleanExtra("hideButton", false);
        if (hideButton) { loginbtn.setVisibility(View.GONE);}

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // Retrofit 객체 생성
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Retrofit을 사용하여 서버에서 이미지를 가져오는 API 호출

        Call<ResponseBody> call = apiService.getImage("9791158742188.jpg"); // 이미지 파일 이름 지정
        call.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response","이미지 응답 완료");
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // 서버에서 반환된 이미지의 경로를 변수에 저장
                        String imageUrl = "http://3.39.9.175:8080/images/9791158742188.jpg";
                        // 이미지를 Glide를 사용하여 로드
                        Glide.with(MainActivity.this)
                                .load(imageUrl)
                                .into(eduImageView1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 에러 처리
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //전체 도서 book_list 이동
        Button allArrow = (Button) findViewById(R.id.all_arrow_btn); //오른쪽 더보기 버튼 book_list 이동
        allArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListAll.class);
                startActivity(intent);
            }
        });

        //교육 book_list
        Button eduArrow = (Button) findViewById(R.id.edu_arrow_btn);
        eduArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListEdu.class);
                startActivity(intent);
            }
        });

        //소설 book_list
        Button ficArrow = (Button) findViewById(R.id.fiction_arrow_btn);
        ficArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListFiction.class);
                startActivity(intent);
            }
        });

        Button toonArrow = (Button) findViewById(R.id. cartoon_arrow_btn); //오른쪽 더보기 버튼 book_list 이동
        toonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListCartoon.class);
                startActivity(intent);
            }
        });

    }

    @Override
//    툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.item_search) {
            Intent searchIntent = new Intent(getApplicationContext(), UserSearch.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.item_more) {
            Intent moreIntent = new Intent(getApplicationContext(), UserMore.class);
            startActivity(moreIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}