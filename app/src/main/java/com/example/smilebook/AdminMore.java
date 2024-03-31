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
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }

        });

        admin_code_modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminModifyIntent = new Intent(getApplicationContext(), AdminCodeModify.class);
                startActivity(adminModifyIntent);
            }
        });

        admin_user_trans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userTransIntent = new Intent(getApplicationContext(), UserMain.class);
                startActivity(userTransIntent);
            }
        });

    }
}
