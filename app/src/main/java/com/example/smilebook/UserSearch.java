package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.databinding.UserSearchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserSearch extends AppCompatActivity {
    private UserSearchBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.user_search);
        // TextView의 text 설정
        binding.setTitleText("검색");
        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
//        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // main_b 화면으로 이동하는 인텐트 생성
//                Intent intent = new Intent(search.this, main_b.class);
//                startActivity(intent);
//                // 현재 액티비티 종료
//                finish();
//            }
//        });

        //뒤로가기
        toolbarTitleBinding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more.xml
        toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSearch.this, UserMore.class);
                startActivity(intent);
            }
        });

        //로그인 유도 버튼
        Button go_login = (Button) findViewById(R.id.login_pink);
        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(goLoginIntent);
            }
        });

        //검색창 설정
        SearchView searchView = findViewById(R.id.searchView);

        //검색 버튼 클릭 시 호출되는 리스너
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //검색 버튼을 눌렀을 때
                if (!query.isEmpty()) {
                    searchBooks(query);
                } else {
                    Toast.makeText(UserSearch.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    //사용자가 입력한 검색어를 서버로 전송하고, 서버로부터 받은 검색 결과를 처리
    private void searchBooks(final String query){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://3.39.9.175:8080/books/search?query=" + query); //검색 요청
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    final String jsonResponse = response.toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleSearchResponse(jsonResponse);
                        }
                    });
                } catch (IOException e) {
                    Log.e("UserSearch","서버 요청 실패");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 서버로부터 받은 JSON 데이터를 파싱하여 도서 정보를 추출하는 메서드
    // 추출한 데이터를 GridBookListData 객체로 생성 후 리스트에 추가햐는 역할
    private List<GridBookListData> parseJsonArray(JSONArray jsonArray) {
        List<GridBookListData> bookList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long bookId = jsonObject.getLong("bookId");
                String coverUrl = jsonObject.getString("coverUrl");
                String bookTitle = jsonObject.getString("bookTitle");
                String author = jsonObject.getString("author");
                // 도서 정보를 GridBookListData 객체로 생성하여 리스트에 추가
                GridBookListData bookData = new GridBookListData(bookId, coverUrl, bookTitle, author);
                bookList.add(bookData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    //서버로 부터 받은 검색 결과 파싱
    private void handleSearchResponse (String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            List<GridBookListData> bookList = parseJsonArray(jsonArray);

            // 검색 결과가 비어 있는지 확인
            if (bookList.isEmpty()) {
                Toast.makeText(UserSearch.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // BookListActivity로 전환하고 검색 결과를 전달하는 Intent 생성
                Intent intent = new Intent(UserSearch.this, BookListActivity.class);
                intent.putExtra("bookList", (Serializable) bookList);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
