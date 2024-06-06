package com.example.smilebook.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.example.smilebook.bookList.GridAdapter;
import com.example.smilebook.bookList.GridBookListData;
import com.example.smilebook.bookList.WishlistClient;
import com.example.smilebook.bookManage.BookRegistration;
import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.example.smilebook.search.SearchActivity;
import com.example.smilebook.userManage.UserList;
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

public class AdminBookListAll extends AppCompatActivity{

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
                startActivity(new Intent(AdminBookListAll.this, SearchActivity.class));
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
        recyclerView = findViewById(R.id.recycler_view);
        gridAdapter = new GridAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(gridAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // GridAdapter 생성 후 관리자 여부 설정
        gridAdapter.setAdmin(true); // 관리자이므로 true로 설정

        // 전체 도서 목록 불러오기
        loadAllBooks();

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
                    // 전체 도서 목록을 출력
                    loadAllBooks();
                } else if (selectedItem.equals("대출 가능 도서")){
                    // 대출 가능 도서만 필터링하여 표시
                    filterAvailableBooks();
                } else if (selectedItem.equals("가나다순")) {
                    // "가나다순" 정렬 메서드 호출
                    sortBooksAlphabetically();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 처리
            }
        });

    }
    // 전체 도서 목록을 불러오는 메서드
    private void loadAllBooks() {
        // Retrofit을 사용하여 서버에 HTTP 요청을 보냄
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService 인터페이스 구현체 생성
        ApiService apiService = retrofit.create(ApiService.class);

        //전체 도서 정보 가져오기 요청
        Call<List<GridBookListData>> call = apiService.getAllBooks();

        call.enqueue(new Callback<List<GridBookListData>>() {
            @Override
            public void onResponse(Call<List<GridBookListData>> call, Response<List<GridBookListData>> response) {
                if (response.isSuccessful()) {
                    //응답 받은 데이터들을 GridBookListData 형태로 bookList 리스트 생성
                    bookList = response.body();
                    //어댑터에 bookList 전송
                    gridAdapter.setData(bookList);
                    Log.d("AdminBookListAll", "전체 도서 불러오기 성공");

                    // 사용자가 로그인한 후에 찜 목록을 가져옴
                    // memberId가 null이 아닌 경우에만 찜 목록을 가져오도록 함
                    if (memberId != null) {
                        wishlistClient.getWishlistByMemberId(memberId, bookList);
                    }
                    // 어댑터에 전체 도서 목록 설정
                    gridAdapter.setData(bookList);
                } else {
                    // 서버 응답에 실패한 경우
                    Log.e("AdminBookListAll", "서버 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<List<GridBookListData>> call, Throwable t) {
                // 네트워크 요청 실패
                Log.e("BookListAll", "네트워크 요청 실패", t);
            }
        });
    }

    //대출 가능 도서 목록 불러오는 메서드
    private void filterAvailableBooks() {
        Log.d("BookListAll","filterAvailableBooks 호출됨");
        // 대출 가능 도서만 필터링하여 표시
        List<GridBookListData> availableBooks = new ArrayList<>();
        for (GridBookListData book : bookList) {
            Log.d("filterAvilableBooks","booklist " +bookList);
            if ("대출가능".equals(book.getStatus())) {
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
                    startActivity(new Intent(AdminBookListAll.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(AdminBookListAll.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(AdminBookListAll.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }

    @Override
    //툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}

