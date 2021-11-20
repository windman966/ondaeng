package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.R;
import com.fourth.ondaeng.databinding.ActivityDailyBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityQuestBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private ActivityDailyBinding binding;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // (날짜랑 맞는) 산책 거리, 시간 가져오기
        // (날짜에 맞는) 상태와 메모 넣기


        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setText(sdf.format(System.currentTimeMillis()));

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DailyActivity.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
                myCalendar.add(Calendar.DAY_OF_MONTH, -1);
                dateButton.setText(sdf.format(myCalendar.getTime()));
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
                myCalendar.add(Calendar.DAY_OF_MONTH, +1);
                dateButton.setText(sdf.format(myCalendar.getTime()));
            }
        });

        // 상태 버튼 중복체크 안되게 하기
        binding.emotion1.setOnTouchListener(new View.OnTouchListener() {
            private boolean emotion1Pressed,emotion2Pressed,emotion3Pressed,emotion4Pressed,emotion5Pressed;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!emotion2Pressed && !emotion3Pressed
                                && !emotion4Pressed && !emotion5Pressed) {
                            binding.emotion1.setBackgroundColor(500029);
                            view.setPressed(true);
                        }
                        emotion1Pressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.emotion1.setBackgroundColor(500109);
                        view.setPressed(false);
                        emotion1Pressed = false;
                        break;
                }
                return true;
            }
        });

        binding.emotion2.setOnTouchListener(new View.OnTouchListener() {
            private boolean emotion1Pressed,emotion2Pressed,emotion3Pressed,emotion4Pressed,emotion5Pressed;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(!emotion1Pressed && !emotion3Pressed
                                && !emotion4Pressed && !emotion5Pressed) {
                            binding.emotion2.setBackgroundColor(500029);
                            view.setPressed(true);
                        }
                        emotion2Pressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.emotion2.setBackgroundColor(500109);
                        view.setPressed(false);
                        emotion2Pressed = false;
                        break;
                }
                return true;
            }
        });

        binding.emotion3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

            }
        });



        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 날짜 맞춰서
                // 메모, 상태 디비로 넘어가기
                // dateButton
//                public void insertMember(String id,String pw,String nn,String ad,String bi,String mo){
//                    //        easyToast("idCheck 실행됨");
//                    String url = "http://14.55.65.181/ondaeng/insertMember?";
//                    //JSON형식으로 데이터 통신을 진행합니다!
//                    JSONObject testjson = new JSONObject();
//                    try {
////            id=test3&password=tt&nickname=tt&address=tt&birth=1999-01-01&mobile=010-1234-1234
//                        url = url +"id="+id;
//                        url = url +"&password="+pw;
//                        url = url +"&nickname="+nn;
//                        url = url +"&address="+ad;
//                        url = url +"&birth="+bi;
//                        url = url +"&mobile="+mo;
//                        easyToast(url);
//                        //이제 전송
//                        final RequestQueue requestQueue = Volley.newRequestQueue(JoinActivity.this);
//                        //            easyToast(url);
//                        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
//
//                            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    //                        easyToast("응답");
//                                    //받은 json형식의 응답을 받아
//                                    //key값에 따라 value값을 쪼개 받아옵니다.
////                        easyToast("onResponse내부");
//                                    JSONObject jsonObject = new JSONObject(response.toString());
//                                    String message = jsonObject.get("message").toString();
//                                    //                        easyToast("dbpw : "+dbpw+" , pw : "+pw);
//                                    if(message.equals("OK")){
//                                        //                        회원가입 성공시
//                                        easyToast("정상적으로 회원가입이 되었습니다.");
//                                        onBackPressed();
//
//                                    }
//                                    else{
//                                        easyToast("회원가입이 되지않았습니다.");
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                error.printStackTrace();
//                            }
//                        });
//                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                        requestQueue.add(jsonObjectRequest);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }

        });




        myCalendar.getTime();

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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Button dateButton = findViewById(R.id.dateButton);
        dateButton.setText(sdf.format(myCalendar.getTime()));
    }

    public void getWalkData() {

    }



}