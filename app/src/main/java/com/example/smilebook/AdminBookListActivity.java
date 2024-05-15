package com.example.smilebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.api.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminBookListActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    private WishlistClient wishlistClient;
    private List<GridBookListData> bookList = new ArrayList<>();
    private String memberId; // memberId를 저장할 변수 추가
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        //검색 아이템 화면인텐트
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminBookListActivity.this, SearchActivity.class));
            }
        });

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 카테고리 이름을 인텐트에서 받아와 텍스트뷰에 표시
        String category = getIntent().getStringExtra("category");
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText(category);

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view); //사용할 리사이클러뷰 id(=book_list 내 리사이클러뷰 id)
        gridAdapter = new GridAdapter(new ArrayList<>(), this); //사용할 어댑터
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰랑 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)

        // GridAdapter 생성 후 관리자 여부 설정
        gridAdapter.setAdmin(true); // 관리자이므로 true로 설정

        // 전체 도서 목록 불러오기
        loadAllBooks(category);

//        // SharedPreferences를 사용하여 memberId 값을 가져옴
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        memberId = sharedPreferences.getString("memberId", null);

        //스피너 설정
        String[] items = {"전체", "가나다순", "대출 가능 도서"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.filter);
        spinner.setAdapter(spinnerAdapter);

        //스피너 아이템 선택 시
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 스피너에서 선택된 항목의 위치(position)을 확인하여 조건을 처리
                String selectedItem = items[position]; // 선택된 항목의 문자열을 가져옴
                if (selectedItem.equals("전체")){
                    Log.d("onItemSelected","전체 선택됨");
                    // 전체 도서 목록을 출력
                    loadAllBooks(category);
                } else if (selectedItem.equals("대출 가능 도서")){
                    Log.d("onItemSelected","대출 가능 도서 선택됨");
                    // 대출 가능 도서만 필터링하여 표시
                    filterAvailableBooks();
                } else if (selectedItem.equals("가나다순")) {
                    sortBooksAlphabetically(); // "가나다순"일 때 정렬 메서드 호출
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 처리
            }
        });
    }
    private void loadAllBooks(String category) {
        // Retrofit을 사용하여 서버에서 카테고리별 도서 목록을 가져옴
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<GridBookListData>> call = apiService.getBooksByCategory(category); // 서버에게 카테고리 전달하여 도서 목록 요청

        call.enqueue(new Callback<List<GridBookListData>>() {
            @Override
            public void onResponse(Call<List<GridBookListData>> call, Response<List<GridBookListData>> response) {
                if (response.isSuccessful()) {
                    Log.d("BookListActivity", "도서 불러오기 성공 category : " + category);
                    List<GridBookListData> books = response.body();
                    gridAdapter.setData(books); // 불러온 도서 목록을 어댑터에 설정
                    bookList = books; // bookList 변수 업데이트
                    if (memberId != null) {
                        wishlistClient.getWishlistByMemberId(memberId, books); // 사용자의 찜 목록 가져오기
                    }
                } else {
                    Log.e("BookListActivity", "서버 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<List<GridBookListData>> call, Throwable t) {
                Log.e("BookListActivity", "네트워크 요청 실패"+t);
            }
        });
    }

    //대출 가능 도서 목록 불러오는 메서드
    private void filterAvailableBooks() {
        Log.d("AdminBookListActivity","filterAvailableBooks 호출됨");
        // 대출 가능 도서만 필터링하여 표시
        List<GridBookListData> availableBooks = new ArrayList<>();
        for (GridBookListData book : bookList) {
            Log.d("filterAvilableBooks","booklist " +bookList);
            if ("대출가능".equals(book.getStatus())) {
                Log.d("BookListAll", "도서 상태: " + book.getStatus());
                availableBooks.add(book);
            }
        }
        gridAdapter.setData(availableBooks);
    }

    // 가나다순으로 도서 제목 정렬하는 메서드
    private void sortBooksAlphabetically() {
        Collections.sort(bookList, new Comparator<GridBookListData>() {
            @Override
            public int compare(GridBookListData book1, GridBookListData book2) {
                return book1.getTitle().compareToIgnoreCase(book2.getTitle());
            }
        });
        gridAdapter.setData(bookList); // 정렬된 목록을 어댑터에 설정
    }
    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminBookListActivity.this, book_registration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(AdminBookListActivity.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(AdminBookListActivity.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }
}
