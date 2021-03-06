package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityCorrMyPageBinding;

import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class CorrMyPage extends AppCompatActivity {
    private ActivityCorrMyPageBinding binding;

    String imgName = "profilePic.png";
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCorrMyPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = binding.corrUserPhoto;



        binding.corrUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

        Intent intent = new Intent(this, myPage.class);

        binding.goToSaveMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.horizon_exit);
                finish();
            }
        });

        //???????????? ?????? ?????????????????? URL
        String url = "http://14.55.65.181/ondaeng/getMemberId?";
        //???????????? ???????????? ????????????
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.horizon_exit);
                finish();
            }
        });
        //?????????????????????
        try {
            String id = (String) appData.id;
            url = url + "id=" + id;
            final RequestQueue requestQueue = Volley.newRequestQueue(CorrMyPage.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());

                        binding.nickNameOnNav.setText(data.get("nickname").toString());
                        binding.idOnNav.setText(data.get("id").toString());
                        binding.addrOnNav.setText(data.get("address").toString());
                        binding.birthOnNav.setText(data.get("birth").toString().substring(0,10));
                        binding.mobileOnNav.setText(data.get("mobile").toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // ?????????
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imageView.setImageBitmap(imgBitmap);    // ????????? ????????? ??????????????? ???
                    instream.close();   // ????????? ????????????
                    saveBitmapToJpeg(imgBitmap);    // ?????? ???????????? ??????
                    String imgpath = getCacheDir() + "/" + imgName;   // ?????? ???????????? ???????????? ?????? ????????? ??????

                    Bitmap bm = BitmapFactory.decodeFile(imgpath);
                    imageView.setImageBitmap(bm);   // ?????? ???????????? ????????? ???????????? ??????????????? ???


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // ????????? ????????? ?????? ???????????? ??????
        File tempFile = new File(getCacheDir(), imgName);    // ?????? ????????? ?????? ??????
        try {
            tempFile.createNewFile();   // ???????????? ??? ????????? ????????????
            FileOutputStream out = new FileOutputStream(tempFile);  // ????????? ??? ??? ?????? ???????????? ????????????
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress ????????? ????????? ???????????? ???????????? ????????????
            out.close();    // ????????? ????????????
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, myPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.horizon_exit);
        finish();
    }
}

