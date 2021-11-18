package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.fourth.ondaeng.databinding.ActivityMyPageBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class myPage extends AppCompatActivity {

    private ActivityMyPageBinding binding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //프로필사진 불러오기
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        binding.userPhoto.setImageBitmap(bm);

        //아이디를 통한 정보불러오기 URL
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        //수정하기 페이지로 넘어가기
        Intent intent = new Intent(this, CorrMyPage.class);
        binding.goToMyCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                finish();
            }
        });
        //데이터가져오기
        try{
            String id = (String)appData.id;
            url = url +"id=" +id;
            final RequestQueue requestQueue = Volley.newRequestQueue(myPage.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());

                        binding.nickNameOnNav.setText(data.get("nickname").toString());
                        binding.pdgPoint.setText(data.get("pdg_point").toString());
                        binding.idOnNav.setText(data.get("id").toString());
                        binding.addrOnNav.setText(data.get("address").toString());

                        binding.birthOnNav.setText(data.get("birth").toString());
                        binding.mobileOnNav.setText(data.get("mobile").toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPage.super.onBackPressed();
            }
        });


    }


    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.none,R.anim.horizon_exit);
        }
    }

