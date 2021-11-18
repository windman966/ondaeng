package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityMainBinding databinding;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ViewPager2 viewPager2;
    private CircleIndicator3 mIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appData.getInstance().header_name_tv = findViewById(R.id.goToMyCorrection);


//        아이디 값 받아오기
        String id = (String) appData.id;
        TextView nnOnNav = findViewById(R.id.nickNameOnNav);
        nnOnNav.setText(id);

//        Toast.makeText(getApplicationContext(),id+" appData에서 받음",Toast.LENGTH_SHORT).show();

        //강아지등록증 페이저
        viewPager2 = findViewById(R.id.dogIdCardPager);
        
        //네비게이션 메뉴 코드
        drawerLayout = binding.drawerLayout;
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_open = (Button)findViewById(R.id.btn_back);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });



        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        //등록증 데이터 및 코드 공유유
       ArrayList<dogIdCardData> list = new ArrayList<>();
        list.add(new dogIdCardData("댕댕이","010-2432-1677","199-500-500","포메라니안"));
        list.add(new dogIdCardData("댕댕이2","010-2432-16772","199-500-5002","포메라니안2"));
        list.add(new dogIdCardData("물댕이2","010-2432-16772","199-500-5003","포메라니안2"));
        // 강아지 데이터 쿼리
        String url = "http://14.55.65.181/ondaeng/getDogById?id=test?";
        JSONObject dogJson = new JSONObject();


        viewPager2.setAdapter(new item_viewpager(list));

        //인디케이터 코드
        mIndicator = binding.indicator;
        mIndicator.setViewPager(viewPager2);
        mIndicator.createIndicators(list.size(),0);


        //메뉴 이동 코드
        Intent myPageIntent = new Intent(this, myPage.class);
        Intent careIntent = new Intent(this, Vaccination1Activity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        Intent dailyCareIntent = new Intent(this,DailyActivity.class);
        Intent healthCareIntent = new Intent(this,HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this,Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);

        //마이페이지 이동
        findViewById(R.id.goToQuest).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(questIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });




        findViewById(R.id.goToMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(myPageIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(shopIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToCareVaccin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(careIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToCareDaily).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(dailyCareIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToCareHealth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(healthCareIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToComm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(commIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToWalk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(walkIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });
        findViewById(R.id.goToHosp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(hospIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });




    }
    //뒤로가기 키 눌렀을 때
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //drawer액티비티
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

}



