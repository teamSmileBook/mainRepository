package com.example.smilebook.api;
import com.example.smilebook.model.LoginRequest;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ResponseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/member/join")
    Call<ResponseDTO> join(@Body MemberDTO memberDTO);

    @POST("/api/login")
    Call<Void> login(@Body LoginRequest loginRequest);
}