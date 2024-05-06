package com.example.smilebook.ItemData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smilebook.BookInfo;
import com.example.smilebook.R;
import com.example.smilebook.WishlistClient;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<GridBookListData> dataList;
    private List<Long> wishlist; // 찜 목록 데이터
    private Context context;
    private boolean isLoggedIn; // 로그인 여부 확인 변수
    private WishlistClient wishListClient;

    //생성자
    public GridAdapter(List<GridBookListData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        //로그인 여부 확인
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false); // true: 로그인o ,false: 로그인x
        // WishlistClient 초기화
        wishListClient = new WishlistClient(context, this);
    }

    //리사이클러뷰의 각 아이템 뷰 생성(XML 레이아웃 파일에서 아이템 뷰를 inflate하여 ViewHolder 객체를 생성 및 반환)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    //각 아이템에 대한 데이터를 ViewHolder에 연결
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GridBookListData data = dataList.get(position);
        holder.bind(data);
    }

    //데이터 리스트의 크기 반환
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //데이터 업데이트 및 리사이클러뷰 갱신
    public void setData(List<GridBookListData> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton bookCover;
        private ImageButton heart;
        private TextView bookTitle;
        private TextView bookStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            heart = itemView.findViewById(R.id.heart);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookStatus = itemView.findViewById(R.id.book_status);

            // 클릭 리스너 설정
            bookCover.setOnClickListener(this);
            heart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // 클릭된 아이템의 position을 가져옴
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                GridBookListData clickedItem = dataList.get(position);

                if (v.getId() == R.id.bookCover) {
                    // BookInfo 액티비티를 시작하고 클릭된 아이템의 정보를 전달
                    Intent intent = new Intent(context, BookInfo.class);
                    // 클릭된 아이템의 정보를 intent에 추가(도서ID)
                    intent.putExtra("bookId", clickedItem.getBookId());
                    context.startActivity(intent);
                } else if (v.getId() == R.id.heart) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);
                    // memberId가 null이면 로그인되지 않은 상태이므로 토스트 메시지를 표시하고 함수를 종료
                    if (memberId == null) {
                        Toast.makeText(context, "로그인 후 이용해보세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 로그인된 상태이므로 찜 상태 변경
                    clickedItem.setBookWished(!clickedItem.isBookWished());
                    // UI 갱신
                    heart.setBackgroundResource(clickedItem.isBookWished() ? R.drawable.heart : R.drawable.empty_heart);
                    // 찜 기능 서버 처리
                    addToWishlist(clickedItem.getBookId());
                }
            }
        }

        //데이터 바인딩(도서 표지, 제목, 도서 상태 설정)
        public void bind(GridBookListData data) {
            //표지
            Glide.with(context)
                    .load(data.getCoverUrl())
                    .error(R.drawable.icons8_smile_32)
                    .into(bookCover);
            //제목
            bookTitle.setText(data.getTitle());
            //도서 상태
            String status = data.getStatus();
            bookStatus.setText(status);

            // 대출 가능 여부에 따라 텍스트 색상을 변경
            if (!"대출가능".equals(status)) {
                bookStatus.setTextColor(Color.RED);
                bookStatus.setText("대출 불가능");
            }

            // 찜 상태에 따라 하트 이미지 설정
            heart.setBackgroundResource(data.isBookWished() ? R.drawable.heart : R.drawable.empty_heart);
        }
        //찜 기능 서버 처리
        private void addToWishlist(long bookId) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String memberId = sharedPreferences.getString("memberId", ""); //memberId 불러오기
            wishListClient.addToWishlist(memberId, bookId);
            Log.d("GridAdapter", "addToWishlist() memberId:" + memberId + " bookId: " + bookId);
        }
    }
    // 찜 목록 데이터 업데이트 메서드
    public void updateWishlistData(List<Long> wishlist) {
        this.wishlist = wishlist;
        notifyDataSetChanged();

        // 각 아이템에 대해 찜 여부를 업데이트
        for (GridBookListData book : dataList) {
            book.setBookWished(wishlist.contains(book.getBookId()));
            Log.d("GridAdapter","updateWishlistData() datalist : "+dataList);
        }
    }
}