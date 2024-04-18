package com.example.smilebook.ItemData;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smilebook.BookInfo;
import com.example.smilebook.R;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private List<GridBookListData> dataList;
    private Context context;

    public GridAdapter(List<GridBookListData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GridBookListData data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

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

        //데이터 바인딩
        public void bind(GridBookListData data) {
            //표지
            Glide.with(context)
                    .load(data.getCoverUrl())
                    .error(R.drawable.icons8_smile_32)
                    .into(bookCover);
            //제목
            bookTitle.setText(data.getTitle());
            //도서 상태
            bookStatus.setText(data.getStatus());
        }
    }
}
