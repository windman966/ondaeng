package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityQuestBinding;

import org.json.JSONObject;

public class QuestActivity extends AppCompatActivity {
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;
    ActivityQuestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
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
                finish();
            }
        });
        findViewById(R.id.goToMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(myPageIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(shopIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareVaccin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(careIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareDaily).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(dailyCareIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareHealth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(healthCareIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToComm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(commIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToWalk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(walkIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToHosp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(hospIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });

//        상단의 일간,주간,월간버튼
        binding.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                버튼 색상 변경
                binding.day.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                binding.week.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
                binding.month.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

//                리스트 표시
                binding.dayQuestList.setVisibility(View.VISIBLE);
                binding.weekQuestList.setVisibility(View.GONE);
                binding.monthQuestList.setVisibility(View.GONE);
            }
        });
        binding.week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                버튼색상 변경
                binding.day.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
                binding.week.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                binding.month.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

//                리스트 표시
                binding.dayQuestList.setVisibility(View.GONE);
                binding.weekQuestList.setVisibility(View.VISIBLE);
                binding.monthQuestList.setVisibility(View.GONE);
            }
        });
        binding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                버튼색상 변경
                binding.day.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
                binding.week.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
                binding.month.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));

//                리스트 표시
                binding.dayQuestList.setVisibility(View.GONE);
                binding.weekQuestList.setVisibility(View.GONE);
                binding.monthQuestList.setVisibility(View.VISIBLE);

//                Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        달성하러가기 버튼
        binding.walkingDogBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(walkIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        binding.diaryBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(dailyCareIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        binding.questWritePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(hospIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });

        questCheck("day");

//TODO        완료버튼 동작 만들기
        binding.walkingDogFinishBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//           TODO
//                qd0_d가 0이면
//                member테이블의 qd0_d 1로 만들기
//                member 테이블의 pdg += quest 테이블의 reward
//                qd0_d가 1이면
//                이미 완료한 퀘스트입니다 Toast하기
            }
        });


    }

    public void questCheck(String division) {
//TODO
//        member 테이블에서 qd0_c 받아서 퀘스트 완료 수치와 비교
//        100*qd0_c*cnt 값으로 progressbar 설정
//        appdata.id 를 기준으로 select 할것
//        qd0_c 가 count이상 이면 해당 버튼 완료버튼으로 변경할것
//

    }


//  함수로 만들어서 일반화 하려했으나 안되므로 각각 만들것
    public JSONObject accessDB(String url){
        String baseUrl = "http://14.55.65.181/ondaeng/";
        url=baseUrl+url;
        JSONObject testjson = new JSONObject();
        JSONObject rdata = new JSONObject();
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(QuestActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        easyToast("응답");
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
//                        easyToast(Integer.valueOf(jsonObject.getJSONArray("data").length()));
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        }catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), rdata.toString(), Toast.LENGTH_SHORT).show();
        return rdata;
    }



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