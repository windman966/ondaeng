package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText edit_title;
    EditText edit_content;
    Button b_post;
    String category ="양육 꿀팁";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityWritingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)appData.id;
                String title = binding.editTitle.getText().toString();
                String content = binding.editContent.getText().toString();

                if(title.isEmpty()||content.isEmpty()){
                    easyToast("제목과 내용을 입력해주세요");
                }
                else if(content.length()<10){
                    easyToast("내용에 10글자 이상은 입력해주세요");
                }
                else {
                    createPost(id,title,content,category);
                }
            }
        });
    }


    //post 등록
    public void createPost(String id,String title,String content,String category){
        //        easyToast("idCheck 실행됨");
        String url = "http://14.55.65.181/ondaeng/createPost?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
//            id=11&title=테스트용이라구용&content=테 테테테테 테스트&category=내 동네
            url = url +"id="+id;
            url = url +"&title="+title;
            url = url +"&content="+content;
            url = url +"&category="+category;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityWritingActivity.this);
            //            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //                        easyToast("응답");
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
//                        easyToast("onResponse내부");
                        JSONObject jsonObject = new JSONObject(response.toString());
                        String message = jsonObject.get("message").toString();
                        //                        easyToast("dbpw : "+dbpw+" , pw : "+pw);
                        if(message.equals("OK")){
//                            정상 등록시
                            easyToast("글이 작성되었습니다");
                            onBackPressed();

                        }
                        else{
                            easyToast("글이 작성중에 오류가 발생하였습니다.");
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


    //뒤로가기 키 눌렀을 때
    @Override
    public void onBackPressed() {
        CommunityWritingActivity.this.startActivity(new Intent(CommunityWritingActivity.this, CommunityActivity.class));
        CommunityWritingActivity.this.finish();
    }

    void easyToast(Object obj){
        Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_SHORT).show();
    }
}