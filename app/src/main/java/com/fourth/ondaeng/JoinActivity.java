package com.fourth.ondaeng;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityJoinBinding;
import com.fourth.ondaeng.databinding.ActivityLoginBinding;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    ActivityJoinBinding binding;
    final boolean[] idCheckCnt = {false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //아이디 중복검사후 아이디 변경 방지
        binding.memIdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                idCheckCnt[0] = false;
//                easyToast("id변경");
            }
        });

//        아이디 중복검사
        binding.overlapCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.memIdInput.getText().toString();

                if(id.isEmpty()){
                    easyToast("아이디를 입력해주세요");

                }
                else if(id.equals(id.replaceAll("\\p{Space}",""))){
//                    easyToast("공백문자 검사");
//                    easyToast(id);
//                    easyToast(id.replaceAll("\\p{Space}",""));
                    idCheck(id);
                }
                else{
                    easyToast("id 값에는 공백을 입력할 수 없습니다.");
                }

            }
        });

//        회원가입
        binding.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = binding.memIdInput.getText().toString();
                String pw = binding.memPwInput.getText().toString();
                String nn = binding.nickNameInput.getText().toString();
                String ad = binding.memAddressInput.getText().toString();
                String biy = binding.memBirthYearInput.getText().toString();
                String bim = binding.memBirthMonthInput.getText().toString();
                String bid = binding.memBirthDayInput.getText().toString();
                String mo = binding.memMobileInput.getText().toString();
                if(id.isEmpty()||pw.isEmpty()||nn.isEmpty()||ad.isEmpty()||biy.isEmpty()||bim.isEmpty()||bid.isEmpty()||mo.isEmpty()){
                    easyToast("모두 입력해주세요");
                }
                else if(idCheckCnt[0] == false){
                    easyToast("아이디 중복검사를 해주세요");
                }
                else {
                    String bi = biy+"-"+bim+"-"+bid;
                    insertMember(id,pw,nn,ad,bi,mo);
                }
            }
        });

    }
    //뒤로가기 키 눌렀을 때
    @Override
    public void onBackPressed() {
        JoinActivity.this.startActivity(new Intent(JoinActivity.this, LoginActivity.class));
        JoinActivity.this.finish();
    }

//    id중복 체크
    public void idCheck(String id){
    //        easyToast("idCheck 실행됨");
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {

            url = url +"id="+id;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(JoinActivity.this);
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
                        String data = jsonObject.get("data").toString();

                        easyToast(data);
    //                        easyToast("dbpw : "+dbpw+" , pw : "+pw);
                        if(data.equals("[]")){
    //                        로그인 성공시
                            easyToast("사용가능한 아이디 입니다");

                            idCheckCnt[0] = true;
                        }
                        else{
                            easyToast("사용할수 없는 아이디 입니다.");
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

//    회원가입
    public void insertMember(String id,String pw,String nn,String ad,String bi,String mo){
        //        easyToast("idCheck 실행됨");
        String url = "http://14.55.65.181/ondaeng/insertMember?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
//            id=test3&password=tt&nickname=tt&address=tt&birth=1999-01-01&mobile=010-1234-1234
            url = url +"id="+id;
            url = url +"&password="+pw;
            url = url +"&nickname="+nn;
            url = url +"&address="+ad;
            url = url +"&birth="+bi;
            url = url +"&mobile="+mo;
            easyToast(url);
            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(JoinActivity.this);
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
                            //                        회원가입 성공시
                            easyToast("정상적으로 회원가입이 되었습니다.");
                            onBackPressed();

                        }
                        else{
                            easyToast("회원가입이 되지않았습니다.");
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
