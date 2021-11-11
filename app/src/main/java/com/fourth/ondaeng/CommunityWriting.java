package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CommunityWriting extends AppCompatActivity {

    EditText edit_title;
    EditText edit_content;
    Button bpost;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_writing);

        //listActivity 가 무슨 activity인지 체크 -> 코드 추가해주기
        user_id = getIntent().getStringExtra("user_id");

        //컴포넌트 초기화


    }
}