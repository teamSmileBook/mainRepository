package com.example.smilebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.smilebook.ItemData.GridAdapter;
import com.example.smilebook.ItemData.GridBookListData;
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

public class WishlistClient {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService;
    private Context context;
    private GridAdapter adapter;
    private List<GridBookListData> wishlist = new ArrayList<>();
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

    //찜 상태 업데이트 메서드
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
    // 특정 사용자의 찜 목록 불러오기 (도서 목록에서 사용)
    public void getWishlistByMemberId(String memberId, List<GridBookListData> bookList) {
        // API 서비스를 통해 찜 목록을 가져오기 위한 요청
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

    //특정 사용자의 찜 목록 불러오기 (스피너에서 사용)
    public void getWishlistForCurrentUser(String memberId) {
        // API 서비스를 통해 사용자의 찜 목록을 가져오기 위한 요청
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

                        // 찜 목록의 도서 ID를 사용하여 각 도서의 상세 정보를 가져와서 리스트로 변환
                        List<GridBookListData> wishlistBooks = new ArrayList<>();
                        for (Long bookId : wishlistIds) {
                            // 도서 ID를 사용하여 각 도서의 상세 정보를 가져오는 메서드 호출
                            getBookDetailsById(bookId, wishlistBooks);
                        }

                        // 리사이클러뷰 어댑터에 찜 목록 데이터를 전달하여 갱신
                        adapter.setData(wishlistBooks);
                    }
                } else {
                    // 찜 목록을 가져오는데 실패한 경우
                    Log.e("BookListAll", "getWishlistForCurrentUser : Failed to fetch wishlist");
                }
            }

            @Override
            public void onFailure(Call<WishlistItemDTO> call, Throwable t) {
                // 네트워크 오류 등으로 찜 목록을 가져오는데 실패한 경우
                Log.e("BookListAll", "Failed to fetch wishlist: " + t.getMessage());
            }
        });
    }

    // 각 도서의 상세 정보를 가져오는 메서드
    private void getBookDetailsById(Long bookId, List<GridBookListData> wishlistBooks) {
        // API 서비스를 통해 도서의 상세 정보를 가져오기 위한 요청
        Call<BookDTO> call = apiService.getBookById(bookId);
        // 비동기적으로 API 요청 수행
        call.enqueue(new Callback<BookDTO>() {
            @Override
            public void onResponse(Call<BookDTO> call, Response<BookDTO> response) {
                if (response.isSuccessful()) {
                    BookDTO bookDetailsDTO = response.body();
                    if (bookDetailsDTO != null) {
                        // 도서의 상세 정보를 GridBookListData 객체로 변환하여 리스트에 추가
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
                    // 도서의 상세 정보를 가져오는데 실패한 경우
                    Log.e("BookListAll", "Failed to fetch book details");
                }
            }

            @Override
            public void onFailure(Call<BookDTO> call, Throwable t) {
                // 네트워크 오류 등으로 도서의 상세 정보를 가져오는데 실패한 경우
                Log.e("BookListAll", "Failed to fetch book details: " + t.getMessage());
            }
        });
    }

    //특정 사용자 특정 카테고리의 찜 목록 불러오기

    public void getWishlistByMemberIdAndCategory(String memberId, String category) {
        // API 서비스를 통해 사용자의 특정 카테고리에 속하는 찜 목록을 가져오기 위한 요청
        Call<WishlistDTO> call = apiService.getWishlistByMemberIdAndCategory(memberId, category);
        Log.d("WishlistClient", "특정 사용자 특정 카테고리의 찜 목록 가져오기 요청 memberId: " + memberId + ", category: " + category);
        // 비동기적으로 API 요청 수행
        call.enqueue(new Callback<WishlistDTO>() {
            @Override
            public void onResponse(Call<WishlistDTO> call, Response<WishlistDTO> response) {
                if (response.isSuccessful()) {
                    WishlistDTO wishlistDTO = response.body();
                    if (wishlistDTO != null) {
                        // 찜 목록 가져오기 성공한 경우
                        List<Long> wishlist = wishlistDTO.getBookIds();
                        Log.d("WishlistClient", "getWishlistByMemberIdAndCategory() wishlist: " + wishlist);
                        if (adapter != null) {
                            adapter.updateWishlistData(wishlist);
                        } else {
                            Log.e("WishlistClient", "getWishlistByMemberIdAndCategory: Failed to fetch wishlist");
                        }
                    }
                } else {
                    // 찜 목록을 가져오는데 실패한 경우
                    Log.e("WishlistClient", "Failed to fetch wishlist for memberId: " + memberId + " and category: " + category);
                }
            }

            @Override
            public void onFailure(Call<WishlistDTO> call, Throwable t) {
                // 네트워크 오류 등으로 찜 목록을 가져오는데 실패한 경우
                Log.e("WishlistClient", "Failed to fetch wishlist: " + t.getMessage());
            }
        });
    }

    // WishlistListener 인터페이스를 구현하여 찜 목록을 받아올 경우의 동작 정의
    private WishlistListener wishlistListener = new WishlistListener() {
        @Override
        public void onWishlistReceived(List<Long> wishlist) {
            // 찜 목록을 어댑터에 전달하여 데이터를 업데이트
            adapter.updateWishlistData(wishlist);
        }

        @Override
        public void onWishlistFailed(String errorMessage) {
            // 찜 목록을 받아오지 못한 경우에 대한 처리
            Toast.makeText(context, "찜 목록을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public interface WishlistListener {
        void onWishlistReceived(List<Long> wishlist);

        void onWishlistFailed(String errorMessage);
    }
}