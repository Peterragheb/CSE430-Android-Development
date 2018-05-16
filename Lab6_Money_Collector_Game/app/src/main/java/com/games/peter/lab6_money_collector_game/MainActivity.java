package com.games.peter.lab6_money_collector_game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
//TODO HANDLE APP STOP && TRY USING GEOFENCE
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener , GoogleMap.OnMyLocationButtonClickListener,View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private ArrayList<Marker> polygon1Markers,polygon2Markers;
    // The entry points to the Places API.
    //private GeoDataClient mGeoDataClient;
    //private PlaceDetectionClient mPlaceDetectionClient;

    public static final String MY_PREFS_NAME = "MyPrefs";
    public static final String MY_PREFS_NEVER_ASK_AGIN = "never_ask_again";

    // The entry point to the Fused Location Provider.
    //private FusedLocationProviderClient mFusedLocationProviderClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(30.0444, 31.2357);
    private static final int DEFAULT_ZOOM = 14;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static boolean FIRST_TIME = true;
    private boolean mLocationPermissionGranted;
    private int mLastVisitedPolygon;
    private boolean mLocationProviderEnabled;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 3;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP);

    private int score,coins_num=2;
    private boolean game_started=false,show_leaderboard=false;
    private Polygon polygon1,polygon2;
    private TextView tv_remaining_coins,tv_score;
    private FloatingActionButton fab_settings;
    EditText et_coin_number;
    private Button btn_start_game,btn_restart_game,btn_show_leaderboard,btn_submit_settings;
    private LinearLayout linl_top_layout,linl_bottom_layout,linl_bottom_congrat_layout,linl_records_layout,linl_middle_layout;
    LocationManager locationManager;
    private static final String TAG2 = "DBAdapter";
    ListView lv_records;
    DBAdapter dbAdapter;
    ArrayList<Record> records;
    String recommended;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_main);
        tv_remaining_coins=findViewById(R.id.tv_remaining_coins);
        tv_score=findViewById(R.id.tv_score);
        btn_start_game=findViewById(R.id.btn_start_game);
        btn_restart_game=findViewById(R.id.btn_restart_game);
        btn_show_leaderboard=findViewById(R.id.btn_show_leaderboard);
        btn_submit_settings=findViewById(R.id.btn_submit_settings);
        fab_settings=findViewById(R.id.fab_settings);
        et_coin_number=findViewById(R.id.et_coin_number);
        linl_top_layout=findViewById(R.id.linl_top_layout);
        linl_bottom_layout=findViewById(R.id.linl_bottom_layout);
        linl_bottom_congrat_layout=findViewById(R.id.linl_bottom_congrat_layout);
        linl_records_layout=findViewById(R.id.linl_records_layout);
        linl_middle_layout=findViewById(R.id.linl_middle_layout);
        linl_top_layout.setVisibility(View.INVISIBLE);
        linl_bottom_layout.setVisibility(View.INVISIBLE);
        linl_bottom_congrat_layout.setVisibility(View.INVISIBLE);
        linl_records_layout.setVisibility(View.INVISIBLE);
        linl_middle_layout.setVisibility(View.INVISIBLE);
        lv_records=findViewById(R.id.lv_records);
        openDB();

        // Construct a GeoDataClient.
        //mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
       // mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
       // mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_start_game.setOnClickListener(this);
        btn_restart_game.setOnClickListener(this);
        btn_show_leaderboard.setOnClickListener(this);
        fab_settings.setOnClickListener(this);
        btn_submit_settings.setOnClickListener(this);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mMap.setPadding(0, linl_top_layout.getHeight()+5, 0, 0);
            }
        });
        mMap.setOnMyLocationButtonClickListener(MainActivity.this);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if((prefs.contains(MY_PREFS_NEVER_ASK_AGIN)&&prefs.getBoolean(MY_PREFS_NEVER_ASK_AGIN,false)!=true) || (!prefs.contains(MY_PREFS_NEVER_ASK_AGIN))){
                // Prompt the user for permission.
            Log.v("NOPREF","WILL GO GET PERMISSON");
                getLocationPermission();
        }

    }
    private void initStartupComponents(){
        initLocationManager();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        initLocationListener();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Add polygons to indicate areas on the map.
        initpolygons();
    }
    private void initpolygons(){

        // Add polygons to indicate areas on the map.
//        polygon1 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.063686, 31.278328),
//                        new LatLng(30.063686, 31.278348),
//                        new LatLng(30.063728, 31.278348),
//                        new LatLng(30.063728, 31.278328)
//                ));
        polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(30.063879, 31.278259),
                        new LatLng(30.063879, 31.278413),
                        new LatLng(30.063652, 31.278413),
                        new LatLng(30.063652, 31.278259)
                ));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(30.063707,31.278338)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
