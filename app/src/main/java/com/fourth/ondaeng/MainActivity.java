package com.fourth.ondaeng;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.fourth.ondaeng.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ViewPager2 viewPager2;
    private CircleIndicator3 mIndicator;

    //커뮤니티 어댑터
    private ListView listView;
    private CommunityAdapter communityAdapter;
    private ArrayList<community_listitems> community_listitems;
    private Object list_community_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //강아지등록증 페이저
        viewPager2 = findViewById(R.id.dogIdCardPager);
        
        //네비게이션 메뉴 코드
        drawerLayout = binding.drawerLayout;
        drawerView = (View)findViewById(R.id.drawer);
        Button btn_open = (Button)findViewById(R.id.btn_open);
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
        Intent careIntent = new Intent(this, CareActivity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        //마이페이지 이동
        findViewById(R.id.goToMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(myPageIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });

        findViewById(R.id.goToCare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(careIntent);
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


        //커뮤니티 리스트뷰, 어댑터
        listView = (ListView)findViewById(R.id.community_listView);
        list_community_items = new ArrayList<community_listitems>();

        //커뮤니티 게시물(인앱)
        // userid, title, content, category, date
        community_listitems.add(new community_listitems("닉네임1", "간식 추천", "강아지 간식 추천해주세요", "양육 꿀팁", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임2", "장난감 나눔해요", "강아지 장난감 무료나눔합니다", "나눔", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임3", "샵 추천", "학동역 미용 잘 하는 곳 추천해주세요", "내 동네", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임4", "배변 훈련방법", "강아지 배변 훈련하는 방법 알려드려요!", "양육 꿀팁", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임5", "간식 나눔", "강아지 간식 나눔합니다", "나눔", new Date(System.currentTimeMillis())));


        //어댑터 연결, 객체생성
        communityAdapter = new CommunityAdapter(getApplicationContext(), community_listitems);
        //communityAdapter = new CommunityAdapter(MainActivity.this, (ArrayList<com.fourth.ondaeng.community_listitems>) list_community_items);
        listView.setAdapter(communityAdapter);


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



