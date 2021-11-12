package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fourth.ondaeng.databinding.ActivityCommunityBinding;

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

import javax.net.ssl.HttpsURLConnection;

public class CommunityDetailActivity extends AppCompatActivity {

    ActivityCommunityBinding binding;

    TextView tv_title;
    TextView tv_content;
    Button b_back;

    String board_seq = ""; //선택한 게시물의 번호
    String userid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //CommunityActivity에서 넘긴 변수
        board_seq = getIntent().getStringExtra("board_seq");
        userid = getIntent().getStringExtra("userid");

        //컴포넌트 초기화
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        b_back = findViewById(R.id.b_back);

        //목록으로 버튼 누르면 뒤로가기
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });


        //해당 게시물 데이터 불러오기
        //InitData();
    }

    /*
    private void InitData() {

        LoadBoard loadBoard = new LoadBoard();
        loadBoard.execute(board_seq);

    }

    class LoadBoard extends AsyncTask<String, void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                // 결과값이 JSONArray 형태로 넘어오기 때문에
                // JSONArray, JSONObject 를 사용해서 파싱
                JSONArray jsonArray = null;
                jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Database 의 데이터들을 변수로 저장한 후 해당 TextView 에 데이터 입력
                    String title = jsonObject.optString("title");
                    String content = jsonObject.optString("content");

                    tv_title.setText(title);
                    tv_content.setText(content);
                }

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

        @Override
        protected String doInBackground(String... params) {

            String board_seq = params[0];
            String server_url = "http://14.55.65.181/ondaeng/getMemberId?";


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
                        .appendQueryParameter("board_seq", board_seq);
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
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
     */

}