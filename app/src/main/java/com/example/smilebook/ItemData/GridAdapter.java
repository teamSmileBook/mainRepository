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
import com.example.smilebook.BookListActivity;
import com.example.smilebook.R;
import com.example.smilebook.WishlistClient;
import com.example.smilebook.model.BookDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<GridBookListData> dataList;
    private Context context;
    private WishlistClient wishListClient;

    //생성자
    public GridAdapter(List<GridBookListData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        this.wishListClient = new WishlistClient(); // WishlistClient 초기화
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

        //찜 기능
        boolean isBookWished = data.isBookWished(); // 아이템의 찜 상태 가져오기
        holder.heart.setBackgroundResource(isBookWished ? R.drawable.heart : R.drawable.empty_heart);
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long bookId = data.getBookId();
                Log.d("GridAdapter", "setonClickListener : " + bookId);
                addToWishlist(bookId);
                // 아이템의 찜 상태 업데이트
                data.setBookWished(!isBookWished);
                // 리사이클러뷰 갱신
                notifyDataSetChanged();

            }
        });
    }

    //서버로 요청 보내기
    private void addToWishlist(long bookId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String memberId = sharedPreferences.getString("memberId", "");
        Log.d("GridAdapter", "addToWishlist() memberId:" + memberId + " bookId: " + bookId);
        wishListClient.addToWishlist(memberId, bookId);
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

            // ImageButton에 클릭 리스너 설정
            bookCover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // 클릭된 아이템의 position을 가져옴
            int position = getAdapterPosition();
            // 해당 position의 데이터를 가져옴
            GridBookListData clickedItem = dataList.get(position);

            // BookInfo 액티비티를 시작하고 클릭된 아이템의 정보를 전달
            Intent intent = new Intent(context, BookInfo.class);
            // 클릭된 아이템의 정보를 intent에 추가(도서ID)
            intent.putExtra("bookId", clickedItem.getBookId());
            context.startActivity(intent);
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
        }
    }
}
