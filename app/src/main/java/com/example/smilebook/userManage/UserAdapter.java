package com.example.smilebook.userManage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<UserListData> userList;
    private Context context;

    public UserAdapter(Context context, List<UserListData> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserListData user = userList.get(position);
        holder.memberIdTextView.setText(user.getMemberId());
        holder.nicknameTextView.setText(user.getNickname());
        holder.memberStatusTextView.setText(user.getMemberStatus());

        // memberStatus에 따라 텍스트 색상 변경
        if ("이용 가능한 사용자".equals(user.getMemberStatus())) {
            holder.memberStatusTextView.setTextColor(Color.parseColor("#009000")); // 초록색
        } else if ("이용 정지 대상자".equals(user.getMemberStatus())) {
            holder.memberStatusTextView.setTextColor(Color.RED); // 빨간색
        }
        holder.memberStatusTextView.setText(user.getMemberStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭된 아이템의 memberId 가져오기
                String memberId = user.getMemberId();

                // memberId를 인텐트에 담아 user_data 액티비티 실행
                Intent intent = new Intent(context, UserData.class);
                intent.putExtra("memberId", memberId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView memberIdTextView;
        TextView nicknameTextView;
        TextView memberStatusTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            memberIdTextView = itemView.findViewById(R.id.member_id);
            nicknameTextView = itemView.findViewById(R.id.nickname);
            memberStatusTextView = itemView.findViewById(R.id.member_status);
        }
    }
}
