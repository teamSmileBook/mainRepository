package com.example.smilebook.bookList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.search.AdminSearch;
import com.example.smilebook.LoginActivity;
import com.example.smilebook.R;
import com.example.smilebook.admin.UserAdminModeSwitch;
import com.example.smilebook.alarm.UserAlarm;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.UserMyInfo;
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

// 카테고리별 도서 목록을 표시하고 관련된 기능을 제공하는 액티비티. 사용자가 도서 목록을 필터링하고, 카테고리별로 확인할 수 있음.
public class BookListActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/"; // 서버의 기본 URL
    private RecyclerView recyclerView; // 도서 목록을 표시하는 RecyclerView
    private GridAdapter gridAdapter; // RecyclerView에 데이터를 설정하는 어댑터
    private WishlistClient wishlistClient; // 찜 목록 클라이언트
    private List<GridBookListData> bookList = new ArrayList<>(); // 도서 목록을 저장하는 리스트
    private String memberId; // memberId를 저장할 변수
    private String currentCategory; // 현재 선택된 카테고리를 저장할 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // 검색 화면으로 이동하는 클릭 이벤트 처리
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookListActivity.this, AdminSearch.class));
            }
        });

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 카테고리 이름을 인텐트에서 받아와 텍스트뷰에 표시 및 카테고리 값 저장
        String category = getIntent().getStringExtra("category");
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        categoryTextView.setText(category);

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view); // 사용할 리사이클러뷰 id 설정
        gridAdapter = new GridAdapter(new ArrayList<>(), this); // 사용할 어댑터 생성
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰와 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // LayoutManager 그리드 레이아웃 2열로 설정

        // SharedPreferences를 사용하여 memberId 값을 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        memberId = sharedPreferences.getString("memberId", null);

        // WishlistClient 인스턴스 생성
        wishlistClient = new WishlistClient(this, gridAdapter);

        // Intent로부터 현재 카테고리 저장 및 텍스트뷰에 표시
        currentCategory = getIntent().getStringExtra("category");

        // 초기 카테고리로 도서 목록 불러오기
        loadAllBooks(currentCategory);

        //스피너 설정
        String[] items = {"전체", "가나다순", "대출 가능 도서", "찜 도서"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.filter);
        spinner.setAdapter(spinnerAdapter);

        //스피너 아이템 선택 시
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = items[position]; // 선택된 항목의 문자열을 가져옴

                if (selectedItem.equals("전체")){
                    // 전체 도서 목록을 출력
                    loadAllBooks(currentCategory);
                } else if (selectedItem.equals("대출 가능 도서")){
                    // 대출 가능 도서 필터링
                    filterAvailableBooks();
                } else if (selectedItem.equals("찜 도서")) {
                    // 회원일 경우 찜 목록 가져오기
                    if (memberId != null) {
                        wishlistClient.getWishlistForCurrentUser(memberId);
                    }
                } else if (selectedItem.equals("가나다순")) {
                    // 가나다순 정렬
                    sortBooksAlphabetically();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택되지 않았을 때의 처리
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 도서 목록을 다시 불러옴
        loadAllBooks(currentCategory);
    }

    //도서 목록을 불러오는 메서드
    private void loadAllBooks(String category) {

        // Retrofit을 사용하여 서버에서 카테고리별 도서 목록을 가져옴
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService 인터페이스 구현체 생성
        ApiService apiService = retrofit.create(ApiService.class);

        // 서버에 카테고리를 전달하여 도서 목록 불러오기
        Call<List<GridBookListData>> call = apiService.getBooksByCategory(category);

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
                Log.e("BookListActivity", "네트워크 요청 실패" + t);
            }
        });
    }

    //대출 가능 도서 목록 불러오는 메서드
    private void filterAvailableBooks() {
        Log.d("BookListActivity", "filterAvailableBooks 호출됨");
        if (bookList != null) {
            // 대출 가능 도서만 필터링하여 표시
            List<GridBookListData> availableBooks = new ArrayList<>();
            for (GridBookListData book : bookList) {
                if ("대출가능".equals(book.getStatus())) {
                    Log.d("BookListActivity", "도서 상태: " + book.getStatus());
                    availableBooks.add(book);
                }
            }
            gridAdapter.setData(availableBooks); // 필터링된 목록을 어댑터에 설정
        } else {
            Log.e("BookListActivity", "bookList가 null입니다.");
        }
    }

    // 가나다순으로 도서 제목을 정렬하는 메서드
    private void sortBooksAlphabetically() {
        Collections.sort(bookList, new Comparator<GridBookListData>() {
            @Override
            public int compare(GridBookListData book1, GridBookListData book2) {
                return book1.getTitle().compareToIgnoreCase(book2.getTitle());
            }
        });
        gridAdapter.setData(bookList); // 정렬된 목록을 어댑터에 설정
    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    // 알림 화면으로 이동
                    startActivity(new Intent(BookListActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    // 내 정보 화면으로 이동
                    startActivity(new Intent(BookListActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    // 내 도서 화면으로 이동
                    startActivity(new Intent(BookListActivity.this, MyBookList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    // 관리자 인증 화면으로 이동
                    startActivity(new Intent(BookListActivity.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(BookListActivity.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(BookListActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        // SharedPreferences를 사용하여 "memberId" 값을 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("memberId", null);

        // memberId가 null이면 로그인 버튼 텍스트 설정
        MenuItem logOutMenuItem = popupMenu.getMenu().findItem(R.id.user_logOutBtn);
        if (memberId == null) {
            logOutMenuItem.setTitle("로그인");
        } else {
            logOutMenuItem.setTitle("로그아웃");
        }

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
