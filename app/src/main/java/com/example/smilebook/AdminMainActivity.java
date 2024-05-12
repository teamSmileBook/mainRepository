package com.example.smilebook;

import static com.example.smilebook.ItemData.ImageLoader.loadImage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;


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

        //전체 도서 book_list 이동
        Button allArrow = (Button) findViewById(R.id.all_arrow_btn); //오른쪽 더보기 버튼 book_list 이동
        allArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminBookListAll.class);
                intent.putExtra("category","전체 도서");
                startActivity(intent);
            }
        });

        //교육 book_list
        Button eduArrow = (Button) findViewById(R.id.edu_arrow_btn);
        eduArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","교육");
                startActivity(intent);
            }
        });

        //소설 book_list
        Button ficArrow = (Button) findViewById(R.id.fiction_arrow_btn);
        ficArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","소설");
                startActivity(intent);
            }
        });

        //만화 book_list
        Button toonArrow = (Button) findViewById(R.id. cartoon_arrow_btn);
        toonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","만화");
                startActivity(intent);
            }
        });

        //검색 아이템 화면인텐트
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

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminMainActivity.this, book_registration.class));
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

