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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
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

    long walkTime;
    int time;
    int walkMin;
    int walkSec;

    Handler handler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //?????? ?????? ??????
        binding.goToMyCorrection.setText(appData.getDogName().toString());
        TextView tv = findViewById(R.id.nickNameOnNav);
        String nickName = appData.getNickName().toString();
        tv.setText(nickName);
        String imgpath = getCacheDir() + "/profilePic.png";
        Bitmap bm = BitmapFactory.decodeFile(imgpath);
        ImageView imageView = findViewById(R.id.userPhoto);
        imageView.setImageBitmap(bm);

        //mGeofenceList = new ArrayList<>();

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("????????????  %s");
        //binding.walkLength.setText("???????????? : " + totalDistance);

        activityDrawerBinding = ActivityDrawerBinding.inflate(getLayoutInflater());
        //??????????????? ?????? ??????
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

//???????????? ??????
        goToFunc(findViewById(R.id.goToHosp),hospIntent);
        goToFunc(findViewById(R.id.goToWalk),walkIntent);
        goToFunc(findViewById(R.id.goToComm),commIntent);
        goToFunc(findViewById(R.id.goToCareHealth),healthCareIntent);
        goToFunc(findViewById(R.id.goToCareVaccin),careIntent);
        goToFunc(findViewById(R.id.goToCareDaily),dailyCareIntent);
        goToFunc(findViewById(R.id.goToShop),shopIntent);
        goToFunc(findViewById(R.id.goToMyPage),myPageIntent);
        goToFunc(findViewById(R.id.goToQuest),questIntent);

        binding.startWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.GONE);
                binding.finishWalk.setVisibility(View.VISIBLE);
                //binding.walkInfo.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis();

                //????????????
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }

                //???????????? ?????? ????????? ?????? ??????
                settingGPS();

                // ????????? ?????? ??????
                Location userLocation = getMyLocation();

