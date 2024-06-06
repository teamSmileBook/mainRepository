package com.example.smilebook.api;
import com.example.smilebook.myBook.MyBookItemData;
import com.example.smilebook.bookList.GridBookListData;
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

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //회원 가입 요청
    @POST("/member/join")
    Call<ResponseDTO> join(@Body MemberDTO memberDTO);

    //로그인 요청
    @POST("/api/login")
    Call<Void> login(@Body LoginRequest loginRequest);

    //관리자 코드 확인 요청
    @POST("/api/checkAdminCode")
    Call<String> checkAdminCode(@Query("adminCode") String adminCode);

    //책 위치 조회 요청
    @GET("/books/bookLocation")
    Call<BookLocationDTO> getBookLocation(@Query("title") String bookTitle);

    //이미지 조회 요청
    @GET ("/images/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);

    //책 정보 조회 요청
    @GET("/books/{bookId}")
    Call<BookDTO> getBookById(@Path("bookId") Long bookId);

    //모든 책 목록 조회 요청
    @GET("/books")
    Call<List<GridBookListData>> getAllBooks();

    //책 위치 조회 요청
    @GET("/books/{bookId}/location")
    Call<BookLocationDTO> getBookLocationById(@Path("bookId") Long bookId);

    //카테고리별 책 목록 조회 요청
    @GET("/books/category/{category}")
    Call<List<GridBookListData>> getBooksByCategory(@Path("category") String category);

    //회원 상세 정보 조회 요청
    @POST("/api/member/details")
    Call<MemberDTO> getMemberDetails(@Body MemberDTO request);

    //회원 정보 업데이트 요청
    @POST("/api/member/update")
    Call<Void> updateMemberInfo(@Body MemberDTO memberDTO);

    //위시리스트에 책 추가 요청
    @POST("/wishlist/add")
    Call<Void> addToWishlist(@Body WishlistDTO wishlistDTO);

    //책 예약 요청
    @POST("/book/reserve")
    Call<ReservationResponseDTO> reserveBook(@Body ReservationDTO reservationDTO);

    //위시리스트 조회 요청
    @POST("/wishlist/check")
    Call<WishlistDTO> getWishlist(@Body WishlistDTO wishlistDTO);

    //회원 ID로 위시리스트 조회 요청
    @POST("/wishlist/{memberId}")
    Call<WishlistItemDTO> getWishlistByMemberId(@Path("memberId") String memberId);

    //위시리스트에서 책 삭제 요청
    @DELETE("/wishlist/delete/{memberId}/{bookId}")
    Call<Void> deleteFromWishlist(@Path("memberId") String memberId, @Path("bookId") Long bookId);

    //회원 정보 조회 요청
    @GET("/member/{memberId}")
    Call<UserDataDTO> getMemberInfo(@Path("memberId") String memberId);

    //정지 사유 업데이트 요청
    @POST("/member/updateSuspensionReason")
    Call<Void> updateSuspensionReason(@Body SuspensionReasonDTO suspensionReasonDTO);

    //경고 업데이트 요청
    @POST("/member/updateWarning")
    Call<Void> updateWarning(@Body SuspensionReasonDTO suspensionReasonDTO);

    //모든 사용자 목록 조회 요청
    @GET("/admin/users")
    Call<List<UserListDTO>> getAllUsers();

    //Firebase 토큰 전송 요청
    @POST("/firebase/token")
    Call<Void> sendFirebaseToken(@Body TokenDTO tokenDTO);

    //회원 ID로 해당 회원의 모든 책 목록 조회 요청
    @GET("/member/{memberId}/all-books")
    Call<List<MyBookItemData>> getAllBooksForMember(@Path("memberId") String memberId);

    //책 추가 요청
    @POST("/books/add")
    Call<Void> addbook(@Body BookDTO bookDTO);

    //대출 연장 요청
    @PUT("/books/{bookId}/extendLoan")
    Call<Void> extendLoan(@Path("bookId") Long bookId);

}
