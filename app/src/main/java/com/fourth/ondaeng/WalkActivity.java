package com.fourth.ondaeng;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityDrawerBinding;
import com.fourth.ondaeng.databinding.ActivityWalkBinding;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WalkActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "WalkActivity";
    private ActivityWalkBinding binding;
    private LocationManager locationManager;
    private LocationListener locationListener;

    //    walkingFragment walkingFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    private ActivityDrawerBinding activityDrawerBinding;
    private DrawerLayout drawerLayout;
    private View drawerView;

    double myLatitude;
    double myLongitude;

    String spot_name;
    double boneLatitude;
    double boneLongitude;

    public static Marker markers[] = new Marker[11];
    PathOverlay path = new PathOverlay();

    Long startTime;
    Long endTime;

    Chronometer chronometer;
    boolean running;
    long pauseOffset;

    DecimalFormat myFormatter = new DecimalFormat("0.00");
    double totalDistance = 0.0;
    String formatted;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //mGeofenceList = new ArrayList<>();

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("산책시간 : %s");
        //binding.walkLength.setText("산책거리 : " + totalDistance);

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //네비게이션 메뉴 코드
        drawerLayout = binding.drawerLayout;
        drawerView = (View) findViewById(R.id.drawer);

        Button btn_open = (Button) findViewById(R.id.btn_back);
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
        Intent dailyCareIntent = new Intent(this, DailyActivity.class);
        Intent healthCareIntent = new Intent(this, HealthCheck1Activity.class);
        Intent shopIntent = new Intent(this, Shop.class);
        Intent questIntent = new Intent(this, QuestActivity.class);

        //마이페이지 이동
        findViewById(R.id.goToQuest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(questIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });

        findViewById(R.id.goToMyPage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(myPageIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToShop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(shopIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareVaccin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(careIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareDaily).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(dailyCareIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToCareHealth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(healthCareIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToComm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(commIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToWalk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(walkIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });
        findViewById(R.id.goToHosp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(hospIntent);
                overridePendingTransition(R.anim.horizon_enter, R.anim.none);
                finish();
            }
        });

        binding.startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.GONE);
                binding.finishWalk.setVisibility(View.VISIBLE);
                //binding.walkInfo.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis();

                //스탑워치
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }

                //사용자의 위치 수신을 위한 세팅
                settingGPS();
                // 사용자 현재 위치
                Location userLocation = getMyLocation();

//                if (userLocation != null) {
//                    double myLatitude = userLocation.getLatitude();
//                    double myLongitude = userLocation.getLongitude();
//                }

                //뼈다구 마커 추가
                for (int i = 1; i <= 10; i++) {
                    //편의점 위치 좌표 불러오기
                    getWalkSpot(i);

                    Log.d("뼈다구", boneLatitude+","+boneLongitude);
                }

            }
        });

        binding.finishWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.VISIBLE);
                binding.finishWalk.setVisibility(View.GONE);
                //binding.walkInfo.setVisibility(View.GONE);

                //스톱워치, 거리 중지
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }

                //위치 좌표 불러오기 중지
                stopGPS();

                //뼈다구 마커 없애기


                //산책기록 데이터 저장

            }
        });

        binding.walkLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = new Location("test");
                location.setLatitude(myLatitude);
                location.setLongitude(myLongitude);

                spotDis(location);

            }
        });

        requestPermission();

        // 지도 객체 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // getMapAsync를 호출하여 비동기로 onMapReady 콜백 메서드 호출
        // onMapReady에서 NaverMap 객체를 받음
        mapFragment.getMapAsync(this);

    }

    public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return distance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPermission() {
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                settingGPS();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현재위치

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //현재위치 표시할때 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5158, 127.0350), 15);
        naverMap.setCameraPosition(cameraPosition);
    }

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;

    int rqCode = 1004;

    //사용자 위치 수신
    private Location getMyLocation() {
        Location currentLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    rqCode);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            //수동으로 위치 구하기
