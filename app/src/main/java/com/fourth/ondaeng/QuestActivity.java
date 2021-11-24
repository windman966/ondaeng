package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        //드로어 닉네임, 프사 지정.

        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//액티비티 이동
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);


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

                questCheck();
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
                startActivity(commIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });

        questCheck();


        binding.walkingDogFinishBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuestIsDone("1");
            }
        });
        binding.diaryFinishBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuestIsDone("2");
            }
        });
        binding.questWritePostFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkQuestIsDone("3");
            }
        });


    }

    public void checkQuestIsDone(String qNo) {
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        url = url +"id="+id;
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
//            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            // 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(QuestActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Toast.makeText(getApplicationContext(), "응답", Toast.LENGTH_SHORT).show();
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
//                        easyToast(Integer.valueOf(jsonObject.getJSONArray("data").length()));
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
//                        Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();

                        int qd_d = Integer.parseInt(data.get("qd"+qNo+"_d").toString());

                        if(qd_d==0){
                            addPoint(qNo,1);
                            Toast.makeText(getApplicationContext(), "퀘스트를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                            switch (qNo){
                                case "1":binding.walkingDogFinishBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                                    binding.walkingDogFinishBotton.setText("완료된 퀘스트");
                                    break;
                                case "2":binding.diaryFinishBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                                    binding.diaryFinishBotton.setText("완료된 퀘스트");
                                    break;
                                case "3":binding.questWritePostFinishButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                                    binding.questWritePostFinishButton.setText("완료된 퀘스트");
                                    break;
                                default:break;
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "이미 완료한 퀘스트 입니다", Toast.LENGTH_SHORT).show();
                        }

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

    public void addPoint(String qNo,int point) {
//        http://14.55.65.181/ondaeng/updatePoint?id=1&point=10&type=2
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/doneQuest?";
        url = url +"id="+id;
        url = url +"&point="+point;
        url = url +"&type="+qNo;
        //JSON형식으로 데이터 통신을 진행합니다!

        try {
//            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            // 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(QuestActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Toast.makeText(getApplicationContext(), "응답", Toast.LENGTH_SHORT).show();
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    퀘스트 체크, 해당 퀘스트 완료여부및 프로그래스바 설정 버튼 변경
    public void questCheck(){
//        easyToast("getPwById 실행됨");
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        url = url +"id="+id;
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
//            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            // 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(QuestActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Toast.makeText(getApplicationContext(), "응답", Toast.LENGTH_SHORT).show();
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
//                        easyToast(Integer.valueOf(jsonObject.getJSONArray("data").length()));
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
//                        Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();

                        int qd1_c = Integer.parseInt(data.get("qd1_c").toString());
                        if(qd1_c<3){
//                            Toast.makeText(getApplicationContext(), "qd1_c<3", Toast.LENGTH_SHORT).show();
                            binding.walkingDogBotton.setVisibility(View.VISIBLE);
                            binding.walkingDogFinishBotton.setVisibility(View.GONE);
                            binding.questProgressd1.setProgress(100*qd1_c/3);
                        }
                        else if(qd1_c>=3){
                            binding.walkingDogBotton.setVisibility(View.GONE);
                            binding.walkingDogFinishBotton.setVisibility(View.VISIBLE);
                            binding.questProgressd1.setProgress(100);
                        }

                        int qd2_c = Integer.parseInt(data.get("qd2_c").toString());
                        if(qd2_c<1){
                            binding.diaryBotton.setVisibility(View.VISIBLE);
                            binding.diaryFinishBotton.setVisibility(View.GONE);
                            binding.questProgressd2.setProgress(0);
                        }
                        else if(qd2_c>=1){
                            binding.diaryBotton.setVisibility(View.GONE);
                            binding.diaryFinishBotton.setVisibility(View.VISIBLE);
                            binding.questProgressd2.setProgress(100);
                        }
                        int qd3_c = Integer.parseInt(data.get("qd3_c").toString());
                        if(qd3_c<1){
                            binding.questWritePostButton.setVisibility(View.VISIBLE);
                            binding.questWritePostFinishButton.setVisibility(View.GONE);
                            binding.questProgressd3.setProgress(0);
                        }
                        else if(qd3_c>=1){
                            binding.questWritePostButton.setVisibility(View.GONE);
                            binding.questWritePostFinishButton.setVisibility(View.VISIBLE);
                            binding.questProgressd3.setProgress(100);
                        }

                        int qd1_d = Integer.parseInt(data.get("qd1_c").toString());
                        int qd2_d = Integer.parseInt(data.get("qd1_c").toString());
                        int qd3_d = Integer.parseInt(data.get("qd1_c").toString());
                        if (qd1_d==1){
                            binding.walkingDogFinishBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                            binding.walkingDogFinishBotton.setText("완료된 퀘스트");
                        }
                        if (qd2_d==1){
                            binding.diaryFinishBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                            binding.diaryFinishBotton.setText("완료된 퀘스트");
                        }
                        if (qd3_d==1){
                            binding.questWritePostFinishButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark)));
                            binding.questWritePostFinishButton.setText("완료된 퀘스트");
                        }



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
    //강아지 선택 안 할 때 알림 띄우기
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        //타이틀설정
        String tv_text = "강아지를 선택해주세요.";
        builder.setMessage(tv_text);
        //내용설정
        builder.setNeutralButton("강아지 등록하기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    };

    public void getPointData(){
//        easyToast("getPwById 실행됨");
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            url = url +"id="+appData.id;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(this);

//            easyToast(url);
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
                        String dbpw =data.get("password").toString();
//                        easyToast("dbpw : "+dbpw+" , pw : "+pw);
                        String nick = data.get("nickname").toString();



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
    //펑션이동하기
    public void goToFunc(View view, Intent intent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appData.getDogName() == "") {
                    checkDog();
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                    finish();
                }
            }
        });
    }
}
