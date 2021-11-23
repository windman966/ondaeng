package com.fourth.ondaeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityAddNewDogBinding;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class addNewDog extends AppCompatActivity {
    private ActivityAddNewDogBinding binding;

    //String imgName = binding.dogNameOnAdd.getText().toString();
    String imgName = "test";
    ImageView imageView;
    Bitmap imgBitmap;
    int photoCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewDogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView imageView = binding.dogPhoto;

        Intent intent = new Intent(this,MainActivity.class);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                overridePendingTransition(R.anim.none, R.anim.horizon_exit);
                finish();
            }
        });

        binding.goToSaveMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dogName = binding.dogNameOnAdd.getText().toString();
                String dogBirthYear = binding.dogBirthYearInput.getText().toString();
                String dogBirthMonth = binding.dogBirthMonthInput.getText().toString();
                String dogBirthDay = binding.dogBirthDayInput.getText().toString();
                String dogRegist = binding.dogRegistOnAdd.getText().toString();
                String dogBreed = binding.dogBreedOnAdd.getText().toString();


                if(dogName.isEmpty()||dogBirthDay.isEmpty()||dogBirthMonth.isEmpty()||dogBirthYear.isEmpty()||dogBreed.isEmpty()||dogRegist.isEmpty()||photoCount==0){
                    easyToast("모두 입력해주세요");
                } else {
                    saveBitmapToJpeg(imgBitmap,dogName);    // 내부 저장소에 저장
                    String imgpath = getCacheDir() + "/" + dogName;   // 내부 저장소에 저장되어 있는 이미지 경로
                    Bitmap bm = BitmapFactory.decodeFile(imgpath);
                    imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
                    String birth = dogBirthYear+"-"+dogBirthMonth+"-"+dogBirthDay;
                    insertDogInfo(dogName,birth,dogRegist,dogBreed,"없음");
                }
            }
        });

        binding.dogPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    imgBitmap = BitmapFactory.decodeStream(instream);
                    binding.dogPhoto.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    photoCount++;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap,String dogName) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getCacheDir(), dogName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패1", Toast.LENGTH_SHORT).show();
        }
    }

    //    강아지 정보입력
    public void insertDogInfo(String dogName,String birth,String regist,String breed, String dogPhoto){
        //        easyToast("idCheck 실행됨");
        String url = "http://14.55.65.181/ondaeng/insertDogInfo?";
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
//            id=test3&password=tt&nickname=tt&address=tt&birth=1999-01-01&mobile=010-1234-1234
            String id =(String)appData.id;
            url = url +"id="+id;
            url = url +"&name="+dogName;
            url = url +"&birth="+birth;
            url = url +"&regist="+regist;
            url = url +"&breed="+breed;
            url = url + "&dogPhoto=" +dogPhoto;
            easyToast(url);
            //이제 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(addNewDog.this);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.horizon_exit);
        finish();
    }

    void easyToast(String str){
        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
    }
}

