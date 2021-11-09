package com.fourth.ondaeng;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;


public class SplashActivity extends AppCompatActivity {

    private HashMap findViewCache;

    //Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        (new Handler()).postDelayed((Runnable)new Runnable() {
            public final void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        }, 1500);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public View findCachedViewById(int var1) {
        if (this.findViewCache == null) {
            this.findViewCache = new HashMap();
        }

        View var2 = (View)this.findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this.findViewCache.put(var1, var2);
        }
        return var2;
    }

    public void clearFindViewByIdCache() {
        if (this.findViewCache != null) {
            this.findViewCache.clear();
        }
    }

}
