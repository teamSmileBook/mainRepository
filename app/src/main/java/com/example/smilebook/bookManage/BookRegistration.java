package com.example.smilebook.bookManage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.userManage.UserList;
import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.BookRegistrationBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.model.BookDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRegistration extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    private BookRegistrationBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    private EditText edtBookId, edtBookTitle, edtAuthor, edtPublisher, edtCoverUrl, edtBookRfid,
            edtDescription, edtContents;
    private Button btnRegister, btnavailable, btnimpossible;
    private String bookStatus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_registration);
        binding.setTitleText("도서 등록");
        toolbarTitleBinding = binding.toolbar;

        edtBookId = findViewById(R.id.edt_book_id);
        edtBookTitle = findViewById(R.id.edt_book_title);
        edtAuthor = findViewById(R.id.edt_author);
        edtPublisher = findViewById(R.id.edt_publisher);
        edtCoverUrl = findViewById(R.id.edt_cover_url);
        edtBookRfid = findViewById(R.id.edt_book_rfid);
        edtDescription = findViewById(R.id.edt_descripsion);
        edtContents = findViewById(R.id.edt_contents);
        btnRegister = findViewById(R.id.registration_btn);
        btnavailable = findViewById(R.id.available_btn);
        btnimpossible = findViewById(R.id.impossible_btn);

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //뒤로가기
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //스피너 설정
        String[] items = {"선택","교육", "만화", "소설", "요리", "스포츠", "여행"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.category_filter);
        spinner.setAdapter(spinnerAdapter);

        // 대출 가능 버튼 클릭 이벤트 처리
        btnavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookStatus = "대출가능";
                btnavailable.setTextColor(Color.parseColor("#009000")); // 글자 색 변경
                btnimpossible.setTextColor(Color.parseColor("#8F8F8F")); // 대출 불가능 버튼의 글자 색 원래대로 변경
            }
        });

        // 대출 불가능 버튼 클릭 이벤트 처리
        btnimpossible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookStatus = "대출 불가능";
                btnimpossible.setTextColor(Color.RED); // 글자 색 변경
                btnimpossible.setTextColor(Color.parseColor("#FFFFFF")); // 대출 가능 버튼의 글자 색 원래대로 변경
            }
        });

        // 등록 버튼 클릭 이벤트 처리
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("book_registration", "등록 버튼 클릭됨");
                // 사용자가 입력한 정보 가져오기
                String bookIdStr = edtBookId.getText().toString();
                String bookTitle = edtBookTitle.getText().toString();
                String author = edtAuthor.getText().toString();
                String publisher = edtPublisher.getText().toString();
                String coverUrl = edtCoverUrl.getText().toString();
                String bookRfid = edtBookRfid.getText().toString();
                String description = edtDescription.getText().toString();
                String contents = edtContents.getText().toString();
                String selectedCategory = spinner.getSelectedItem().toString(); //스피너에서 선택된 카테고리 값 가져오기

                // 입력값 검증
                if (bookIdStr.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "책 ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "책 ID가 입력되지 않음");
                    return;
                }
                if (bookTitle.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "책 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "책 제목이 입력되지 않음");
                    return;
                }
                if (author.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "저자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "저자가 입력되지 않음");
                    return;
                }
                if (publisher.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "출판사를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "출판사가 입력되지 않음");
                    return;
                }
                if (coverUrl.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "표지 URL을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "표지 URL이 입력되지 않음");
                    return;
                }
                if (bookStatus == null || bookStatus.isEmpty()) {
                    Toast.makeText(BookRegistration.this, "도서 상태를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "도서 상태가 입력되지 않음");
                    return;
                }
                if(selectedCategory == "선택")
                {
                    Toast.makeText(BookRegistration.this, "도서 카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("book_registration", "도서 카테고리가 입력되지 않음");
                    return;
                }

                Long bookId = Long.parseLong(bookIdStr);

                // BookDTO 객체 생성
                BookDTO bookDTO = new BookDTO(
                        bookId,
                        bookRfid,
                        bookTitle,
                        coverUrl,
                        author,
                        publisher,
                        null, // memo 필드는 빈 값으로 설정
                        description,
                        contents,
                        bookStatus, // bookStatus 값 설정
                        null, // reservationTime 초기값은 null로 설정
                        null, // loneDate 초기값은 null로 설정
                        null, // dueDate 초기값은 null로 설정
                        null,// member 초기값은 null로 설정
                        selectedCategory // 선택된 카테고리 값 설정
                );

                // 서버로 전송
                addBookInfoToServer(bookDTO);
                Log.d("book_registration","도서 정보 전송"+bookDTO);
            }
        });
    }

    // 서버로 도서 정보를 전송하는 메서드
    private void addBookInfoToServer(BookDTO bookDTO) {
        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // 서버에 도서 정보 전송
        Call<Void> call = apiService.addbook(bookDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 요청을 처리한 경우
                    Toast.makeText(BookRegistration.this, "도서가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 서버에서 오류가 발생한 경우
                    Log.e("BookRegistration","서버 오류 : "+response.code());
                    Toast.makeText(BookRegistration.this, "도서 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 네트워크 오류 등 요청 실패한 경우
                Toast.makeText(BookRegistration.this, "도서 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("BookRegistration", "도서 등록 실패", t);
            }
        });
    }

    // 상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    // 아무 동작 하지않음
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(BookRegistration.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(BookRegistration.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }
}
