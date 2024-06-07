package com.example.smilebook.bookList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.model.BookDTO;
import com.example.smilebook.model.WishlistDTO;
import com.example.smilebook.model.WishlistItemDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//사용자의 찜 목록을 관리하고 API를 통해 찜 목록을 업데이트하고 가져오는 역할
public class WishlistClient {

    private static final String BASE_URL = "http://3.39.9.175:8080/"; // 서버의 기본 URL
    private ApiService apiService; // Retrofit을 사용하여 API와 통신하기 위한 ApiService 객체
    private Context context;
    private GridAdapter adapter;  // RecyclerView에 데이터를 표시하기 위한 어댑터 객체
    private List<GridBookListData> wishlist = new ArrayList<>(); // 사용자의 찜 목록을 저장하는 리스트

    // 생성자: Retrofit을 초기화하고 ApiService 인터페이스를 구현하는 객체를 생성
    public WishlistClient(Context context, GridAdapter adapter) {

        this.context = context;
        this.adapter = adapter;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        wishlist = new ArrayList<>();
    }

    // 찜 목록에 도서를 추가 또는 삭제하는 메서드
    // API를 통해 해당 도서가 이미 찜 목록에 있는지 확인하고, 그에 따라 추가 또는 삭제를 처리한다.
    public void addToWishlist(String memberId, Long bookId) {
        Log.d("WishlistClient", "addToWishlist() memberId:" + memberId + " bookId: " + bookId);
        WishlistDTO wishlistDTO = new WishlistDTO(memberId, bookId);
        // 찜 목록 조회
        Call<WishlistDTO> call = apiService.getWishlist(wishlistDTO);
        call.enqueue(new Callback<WishlistDTO>() {
            @Override
            public void onResponse(Call<WishlistDTO> call, Response<WishlistDTO> response) {
                if (response.isSuccessful()) {
                    WishlistDTO wishlistDTO = response.body();
                    if (wishlistDTO != null) {
                        // 이미 찜한 도서인 경우 찜 삭제
                        Log.d("WishlistClient", "찜 삭제 성공");
                        deleteFromWishlist(memberId, bookId);
                    }
                } else if (response.code() == 404) {
                    // 찜 목록이 없는 경우 찜 추가
                    Log.d("WishlistClient", "찜 추가 성공");
                    addNewToWishlist(memberId, bookId); //새로운 찜을 추가
                } else {
                    Log.e("WishlistClient", "찜 목록 조회 실패");
                }
            }
            @Override
            public void onFailure(Call<WishlistDTO> call, Throwable t) {
                Log.e("WishlistClient", "addToWishlist() 네트워크 연결 오류");
            }
        });
    }

