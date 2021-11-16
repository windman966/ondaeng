package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
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
import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;

import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    ActivityCommunityBinding binding;

    ListView listView;
    CommunityAdapter communityAdapter;
    ArrayList<community_listitems> community_listitems;

    ArrayList<postData> postDataList;

    Button b_writing;
    String userid = "";
    Object CommunityAdapter;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        String id = (String)appData.id;
        //스피너
        Spinner spinner = binding.spinner1;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = spinner.getSelectedItem().toString();

                //아이템이 선택되면 선택된 아이템의 순서에 있는 값을 가져옵니다.
                //parent.getItemAtPosition(position);

//                if(position == 1) {}
                if(text.equals("전체")) {
                    text = "%";
                }
                getPostLength(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //글쓰기 버튼 클릭 시
        binding.bWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(CommunityActivity.this, CommunityWritingActivity.class);
                CommunityActivity.this.startActivity(MainIntent);
                CommunityActivity.this.finish();
            }
        });
    }

    public void getPostLength(String category) {//1
        //easyToast("getPost 실행됨");
        String url = "http://14.55.65.181/ondaeng/getPost?";//2
        url = url +"category="+category;
        final int[] length = {0};
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityActivity.this);

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
                        postDataList = new ArrayList<postData>();
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());
                        //easyToast(length);

                        postDataList = new ArrayList<postData>();
                        for(int i=length-1;i>=0;i--){
                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(i).toString());
                            String id =data.get("user_id").toString();
                            String title =data.get("title").toString();
                            String date =data.get("date").toString();
                            int postNo = (Integer) (data.get("post_no"));
                            date = date.substring(0,10);
                            postDataList.add(new postData(id,title,date,postNo));
                        }

                        ListView listView = (ListView)findViewById(R.id.community_listView);
                        final CommunityAdapter myAdapter = new CommunityAdapter(CommunityActivity.this,postDataList);

                        listView.setAdapter(myAdapter);

//                        게시물 클릭시

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView parent, View v, int position, long id){
                                //easyToast(myAdapter.getItem(position).gettitle());
                                Intent CommIntent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                                int postNo = myAdapter.getItem(position).getpostNo();
//                                easyToast(postNo);
                                CommIntent.putExtra("postNo", postNo);
                                CommunityActivity.this.startActivity(CommIntent);
                                CommunityActivity.this.finish();
                            }
                        });


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

    }

    void easyToast(Object obj){
        Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_SHORT).show();
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
