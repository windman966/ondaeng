package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityHealthcheck2Binding;

import org.json.JSONObject;

import java.util.ArrayList;


public class HealthCheck2Activity extends AppCompatActivity {

    ActivityHealthcheck2Binding binding;

    ArrayList<HealthcheckData> HealthcheckDataList;
    ListView listView;
    HealthCheckAdapter healthcheckAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHealthcheck2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String part = intent.getStringExtra("part");
        if(part.equals("eye")) {
            binding.eyeLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("ear")) {
            binding.earLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("face")) {
            binding.faceLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("mouth")) {
            binding.mouthLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("action")) {
            binding.actionLayout.setVisibility(View.VISIBLE);
        } else if(part.equals("bone")) {
            binding.boneLayout.setVisibility(View.VISIBLE);
        }

//        binding.eyeButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String symptom = binding.eyeButton1.getText().toString();
//                getExplanation(symptom);
//
//            }
//
//        });
        Button.OnClickListener clickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), symptom, Toast.LENGTH_SHORT).show();
                // getExplanation(symptom);


            }
        };
        binding.eyeButton1.setOnClickListener(clickListener);
    }

    private void getExplanation(String symptom) {
        String url = "http://14.55.65.181/ondaeng/getDisease?";//2
        url = url +"symptom=%25"+symptom+"%25";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(HealthCheck2Activity.this);

            //easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //easyToast("응답");
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
                        //easyToast("test");
                        HealthcheckDataList = new ArrayList<HealthcheckData>();
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());
                        Toast.makeText(getApplicationContext(), Integer.toString(length), Toast.LENGTH_SHORT).show();

                        HealthcheckDataList = new ArrayList<HealthcheckData>();
                        for(int i=length-1;i>=0;i--){
                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(i).toString());
                            String diseaseName = data.get("diseaseName").toString();
                            String explanation = data.get("explanation").toString();
                            String cause = data.get("cause").toString();
                            String symptom = data.get("symptom").toString();
                            String preventive = data.get("preventive").toString();
                            HealthcheckDataList.add(new HealthcheckData(diseaseName, explanation, cause, symptom, preventive));
                        }

                        ListView listView = (ListView)findViewById(R.id.Healthckeck_listView);
                        final HealthCheckAdapter myAdapter = new HealthCheckAdapter(HealthCheck2Activity.this,HealthcheckDataList);

                        listView.setAdapter(myAdapter);


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