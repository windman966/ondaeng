package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.fourth.ondaeng.R;
import com.fourth.ondaeng.databinding.ActivityDailyBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityQuestBinding;

import org.json.JSONObject;

import java.sql.Time;
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
                        getWalkInfo(sdf.format(myCalendar.getTime()));
            }
        });

        binding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
                myCalendar.add(Calendar.DAY_OF_MONTH, -1);
                dateButton.setText(sdf.format(myCalendar.getTime()));
                getWalkInfo(sdf.format(myCalendar.getTime()));
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
                myCalendar.add(Calendar.DAY_OF_MONTH, +1);
                dateButton.setText(sdf.format(myCalendar.getTime()));
                getWalkInfo(sdf.format(myCalendar.getTime()));
            }
        });

        //???????????? ????????????

        getWalkInfo(sdf.format(myCalendar.getTime()));

        // ???????????? ????????? ???????????? ??????



        // db??? ??????(??????, ??????)
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }

        });



        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //??????????????? ?????? ??????
        drawerLayout = binding.drawerLayout;
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_open = (Button)findViewById(R.id.btn_back);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
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

        //????????? ?????????, ?????? ??????.
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//???????????? ??????
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);

//        binding.walkDis.setText("102.30");
//        binding.walkTime.setText("1");
//        binding.walkCalorie.setText("11.22");

        getWalkInfo(sdf.format(myCalendar.getTime()));
    }

    //????????? ?????? ??? ??? ??? ?????? ?????????
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        //???????????????
        String tv_text = "???????????? ??????????????????.";
        builder.setMessage(tv_text);
        //????????????
        builder.setNeutralButton("????????? ????????????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    };
    //??????????????????
    public void goToFunc(View view, Intent intent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getDogName()==""){
                    checkDog();
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                    finish();
                }
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
    //drawer????????????
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



    public void insertData() {
        // ?????? ????????? (dateButton)
        // ??????, ?????? ????????? ????????????

    }

    //    ????????? ????????????
    public void getWalkInfo(String date){
        String url = "http://14.55.65.181/ondaeng/getWalkInfo?";
//        http://14.55.65.181/ondaeng/getWalkInfo?id=lxacademy&date=2021-11-24
        //JSON???????????? ????????? ????????? ???????????????!
        JSONObject testjson = new JSONObject();
        try {
//            id=test3&password=tt&nickname=tt&address=tt&birth=1999-01-01&mobile=010-1234-1234
            String id =(String)appData.id;
            url = url +"id="+id;
            url = url +"&date="+date;
//            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            //?????? ??????
            final RequestQueue requestQueue = Volley.newRequestQueue(DailyActivity.this);
            //            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //                        easyToast("??????");
                        //?????? json????????? ????????? ??????
                        //key?????? ?????? value?????? ?????? ???????????????.
//                        easyToast("onResponse??????");
                        JSONObject jsonObject = new JSONObject(response.toString());
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());



                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(length-1).toString());
                        String time = data.get("walk_time").toString();
                        double dis = (double) data.get("walk_dis");
//                        Toast.makeText(getApplicationContext(), time+","+dis, Toast.LENGTH_SHORT).show();


                        binding.walkDis.setText(Double.toString(dis));
                        binding.walkTime.setText(time);
//                        int walkTime = Integer.parseInt(time);
                        double cal = dis*0.11;

                        String cal2 = String.format("%.2f", cal);

                        binding.walkCalorie.setText(cal2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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