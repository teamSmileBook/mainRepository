package com.example.smilebook.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    //Retrofit 인스턴스
    private static Retrofit retrofit;
    
    //API의 기본 URL
    private static final String BASE_URL = "http://3.39.9.175:8080";

    //OkHttpClient 설정
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    //Retrofit 인스턴스 반환 메소드
    public static Retrofit getInstance() {
        if (retrofit != null) {
            return retrofit;
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
            return retrofit;
        }


    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }

}