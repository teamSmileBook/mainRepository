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

// 사용자 목록을 표시하는 RecyclerView의 어댑터 클래스
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private List<UserListData> userList;
    private Context context;

    // UserAdapter의 생성자
    public UserAdapter(Context context, List<UserListData> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 사용자 목록 아이템의 뷰를 생성하여 ViewHolder에 바인딩한다.
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        // ViewHolder에 데이터를 바인딩한다.
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
                // 클릭된 아이템의 memberId를 가져와서 UserData 액티비티로 이동
                String memberId = user.getMemberId();
                Intent intent = new Intent(context, UserData.class);
                intent.putExtra("memberId", memberId);
                context.startActivity(intent);
            }
        });
    }

    // 데이터 리스트의 크기 반환
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // 사용자 목록 아이템의 ViewHolder 클래스
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