//        polygon1 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//
//                        new LatLng(30.097950, 31.332192),
//                        new LatLng(30.097950, 31.332081),
//                        new LatLng(30.098157, 31.332081),
//                        new LatLng(30.098157, 31.332192)
//
//                ));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(30.098000,31.332080)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));

        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        // Style the polygon.
        stylePolygon(polygon1);

//        polygon2 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.063232, 31.278523),
//                        new LatLng(30.063232, 31.278581),
//                        new LatLng(30.063264, 31.278581),
//                        new LatLng(30.063264, 31.278523)
//                        ));
        polygon2 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(30.063240, 31.278496),
                        new LatLng(30.063240, 31.278741),
                        new LatLng(30.063121, 31.278741),
                        new LatLng(30.063121, 31.278496)
                ));
////        mMap.addMarker(new MarkerOptions().position(new LatLng(30.063248,31.278552)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
//        polygon2 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.097940, 31.332192),
//                        new LatLng(30.097940, 31.332081),
//                        new LatLng(30.097855, 31.332081),
//                        new LatLng(30.097855, 31.332192)));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(30.097985,31.332100)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
        polygon2.setTag("beta");
        stylePolygon(polygon2);

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.063686, 31.278328), 19));

        // Set listeners for click events.
        mMap.setOnPolygonClickListener(this);

        //tv_remaining_coins.setText(polygon1Markers.size()+"");
        tv_score.setText("0");
        score=0;
        genertateCoins();
        mLastVisitedPolygon=0;
        tv_remaining_coins.setText(polygon1Markers.size()+polygon2Markers.size()+"");
        linl_top_layout.setVisibility(View.VISIBLE);
        linl_bottom_layout.setVisibility(View.VISIBLE);
    }
    private void genertateCoins(){
        polygon1Markers=new ArrayList<>();
        polygon2Markers=new ArrayList<>();
        Random r = new Random();
        double randomLatitude,randomLongitude,leastLatitude, mostLatitude,leastLongitude, mostLongitude;
        double [] polygonlimits;
        for (int i=0;i<2;i++){//change limit of i for more polygons
            polygonlimits=getPolygonLimits(i);
            leastLatitude=polygonlimits[0];
            mostLatitude=polygonlimits[1];
            leastLongitude=polygonlimits[2];
            mostLongitude=polygonlimits[3];
            for (int j=0;j<coins_num;j++){//change value of j for more coins in one polygon
                randomLatitude= leastLatitude + (mostLatitude - leastLatitude) * r.nextDouble();
                randomLongitude= leastLongitude + (mostLongitude - leastLongitude) * r.nextDouble();
                if (i==0)
                    polygon1Markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(randomLatitude,randomLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin))));
                else if (i==1)
                    polygon2Markers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(randomLatitude,randomLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin))));
            }
        }
    }
    private double[] getPolygonLimits(int i){
        ArrayList<LatLng> vertices=new ArrayList<>();
        double [] returnvalues=new double[4];
        double leastLatitude;
        double mostLatitude;
        double leastLongitude;
        double mostLongitude;
        if (i==0){
            vertices=(ArrayList<LatLng>) polygon1.getPoints();
        }
        else if (i==1){
            vertices=(ArrayList<LatLng>) polygon2.getPoints();
        }
        leastLatitude= vertices.get(0).latitude<vertices.get(1).latitude?vertices.get(0).latitude:vertices.get(1).latitude;
        mostLatitude= vertices.get(0).latitude>vertices.get(1).latitude?vertices.get(0).latitude:vertices.get(1).latitude;
        leastLongitude= vertices.get(0).longitude<vertices.get(2).longitude?vertices.get(0).longitude:vertices.get(2).longitude;
        mostLongitude= vertices.get(0).longitude>vertices.get(2).longitude?vertices.get(0).longitude:vertices.get(2).longitude;
        returnvalues[0]=leastLatitude;
        returnvalues[1]=mostLatitude;
        returnvalues[2]=leastLongitude;
        returnvalues[3]=mostLongitude;
        return returnvalues;
    }
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }


    private void initLocationManager(){
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(false);
        recommended = locationManager.getBestProvider(criteria, false);

        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))
            mLocationProviderEnabled=true;
        else
            mLocationProviderEnabled=false;
    }
    @SuppressLint("MissingPermission")
    private void initLocationListener(){
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.v("onLocationChanged","     ====================LOCATION_CHANGED=====================   !!!! ");
                Log.v("onLocationChanged"," Location:       Latitude: "+ location.getLatitude()+"      Longitude:  "+location.getLongitude());
                Log.v("onLocationChanged","POLYGON COORDINATES:       Latitude: "+ polygon1.getPoints().get(0).latitude+"   ----->  "+polygon1.getPoints().get(1).latitude+
                        "\n  \t \t Longitude:  "+polygon1.getPoints().get(0).longitude+"   ----->  "+polygon1.getPoints().get(2).longitude);

                updatetoNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                Log.v("onProviderEnabled","mLocationProviderEnabled       IN    onProviderEnabled        TRUE    !!!! ");
                mLocationProviderEnabled=true;
                updateLocationUI();
            }

            public void onProviderDisabled(String provider) {
                Log.v("onProviderDisabled","mLocationProviderEnabled       IN    onProviderDisabled        FALSE    !!!! ");
                mLocationProviderEnabled=false;
                displayLocationSettingsRequest(MainActivity.this);
            }};
        locationManager.requestLocationUpdates(recommended, 1000, 0, locationListener);
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                if (!mLocationProviderEnabled){
                    Log.v("getDeviceLocation","mLocationProviderEnabled       IN    getDeviceLocation        FALSE    !!!! ");
                    if (locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER)!=null){
                        mLastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                    else {
                        Log.v("getDeviceLocation","mLastKnownLocation       IN    getDeviceLocation        NULL    !!!! ");

                        Log.d(TAG, "Current location is null. Using defaults.");
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }

                }

            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void updatetoNewLocation(Location location){
        Log.v("updatetoNewLocation","CALLED !!!! ");
        mLastKnownLocation=location;
        if (mLastKnownLocation!=null){
            Log.v("updatetoNewLocation","mLastKnownLocation       IN    updatetoNewLocation        NOT NULL    !!!! ");
            if (FIRST_TIME)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 19));
            FIRST_TIME=false;
            //updateLocationUI();
            //===============================================
            if (game_started)
                game_logic();
        }
        else {
            Log.v("updatetoNewLocation","mLastKnownLocation       IN    updatetoNewLocation        NULL    !!!! ");
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    private void game_logic(){
        //if (isPositionInPolygon(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),(ArrayList<LatLng>) polygon1.getPoints())){
        if (PolyUtil.containsLocation(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), polygon1.getPoints(), false)){
            Log.v("INPOLYGON","=============================================================ENTERED POLYGON 1=================================================================================");
            Log.v("mLastVisitedPolygon","mLastVisitedPolygon :"+mLastVisitedPolygon+"");
            if (mLastVisitedPolygon!=1){
                mLastVisitedPolygon=1;
            if (!polygon1Markers.isEmpty()){
                Toast.makeText(MainActivity.this,"Entered Polygon 1",Toast.LENGTH_SHORT).show();
                Log.v("polygon1Markers","Size:"+polygon1Markers.size());
                polygon1Markers.get(polygon1Markers.size()-1).remove();
                polygon1Markers.remove(polygon1Markers.size()-1);
                score+=10;
                tv_score.setText(score+"");
                tv_remaining_coins.setText(polygon1Markers.size()+polygon2Markers.size()+"");
                //tv_remaining_coins.setText(polygon1Markers.size()+"");

            }
        }
        }

           // if (isPositionInPolygon(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),(ArrayList<LatLng>) polygon2.getPoints())){
        if (PolyUtil.containsLocation(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), polygon2.getPoints(), false)){
                Log.v("INPOLYGON","=============================================================ENTERED POLYGON 2=================================================================================");
            Log.v("mLastVisitedPolygon","mLastVisitedPolygon :"+mLastVisitedPolygon+"");
                if (mLastVisitedPolygon!=2){
                    mLastVisitedPolygon=2;
                    if (!polygon2Markers.isEmpty()){
                        Toast.makeText(MainActivity.this,"Entered Polygon 2",Toast.LENGTH_SHORT).show();
                        Log.v("polygon2Markers","Size:"+polygon2Markers.size());
                        polygon2Markers.get(polygon2Markers.size()-1).remove();
                        polygon2Markers.remove(polygon2Markers.size()-1);
                        score+=10;
                        tv_score.setText(score+"");
                        tv_remaining_coins.setText(polygon1Markers.size()+polygon2Markers.size()+"");
                    }
                }
            }
        if (polygon1Markers.isEmpty()&&polygon2Markers.isEmpty()){
            Record record;
            if (Integer.valueOf(tv_score.getText().toString())!=0){
                record=new Record(Calendar.getInstance().getTime(),
                        Integer.valueOf(tv_score.getText().toString()),
                        Integer.valueOf(tv_score.getText().toString())/10);
                Log.v("RECORD INFO:","DATE : "+record.parseDate(null)+" SCORE: "+record.getScore()+" COINS: "+record.getCoins());
                dbAdapter.insertRow(record.parseDate(null),record.getScore(),record.getCoins());
            game_started=false;
            linl_bottom_congrat_layout.setVisibility(View.VISIBLE);
            linl_bottom_congrat_layout.setTranslationY(linl_bottom_congrat_layout.getHeight());
            linl_bottom_congrat_layout.animate().setDuration(500).translationYBy(-linl_bottom_congrat_layout.getHeight());
            fab_settings.setVisibility(View.INVISIBLE);
        }
    }}
    private void reset(){
        score=0;
        game_started=false;
        mLastVisitedPolygon=0;
        tv_remaining_coins.setText(polygon1Markers.size()+polygon2Markers.size()+"");
        //tv_remaining_coins.setText(polygon1Markers.size()+"");
        tv_score.setText("0");
        linl_top_layout.setVisibility(View.VISIBLE);
        linl_bottom_congrat_layout.setVisibility(View.GONE);
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mLocationPermissionGranted = true;
            initStartupComponents();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    initStartupComponents();
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(permissions[0].toString())){
                        Log.v("RequestPermissionResult","WILL ADD TO SHARED PREFS");
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putBoolean(MY_PREFS_NEVER_ASK_AGIN, true);
                        editor.apply();
                    }
                    else
                        showExitorPermitDialog();
                }
                else{
                    showExitorPermitDialog();
                }
            }
        }
        //updateLocationUI();
    }
    private void showExitorPermitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission denied")
                .setMessage("Without this permission the application is unable to operate.\n" +
                        "Are you sure you want to deny this permission?")
                .setCancelable(false)
                .setPositiveButton("I'm Sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Re-try", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getLocationPermission();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        Log.v("updateLocationUI","CALLED !!!! ");
        if (mMap == null) {
            Log.v("updateLocationUI","NMAP   IN  updateLocationUI  IS NULL !!!! ");
            return;
        }
        try {
            Log.v("updateLocationUI","TRY   IN  updateLocationUI  ENTERED !!!! ");
            if (mLocationPermissionGranted) {
                Log.v("updateLocationUI","mLocationPermissionGranted   IN  updateLocationUI  TRUE !!!! ");
                if (mLocationProviderEnabled){
                    Log.v("updateLocationUI","mLocationProviderEnabled   IN  updateLocationUI  TRUE !!!! ");

                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);



                }

            } else {
                Log.v("updateLocationUI","mLocationPermissionGranted   IN  updateLocationUI  FALSE !!!! ");
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mLastKnownLocation = null;
                    getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    private boolean isPositionInPolygon(double latitude,double longitude , ArrayList<LatLng> vertices) {
        boolean result=false;
       if (vertices.size()-1!=4){
           Log.v("isPositionInPolygon","POLYGON SIZE:"+vertices.size());
           Log.v("isPositionInPolygon","POLYGON DOESNT FIT A QUADRATURE STRUCTURE");
           return false;
       }
       double leastLatitude= vertices.get(0).latitude<vertices.get(1).latitude?vertices.get(0).latitude:vertices.get(1).latitude;
       double mostLatitude= vertices.get(0).latitude>vertices.get(1).latitude?vertices.get(0).latitude:vertices.get(1).latitude;
       double leastLongitude= vertices.get(0).longitude<vertices.get(2).longitude?vertices.get(0).longitude:vertices.get(2).longitude;
       double mostLongitude= vertices.get(0).longitude>vertices.get(2).longitude?vertices.get(0).longitude:vertices.get(2).longitude;
       if (latitude>=leastLatitude && latitude<=mostLatitude && longitude>=leastLongitude && longitude<=mostLongitude )
           result=true;
        Log.v("isPositionInPolygon","==========================RESULT:       "+result+"         ============================");
        return result;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });


    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (!mLocationProviderEnabled){
            displayLocationSettingsRequest(MainActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_start_game){
            if (!FIRST_TIME){
                Log.v("onclick","btn_start CLICKED !!!!!!!!!!!!!!!!!!!!!");
                game_started=true;
                //===========================================
                //hide bottom Layout Slide Down
                linl_bottom_layout.setTranslationY(0);
                linl_bottom_layout.animate().setDuration(500).translationYBy(linl_bottom_layout.getHeight()).start();
                //===========================================
                fab_settings.setVisibility(View.VISIBLE);
                return;
            }

        }
        else if (v.getId()==R.id.btn_restart_game){
            reset();
            genertateCoins();
            game_started=true;
            //hide bottom congrats Layout Slide Down
            linl_bottom_congrat_layout.setTranslationY(0);
            linl_bottom_congrat_layout.animate().setDuration(500).translationYBy(linl_bottom_congrat_layout.getHeight()).start();
            //===========================================
            fab_settings.setVisibility(View.VISIBLE);
            return;
        }
        else if (v.getId()==R.id.btn_show_leaderboard){
            show_leaderboard=true;
            displayRecordSet();
            //hide top Layout Slide Up
            linl_top_layout.setTranslationY(0);
            linl_top_layout.animate().setDuration(500).translationYBy(-linl_top_layout.getHeight()).start();
            //===========================================
            //show records Slide Up
            linl_records_layout.setVisibility(View.VISIBLE);
            linl_records_layout.setTranslationY(linl_records_layout.getHeight());
            linl_records_layout.animate().setDuration(500).translationYBy(-linl_records_layout.getHeight()).start();
            //===========================================
            //hide bottom congrats Layout Slide Down
            linl_bottom_congrat_layout.setTranslationY(0);
            linl_bottom_congrat_layout.animate().setDuration(500).translationYBy(linl_bottom_congrat_layout.getHeight()).start();
            //===========================================
            return;
        }
        else if (v.getId()==R.id.fab_settings){
            linl_middle_layout.setVisibility(View.VISIBLE);
            linl_middle_layout.setTranslationX(-linl_middle_layout.getWidth());
            linl_middle_layout.animate().setDuration(500).translationXBy(linl_middle_layout.getWidth()).start();
        }
        else if (v.getId()==R.id.btn_submit_settings){
            if (!et_coin_number.getText().toString().isEmpty()){
                int num_of_coins=Integer.valueOf(et_coin_number.getText().toString());
                if (num_of_coins>0&&num_of_coins<5){
                    coins_num=num_of_coins;
                    Log.v("Coins",coins_num+"");
                    // Check if no view has focus:
                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    mMap.clear();
                    polygon1Markers.clear();
                    polygon2Markers.clear();
                    initStartupComponents();
                    game_started=false;
                    reset();
                    //show bottom congrat Layout Slide Up
                    linl_bottom_layout.setTranslationY(linl_bottom_layout.getHeight());
                    linl_bottom_layout.animate().setDuration(500).translationYBy(-linl_bottom_layout.getHeight()).start();
                    //===========================================
                }
            }
            fab_settings.setVisibility(View.INVISIBLE);
            linl_middle_layout.setVisibility(View.VISIBLE);
            linl_middle_layout.setTranslationX(0);
            linl_middle_layout.animate().setDuration(500).translationXBy(-linl_middle_layout.getWidth()-90).start();
        }
    }


    private void openDB(){
        dbAdapter=new DBAdapter(MainActivity.this);
        dbAdapter.open();
    }
    private void closeDB(){
        if (dbAdapter!=null)
            dbAdapter.close();
    }

    //display all records
    public void displayRecordSet() {
        Cursor cursor=dbAdapter.getAllRows();
        String message = "";
        records=new ArrayList<>();
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String date = cursor.getString(dbAdapter.COL_DATE);
                int score = cursor.getInt(dbAdapter.COL_SCORE);
                int coins = cursor.getInt(dbAdapter.COL_COINS);
                //=====================================

                // Append data to the message:
                message = "date=" + date
                        +", score=" + score
                        +", coins=" + coins
                        +"\n";
                Log.d(TAG2,message);
                Log.d(TAG2,"---------------------");
                //=====================================

                //add users to arraylist
                records.add(new Record(null,date,score,coins));
            } while(cursor.moveToNext());
        }
        //=====================================

        // Close the cursor to avoid a resource leak.
        cursor.close();
        //=====================================
        //sort list of users according to the score
        // Collections.sort(records);
        //=====================================
        //creating new custom adapter
        //CustomAdapter adapter = new CustomAdapter(this, users);

        CustomAdapter adapter = new CustomAdapter(MainActivity.this,R.layout.list_item,records);
        //binding adapter to the listview
        lv_records.setAdapter(adapter);
        //=====================================

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    @Override
    public void onBackPressed() {
        if (show_leaderboard){
            show_leaderboard=false;
            //show top Layout Slide Down
            linl_top_layout.setTranslationY(-linl_top_layout.getHeight());
            linl_top_layout.animate().setDuration(500).translationYBy(linl_top_layout.getHeight()).start();
            //===========================================
            //hide records Slide Down
            linl_records_layout.setTranslationY(0);
            linl_records_layout.animate().setDuration(500).translationYBy(linl_records_layout.getHeight()).start();
            //===========================================
            //show bottom congrat Layout Slide Up
            linl_bottom_congrat_layout.setTranslationY(linl_bottom_congrat_layout.getHeight());
            linl_bottom_congrat_layout.animate().setDuration(500).translationYBy(-linl_bottom_congrat_layout.getHeight()).start();
            //===========================================
        }
        else
            super.onBackPressed();
    }
}

