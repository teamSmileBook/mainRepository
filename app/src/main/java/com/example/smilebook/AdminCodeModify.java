package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.AdminCodeModifyBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

import org.w3c.dom.Text;

public class AdminCodeModify extends AppCompatActivity {

    private ToolbarTitleBinding toolbarTitleBinding;
    private AdminCodeModifyBinding binding;
    private EditText adminCodeModifyEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.admin_code_modify);

        binding = DataBindingUtil.setContentView(this, R.layout.admin_code_modify);

        binding.setTitleText("관리자 인증");
        toolbarTitleBinding = binding.toolbar;
        adminCodeModifyEditText = binding.adminCodeEditText;

        //뒤로가기(more.xml)
        toolbarTitleBinding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more.xml
        toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCodeModify.this, AdminMore.class);
                startActivity(intent);
            }
        });

        adminCodeModifyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    if (adminCodeModifyEditText.getText().length() == 4 ) {
                        //입력 값이 4자리인 경우
                        TextView adminCodeTextView = findViewById(R.id.admin_code_textView);
                        //TextView 내용 변경
                        adminCodeTextView.setText("변경할 인증번호를 입력해주세요");
                        //및 EditText 초기화
                        adminCodeModifyEditText.setText("");
                        adminCodeModifyEditText.requestFocus();

                        return true;
                    }
                }
                return false;
            }
        });


    }
}
