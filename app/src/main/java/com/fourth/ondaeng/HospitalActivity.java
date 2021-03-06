package com.fourth.ondaeng;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.w3c.dom.Text;

// 여기서 부터 해야할 부분 11.12일 에 마감함
public class HospitalActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {
    private static String TAG = "HospitalActivity";
    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;



    //    walkingFragment walkingFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationSource locationSource;
    private NaverMap naverMap;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        TextView tv = findViewById(R.id.goToMyCorrection);
        tv.setText(appData.dogName.toString());
        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //네비게이션 메뉴 코드
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerView = (View)findViewById(R.id.drawer);

        Button btn_open = (Button)findViewById(R.id.btn_back);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });

        Intent myPageIntent = new Intent(this, myPage.class);
        Intent careIntent = new Intent(this, Vaccination1Activity.class);
        Intent commIntent = new Intent(this, CommunityActivity.class);
        Intent walkIntent = new Intent(this, WalkActivity.class);
        Intent hospIntent = new Intent(this, HospitalActivity.class);
        Intent dailyCareIntent = new Intent(this,DailyActivity.class);
        Intent healthCareIntent = new Intent(this,HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this,Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);

        //드로어 닉네임, 프사 지정.

        TextView textView = findViewById(R.id.nickNameOnNav);
        textView.setText(appData.getNickName().toString());
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);
//액티비티 이동
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);
        /////////////////////////
        InfoWindow infoWindow = new InfoWindow();

        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "정보 창 내용";
            }
        });
        ////////////////////////