    // 찜 목록에서 도서를 삭제하는 메서드
    private void deleteFromWishlist(String memberId, long bookId) {
        Call<Void> call = apiService.deleteFromWishlist(memberId, bookId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 찜 삭제 성공
                    Toast.makeText(context, "찜이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("WishlistClient", "찜 삭제 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("WishlistClient", "deleteFromWishist() 네트워크 연결 오류");
            }
        });
    }

    // 찜 목록에 도서를 추가하는 메서드
    private void addNewToWishlist(String memberId, long bookId) {
        WishlistDTO wishlistDTO = new WishlistDTO(memberId, bookId);
        Call<Void> call = apiService.addToWishlist(wishlistDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 찜 추가 성공
                    Toast.makeText(context, "찜이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("WishlistClient", "찜 추가 실패");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("WishlistClient", "addNewToWishlist() 네트워크 연결 오류");
            }
        });
    }

    // 특정 사용자의 찜 목록을 불러와 찜 처리 후 버튼 상태를 업데이트하는 메서드
    public void getWishlistByMemberId(String memberId, List<GridBookListData> bookList) {

        // API를 통해 특정 사용자의 찜 목록을 가져오기
        Call<WishlistItemDTO> call = apiService.getWishlistByMemberId(memberId);
        Log.d("WishlistClient","찜 목록 요청 memberId : "+memberId);

        // 비동기적으로 API 요청 수행
        call.enqueue(new Callback<WishlistItemDTO>() {
            @Override
            public void onResponse(Call<WishlistItemDTO> call, Response<WishlistItemDTO> response) {
                if (response.isSuccessful()) {
                    WishlistItemDTO wishlistItemDTO = response.body();
                    if (wishlistItemDTO != null) {
                        // 찜 목록 가져오기 성공한 경우
                        List<Long> wishlist = wishlistItemDTO.getBookIds();
                        Log.d("WishlistClient","getWishlistByMemberId() wishlist "+wishlist);
                        if (adapter != null) {
                            adapter.updateWishlistData(wishlist);
                        }else {
                            Log.e("WishlistClient", "getWishlistByMemberId: Failed to fetch wishlist");
                        }
                    }
                } else {
                    // 찜 목록을 가져오는데 실패한 경우
                    Log.e("WishlistClient", "getWishlistByMemberId : Failed to fetch wishlist");
                }
            }

            @Override
            public void onFailure(Call<WishlistItemDTO> call, Throwable t) {
                // 네트워크 오류 등으로 찜 목록을 가져오는데 실패한 경우
                Log.e("WishlistClient", "Failed to fetch wishlist: " + t.getMessage());
            }
        });
    }

    // 특정 사용자의 찜 목록을 불러와 RecyclerView에 표시하는 메서드
    public void getWishlistForCurrentUser(String memberId) {

        // API를 통해 특정 사용자의 찜 목록을 불러오기
        Call<WishlistItemDTO> call = apiService.getWishlistByMemberId(memberId);
        Log.d("BookListAll", "사용자의 찜 목록 가져오기 요청 memberId : " + memberId);

        // 비동기적으로 API 요청 수행
        call.enqueue(new Callback<WishlistItemDTO>() {
            @Override
            public void onResponse(Call<WishlistItemDTO> call, Response<WishlistItemDTO> response) {
                if (response.isSuccessful()) {
                    WishlistItemDTO wishlistItemDTO = response.body();
                    if (wishlistItemDTO != null) {
                        // 찜 목록 가져오기 성공한 경우
                        List<Long> wishlistIds = wishlistItemDTO.getBookIds();
                        Log.d("BookListAll", "getWishlistForCurrentUser() wishlist " + wishlistIds);

                        if (wishlistIds.isEmpty()) {
                            // 찜 목록이 비어있는 경우
                            Toast.makeText(context, "찜 내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 찜 목록의 도서 ID를 사용하여 각 도서의 상세 정보를 가져와서 리스트로 변환
                            List<GridBookListData> wishlistBooks = new ArrayList<>();
                            for (Long bookId : wishlistIds) {
                                // 도서 ID를 사용하여 각 도서의 상세 정보를 가져오는 메서드 호출
                                getBookDetailsById(bookId, wishlistBooks);
                            }

                            // 리사이클러뷰 어댑터에 찜 목록 데이터를 전달하여 갱신
                            adapter.setData(wishlistBooks);
                        }
                    }
                } else {
                    // 찜 목록을 가져오는데 실패한 경우
                    Log.e("BookListAll", "getWishlistForCurrentUser : Failed to fetch wishlist");
                    Toast.makeText(context, "찜 내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WishlistItemDTO> call, Throwable t) {
                // 네트워크 오류 등으로 찜 목록을 가져오는데 실패한 경우
                Log.e("BookListAll", "Failed to fetch wishlist: " + t.getMessage());
            }
        });
    }

    // 각 도서의 기본 정보를 가져와 RecyclerView에 표시하는 메서드
    private void getBookDetailsById(Long bookId, List<GridBookListData> wishlistBooks) {
        Call<BookDTO> call = apiService.getBookById(bookId);
        // 비동기적으로 API 요청 수행
        call.enqueue(new Callback<BookDTO>() {
            @Override
            public void onResponse(Call<BookDTO> call, Response<BookDTO> response) {
                if (response.isSuccessful()) {
                    BookDTO bookDetailsDTO = response.body();
                    if (bookDetailsDTO != null) {
                        // 도서의 기본 정보를 GridBookListData 객체로 변환하여 리스트에 추가
                        GridBookListData gridBook = new GridBookListData(
                                bookDetailsDTO.getBookId(),
                                bookDetailsDTO.getCoverUrl(),
                                bookDetailsDTO.getBookTitle(),
                                bookDetailsDTO.getBookStatus(),
                                true
                        );
                        wishlistBooks.add(gridBook);
                        // 어댑터에 데이터 변경 사항을 알림
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // 도서의 기본 정보를 가져오는데 실패한 경우
                    Log.e("WishlistClient", "getBookDetailsById() 찜 도서 정보 불러오기 실패, "+response.code());
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                // 네트워크 오류 등으로 데이터를 가져오는데 실패한 경우
                Log.e("WishlistClient", "getBookDetailsById() 찜 도서 정보 불러오기 실패: " + t.getMessage());
            }
        });
    }
}