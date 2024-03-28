package com.example.smilebook.api;
import com.example.smilebook.model.MemberDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/join")
    Call<MemberDTO> join(@Body MemberDTO memberDTO);
}