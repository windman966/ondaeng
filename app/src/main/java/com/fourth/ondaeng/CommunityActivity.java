package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
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

        list_community_items = new ArrayList<community_listitems>();

        //커뮤니티 게시물(인앱)
        // userid, title, content, category, date
        community_listitems.add(new community_listitems("닉네임1", "간식 추천", "강아지 간식 추천해주세요", "양육 꿀팁", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임2", "장난감 나눔해요", "강아지 장난감 무료나눔합니다", "나눔", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임3", "샵 추천", "학동역 미용 잘 하는 곳 추천해주세요", "내 동네", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임4", "배변 훈련방법", "강아지 배변 훈련하는 방법 알려드려요!", "양육 꿀팁", new Date(System.currentTimeMillis())));
        community_listitems.add(new community_listitems("닉네임5", "간식 나눔", "강아지 간식 나눔합니다", "나눔", new Date(System.currentTimeMillis())));

        //커뮤니티 어댑터
        CommunityAdapter communityAdapter;
        ArrayList<community_listitems> community_listitems = null;
        Object list_community_items;

        //어댑터 연결, 객체생성
        communityAdapter = new CommunityAdapter(getApplicationContext(), community_listitems);
        //communityAdapter = new CommunityAdapter(MainActivity.this, (ArrayList<com.fourth.ondaeng.community_listitems>) list_community_items);
        binding.communityListView.setAdapter(communityAdapter);

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
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
    }


    // onResume() 해당 액티비티가 화면에 나타날 때 호출됨
    @Override
    protected void onResume() {
        super.onResume();
        // 해당 액티비티가 활성화되면 게시물 리스트를 불러오는 함수 호출
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }


    // 게시물 리스트를 읽어오는 함수
    class GetBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 배열 초기화
            titleList.clear();
            seqList.clear();

            try {

                // 결과물이 JSONArray 형태로 넘어오기 때문에 파싱
                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.optString("title");
                    String seq = jsonObject.optString("seq");

                    // title, seq 값을 변수로 받아서 배열에 추가
                    titleList.add(title);
                    seqList.add(seq);

                }

                // ListView 에서 사용할 arrayAdapter를 생성하고, ListView 와 연결
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(CommunityActivity.this, R.layout.item_community, titleList);
                listView.setAdapter(arrayAdapter);

                // arrayAdapter의 데이터가 변경되었을때 새로고침
                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        @Override
        protected String doInBackground(String... params) {

            // String userid = params[0];
            // String passwd = params[1];

            String server_url = "http://14.55.65.181/ondaeng/getMemberId?"; // ?? 여기 url 확인하기


            URL url;
            String response = "";
            try {
                url = new URL(server_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", "");
                // .appendQueryParameter("passwd", passwd);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response += line;
                    }
                }
                else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
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

    }