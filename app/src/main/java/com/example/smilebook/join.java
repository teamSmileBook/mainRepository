package com.example.smilebook;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class
join extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        Button arrow_back_btn = findViewById(R.id.arrow_back);
        Button phone_num_certifi_btn = findViewById(R.id.phoneNum_certifi_button);
        arrow_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });

        phone_num_certifi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(join.this);
                builder.setMessage("인증번호가 전송되었습니다.");
                builder.setPositiveButton("확인", null);
                builder.show();
            }
        });


    }

}
