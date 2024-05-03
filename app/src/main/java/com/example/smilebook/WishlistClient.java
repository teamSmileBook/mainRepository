package com.example.smilebook;

import android.util.Log;
import android.widget.Toast;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.model.WishlistDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WishlistClient {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;

    public WishlistClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void addToWishlist(String memberId, Long bookId) {
        WishlistDTO wishlistDTO = new WishlistDTO(memberId, bookId); // WishlistDTO 객체 생성
        Call<Void> call = apiService.addToWishlist(wishlistDTO); // 서비스로 전달
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 서버 응답 처리
                Log.d("WishlistClient","찜 요청 성공");
                if (response.isSuccessful()) {
                    Log.d("WishlistClient","찜 응답 성공");
                } else {
                    Log.e("WishlistClient","찜 응답 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("WishlistClient","네트워크 연결 오류");
            }
        });
    }
}
