package com.fourth.ondaeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityLoginBinding;


import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = binding.memPwInput.getText().toString();
                String id = binding.memIdInput.getText().toString();

//                easyToast("로그인 버튼 눌림");
                if(id.isEmpty()||pw.isEmpty()){
                    easyToast("아이디와 비밀번호를 모두 입력해주세요");
                }
                else if(id.equals(id.replaceAll("\\p{Space}",""))&&pw.equals(pw.replaceAll("\\p{Space}",""))){
//                    easyToast("공백문자 검사");
//                    easyToast(id);
//                    easyToast(id.replaceAll("\\p{Space}",""));
                    getPwById(id,pw);
                }
                else{
                    easyToast("id, pw 값에는 공백을 입력할 수 없습니다.");
                }

            }
        });

        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, JoinActivity.class));
                LoginActivity.this.finish();
            }
        });
    }

    public void getPwById(String id, String pw){
//        easyToast("getPwById 실행됨");
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            url = url +"id="+id;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

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
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
                        String dbpw =data.get("password").toString();
//                        easyToast("dbpw : "+dbpw+" , pw : "+pw);
                        if(pw.equals(dbpw)){
//                        로그인 성공시
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                            
                        }
                        else{
                            easyToast("ID혹은 password가 잘못되었습니다");
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


     void easyToast(String str){
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }
}
