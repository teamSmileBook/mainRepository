package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class book_list extends AppCompatActivity {

    private int currentPage = 1; //현재 페이지 번호
    private TextView pageNumberTextView;


    public book_list() {
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.book_list);

        LinearLayout book_list1 = findViewById(R.id.book_list1);
        LinearLayout book_list2 = findViewById(R.id.book_list2);
        LinearLayout book_list3 = findViewById(R.id.book_list3);
        LinearLayout book_list4 = findViewById(R.id.book_list4);
        LinearLayout book_list5 = findViewById(R.id.book_list5);
        LinearLayout book_list6 = findViewById(R.id.book_list6);
        LinearLayout book_list7 = findViewById(R.id.book_list7);
        LinearLayout book_list8 = findViewById(R.id.book_list8);
        final View heartview1 = findViewById(R.id.heart1);
        final View heartview2 = findViewById(R.id.heart2);
        final View heartview3 = findViewById(R.id.heart3);
        final View heartview4 = findViewById(R.id.heart4);
        final View heartview5 = findViewById(R.id.heart5);
        final View heartview6 = findViewById(R.id.heart6);
        final View heartview7 = findViewById(R.id.heart7);
        final View heartview8 = findViewById(R.id.heart8);
        pageNumberTextView = findViewById(R.id.paging);
        Button preview = findViewById(R.id.preview);
        Button next = findViewById(R.id.next);

        //레이아웃 누를때마다 책 정보 화면으로 전환
        book_list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });
        book_list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });
        book_list3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        book_list8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(book_list.this, my_book.class);
                startActivity(intent);
            }
        });

        //스피너 설정
        String[] items = {"전체", "가나다순", "대출가능순"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.filter);
        spinner.setAdapter(adapter);


        //하트 누르면 찜으로 변경
        heartview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview1.getBackground().getConstantState().equals
                        (getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview1.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview1.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview2.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview2.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview2.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview3.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview3.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview3.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview4.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview4.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview4.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview5.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview5.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview5.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview6.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview6.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview6.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview7.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview7.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview7.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        heartview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (heartview8.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.heart).getConstantState())) {
                    heartview8.setBackgroundResource(R.drawable.empty_heart);
                } else {
                    heartview8.setBackgroundResource(R.drawable.heart);
                }
            }
        });

        //이전 페이지 버튼
        if (currentPage > 1) {
            preview.setVisibility(View.VISIBLE);
        } else {
            preview.setVisibility(View.INVISIBLE);
        }
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 1) {
                    currentPage--;
                    updatePageNumber(); //페이지 번호 업데이트
                    if (currentPage > 1) {
                        preview.setVisibility(View.VISIBLE);
                    } else {
                        preview.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        //다음 페이지 버튼
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage++;
                updatePageNumber(); //페이지 번호 업데이트

                //페이지가 1 이상이면 이전 버튼 보이도록 설정
                if (currentPage > 1) {
                    preview.setVisibility(View.VISIBLE);
                } else {
                    preview.setVisibility(View.INVISIBLE);
                }
            }
        });

        //페이지 번호 초기 설정
        updatePageNumber();
    }

    //페이지 번호를 업데이트하여 paging에(TextView) 표시
    private void updatePageNumber() {
        pageNumberTextView.setText(String.valueOf(currentPage));
    }

}