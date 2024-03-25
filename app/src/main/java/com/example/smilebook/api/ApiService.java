package com.example.smilebook.api;

import com.example.smilebook.model.JoinRequest;
import com.example.smilebook.model.JoinResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/member/register")
    Call<JoinResponse> join(@Body JoinRequest request);
}