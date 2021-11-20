package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fourth.ondaeng.databinding.ActivityQuestBinding;

public class QuestActivity extends AppCompatActivity {

    ActivityQuestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView29.setVisibility(View.VISIBLE);

    }
}