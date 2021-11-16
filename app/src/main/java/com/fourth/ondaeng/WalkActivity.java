package com.fourth.ondaeng;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fourth.ondaeng.databinding.ActivityWalkBinding;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
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

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    /**
     * Provides access to the Geofencing API.
     */
    GeofencingClient mGeofencingClient;
    /**
     * The list of geofences used in this sample.
     */
    ArrayList<Geofence> mGeofenceList;
    /**
     * Used when requesting to add or remove geofences.
     */
    PendingIntent mGeofencePendingIntent;
    PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;

    String spot_name;
    double boneLatitude;
    double boneLongitude;

    public Marker markers[] = new Marker[11];
    PathOverlay path = new PathOverlay();

    Long startTime;
    Long endTime;

    Chronometer chronometer;
    boolean running;
    long pauseOffset;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("산책시간 : %s");
        binding.walkLength.setText("거리 테스트");

        binding.startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.GONE);
                binding.finishWalk.setVisibility(View.VISIBLE);
                //binding.walkInfo.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis();

                //스탑워치
                if(!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }

                //사용자의 위치 수신을 위한 세팅
                settingGPS();
                // 사용자 현재 위치
                Location userLocation = getMyLocation();

                if (userLocation != null) {
                    double myLatitude = userLocation.getLatitude();
                    double myLongitude = userLocation.getLongitude();
                }

                //현재위치 경로선 나타내기

                //스톱워치, 경로선 거리 나타내기
                //timeThread = new Thread(new timeThread());
                //timeThread.start();

                //뼈다구 마커 추가
                for (int i = 1; i <= 10; i++) {
                    //편의점 위치 좌표 불러오기
                    getWalkSpot(i);
                }

                mGeofenceList = new ArrayList<>();

                mGeofencePendingIntent = null;

                //mGeofenceList();

                mGeofencingClient = LocationServices.getGeofencingClient(WalkActivity.this);

            }
        });

        binding.finishWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.VISIBLE);
                binding.finishWalk.setVisibility(View.GONE);
                //binding.walkInfo.setVisibility(View.GONE);

                //스톱워치, 거리 중지
                if(running){
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

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
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

    @Override
    protected void onStart() {
        super.onStart();
//        if(!checkPermission()) {
//            requestPermissions();
//        }else{
//            performPendingGeofenceTask();
//        }
        Log.d(TAG, "onStart");
    }

//    //모니터링할 지오펜싱을 지정하고 관련 지오펜싱 이벤트가 트리거되는 방법 설
//    private GeofencingRequest getGeofencingRequest() {
//        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
//
//        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
//        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
//        // is already inside that geofence.
//        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
//
//        // Add the geofences to be monitored by geofencing service.
//        builder.addGeofences(mGeofenceList);
//
//        // Return a GeofencingRequest.
//        return builder.build();
//
//    }
//
//    /**
//     * Adds geofences. This method should be called after the user has granted the location
//     * permission.
//     */
//    @SuppressWarnings("MissingPermission")
//    private void addGeofences() {
//        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
//                .addOnCompleteListener(this);
//    }
//
//    /**
//     * Removes geofences. This method should be called after the user has granted the location
//     * permission.
//     */
//    @SuppressWarnings("MissingPermission")
//    private void removeGeofences() {
//        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
//    }
//
//    private boolean checkPermissions() {
//        int permissionState = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }
//
//    /**
//     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
//     * specified geofences. Handles the success or failure results returned by addGeofences().
//     */
//    public void addGeofencesButtonHandler(View view) {
//        if (!checkPermissions()) {
//            mPendingGeofenceTask = PendingGeofenceTask.ADD;
//            requestPermissions();
//            return;
//        }
//        addGeofences();
//    }
//
//    /**
//     * Removes geofences, which stops further notifications when the device enters or exits
//     * previously registered geofences.
//     */
//    public void removeGeofencesButtonHandler(View view) {
//        if (!checkPermissions()) {
//            mPendingGeofenceTask = PendingGeofenceTask.REMOVE;
//            requestPermissions();
//            return;
//        }
//        removeGeofences();
//    }
//
//    /**
//     * Runs when the result of calling {@link #addGeofences()} and/or {@link #removeGeofences()}
//     * is available.
//     * @param task the resulting Task, containing either a result or error.
//     */
//    @Override
//    public void onComplete(@NonNull Task<Void> task) {
//        mPendingGeofenceTask = PendingGeofenceTask.NONE;
//        if (task.isSuccessful()) {
//            updateGeofencesAdded(!getGeofencesAdded());
//            setButtonsEnabledState();
//
//            int messageId = getGeofencesAdded() ? R.string.geofences_added :
//                    R.string.geofences_removed;
//            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
//        } else {
//            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
//            Log.w(TAG, errorMessage);
//        }
//    }
//
//    /**
//     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
//     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
//     * current list of geofences.
//     *
//     * @return A PendingIntent for the IntentService that handles geofence transitions.
//     */
//    private PendingIntent getGeofencePendingIntent() {
//        // Reuse the PendingIntent if we already have it.
//        if (mGeofencePendingIntent != null) {
//            return mGeofencePendingIntent;
//        }
//        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
//        // addGeofences() and removeGeofences().
//        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return mGeofencePendingIntent;
//    }
//
//    /**
//     * This sample hard codes geofence data. A real app might dynamically create geofences based on
//     * the user's location.
//     */
//    private void mGeofenceList() {
//        for (Map.Entry<String, com.google.android.gms.maps.model.LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {
//
//            mGeofenceList.add(new Geofence.Builder()
//                    // Set the request ID of the geofence. This is a string to identify this
//                    // geofence.
//                    .setRequestId(entry.getKey())
//
//                    // Set the circular region of this geofence.
//                    .setCircularRegion(
//                            entry.getValue().latitude,
//                            entry.getValue().longitude,
//                            Constants.GEOFENCE_RADIUS_IN_METERS
//                    )
//
//                    // Set the expiration duration of the geofence. This geofence gets automatically
//                    // removed after this period of time.
//                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//
//                    // Set the transition types of interest. Alerts are only generated for these
//                    // transition. We track entry and exit transitions in this sample.
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
//                            Geofence.GEOFENCE_TRANSITION_EXIT)
//
//                    // Create the geofence.
//                    .build());
//        }
//    }
//
//    /**
//     * Performs the geofencing task that was pending until location permission was granted.
//     */
//    private void performPendingGeofenceTask() {
//        if (mPendingGeofenceTask == PendingGeofenceTask.ADD) {
//            addGeofences();
//        } else if (mPendingGeofenceTask == PendingGeofenceTask.REMOVE) {
//            removeGeofences();
//        }
//    }

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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //현재위치

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //현재위치 표시할때 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5158,127.0350),15);
        naverMap.setCameraPosition(cameraPosition);

    }

    int rqCode = 1004;
    //사용자 위치 수신
    private Location getMyLocation() {
        Location currentLocation = null;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(
                        this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //사용자 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    rqCode);
        }else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            //수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if(currentLocation!=null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("Walk", "latitude="+lat+", longitude="+lng);
            }
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

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double myLatitude = location.getLatitude();
                double myLongitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
                Log.d("좌표", "latitude="+myLatitude+",longitude="+myLongitude);

                LatLng temp = new LatLng(myLatitude,myLongitude);
                latLngList.add(temp);

                if(latLngList.size()>1) {
                    path.setCoords(latLngList);
                    path.setColor(Color.YELLOW);
                    path.setMap(naverMap);
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

    }

    public void stopGPS(){

//      pdg제거
        for(int j=1; j<=10;j++){

//                    Log.i(TAG, j+"생성 markers : "+markers[j].getPosition());
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
        Toast.makeText(getApplicationContext(), "시작"+rsTime,Toast.LENGTH_SHORT).show();

        Date end = new Date(endTime);
        String reTime = simpleDateFormat.format(end);
        Toast.makeText(getApplicationContext(), "종료"+reTime,Toast.LENGTH_SHORT).show();

        long resTime = endTime - startTime;
        Date res = new Date(resTime);
        String intervalTime = simpleDateFormat.format(res);
        Toast.makeText(getApplicationContext(), "결과"+intervalTime,Toast.LENGTH_SHORT).show();
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
                        WalkActivity.this.boneLatitude = (double) data.get("latitude");
                        WalkActivity.this.boneLongitude = (double) data.get("longitude");
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
