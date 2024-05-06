package com.example.smilebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookListAll extends AppCompatActivity implements WishlistClient.WishlistListener {

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
                startActivity(new Intent(BookListAll.this, SearchActivity.class));
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

//        //스피너 설정 - 나영 (레이아웃 파일에 simple_spinner_item이 없어서 튕기길래 잠깐 주석)
//        String[] items = {"전체", "가나다순", "대출가능순"};
//        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner spinner = findViewById(R.id.filter);
//        spinner.setAdapter(spinnerAdapter);

        //리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view); //사용할 리사이클러뷰 id(=book_list 내 리사이클러뷰 id)
        gridAdapter = new GridAdapter(new ArrayList<>(), this); //사용할 어댑터
        recyclerView.setAdapter(gridAdapter); //리사이클러뷰랑 어댑터 연결
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); //사용할 LayoutManager (그리드레이아웃 2열로 정렬)

        // Retrofit을 사용하여 서버에 HTTP 요청을 보냄
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService 인터페이스 구현체 생성
        ApiService apiService = retrofit.create(ApiService.class);

        //전체 도서 정보 가져오기 요청
        Call<List<GridBookListData>> call = apiService.getAllBooks();

        // WishlistClient 인스턴스 생성
        wishlistClient = new WishlistClient(this, gridAdapter);
        wishlistClient.getWishlistByMemberId(memberId, null);

        // SharedPreferences를 사용하여 memberId 값을 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        memberId = sharedPreferences.getString("memberId", null);

        call.enqueue(new Callback<List<GridBookListData>>() {
            @Override
            public void onResponse(Call<List<GridBookListData>> call, Response<List<GridBookListData>> response) {
                if (response.isSuccessful()) {
                    Log.d("BookListAll", "전체 도서 불러오기 성공");

                    //응답 받은 데이터들을 GridBookListData 형태로 bookList 리스트 생성
                    List<GridBookListData> bookList = response.body();
                    //어댑터에 bookList 전송
                    gridAdapter.setData(bookList);
                    // 사용자가 로그인한 후에 찜 목록을 가져옴
                    // memberId가 null이 아닌 경우에만 찜 목록을 가져오도록 함
                    if (memberId != null) {
                        wishlistClient.getWishlistByMemberId(memberId, bookList);
                    }
                } else {
                    // 서버 응답에 실패한 경우
                    Log.e("BookListAll", "서버 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<List<GridBookListData>> call, Throwable t) {
                // 네트워크 요청 실패
                Log.e("BookListAll", "네트워크 요청 실패", t);
            }
        });
    }

    // WishlistListener 인터페이스 구현
    @Override
    public void onWishlistReceived(List<Long> wishlist) {
        // wishlist 데이터를 받아서 처리하는 작업 수행
    }

    @Override
    public void onWishlistFailed(String errorMessage) {
        // 실패 시 처리
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(BookListAll.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(BookListAll.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(BookListAll.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(BookListAll.this, BookListAll.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(BookListAll.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(BookListAll.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(BookListAll.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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

    //툴바 아이템 선택 시 실행되는 메서드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.item_search) {
            Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.item_more) {
            Intent moreIntent = new Intent(getApplicationContext(), UserMore.class);
            startActivity(moreIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}