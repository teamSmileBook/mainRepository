package com.example.smilebook.myBook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smilebook.R;

import java.util.ArrayList;

// 내 도서 화면의 RecycleerView에 데이터를 연결하는 어댑터.
public class MyBookItemAdapter extends RecyclerView.Adapter<MyBookItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<MyBookItemData> bookItems;

    // 생성자: 어댑터에 컨텍스트와 도서 목록 데이터를 전달
    public MyBookItemAdapter(Context context, ArrayList<MyBookItemData> bookItems) {
        this.context = context;
        this.bookItems = bookItems;
    }

    // ViewHolder 클래스: 아이템 뷰 요소 초기화
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView bookStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookStatus = itemView.findViewById(R.id.book_status);
        }
    }

    // ViewHolder 객체 생성 및 레이아웃 연결
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_book_item, parent, false);
        return new ViewHolder(view);
    }

    // ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBookItemData bookItem = bookItems.get(position);

        // 도서 제목 설정
        holder.bookTitle.setText(bookItem.getBookTitle());

        // 도서 상태 설정(상태에 따라 텍스트 컬러 설정)
        if ("예약 중".equals(bookItem.getBookStatus())) {
            holder.bookStatus.setTextColor(Color.parseColor("#DA9D00")); // 노란색
        } else if("대출 중".equals(bookItem.getBookStatus())){
            holder.bookStatus.setTextColor(Color.RED); // 빨간색
        }
        holder.bookStatus.setText(bookItem.getBookStatus());

        // 표지 이미지 설정(Glide 사용)
        Glide.with(context)
                .load(bookItem.getCoverUrl())
                .into(holder.bookCover);

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long bookId = bookItem.getBookId();
                // 클릭한 아이템의 bookId를 가져와서 MyBookExtention 액티비티를 시작
                Intent intent = new Intent(context, MyBookExtension.class);
                intent.putExtra("bookId", bookId);
                context.startActivity(intent);
            }
        });
    }

    // 데이터 리스트의 크기 반환
    @Override
    public int getItemCount() {
        return bookItems.size();
    }
}
