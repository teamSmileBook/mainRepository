package com.example.smilebook.ItemData;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smilebook.MyBookExtextion;
import com.example.smilebook.R;

import java.util.ArrayList;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<BookItemData> bookItems;

    public BookItemAdapter(Context context, ArrayList<BookItemData> bookItems) {
        this.context = context;
        this.bookItems = bookItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookItemData bookItem = bookItems.get(position);

        // 데이터를 뷰에 바인딩
        holder.bookTitle.setText(bookItem.getBookTitle());
        holder.bookStatus.setText(bookItem.getBookStatus());
        // 표지 이미지 설정 (Glide 사용)
        Glide.with(context)
                .load(bookItem.getCoverUrl())
                .into(holder.bookCover);

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long bookId = bookItem.getBookId();
                // 클릭한 아이템의 bookId를 가져와서 MyBookExtention 액티비티를 시작
                Intent intent = new Intent(context, MyBookExtextion.class);
                intent.putExtra("bookId", bookId);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return bookItems.size();
    }

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
}
