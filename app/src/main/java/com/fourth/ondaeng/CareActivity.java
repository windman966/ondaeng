package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fourth.ondaeng.databinding.ActivityCareBinding;

public class CareActivity extends AppCompatActivity {

    private ActivityCareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}