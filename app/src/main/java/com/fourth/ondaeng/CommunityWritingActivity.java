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

import com.fourth.ondaeng.databinding.ActivityCommunityBinding;
import com.fourth.ondaeng.databinding.ActivityCommunityWritingBinding;

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

        String id = (String)appData.id;

        //넘어온 userid를 변수로 받음 -> login 에서 id 확인하기
        //userid = getIntent().getStringExtra("userid");

        //컴포넌트 초기화
        edit_title = findViewById(R.id.edit_title);
        edit_content = findViewById(R.id.edit_content);
        b_post = findViewById(R.id.b_post);

        //글쓰기 버튼
//        b_post.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RegBoard regBoard = new RegBoard();
//                regBoard.execute(userid, edit_title.getText().toString(), edit_content.getText().toString());
//
//                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
//                intent.putExtra("userid", userid);
//                startActivity(intent);
//            }
//        });


    }

    class RegBoard extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("success")) {
                // 결과값이 success 면 이전 액티비티로 이동
                //CommunityActivity onResume() 함수 호출, 데이터 새로 고침
                Toast.makeText(CommunityWritingActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(CommunityWritingActivity.this, result, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String userid = params[0];
            String title = params[1];
            String content = params[2];
            String category = params[3];
            //String date = params[4];

            String server_url = "http://14.55.65.181/ondaeng/getMemberId?"; //?? url 확인하기


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
                        .appendQueryParameter("userid", userid)
                        .appendQueryParameter("title", title)
                        .appendQueryParameter("content", content)
                        .appendQueryParameter("category", category);
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

}