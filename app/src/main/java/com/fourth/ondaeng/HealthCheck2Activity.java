package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityHealthcheck2Binding;

import org.json.JSONObject;

import java.util.ArrayList;


public class HealthCheck2Activity extends AppCompatActivity {

    ActivityHealthcheck2Binding binding;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    ArrayList<HealthcheckData> HealthcheckDataList;
    ListView listView;
    HealthCheckAdapter healthcheckAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthcheck2Binding.inflate(getLayoutInflater());
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

        LinearLayout[] layouts={
                binding.eyeLayout,binding.earLayout,binding.faceLayout,binding.mouthLayout,binding.actionLayout,binding.boneLayout
        };
        LinearLayout nowLayout;
        Intent intent = getIntent();
        String part = intent.getStringExtra("part");
        switch (part){
            case"eye": nowLayout=layouts[0];
                break;
            case"ear": nowLayout=layouts[1];
                break;
            case"face": nowLayout=layouts[2];
                break;
            case"mouth": nowLayout=layouts[3];
                break;
            case"action": nowLayout=layouts[4];
                break;
            case"bone": nowLayout=layouts[5];
                break;
            default: nowLayout=layouts[0];
        }
        nowLayout.setVisibility(View.VISIBLE);



        Button.OnClickListener clickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buttonId = view.getId();
                Button button = (Button)findViewById(buttonId);
                String symptom = button.getText().toString();
                Toast.makeText(getApplicationContext(), symptom, Toast.LENGTH_SHORT).show();
                nowLayout.setVisibility(View.GONE);
                getExplanation(symptom);
            }
        };
        Button[] buttons = {
                binding.eyeButton1,binding.eyeButton2,binding.eyeButton3,binding.eyeButton4,binding.eyeButton5,binding.eyeButton6,
                binding.earButton1,binding.earButton2,binding.earButton3,binding.earButton4,
                binding.faceButton1,binding.faceButton2,binding.faceButton3,binding.faceButton4,
                binding.mouthButton1,binding.mouthButton2,binding.mouthButton3,
                binding.actionButton1,binding.actionButton2,binding.actionButton3,binding.actionButton4,binding.actionButton5,binding.actionButton6,binding.actionButton7,
                binding.boneButton1,binding.boneButton2,binding.boneButton3
        };
        for(int i=0;i<buttons.length;i++){
          buttons[i].setOnClickListener(clickListener);
        }

    }

    private void getExplanation(String symptom) {
        binding.HealthckeckListView.setVisibility(View.VISIBLE);
        String url = "http://14.55.65.181/ondaeng/getDisease?";//2
        url = url +"symptom=%25"+symptom+"%25";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(HealthCheck2Activity.this);

            //easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //easyToast("응답");
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
                        //easyToast("test");
                        HealthcheckDataList = new ArrayList<HealthcheckData>();
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());
//                        Toast.makeText(getApplicationContext(), Integer.toString(length), Toast.LENGTH_SHORT).show();

                        HealthcheckDataList = new ArrayList<HealthcheckData>();
                        for(int i=length-1;i>=0;i--){
                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(i).toString());
                            String diseaseName = data.get("diseaseName").toString();
                            String explanation = data.get("explanation").toString();
                            String cause = data.get("cause").toString();
                            String symptom = data.get("symptom").toString();
                            String preventive = data.get("preventive").toString();
                            HealthcheckDataList.add(new HealthcheckData(diseaseName, explanation, cause, symptom, preventive));
                        }

                        ListView listView = (ListView)findViewById(R.id.Healthckeck_listView);
                        final HealthCheckAdapter myAdapter = new HealthCheckAdapter(HealthCheck2Activity.this,HealthcheckDataList);

                        listView.setAdapter(myAdapter);


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

        } catch (Exception e) {
            e.printStackTrace();
        }

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