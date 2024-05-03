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

public class SearchActivity extends AppCompatActivity {
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

        //뒤로가기
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more.xml
        toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, UserMore.class);
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
            public boolean onQueryTextSubmit(String query) { //검색 버튼을 눌렀을 때;
                if (!query.isEmpty()) {
                    searchBooks(query);
                } else {
                    Toast.makeText(SearchActivity.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
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
        //새로운 Thread 생성하여 백그라운드에서 네트워크 요청 처리
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("UserSearch","searchBooks() 요청 보냄");
                    URL url = new URL("http://3.39.9.175:8080/books/search?query=" + query); //검색 요청
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //서버와의 연결 설정
                    conn.setRequestMethod("GET"); //GET 요청 사용
                    conn.setRequestProperty("Content-Type", "application/json"); //Content-Type 헤더를 apllication/jsen으로 설정

                    //서버로부터 응답 받기 위해 BufferReader 생성
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //응답 데이터를 문자열로 읽어옴 (StringBuilder : 응답 데이터 누적)
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    final String jsonResponse = response.toString();

                    //응답 데이터 반환 후 UI Thread 전환 하여 검색 결과 처리
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleSearchResponse(jsonResponse);
                        }
                    });
                } catch (IOException e) {
                    Log.e("UserSearch","네트워크 연결 없음");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 서버로 부터 받은 JSON 데이터를 파싱하여 도서 정보를 추출하는 메서드
    // 추출한 데이터를 GridBookListData 객체로 생성 후 리스트에 추가 하는 역할
    private List<GridBookListData> parseJsonArray(JSONArray jsonArray) {
        List<GridBookListData> bookList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long bookId = jsonObject.getLong("bookId");
                String coverUrl = jsonObject.getString("coverUrl");
                String bookTitle = jsonObject.getString("bookTitle");
                String bookStatus = jsonObject.getString("bookStatus");
                // 도서 정보를 GridBookListData 객체로 생성하여 리스트에 추가
                GridBookListData bookData = new GridBookListData(bookId, coverUrl, bookTitle, bookStatus);
                bookList.add(bookData);
            }
            Log.d("UserSearch", "parseJsonArray() 데이터 파싱 성공");
        } catch (JSONException e) {
            Log.e("UserSearch", "parseJsonArray() 데이터 파싱 실패");
            e.printStackTrace();
        }

        return bookList;
    }

    //서버로 부터 받은 검색 결과 파싱, SearchResult 액티비티로 검색 결과 전달
    private void handleSearchResponse (String jsonResponse) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            List<GridBookListData> bookList = parseJsonArray(jsonArray);

            // 검색 결과가 비어 있는지 확인
            if (bookList.isEmpty()) {
                Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // SearchResult 액티비티로 전환하고 검색 결과를 전달하는 Intent 생성
                Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                intent.putExtra("bookList", (Serializable) bookList);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
