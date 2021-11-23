package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.fourth.ondaeng.databinding.ActivityShopBinding;

import org.json.JSONObject;

import java.util.Random;

public class Shop extends AppCompatActivity {
    private ActivityShopBinding binding;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    Intent myPageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //네비게이션 메뉴 코드
        drawerLayout = binding.drawerLayout;
        drawerView = (View) findViewById(R.id.drawer);

        updatePointData();

        myPageIntent = new Intent(this, myPage.class);
        Intent careIntent = new Intent(this, Vaccination1Activity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        Intent dailyCareIntent = new Intent(this, DailyActivity.class);
        Intent healthCareIntent = new Intent(this, HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this, Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);


        drawSnackButton(binding.drawSnack1);
        drawSnackButton(binding.drawSnack2);
        drawSnackButton(binding.drawSnack3);
        drawSnackButton(binding.drawSnack4);

        buySnackButton(binding.buySnack1);
        buySnackButton(binding.buySnack2);
        buySnackButton(binding.buySnack3);
        buySnackButton(binding.buySnack4);

        goToLink(binding.imageView13,"https://www.gaeromanjok.com/product/basic_pumkin_duck?nshop=y&gclid=Cj0KCQiAys2MBhDOARIsAFf1D1fp9IFIxukKQ_A4PL6hyLK_EnvjpEOJxHEie-grLmajQ65bqKUIx2UaAhbDEALw_wcB");
        goToLink(binding.imageView14,"https://www.gaeromanjok.com/product/signature_4set");
        goToLink(binding.imageView15, "https://www.gaeromanjok.com/product/signature_blackbean_cookie");
        goToLink(binding.imageView16, "https://www.gaeromanjok.com/product/signature_small_dasik");




        Button btn_open = (Button) findViewById(R.id.btn_back);
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


        //드로어 닉네임, 프사 지정.
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//액티비티 이동
        goToFunc(findViewById(R.id.goToHosp), hospIntent);
        goToFunc(findViewById(R.id.goToWalk), walkIntent);
        goToFunc(findViewById(R.id.goToComm), commIntent);
        goToFunc(findViewById(R.id.goToCareHealth), healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin), careIntent);
        goToFunc(findViewById(R.id.goToCareDaily), dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop), shopIntent);
        goToFunc(findViewById(R.id.goToMyPage), myPageIntent);
        goToFunc(findViewById(R.id.goToQuest), questIntent);
    }

    //강아지 선택 안 할 때 알림 띄우기
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this, addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        //타이틀설정
        String tv_text = "강아지를 선택해주세요.";
        builder.setMessage(tv_text);
        //내용설정
        builder.setNeutralButton("강아지 등록하기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    }

    ;

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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

    //구매 확인
    public void buySnack(){
        updatePointData();
        if(appData.point <1000){
            Toast.makeText(getApplicationContext(), "포인트가 모자릅니다.", Toast.LENGTH_LONG).show();
        } else {
            calcPoint(-1000);
            AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);
            builder.setTitle("구입을 축하드립니다.");
            //타이틀설정
            String tv_text = "익일 배송접수가 진행되오니, 만약 개인정보가 입력하신 부분과 다르다면 수정해주세요.(이름, 주소, 전화번호)";
            builder.setMessage(tv_text);
            //내용설정
            builder.setNeutralButton("마이페이지 이동", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToFunc(findViewById(R.id.goToMyPage), myPageIntent);
                }
            });
            builder.setPositiveButton("닫기",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ;
                        }
                    });
            builder.show();
        }
    }

    //뽑기 결과 확인
    public void checkResult() {
        updatePointData();
        if(appData.point < 10){
            Toast.makeText(getApplicationContext(), "포인트가 모자릅니다.", Toast.LENGTH_LONG).show();
        }else{
            calcPoint(-10);
            Random random = new Random();
            int result = random.nextInt(200) + 1;
            if (result == 77) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);
                builder.setTitle("당첨을 축하드립니다.");
                //타이틀설정
                String tv_text = "익일 배송접수가 진행되오니, 만약 개인정보가 입력하신 부분과 다르다면 수정해주세요.(이름, 주소, 전화번호)";
                builder.setMessage(tv_text);
                //내용설정
                builder.setNeutralButton("마이페이지 이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToFunc(findViewById(R.id.goToMyPage), myPageIntent);
                    }
                });
                builder.setPositiveButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.show();


            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);
                builder.setTitle("결과");
                //타이틀설정
                String tv_text = "아쉽게도 당첨되지 않으셨습니다";
                builder.setMessage(tv_text);
                //내용설정
                builder.setPositiveButton("닫기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "다음 기회를 노려주세요!", Toast.LENGTH_LONG).show();
                            }
                        });

                builder.show();
            }
        }
    }

    //포인트 정보 확인
    public void updatePointData(){
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

                        appData.point = Integer.parseInt(data.get("pdg_point").toString());


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
    //스낵뽑기
    public void drawSnackButton(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePointData();
                checkResult();
            }
        });
    };
    //구입 버튼
    public void buySnackButton(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePointData();
                buySnack();
            }
        });
    };
    //링크가기 버튼
    public void goToLink(View view, String url){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gaeromanjok.com/product/basic_pumkin_duck?nshop=y&gclid=Cj0KCQiAys2MBhDOARIsAFf1D1fp9IFIxukKQ_A4PL6hyLK_EnvjpEOJxHEie-grLmajQ65bqKUIx2UaAhbDEALw_wcB"));
                startActivity(intent);
            }
        });
    }

    public void calcPoint(int point){
//        easyToast("getPwById 실행됨");
        String url = "http://14.55.65.181/ondaeng/calcPoint?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            url = url +"id="+appData.id;
            url = url + "&point="+ point;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(this);

//            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
//                        easyToast(Integer.valueOf(jsonObject.getJSONArray("data").length()));

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
}