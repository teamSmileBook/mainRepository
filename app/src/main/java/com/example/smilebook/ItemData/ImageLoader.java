package com.example.smilebook.ItemData;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageLoader {

    public static void loadImage(String imageName, ImageView imageView) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 이미지 가져오는 API 호출
        Call<ResponseBody> call = apiService.getImage(imageName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 이미지 로드
                    Log.d("ImageLoader", "이미지 로드 응답 성공");
                    try {
                        // 이미지를 바이트 배열로 읽어옴
                        byte[] imageBytes = response.body().bytes();
                        // 바이트 배열로 Glide에 이미지 로드
                        Glide.with(imageView.getContext())
                                .load(imageBytes)
                                .into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 예외 처리
                        Log.e("ImageLoader", "이미지 로드 실패: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ImageLoader", "서버 응답 실패");
            }
        });
    }
}
