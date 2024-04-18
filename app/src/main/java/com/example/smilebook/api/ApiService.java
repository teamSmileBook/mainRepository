package com.example.smilebook.api;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.BookLocationDTO;
import com.example.smilebook.model.LoginRequest;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ResponseDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/member/join")
    Call<ResponseDTO> join(@Body MemberDTO memberDTO);

    @POST("/api/login")
    Call<Void> login(@Body LoginRequest loginRequest);

    @POST("/api/checkAdminCode")
    Call<String> checkAdminCode(@Query("adminCode") String adminCode);

    @GET("/books/bookLocation")
    Call<BookLocationDTO> getBookLocation(@Query("title") String bookTitle);

    @GET ("/images/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);

    @GET("/books/{bookId}")
    Call<BookDTO> getBookById(@Path("bookId") Long bookId);

    @GET("/books")
    Call<List<GridBookListData>> getAllBooks();

    @GET("/books/{bookId}/location")
    Call<BookLocationDTO> getBookLocationById(@Path("bookId") Long bookId);
}
