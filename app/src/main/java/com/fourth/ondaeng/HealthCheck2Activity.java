package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.fourth.ondaeng.databinding.ActivityHealthcheck2Binding;


public class HealthCheck2Activity extends AppCompatActivity {

    ActivityHealthcheck2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthcheck2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String part = intent.getStringExtra("part");
        if(part.equals("eye")) {
            binding.eyeLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("ear")) {
            binding.earLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("face")) {
            binding.faceLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("mouth")) {
            binding.mouthLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("action")) {
            binding.actionLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("bone")) {
            binding.boneLayout.setVisibility(View.VISIBLE);
        }







    }
}