//                if (userLocation != null) {
//                    double myLatitude = userLocation.getLatitude();
//                    double myLongitude = userLocation.getLongitude();
//                }

                //????????? ?????? ??????
                for (int i = 1; i <= 10; i++) {
                    //????????? ?????? ?????? ????????????
                    getWalkSpot(i);

                    Log.d("?????????", boneLatitude+","+boneLongitude);
                }

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 2000);

            }
        });

        binding.finishWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.startWalk.setVisibility(View.VISIBLE);
                binding.finishWalk.setVisibility(View.GONE);
                //binding.walkInfo.setVisibility(View.GONE);

                //???????????? ??????
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }

                //?????? ?????? ???????????? ??????, ??????, ????????? ??????
                stopGPS();


                //???????????? ????????? ??????
                insertWalkInfo(formatted);

            }
        });

        binding.getPointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = new Location("test");
                location.setLatitude(myLatitude);
                location.setLongitude(myLongitude);
                spotDis(location);

                insertPDG();
            }
        });

        requestPermission();

        // ?????? ?????? ??????
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        // getMapAsync??? ???????????? ???????????? onMapReady ?????? ????????? ??????
        // onMapReady?????? NaverMap ????????? ??????
        mapFragment.getMapAsync(this);

    }

    //????????? ?????? ??? ??? ??? ?????? ?????????
    public void checkDog() {
        Intent addNewDogIntent = new Intent(this,addNewDog.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        //???????????????
        String tv_text = "???????????? ??????????????????.";
        builder.setMessage(tv_text);
        //????????????
        builder.setNeutralButton("????????? ????????????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(addNewDogIntent);
            }
        });
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        builder.show();
    }
    //??????????????????
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
            if (!locationSource.isActivated()) { // ?????? ?????????
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource); //????????????

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //???????????? ???????????? ????????????. ????????? onRequestPermissionsResult ?????? ????????? ??????
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.5158, 127.0350), 15);
        naverMap.setCameraPosition(cameraPosition);
    }

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;

    int rqCode = 1004;

    //????????? ?????? ??????
    private Location getMyLocation() {
        Location currentLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //????????? ?????? ??????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    rqCode);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5.0f, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5.0f, locationListener);

            //???????????? ?????? ?????????
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
        return null;
    }

    /**
     * GPS ??? ?????? ?????? ???????????? ????????? ??????
     * LocationManager??? ??????????????? ????????? ??????????????? ???????????? ?????????
     * LocationListener??? ????????? ????????? ?????? ?????? ????????? ?????? ????????? ????????? ???????????? ?????????
     * @return
     */
    public void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        List<LatLng> latLngList;
        latLngList = new ArrayList<>();
        final Location[] currentLocation = {null};

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                // TODO ??????, ????????? ?????? ?????? ???

                if (ActivityCompat.checkSelfPermission(WalkActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        WalkActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5.0f, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5.0f, locationListener);
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
                //locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000,5.0f, locationListener);

                //Get new location
                Location loc2 = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

                //get the current lat and long
                currentLat = loc.getLatitude();
                currentLon = loc.getLongitude();
                Log.d("??????", currentLat+","+currentLon);

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
                binding.walkLength.setText("????????????  " + formatted + "m");

                Log.d("??????", "latitude=" + myLatitude + ",longitude=" + myLongitude);
                Log.d("??????", String.valueOf(distanceMeters));
                Log.d("?????????", Double.toString(totalDistance));

                //????????? ??????????????? ??????
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(myLatitude, myLongitude))
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);

                //???????????? ?????? ??????
                LocationOverlay locationOverlay = naverMap.getLocationOverlay();
                locationOverlay.setVisible(true);
                locationOverlay.setPosition(new LatLng(myLatitude, myLongitude));
                locationOverlay.setIcon(OverlayImage.fromResource(R.drawable.ic_dog));
                //locationOverlay.setIconWidth(120);
                //locationOverlay.setIconHeight(120);
                locationOverlay.setBearing(0);

                //?????? ????????? ??????
                LatLng temp = new LatLng(myLatitude, myLongitude);
                latLngList.add(temp);

                if (latLngList.size() > 1) {
                    path.setCoords(latLngList);
                    path.setColor(Color.YELLOW);
                    path.setMap(naverMap);
                }

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

    //??????????????? ??????????????? ?????? ??????
    public void spotDis(Location location) {
        int spotPlag = 0;
        for(int j=1; j<=10;j++){
//            Toast.makeText(getApplicationContext(), Double.toString(markers[j].getPosition().latitude), Toast.LENGTH_SHORT).show();
            Location spotLocation = new Location(j+"?????? ??????");
            spotLocation.setLatitude(markers[j].getPosition().latitude);
            spotLocation.setLongitude(markers[j].getPosition().longitude);

            Log.i("?????????", String.valueOf(location));
            Log.i("????????????", String.valueOf(spotLocation));
            double spotDis = location.distanceTo(spotLocation);
            //Toast.makeText(getApplicationContext(), Double.toString(spotDis), Toast.LENGTH_SHORT).show();
            if (spotDis<=50) {
                markers[j].setMap(null);
                Toast.makeText(getApplicationContext(), "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                spotPlag = 1;
            }
        }
        if(spotPlag==0){
            Toast.makeText(getApplicationContext(), "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopGPS(){

//      pdg??????
        for(int j=1; j<=10;j++){

            //Log.i(TAG, j+"?????? markers : "+markers[j].getPosition());
            //Log.i("pdg", boneLatitude+","+boneLongitude);
            markers[j].setMap(null);
        }
        //gps ???????????? ??????
        locationManager.removeUpdates(locationListener);

        //path ??????
        path.setMap(null);

        //?????? ??????
        endTime = System.currentTimeMillis();
        Date start = new Date(startTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String rsTime = simpleDateFormat.format(start);
        Toast.makeText(getApplicationContext(), "?????? ????????????"+rsTime,Toast.LENGTH_SHORT).show();

        Date end = new Date(endTime);
        String reTime = simpleDateFormat.format(end);
        Toast.makeText(getApplicationContext(), "?????? ????????????"+reTime,Toast.LENGTH_SHORT).show();

    }

    // ????????? ????????? ????????? ????????? ??????
    public void insertPDG() {
        int point = 1;
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/calcPoint?";
        try {
            url = url + "point=" + point;
            url = url + "&id=" + id;

            Log.i("????????? DB", url);

            final RequestQueue requestQueue = Volley.newRequestQueue(WalkActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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
        questUpdate();
    }

    public void questUpdate(){
        String id = appData.id.toString();
        String url = "http://14.55.65.181/ondaeng/updateQuest?";
        url = url +"id="+id;
        url = url +"&type="+1;

        Log.i("????????? ???????????? DB", url);
        //JSON???????????? ????????? ????????? ???????????????!

        try {
            //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
            // ??????
            final RequestQueue requestQueue = Volley.newRequestQueue(WalkActivity.this);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                        //?????? json????????? ????????? ??????
                        //key?????? ?????? value?????? ?????? ???????????????.
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject data = new JSONObject(jsonObject.getJSONArray("data").get(0).toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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

    //DB??? ????????????, ?????? ??????
    public void insertWalkInfo(String formatted) {
        //Log.i("Info", formatted+","+(SystemClock.elapsedRealtime()-chronometer.getBase())/1000);
        walkTime = (SystemClock.elapsedRealtime()-chronometer.getBase());
        time = (int)(walkTime/1000);
        int walkMin = time % (60 * 60) / 60;
        int walkSec = time % 60;
        String id = appData.id.toString();
        Log.i("????????????", walkMin +"??? "+ walkSec +"???");

        String url = "http://14.55.65.181/ondaeng/insertWalkInfo?";
        try {
            url = url +"id="+id;
            url = url +"&time="+ walkMin +":"+ walkSec;
            url = url +"&dis="+formatted;

            Log.i("????????????,?????? DB", url);

            final RequestQueue requestQueue = Volley.newRequestQueue(WalkActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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

    //DB?????? ????????? ?????? ????????????
    public void getWalkSpot(int spot_no){
//        easyToast("getWalkSpot ?????????");
        String url = "http://14.55.65.181/ondaeng/getWalkSpot?";
        //JSON???????????? ????????? ????????? ??????
        JSONObject testjson = new JSONObject();
        try {
            //????????? spot_no??? url??? ????????? ????????? ??????
            url = url +"spot_no="+spot_no;
            // ??????
            final RequestQueue requestQueue = Volley.newRequestQueue(WalkActivity.this);
//            easyToast(url);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                //????????? ????????? ????????? ?????? ??? ????????? ?????? ???????????????.
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        easyToast("??????");
                        //?????? json????????? ????????? ??????
                        //key?????? ?????? value?????? ?????? ???????????????.
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


                        Log.i(TAG, spot_no+"?????? markers : "+markers[spot_no].getPosition());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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
    //drawer????????????
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