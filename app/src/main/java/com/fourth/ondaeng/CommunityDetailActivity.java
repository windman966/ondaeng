package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityCommunityDetailBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CommunityDetailActivity extends AppCompatActivity {

    ActivityCommunityDetailBinding binding;
    //수정삭제 전처리
    ListView listView;
    ArrayList<String> items;

//    int getNo = getIntent().getIntExtra("postNo",1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int getNo = getIntent().getIntExtra("postNo",1);

        readPost(getNo);

        //수정, 삭제 전처리
        items = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_community, items);

        //'목록으로' 버튼
        binding.bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // !! 로그인 된 유저가 쓴 글만 수정, 삭제 되도록

        //'수정' 버튼
        binding.bModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check;
                int count = adapter.getCount();
                if (count > 0) {
                    check = listView.getCheckedItemPosition(); //선택 항목 position 얻기
                    if (check > -1 && check < count)
                        items.set(check, Integer.toString(check + 1) + ") " + binding.tvTitle.getText() + " (수정됨)");
                        items.set(check, Integer.toString(check + 1) + ") " + binding.tvContent.getText() + " (수정됨)");
                    adapter.notifyDataSetChanged();
                    binding.tvTitle.setText("");
                    binding.tvContent.setText("");
                }
            }
        });


        //'삭제' 버튼
        binding.bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check, count= adapter.getCount();
                if (count>0){
                    check = listView.getCheckedItemPosition();
                    if (check>-1 && check<count){
                        items.remove(check);
                        listView.clearChoices();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

//    게시물 읽어오기
    public void readPost(int postNo){
        String url = "http://14.55.65.181/ondaeng/readPost?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            url = url +"postNo="+postNo;

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityDetailActivity.this);

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
                        //easyToast(Integer.valueOf(jsonObject.getJSONArray("data").length()));
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
                        String title =data.get("title").toString();
                        String content =data.get("content").toString();
                        binding.tvTitle.setText(title);
                        binding.tvContent.setText(content);

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
        CommunityDetailActivity.this.startActivity(new Intent(CommunityDetailActivity.this, CommunityActivity.class));
        CommunityDetailActivity.this.finish();
    }

    void easyToast(Object obj){
        Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_SHORT).show();
    }
}