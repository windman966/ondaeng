package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityMainBinding;
//nav menu 안쪽
public class activity_drawer extends AppCompatActivity {
    private ActivityDrawerBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent myPageIntent = new Intent(this, myPage.class);
        startActivity(myPageIntent);

        findViewById(R.id.goToMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.testText.setText("테스트텍스트");
                Toast.makeText(getApplicationContext(),"테스트완료",Toast.LENGTH_SHORT).show();
            }
        });
    }


}