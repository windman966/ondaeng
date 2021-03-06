package com.fourth.ondaeng;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityCommunityWritingBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CommunityWritingActivity extends AppCompatActivity {

    ActivityCommunityWritingBinding binding;
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    String category ="μμ‘ κΏν";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //λ€λΉκ²μ΄μ λ©λ΄ μ½λ
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

        //λλ‘μ΄ λλ€μ, νμ¬ μ§μ .
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//μ‘ν°λΉν° μ΄λ
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);
        //μ¬κΈ°κΉμ§
        Spinner spinner = binding.spinner2;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = spinner.getSelectedItem().toString();
                //μμ΄νμ΄ μ νλλ©΄ μ νλ μμ΄νμ μμμ μλ κ°μ κ°μ Έμ΅λλ€.
                //parent.getItemAtPosition(position);
//                if(position == 1) {}
                category = text;
                easyToast(category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)appData.id;
                String title = binding.editTitle.getText().toString();
                String content = binding.editContent.getText().toString();

                if(title.isEmpty()||content.isEmpty()){
                    easyToast("μ λͺ©κ³Ό λ΄μ©μ μλ ₯ν΄μ£ΌμΈμ");
                }
                else if(content.length()<10){
                    easyToast("λ΄μ©μ 10κΈμ μ΄μ μλ ₯ν΄μ£ΌμΈμ");
                }
                else {
                    createPost(id,title,content,category);
                    startActivity(commIntent);
                    overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                    finish();
                }
            }
        });
    }

    //κ°μμ§ μ ν μ ν  λ μλ¦Ό λμ°κΈ°
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("μλ¦Ό");
        //νμ΄νμ€μ 
        String tv_text = "κ°μμ§λ₯Ό μ νν΄μ£ΌμΈμ.";
        builder.setMessage(tv_text);
        //λ΄μ©μ€μ 
        builder.setNeutralButton("κ°μμ§ λ±λ‘νκΈ°", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("λ«κΈ°", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    };
    //νμμ΄λνκΈ°
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


    //post λ±λ‘
    public void createPost(String id,String title,String content,String category){
        //        easyToast("idCheck μ€νλ¨");
        String url = "http://14.55.65.181/ondaeng/createPost?";
        //JSONνμμΌλ‘ λ°μ΄ν° ν΅μ μ μ§νν©λλ€!
        JSONObject testjson = new JSONObject();
        try {
//            id=11&title=νμ€νΈμ©μ΄λΌκ΅¬μ©&content=ν νννν νμ€νΈ&category=λ΄ λλ€
            url = url +"id="+id;
            url = url +"&title="+title;
            url = url +"&content="+content;
            url = url +"&category="+category;

            //μ΄μ  μ μ‘
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityWritingActivity.this);
            //            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //λ°μ΄ν° μ λ¬μ λλ΄κ³  μ΄μ  κ·Έ μλ΅μ λ°μ μ°¨λ‘μλλ€.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //                        easyToast("μλ΅");
                        //λ°μ jsonνμμ μλ΅μ λ°μ
                        //keyκ°μ λ°λΌ valueκ°μ μͺΌκ° λ°μμ΅λλ€.
//                        easyToast("onResponseλ΄λΆ");
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String message = jsonObject.get("message").toString();

                        if(message.equals("OK")){
//                            μ μ λ±λ‘μ
                            easyToast("κΈμ΄ μμ±λμμ΅λλ€");
                            onBackPressed();
                            questUpdate();
                        }
                        else{
                            easyToast("μμ±μ€ μ€λ₯κ° λ°μνμμ΅λλ€.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //μλ²λ‘ λ°μ΄ν° μ λ¬ λ° μλ΅ λ°κΈ°μ μ€ν¨ν κ²½μ° μλ μ½λκ° μ€νλ©λλ€.
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

    public void questUpdate(){
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/updateQuest?";
        url = url +"id="+id;
        url = url +"&type="+3;
        //JSONνμμΌλ‘ λ°μ΄ν° ν΅μ μ μ§νν©λλ€!

        try {
            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            // μ μ‘
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityWritingActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //λ°μ΄ν° μ λ¬μ λλ΄κ³  μ΄μ  κ·Έ μλ΅μ λ°μ μ°¨λ‘μλλ€.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Toast.makeText(getApplicationContext(), "μλ΅", Toast.LENGTH_SHORT).show();
                        //λ°μ jsonνμμ μλ΅μ λ°μ
                        //keyκ°μ λ°λΌ valueκ°μ μͺΌκ° λ°μμ΅λλ€.
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //μλ²λ‘ λ°μ΄ν° μ λ¬ λ° μλ΅ λ°κΈ°μ μ€ν¨ν κ²½μ° μλ μ½λκ° μ€νλ©λλ€.
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
    //drawerμ‘ν°λΉν°
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

    void easyToast(Object obj){
        Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_SHORT).show();
    }

}