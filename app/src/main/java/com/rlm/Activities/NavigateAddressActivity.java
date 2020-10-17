package com.rlm.Activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.rlm.Constant.DataParser;
import com.rlm.R;
import com.rlm.databinding.ActivityNavigateAddressBinding;
import com.squareup.picasso.Picasso;
import com.utils.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.develpoeramit.mapicall.ApiCallBuilder;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NavigateAddressActivity extends AppCompatActivity implements LocationListener {
    private ActivityNavigateAddressBinding binding;
    private LocationManager locationManager;
    private Location location;
    private GoogleMap mMap;
    private double current_lat,current_lng;
    private BitmapDescriptor icon;
    private MarkerOptions marker;
    ArrayList<LatLng> points = new ArrayList<>();
    private String address;
    private Marker mR;
    boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_navigate_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Direction");
        address=getIntent().getExtras().getString("address");
        initLocation();
        bindMap();
        FetchRout();
        BindView();
    }

    private void BindView() {
        icon = BitmapDescriptorFactory.fromResource(R.drawable.car);
        marker=new MarkerOptions()
                .position(new LatLng(current_lat,current_lng))
                .title("You are here")
                .icon(icon);
    }
    private String getUrl(LatLng origin) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination="+address;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=TRANSIT";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor +"&"+mode+ "&key=" + getResources().getString(R.string.map_key);
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("URL","===>"+url);
        return url;
    }
    private void FetchRout() {
        HashMap<String, String> param=new HashMap<>();
       param.put("","");
        ApiCallBuilder.build(this)
                .setUrl(getUrl(new LatLng(current_lat,current_lng)))
                .setParam(param).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                points.clear();
                Log.e("RootResponse","======>"+response);
                List<List<HashMap<String, String>>> routes = null;
                try {
                    JSONObject jObject = new JSONObject(response);
                    DataParser parser = new DataParser();
                    routes = parser.parse(jObject);
                    PolylineOptions lineOptions = null;
                    double des_lat=0.0,des_lon=0.0;
                    for (int i = 0; i < routes.size(); i++) {
                        lineOptions = new PolylineOptions();
                        List<HashMap<String, String>> path = routes.get(i);
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);
                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            if (j==path.size()){
                                des_lat=Double.parseDouble(point.get("lat"));
                                des_lon=Double.parseDouble(point.get("lng"));
                            }
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);

                        }
                        lineOptions.addAll(points);
                        lineOptions.width(20);
                        Log.d("onPostExecute", "onPostExecute lineoptions decoded");
                    }
                    if (lineOptions != null) {
                        mMap.clear();
                        mR= mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(current_lat,current_lng))
                                .title("You are here")
                                .icon(icon));
                        mMap.addPolyline(lineOptions);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(des_lat,des_lon))
                                .title("Destination")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                    } else {
                        Log.d("onPostExecute", "without Polylines drawn");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void Failed(String error) {
                Log.e("Root_Error",error);
            }
        });
    }

    private void initLocation() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] arr={ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION};
            requestPermissions(arr,100);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
        Tools.get().isLocationEnabled(this,locationManager);
        current_lat=location.getLatitude();
        current_lng=location.getLongitude();
    }
    private void bindMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frg);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                mMap=map;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                mR= mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(current_lat,current_lng))
                        .title("You are here")
                        .icon(icon));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        if (active) {
            if (location != null) {
                current_lat = location.getLatitude();
                current_lng = location.getLongitude();
                LatLng newLatLng = new LatLng(current_lat, current_lng);
                if (mMap != null) {
                    mR.setRotation(location.getBearing());
                    mR.setPosition(newLatLng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 18.0f));
                    if (!PolyUtil.isLocationOnPath(newLatLng, points, true, 100)) {
                        FetchRout();
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active=false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
