package com.games.peter.lab6_money_collector_game;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(30.0444, 31.2357);
    private static final int DEFAULT_ZOOM = 19;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static boolean FIRST_TIME = true;
    private boolean mLocationPermissionGranted;

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

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA = Arrays.asList(DOT, GAP, DASH, GAP);


    private Polygon polygon1,polygon2;

    LocationManager locationManager;
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

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



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
        // Prompt the user for permission.
        getLocationPermission();
        initLocationService();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();




        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        // Add polygons to indicate areas on the map.
        initpolygons();
    }
    private void initpolygons(){

        // Add polygons to indicate areas on the map.
//        polygon1 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.063686, 31.278328),
//                        new LatLng(30.063728, 31.278328),
//                        new LatLng(30.063728, 31.278348),
//                        new LatLng(30.063686, 31.278348)
//                ));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(30.063707,31.278338)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
        polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(30.098012, 31.332073),
                        new LatLng(30.097959, 31.332073),
                        new LatLng(30.097959, 31.332094),
                        new LatLng(30.098012, 31.332094)
                ));
        mMap.addMarker(new MarkerOptions().position(new LatLng(30.098000,31.332080)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        // Style the polygon.
        stylePolygon(polygon1);

//        polygon2 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.063264, 31.278523),
//                        new LatLng(30.063232, 31.278523),
//                        new LatLng(30.063232, 31.278581),
//                        new LatLng(30.063264, 31.278581)));
////        mMap.addMarker(new MarkerOptions().position(new LatLng(30.063248,31.278552)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
//        polygon2 = mMap.addPolygon(new PolygonOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(30.097987, 31.332098),
//                        new LatLng(30.097984, 31.332098),
//                        new LatLng(30.097984, 31.332103),
//                        new LatLng(30.097987, 31.332103)));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(30.097985,31.332100)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_coin)));
        //polygon2.setTag("beta");
        //stylePolygon(polygon2);

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.063686, 31.278328), 19));

        // Set listeners for click events.
        mMap.setOnPolygonClickListener(this);
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

    private void initLocationService(){
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                    Log.v("onLocationChanged","     ====================LOCATION_CHANGED=====================   !!!! ");
                Log.v("onLocationChanged"," Location:       Latitude: "+ location.getLatitude()+"      Longitude:  "+location.getLongitude());
                Log.v("onLocationChanged","POLYGON COORDINATES:       Altitude: "+ polygon1.getPoints().get(0).latitude+"   ----->  "+polygon1.getPoints().get(1).latitude+
                        "\n  \t \t Longitude:  "+polygon1.getPoints().get(0).longitude+"   ----->  "+polygon1.getPoints().get(2).longitude);

                updatetoNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
                Log.v("onProviderEnabled","mLocationProviderEnabled       IN    onProviderEnabled        TRUE    !!!! ");
                mLocationProviderEnabled=true;
            }

            public void onProviderDisabled(String provider) {
                Log.v("onProviderDisabled","mLocationProviderEnabled       IN    onProviderDisabled        FALSE    !!!! ");
                mLocationProviderEnabled=false;
            }
    };
        if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER))
            mLocationProviderEnabled=true;
        else
            mLocationProviderEnabled=false;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
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
                    Log.v("getDeviceLocation","mLocationProviderEnabled       IN    getDeviceLocation        TRUE    !!!! ");
                    mLastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    if (mLastKnownLocation!=null){
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                    else {
                        Log.v("getDeviceLocation","mLocationProviderEnabled       IN    getDeviceLocation        FALSE    !!!! ");

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            FIRST_TIME=false;
            updateLocationUI();
            //===============================================
            if (isPointInPolygon(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),(ArrayList<LatLng>) polygon1.getPoints())){
                Log.v("INPOLYGON","=============================================================ENTERED POLYGON=================================================================================");
                Toast.makeText(MainActivity.this,"Entered Polygon 1",Toast.LENGTH_SHORT).show();
            }

//            else if (PolyUtil.containsLocation(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),polygon2.getPoints(),false))
//                Toast.makeText(this,"Entered Polygon 2",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.v("updatetoNewLocation","mLastKnownLocation       IN    updatetoNewLocation        NULL    !!!! ");
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
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
        if ((ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
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

    @Override
    public void onPolylineClick(Polyline polyline) {

    }


    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }
        Log.v("isPointInPolygon","==========================RESULT:       "+((intersectCount % 2) == 1)+"         ============================");
        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }
}
