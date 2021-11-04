package com.fourth.ondaeng;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityMainBinding;

public class CommunityActivity extends AppCompatActivity {

    private ActivityCommunityBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