//        walkingFragment = new walkingFragment();

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        // 지도 객체 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);




    }

    //강아지 선택 안 할 때 알림 띄우기
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        //타이틀설정
        String tv_text = "강아지를 선택해주세요.";
        builder.setMessage(tv_text);
        //내용설정
        builder.setNeutralButton("강아지 등록하기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("닫기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    };
    //펑션이동하기
    public void goToFunc(View view, Intent intent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appData.getDogName()==""){
                    checkDog();
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(intent);
                    overridePendingTransition(R.anim.horizon_enter,R.anim.none);
                    finish();
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            }else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showCurrentLocation(Location location) {

    }

    public Overlay.OnClickListener hospitalInfo(int a){
        BottomSheetDialog b = new BottomSheetDialog(this);
        b.setContentView(a);
        b.show();
        return null;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현재위치


        // 지도상에 마커 표시 (강남 25시 병원)
          Marker hospital_marker1 = new Marker();
          //마커 이름도 바꾸고
          hospital_marker1.setCaptionText(R.layout.hospital1+"");
          hospital_marker1.setPosition(new LatLng(37.516112, 127.038610));
          hospital_marker1.setMap(naverMap);
          hospital_marker1.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
          // 이걸 쓰면 이상한 숫자가 없어지는데 앱이 죽음 ㅠㅠ hospital_marker1.setCaptionText("");
          // 불투명도 조절
          hospital_marker1.setAlpha(0.5f);
          hospital_marker1.setOnClickListener(this);

        //hospital_marker1.setOnClickListener(this);
        //해결 했다. 이거 써서 바꾸렴


        // 지도상에 마커 표시 (최영민동물의료센터)
        Marker hospital_marker2 = new Marker();
        hospital_marker2.setCaptionText("최영민 동물의료센터");
        hospital_marker2.setPosition(new LatLng(37.515151, 127.032363));
        hospital_marker2.setMap(naverMap);
        hospital_marker2.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        //(마커의 좌우 크기 조절)hospital_marker2.setWidth(50);
        //(마커의 상하 크기 조절)hospital_marker2.setHeight(80);
        // 불투명도 조절
        hospital_marker2.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker2.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital2);
                return false;
            }
        });


        // 지도상에 마커 표시 (마이펫플러스)  + 협약 병원 이야기 넣고 싶을까봐 마커 색깔 바꿔 보았음
        Marker hospital_marker3 = new Marker();
        hospital_marker3.setCaptionText("마이펫 플러스");
        hospital_marker3.setPosition(new LatLng(37.513139, 127.029043));
        hospital_marker3.setMap(naverMap);
        hospital_marker3.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital2));
        // 불투명도 조절
        hospital_marker3.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker3.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital3);
                return false;
            }
        });

        // 지도상에 마커 표시 (그레이스동물병원)
        Marker hospital_marker4 = new Marker();
        hospital_marker4.setCaptionText("그레이스 동물병원");
        hospital_marker4.setPosition(new LatLng(37.514364, 127.035731));
        hospital_marker4.setMap(naverMap);
        hospital_marker4.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker4.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker4.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital4);
                return false;
            }
        });

        // 지도상에 마커 표시 (와이즈동물병원)
        Marker hospital_marker5 = new Marker();
        hospital_marker5.setCaptionText("와이즈 동물병원");
        hospital_marker5.setPosition(new LatLng(37.511226, 127.023858));
        hospital_marker5.setMap(naverMap);
        hospital_marker5.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker5.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker5.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital5);
                return false;
            }
        });

        // 지도상에 마커 표시 (다정동물병원)
        Marker hospital_marker6 = new Marker();
        hospital_marker6.setCaptionText("다정동물병원");
        hospital_marker6.setPosition(new LatLng(37.514294, 127.032403));
        hospital_marker6.setMap(naverMap);
        hospital_marker6.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker6.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker6.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital6);
                return false;
            }
        });

        // 지도상에 마커 표시 (이비치동물치과병원)
        Marker hospital_marker7 = new Marker();
        hospital_marker7.setCaptionText("이비치 동물치과병원");
        hospital_marker7.setPosition(new LatLng(37.522436910099664, 127.03964116923987 ));
        hospital_marker7.setMap(naverMap);
        hospital_marker7.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker7.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker7.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital7);
                return false;
            }
        });

        // 지도상에 마커 표시 (청담동물병원)
        Marker hospital_marker8 = new Marker();
        hospital_marker8.setCaptionText("청담동물병원");
        hospital_marker8.setPosition(new LatLng(37.52139887957226, 127.03649940725467 ));
        hospital_marker8.setMap(naverMap);
        hospital_marker8.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker8.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker8.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital8);
                return false;
            }
        });

        // 지도상에 마커 표시 (치료멍멍동물병원 신사본원)
        Marker hospital_marker9 = new Marker();
        hospital_marker9.setCaptionText("치료멍멍 동물병원");
        hospital_marker9.setPosition(new LatLng(37.5225429741779, 127.03524393713221 ));
        hospital_marker9.setMap(naverMap);
        hospital_marker9.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker9.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker9.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital9);
                return false;
            }
        });

        // 지도상에 마커 표시 (스마트 동물병원 신사본원)
        Marker hospital_marker10 = new Marker();
        hospital_marker10.setCaptionText("스마트 동물병원");
        hospital_marker10.setPosition(new LatLng(37.52088142393659, 127.02995664040422 ));
        hospital_marker10.setMap(naverMap);
        hospital_marker10.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker10.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker10.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital10);
                return false;
            }
        });

        // 지도상에 마커 표시 (에이드동물병원)
        Marker hospital_marker11 = new Marker();
        hospital_marker11.setCaptionText("에이드 동물병원");
        hospital_marker11.setPosition(new LatLng(37.52131537589881, 127.02783205389673 ));
        hospital_marker11.setMap(naverMap);
        hospital_marker11.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker11.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker11.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital11);
                return false;
            }
        });

        // 지도상에 마커 표시 (신사동물병원)
        Marker hospital_marker12 = new Marker();
        hospital_marker12.setCaptionText("신사동물병원");
        hospital_marker12.setPosition(new LatLng(37.51982822705969, 127.02905664225482 ));
        hospital_marker12.setMap(naverMap);
        hospital_marker12.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker12.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker12.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital12);
                return false;
            }
        });

        // 지도상에 마커 표시 (유림동물안과병원 )
        Marker hospital_marker13 = new Marker();
        hospital_marker13.setCaptionText("유림동물안과병원");
        hospital_marker13.setPosition(new LatLng(37.517434465972315, 127.01855485231229 ));
        hospital_marker13.setMap(naverMap);
        hospital_marker13.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker13.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker13.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital13);
                return false;
            }
        });

        // 지도상에 마커 표시 (라파엘동물병원)
        Marker hospital_marker14 = new Marker();
        hospital_marker14.setCaptionText("라파엘 동물병원");
        hospital_marker14.setPosition(new LatLng(37.50904181221696, 127.02203073487722 ));
        hospital_marker14.setMap(naverMap);
        hospital_marker14.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker14.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker14.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital14);
                return false;
            }
        });

        // 지도상에 마커 표시 (커비동물병원)
        Marker hospital_marker15 = new Marker();
        hospital_marker15.setCaptionText("커비동물병원");
        hospital_marker15.setPosition(new LatLng(37.5093157859073, 127.02683696394958 ));
        hospital_marker15.setMap(naverMap);
        hospital_marker15.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker15.setAlpha(0.5f);
        hospital_marker15.setOnClickListener(this);

        // 지도상에 마커 표시 (논현동물병원)
        Marker hospital_marker16 = new Marker();
        hospital_marker16.setCaptionText("논현동물병원");
        hospital_marker16.setPosition(new LatLng(37.50767735873518, 127.0267522582549  ));
        hospital_marker16.setMap(naverMap);
        hospital_marker16.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker16.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker16.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital16);
                return false;
            }
        });

        // 지도상에 마커 표시 (충현동물종합병원)
        Marker hospital_marker17 = new Marker();
        hospital_marker17.setCaptionText("충현동물종합병원");
        hospital_marker17.setPosition(new LatLng(37.509421320394914, 127.03278456718704 ));
        hospital_marker17.setMap(naverMap);
        hospital_marker17.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker17.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker17.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital17);
                return false;
            }
        });

        // 지도상에 마커 표시 (강남동물병원)
        Marker hospital_marker18 = new Marker();
        hospital_marker18.setCaptionText("강남동물병원");
        hospital_marker18.setPosition(new LatLng(37.50800575124282, 127.03445935574726 ));
        hospital_marker18.setMap(naverMap);
        hospital_marker18.setIcon(OverlayImage.fromResource(R.drawable.ic_hospital1));
        // 불투명도 조절
        hospital_marker18.setAlpha(0.5f);
        //해결 했다. 이거 써서 바꾸렴
        hospital_marker18.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                hospitalInfo(R.layout.hospital18);
                return false;
            }
        });



        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //현재위치 표시할때 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5158,127.0350),15);
        naverMap.setCameraPosition(cameraPosition);

//        PathOverlay path = new PathOverlay();
//        path.setCoords((List<LatLng>) locationSource);
//        path.setMap(naverMap);


    }

    // 준석이 추가 한 것 (시작)


    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        //overlay or clicked marker
        if (overlay instanceof Marker) {//마커가 눌려졌는지를 체크
            String hospitalNoStr = ((Marker) overlay).getCaptionText();
            //(잠깐 뜨는 메시지부분)Toast.makeText(this.getApplicationContext(), hospitalNoStr, Toast.LENGTH_LONG).show();
            int hospitalNo = Integer.parseInt(hospitalNoStr);
            //이걸 한번에 줄일 수 있는 것을 고민해 봐야할 듯
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    HospitalActivity.this, R.style.BottomSheetDialogTheme
            );

            View bottomSheetView = LayoutInflater.from(getApplication())
                    .inflate(
                            hospitalNo,
                            (LinearLayout)findViewById(R.id.bottomSheetContainer)
                    );
            // 추후 협약 병원의 진료표를 올린다는 의미에서 추가해 보았음
            bottomSheetView.findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HospitalActivity.this, "준비 중 입니다!", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            return true;
        }

        return false;


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //drawer액티비티
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}


