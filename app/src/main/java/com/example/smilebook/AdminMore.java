package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMore extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.admin_more);

        Button closeBtn = (Button) findViewById(R.id.closeBtn);
        ImageButton admin_user_trans_btn = (ImageButton) findViewById(R.id.user_mode_switch);
        ImageButton admin_code_modify_btn = (ImageButton) findViewById(R.id.admin_code_modify);
        ImageButton admin_add_book_btn = findViewById(R.id.add_book);
        ImageButton admin_user_btn = findViewById(R.id.user_manage);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }

        });

        admin_code_modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminModifyIntent = new Intent(getApplicationContext(), AdminmodeSwitchActivity.class);
                startActivity(adminModifyIntent);
            }
        });

        admin_user_trans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userTransIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(userTransIntent);
            }
        });

        //도서등록
        admin_add_book_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(AdminMore.this, book_registration.class);
                startActivity(intent);
            }
        });

        //회원관리
        admin_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMore.this, UserListActivity.class);
                startActivity(intent);
            }
        });



    }
}
