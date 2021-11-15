package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.fourth.ondaeng.databinding.ActivityHealthcheck1Binding;


public class HealthCheck1Activity extends AppCompatActivity {

    ActivityHealthcheck1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthcheck1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(HealthCheck1Activity.this, HealthCheck2Activity.class);

        binding.eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "eye");
                startActivity(intent);
            }
        });

        binding.earButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "ear");
                startActivity(intent);
            }
        });

        binding.faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "face");
                startActivity(intent);
            }
        });

        binding.mouthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "mouth");
                startActivity(intent);
            }
        });

        binding.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "action");
                startActivity(intent);
            }
        });

        binding.boneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("part", "bone");
                startActivity(intent);
            }
        });



    }

}