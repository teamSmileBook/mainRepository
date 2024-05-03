package com.example.smilebook;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import android.graphics.Color;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class book_registration extends AppCompatActivity {

    private ImageView imageview;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Button available_btn, impossible_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_registration);

        Button photo = findViewById(R.id.photo);
        ImageView imageview = findViewById(R.id.imageView);
        available_btn =findViewById(R.id.available);
        impossible_btn = findViewById(R.id.impossible);
        Button registration = findViewById(R.id.registration);

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //대출 가능 / 불가능 색상 변경
        available_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impossible_btn.setTextColor(Color.parseColor("#8F8F8F"));
                impossible_btn.setBackgroundColor(Color.parseColor("#EAE6F0"));
                available_btn.setTextColor(Color.parseColor("#FFFFFF"));
                available_btn.setBackgroundColor(Color.parseColor("#000000"));
            }
        });

        impossible_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                available_btn.setTextColor(Color.parseColor("#8F8F8F"));
                available_btn.setBackgroundColor(Color.parseColor("#EAE6F0"));
                impossible_btn.setTextColor(Color.parseColor("#FFFFFF"));
                impossible_btn.setBackgroundColor(Color.parseColor("#000000"));
            }
        });

        // ActivityResultLauncher 초기화
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imageview.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        //앨범에서 사진 선택
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(book_registration.this, "등록이 성공적으로 완료되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       galleryLauncher.launch(galleryIntent);
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(book_registration.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(book_registration.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(book_registration.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(book_registration.this, WishListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(book_registration.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // 로그아웃은 동작 해줘야함
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }

}