//            String locationProvider = LocationManager.GPS_PROVIDER;
//            if (currentLocation != null) {
//                currentLon = currentLocation.getLongitude();
//                currentLat = currentLocation.getLatitude();
//                currentLocation = locationManager.getLastKnownLocation(locationProvider);
//                Log.d("Walk", "latitude=" + currentLat + ", longitude=" + currentLon);
//            }
//
//            lastLat = currentLat;
//            lastLon = currentLon;

        }
        return currentLocation;
    }

    /**
     * GPS 를 받기 위한 매니저와 리스너 설정
     * LocationManager는 디바이스의 위치를 가져오는데 사용하는 매니저
     * LocationListener는 위치가 변할때 마다 또는 상태가 변할 때마다 위치를 가져오는 리스너
     * @return
     */
    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        List<LatLng> latLngList;
        latLngList = new ArrayList<>();
        final Location[] currentLocation = {null};

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
                Log.d("좌표", "latitude=" + myLatitude + ",longitude=" + myLongitude);

                //카메라 현재위치로 이동
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(myLatitude, myLongitude))
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);

                //이동 경로선 생성
                LatLng temp = new LatLng(myLatitude, myLongitude);
                latLngList.add(temp);

                if (latLngList.size() > 1) {
                    path.setCoords(latLngList);
                    path.setColor(Color.YELLOW);
                    path.setMap(naverMap);
                }

                if (ActivityCompat.checkSelfPermission(WalkActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WalkActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String locationProvider = LocationManager.GPS_PROVIDER;
                if (currentLocation[0] != null) {
                    currentLon = currentLocation[0].getLongitude();
                    currentLat = currentLocation[0].getLatitude();
                    currentLocation[0] = locationManager.getLastKnownLocation(locationProvider);
                }

                lastLat = currentLat;
                lastLon = currentLon;

                //start location manager
                Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                //Request new location
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0,0, locationListener);

                //Get new location
                Location loc2 = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                //get the current lat and long
                currentLat = loc.getLatitude();
                currentLon = loc.getLongitude();

                Location locationA = new Location("point A");
                locationA.setLatitude(lastLat);
                locationA.setLongitude(lastLon);

                Location locationB = new Location("point B");
                locationB.setLatitude(currentLat);
                locationB.setLongitude(currentLon);

                double distanceMeters = locationA.distanceTo(locationB);
                if(distanceMeters<100) {
                    totalDistance += distanceMeters;
                }
                double distanceKm = distanceMeters / 1000f;

                formatted = String.format("%.2f",totalDistance);
                binding.walkLength.setText("산책거리 : " + formatted + "m");

                Log.d("거리", String.valueOf(distanceMeters));
                Log.d("총거리", Double.toString(totalDistance));

//                if(lastKnownLocation==null) {
//                    lastKnownLocation = location;
//                }
//                else {
//                    distance=lastKnownLocation.distanceTo(location);
//                    Log.i("Distance","Distance:"+distance);
//                    lastKnownLocation=location;
//                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

    }

    public void spotDis(Location location) {
        for(int j=1; j<=10;j++){
//            Toast.makeText(getApplicationContext(), Double.toString(markers[j].getPosition().latitude), Toast.LENGTH_SHORT).show();
            Location spotLocation = new Location(j+"번째 스팟");
            spotLocation.setLatitude(markers[j].getPosition().latitude);
            spotLocation.setLatitude(markers[j].getPosition().longitude);

            double spotDis = location.distanceTo(spotLocation);
            Toast.makeText(getApplicationContext(), Double.toString(spotDis), Toast.LENGTH_SHORT).show();
            if (spotDis<=50) {
                markers[j].setMap(null);
                Toast.makeText(getApplicationContext(), "스팟에 도착했습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void stopGPS(){

//      pdg제거
        for(int j=1; j<=10;j++){

            //Log.i(TAG, j+"생성 markers : "+markers[j].getPosition());
            //Log.i("pdg", boneLatitude+","+boneLongitude);
            markers[j].setMap(null);
        }
        //gps 업데이트 종료
        locationManager.removeUpdates(locationListener);

        //path 제거
        path.setMap(null);

        //시간 종료
        endTime = System.currentTimeMillis();
        Date start = new Date(startTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String rsTime = simpleDateFormat.format(start);
        Toast.makeText(getApplicationContext(), "산책 시작시간"+rsTime,Toast.LENGTH_SHORT).show();

        Date end = new Date(endTime);
        String reTime = simpleDateFormat.format(end);
        Toast.makeText(getApplicationContext(), "산책 종료시간"+reTime,Toast.LENGTH_SHORT).show();

//        long resTime = endTime - startTime;
//        Date res = new Date(resTime);
//        String intervalTime = simpleDateFormat.format(res);
//        Toast.makeText(getApplicationContext(), "결과"+intervalTime,Toast.LENGTH_SHORT).show();
//
//        //binding.walkTime.setText(intervalTime.toString());
//        binding.chronometer.setText(intervalTime.toString());

    }

    public void distanceToBone() {
        Log.d("PDG", this.boneLatitude+","+WalkActivity.this.boneLongitude);
    }

    //DB에서 뼈다구 좌표 받아오기
    public void getWalkSpot(int spot_no){
//        easyToast("getWalkSpot 실행됨");
        String url = "http://14.55.65.181/ondaeng/getWalkSpot?";
        //JSON형식으로 데이터 통신을 진행
        JSONObject testjson = new JSONObject();
        try {
            //받아온 spot_no를 url에 붙여서 보내줄 준비
            url = url +"spot_no="+spot_no;
            // 전송
            final RequestQueue requestQueue = Volley.newRequestQueue(WalkActivity.this);
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
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());
                        WalkActivity.this.spot_name = data.get("spot_name").toString();
                        boneLatitude = (double) data.get("latitude");
                        boneLongitude = (double) data.get("longitude");
                        //Toast.makeText(getApplicationContext(),"spot_name : " + spot_name + " , latitude : " + boneLatitude + " , longitude : " + boneLongitude,Toast.LENGTH_SHORT).show();

                        Marker marker = new Marker();
                        markers[spot_no] = marker;
                        markers[spot_no].setPosition(new LatLng(boneLatitude, boneLongitude));
                        markers[spot_no].setIcon(OverlayImage.fromResource(R.drawable.bone));
                        markers[spot_no].setWidth(80);
                        markers[spot_no].setHeight(80);
                        markers[spot_no].setMap(naverMap);


                        Log.i(TAG, spot_no+"생성 markers : "+markers[spot_no].getPosition());

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
        return ;
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


}
