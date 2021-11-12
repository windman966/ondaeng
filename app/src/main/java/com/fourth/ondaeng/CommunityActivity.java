package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityLoginBinding;

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
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class CommunityActivity extends AppCompatActivity {

    ActivityCommunityBinding binding;

    private ListView listView;
    Button b_writing;
    String userid = "";


    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();
    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();
    //커뮤니티 어댑터

    private CommunityAdapter communityAdapter;
    private ArrayList<community_listitems> community_listitems;
    private Object list_community_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = (String)appData.id;

        getPost(null);

        /*
//        list_community_items = new ArrayList<community_listitems>();
//
//        //커뮤니티 게시물(인앱)
//        // userid, title, content, category, date
//        community_listitems.add(new community_listitems("닉네임1", "간식 추천", "강아지 간식 추천해주세요", "양육 꿀팁", new Date(System.currentTimeMillis())));
//        community_listitems.add(new community_listitems("닉네임2", "장난감 나눔해요", "강아지 장난감 무료나눔합니다", "나눔", new Date(System.currentTimeMillis())));
//        community_listitems.add(new community_listitems("닉네임3", "샵 추천", "학동역 미용 잘 하는 곳 추천해주세요", "내 동네", new Date(System.currentTimeMillis())));
//        community_listitems.add(new community_listitems("닉네임4", "배변 훈련방법", "강아지 배변 훈련하는 방법 알려드려요!", "양육 꿀팁", new Date(System.currentTimeMillis())));
//        community_listitems.add(new community_listitems("닉네임5", "간식 나눔", "강아지 간식 나눔합니다", "나눔", new Date(System.currentTimeMillis())));
//
//        //커뮤니티 어댑터
//        CommunityAdapter communityAdapter;
//        ArrayList<community_listitems> community_listitems = null;
//        Object list_community_items;
//
//        //어댑터 연결, 객체생성
//        communityAdapter = new CommunityAdapter(getApplicationContext(), community_listitems);
//        //communityAdapter = new CommunityAdapter(MainActivity.this, (ArrayList<com.fourth.ondaeng.community_listitems>) list_community_items);
//        binding.communityListView.setAdapter(communityAdapter);

         */

        /*
        // listView 를 클릭했을 때
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 게시물의 번호와 userid로 CommunityDetailActivity 로 이동
                Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                // CommunityDetailActivity, xml 따로 추가해야 됨

                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userid);
                startActivity(intent);

            }
        });

         */

        //글쓰기 버튼 클릭 시
        binding.bWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // userid를 갖고 CommunityWriting으로 이동
                Intent intent = new Intent(getApplicationContext(), CommunityWritingActivity.class);
                startActivity(intent);
            }
        });
    }



        //스피너
//        Spinner spinner = findViewById(R.id.spinner);
         /*spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent.getItemAtPosition(position);
            }
        });

        spinner.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/

    public void getPost(String category) {//1
        easyToast("getPost 실행됨");
        String url = "http://14.55.65.181/ondaeng/getPost";//2
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

            //url = url +"id="+id;//3

            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(CommunityActivity.this);

            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        easyToast("응답");
                        //받은 json형식의 응답을 받아
                        //key값에 따라 value값을 쪼개 받아옵니다.
                        JSONObject jsonObject = new JSONObject(response.toString());
                        easyToast("test");
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());
                        easyToast(length);

//                        Log.i("length", String.valueOf(jsonObject.getJSONArray("data").length()));


//                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
//                            String dbpw =data.get("password").toString();//4
//                        easyToast("dbpw : "+dbpw+" , pw : "+pw);


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

    void easyToast(Object obj){
        Toast.makeText(getApplicationContext(),obj.toString(),Toast.LENGTH_SHORT).show();
    }
}