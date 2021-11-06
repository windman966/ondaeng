package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fourth.ondaeng.databinding.ActivityMainBinding;

public class CareActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}