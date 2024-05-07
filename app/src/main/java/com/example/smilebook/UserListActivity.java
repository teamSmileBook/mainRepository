package com.example.smilebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.ItemData.UserAdapter;
import com.example.smilebook.ItemData.UserListData;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.model.UserListDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserListActivity extends AppCompatActivity {
    private View allUserView;
    private View stopUserView;
    private boolean  isAllUserVisible  = true;
    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserListData> userList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        //검색 아이템 화면인텐트
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserListActivity.this, SearchActivity.class));
            }
        });

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.user_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrofit을 사용하여 서버에서 카테고리별 도서 목록을 가져옴
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Retrofit 인스턴스 생성
        ApiService apiService = retrofit.create(ApiService.class);

        // 서버에서 회원 목록을 가져오는 요청 보내기
        Call<List<UserListDTO>> call = apiService.getAllUsers();

        call.enqueue(new Callback<List<UserListDTO>>() {
            @Override
            public void onResponse(Call<List<UserListDTO>> call, Response<List<UserListDTO>> response) {
                if (response.isSuccessful()) {
                    List<UserListDTO> userList = response.body();
                    Log.d("UserListActivity", "회원 목록 불러오기 성공");
                    List<UserListData> userListData = convertToUserListData(userList);
                    adapter = new UserAdapter(UserListActivity.this, userListData);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("UserListActivity", "서버 응답 실패");
                    Toast.makeText(UserListActivity.this, "회원 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserListDTO>> call, Throwable t) {
                Log.d("UserListActivity","네트워크 오류: " + t.getMessage());
                Toast.makeText(UserListActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });

//        call.enqueue(new Callback<List<UserListData>>() {
//            @Override
//            public void onResponse(Call<List<UserListData>> call, Response<List<UserListData>> response) {
//                if (response.isSuccessful()) {
//                    Log.d("UserListActivity", "회원 목록 불러오기 성공");
//                    List<UserListData> userList = response.body();
//                    adapter = new UserAdapter(UserListActivity.this, userList);
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Log.d("UserListActivity", "서버 응답 실패");
//                    Toast.makeText(UserListActivity.this, "회원 목록을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UserListData>> call, Throwable t) {
//                Log.d("UserListActivity","네트워크 오류: " + t.getMessage());
//                Toast.makeText(UserListActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
//            }
//        });

        //버튼 및 뷰 초기화
        Button allUserBtn = findViewById(R.id.all_user_btn);
        Button stopUserBtn = findViewById(R.id.stop_user_btn);
        allUserView = findViewById(R.id.all_user_view);
        stopUserView = findViewById(R.id.stop_user_view);

        //전체회원 버튼에 클릭리스너 설정
        allUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllUserVisible) {
                    allUserView.setVisibility(View.VISIBLE);
                    stopUserView.setVisibility(View.GONE);
                    isAllUserVisible = true;
                }
            }
        });

        //정지 회원 버튼에 클릭 리스너 설정
        stopUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllUserVisible) {
                    allUserView.setVisibility(View.GONE);
                    stopUserView.setVisibility(View.VISIBLE);
                    isAllUserVisible = false;
                    stopUserView.setBackgroundResource(R.drawable.user_btn);
                }
            }
        });

    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(UserListActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(UserListActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(UserListActivity.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(UserListActivity.this, WishListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(UserListActivity.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(UserListActivity.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(UserListActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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

    public static List<UserListData> convertToUserListData(List<UserListDTO> userListDTOs) {
        List<UserListData> userListDataList = new ArrayList<>();
        for (UserListDTO dto : userListDTOs) {
            UserListData data = new UserListData(dto.getMemberId(), dto.getNickname(), dto.getMemberStatus());
            userListDataList.add(data);
        }
        return userListDataList;
    }

}
