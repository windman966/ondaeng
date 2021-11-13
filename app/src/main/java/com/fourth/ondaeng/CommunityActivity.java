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

    ArrayList<postData> postDataList;

    Button b_writing;
    String userid = "";
    Object CommunityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        String id = (String)appData.id;
        getPostLength();



        /*//글쓰기 버튼 클릭 시
        binding.bWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // userid를 갖고 CommunityWriting으로 이동
                Intent intent = new Intent(getApplicationContext(), CommunityWritingActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void getPostLength() {//1
        easyToast("getPost 실행됨");
        String url = "http://14.55.65.181/ondaeng/getPost";//2
        final int[] length = {0};
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.

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
                        postDataList = new ArrayList<postData>();
                        int length = Integer.valueOf(jsonObject.getJSONArray("data").length());
                        easyToast(length);

                        postDataList = new ArrayList<postData>();
                        for(int i=0;i<length;i++){
                            JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(i).toString());
                            String id =data.get("user_id").toString();
                            String title =data.get("title").toString();
                            String date =data.get("date").toString();
                            date = date.substring(9);
                            postDataList.add(new postData(id,title,date));
                        }

                        ListView listView = (ListView)findViewById(R.id.community_listView);
                        final CommunityAdapter myAdapter = new CommunityAdapter(CommunityActivity.this,postDataList);

                        listView.setAdapter(myAdapter);

//                        게시물 클릭시
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView parent, View v, int position, long id){
                                Toast.makeText(getApplicationContext(),
                                        myAdapter.getItem(position).gettitle(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });


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