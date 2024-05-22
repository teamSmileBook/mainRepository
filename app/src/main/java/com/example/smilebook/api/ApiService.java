package com.example.smilebook.api;
import com.example.smilebook.ItemData.BookItemData;
import com.example.smilebook.ItemData.GridBookListData;
import com.example.smilebook.ItemData.UserListData;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.BookLocationDTO;
import com.example.smilebook.model.LoginRequest;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ReservationDTO;
import com.example.smilebook.model.ReservationResponseDTO;
import com.example.smilebook.model.ResponseDTO;
import com.example.smilebook.model.SuspensionReasonDTO;
import com.example.smilebook.model.TokenDTO;
import com.example.smilebook.model.UserDataDTO;
import com.example.smilebook.model.UserListDTO;
import com.example.smilebook.model.WishlistDTO;
import com.example.smilebook.model.WishlistItemDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("/books/category/{category}")
    Call<List<GridBookListData>> getBooksByCategory(@Path("category") String category);

    @POST("/api/member/details")
    Call<MemberDTO> getMemberDetails(@Body MemberDTO request);

    @POST("/api/member/update")
    Call<Void> updateMemberInfo(@Body MemberDTO memberDTO);

    @POST("/wishlist/add")
    Call<Void> addToWishlist(@Body WishlistDTO wishlistDTO);

    @POST("/book/reserve")
    Call<ReservationResponseDTO> reserveBook(@Body ReservationDTO reservationDTO);

    @POST("/wishlist/check")
    Call<WishlistDTO> getWishlist(@Body WishlistDTO wishlistDTO);

    @POST("/wishlist/{memberId}")
    Call<WishlistItemDTO> getWishlistByMemberId(@Path("memberId") String memberId);

    @DELETE("/wishlist/delete/{memberId}/{bookId}")
    Call<Void> deleteFromWishlist(@Path("memberId") String memberId, @Path("bookId") Long bookId);

    @GET("/member/{memberId}")
    Call<UserDataDTO> getMemberInfo(@Path("memberId") String memberId);

    @POST("/member/updateSuspensionReason")
    Call<Void> updateSuspensionReason(@Body SuspensionReasonDTO suspensionReasonDTO);

    @POST("/member/updateWarning")
    Call<Void> updateWarning(@Body SuspensionReasonDTO suspensionReasonDTO);

    @GET("/admin/users")
    Call<List<UserListDTO>> getAllUsers();

    @POST("/firebase/token")
    Call<Void> sendFirebaseToken(@Body TokenDTO tokenDTO);

    @GET("/member/{memberId}/all-books")
    Call<List<BookItemData>> getAllBooksForMember(@Path("memberId") String memberId);

    @POST("/books/add")
    Call<Void> addbook(@Body BookDTO bookDTO);

    @PUT("/books/{bookId}/extendLoan")
    Call<Void> extendLoan(@Path("bookId") Long bookId);
}
