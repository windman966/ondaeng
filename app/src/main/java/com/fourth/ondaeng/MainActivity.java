package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.fourth.ondaeng.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private ViewPager2 viewPager2;
    private CircleIndicator3 mIndicator;


    ArrayList<dogIdCardData> dogIdCardDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appData.getInstance().header_name_tv = findViewById(R.id.goToMyCorrection);

        //?????? ?????? ??????
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        if(bm!=null){
            ImageView imageView = findViewById(R.id.userPhoto);
            imageView.setImageBitmap(bm);
        }


//        Toast.makeText(getApplicationContext(),id+" appData?????? ??????",Toast.LENGTH_SHORT).show();

        //?????????????????? ?????????
        viewPager2 = findViewById(R.id.dogIdCardPager);
        
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



        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        //????????? ????????? ??? ?????? ?????????
//        ????????? ??????
        setDogCard();



        //?????? ?????? ??????
        Intent myPageIntent = new Intent(this, myPage.class);
        Intent careIntent = new Intent(this, Vaccination1Activity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        Intent dailyCareIntent = new Intent(this,DailyActivity.class);
        Intent healthCareIntent = new Intent(this,HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this,Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);
        //????????? ??????????????? ??????
        Intent addNewDogIntent = new Intent(this,addNewDog.class);

        binding.addNewDogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addNewDogIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });

        binding.textAddNewDog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(addNewDogIntent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
            }
        });

        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);



    }

    private void setDogCard() {
        ArrayList<dogIdCardData> list = new ArrayList<>();

        String url = "http://14.55.65.181/ondaeng/getDogById?";//2
        String id = appData.id.toString();
        url += "id="+id;
        //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        final int[] length = {0};
        //JSON???????????? ????????? ????????? ???????????????!
        JSONObject testjson = new JSONObject();
        try {
            //???????????? edittext??? id??? pw?????? ????????? put???????????? : ???????????? json???????????? ?????? ?????????????????????.

            //?????? ??????
            final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
            //easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
//                    Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                    try {
                        //easyToast("??????");
                        //?????? json????????? ????????? ??????
                        //key?????? ?????? value?????? ?????? ???????????????.


                        JSONObject jsonObject = new JSONObject(response.toString());
                        //easyToast("test");
                        JSONArray dataArray = jsonObject.getJSONArray("data");
//                        Toast.makeText(getApplicationContext(), dataArray.toString(), Toast.LENGTH_SHORT).show();



                        int length = dataArray.length();
//                        Toast.makeText(getApplicationContext(), "?????? : "+length, Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), length, Toast.LENGTH_SHORT).show();
                        //easyToast(length);

                        ArrayList<dogIdCardData> list2 = new ArrayList<>();

                        for(int i=0;i<length;i++){
                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(i).toString());
                            String name =data.get("name").toString();
                            String dog_birth =data.get("dog_birth").toString();
                            String regist_no =data.get("regist_no").toString();
                            String breed =data.get("breed").toString();
                            String dogPhoto = data.get("dog_photo").toString();
                            dog_birth = dog_birth.substring(0,10);
                            String imgPath = getCacheDir()+"/"+name;
                            Bitmap bm = BitmapFactory.decodeFile(imgPath);
//                            Toast.makeText(getApplicationContext(), name+dog_birth+regist_no+breed, Toast.LENGTH_SHORT).show();
                            list2.add(new dogIdCardData(name,dog_birth,regist_no,breed, bm));
//                            list.add(new dogIdCardData("?????????","010-2432-1677","199-500-500","???????????????"));
                        }
//                        list2.add(new dogIdCardData("?????????1","010-2432-1677","199-500-500","???????????????"));

                        if(list2.size()==0){
                            viewPager2.setVisibility(View.GONE);
                            binding.addNewDog.setVisibility(View.VISIBLE);
                            binding.textAddNewDog.setVisibility(View.GONE);
                        }
                        viewPager2.setAdapter(new item_viewpager(list2));



                        //??????????????? ??????
                        mIndicator = binding.indicator;
                        mIndicator.setViewPager(viewPager2);
                        mIndicator.createIndicators(list2.size(),0);

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






//        viewPager2.setAdapter(new item_viewpager(list));
//
//        //??????????????? ??????
//        mIndicator = binding.indicator;
//        mIndicator.setViewPager(viewPager2);
//        mIndicator.createIndicators(list.size(),0);
    }

    //???????????? ??? ????????? ???
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

    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                }
            }
        });
    }

}



