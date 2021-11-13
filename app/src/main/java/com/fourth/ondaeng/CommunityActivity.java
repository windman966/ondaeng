package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityCommunityBinding;

import org.json.JSONObject;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {

    ActivityCommunityBinding binding;

    ListView listView;
    CommunityAdapter communityAdapter;
    ArrayList<community_listitems> community_listitems;
    //ArrayList<String> community_listitems;
    Button b_writing;
    String userid = "";
    Object CommunityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = (String)appData.id;
        getPostLength(null);


        /*community_listitems = new ArrayList<community_listitems>();
        communityAdapter = new CommunityAdapter(this, R.layout.item_community, community_listitems);

        listView = binding.communityListView;
        //listView = (ListView) findViewById(R.id.community_listView);
        listView.setAdapter((ListAdapter) CommunityAdapter);

        for(int i = 0; i < community_listitems.size(); i++) {
            community_listitems.add(); //게시물 하나
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CommunityActivity.this, position + "번째 글", Toast.LENGTH_SHORT).show();
            }
        });*/



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

    public void getPostLength(String category) {//1
        easyToast("getPost 실행됨");
        String url = "http://14.55.65.181/ondaeng/getPostCount";//2
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
//                        easyToast(length)
//                        Log.i("length", String.valueOf(jsonObject.getJSONArray("data").length()));


                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
                        int length = (int) data.get("count(*)");//4
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