package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class user extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        LinearLayout user_list1 = findViewById(R.id.user_list1);
        LinearLayout user_list2 = findViewById(R.id.user_list2);
        LinearLayout user_list3 = findViewById(R.id.user_list3);
        LinearLayout user_list4 = findViewById(R.id.user_list4);

        user_list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, user_data.class);
                startActivity(intent);
            }
        });

        user_list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, user_data.class);
                startActivity(intent);
            }
        });

        user_list3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, user_data.class);
                startActivity(intent);
            }
        });

        user_list4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user.this, user_data.class);
                startActivity(intent);
            }
        });
    }
}
