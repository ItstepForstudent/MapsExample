package com.itstep.mapsexample;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.itstep.mapsexample.models.DirectionResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    DirectionsApi api;
    GoogleMap map;


    void drawRoute(List<LatLng> points){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(4).color(0xFFFF0055);
        LatLngBounds.Builder laBuilder = new LatLngBounds.Builder();
        for(LatLng point:points){
            polylineOptions.add(point);
            laBuilder.include(point);
        }

        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(points.get(0))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(points.get(points.size()-1))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        map.addMarker(markerOptions1);
        map.addMarker(markerOptions2);


        LatLngBounds bounds = laBuilder.build();
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,15);
        map.moveCamera(update);
        map.addPolyline(polylineOptions);

    }
    void loadRoute(){
        api.getRoute("35.000,34.000","35.000,34.020","AIzaSyBaPfiTa-pWC4cmASeTsAFKVW547F9AXPY",true,"ru")
                .enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                        List<LatLng> points = PolyUtil.decode(response.body().getRoutes().get(0).overviewPolyline.getPoints());
                        drawRoute(points);
                    }
                    @Override
                    public void onFailure(Call<DirectionResponse> call, Throwable t) {
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit =new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com")
                .build();

        api = retrofit.create(DirectionsApi.class);




        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map=googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .zoom(15)
                .target(new LatLng(35.000,34.000))
                .build())
        );

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                CameraPosition position = googleMap.getCameraPosition();
                LatLng curPos = position.target;


                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(35.000,34.020)),new GoogleMap.CancelableCallback(){
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this,"finosh",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        loadRoute();
//        PolylineOptions polylineOptions = new PolylineOptions()
//                .add(new LatLng(35.003,33.998))
//                .add(new LatLng(35.003,34.000))
//                .add(new LatLng(35.000,34.000))
//                .add(new LatLng(35.000,33.998))
//                .add(new LatLng(35.003,33.998))
//                .color(Color.RED)
//                .width(40);
//
//        Polyline polyline = googleMap.addPolyline(polylineOptions);

//        PolygonOptions polygonOptions = new PolygonOptions()
//                .add(new LatLng(35.003,33.998))
//                .add(new LatLng(35.003,34.000))
//                .add(new LatLng(35.000,34.000))
//                .add(new LatLng(35.000,33.998))
//                .addHole(
//                        Arrays.asList(
//                                new LatLng(35.002,33.999),
//                                new LatLng(35.002,34.000),
//                                new LatLng(35.001,34.000),
//                                new LatLng(35.001,33.999)
//                        )
//                )
//                .fillColor(Color.RED);
//
//
//        Polygon polyline = googleMap.addPolygon(polygonOptions);


//        CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(35.000,34.000))
//                .radius(200)
//                .zIndex(12)
//                ;
//
//        googleMap.addCircle(circleOptions);
//
//        CircleOptions circleOptions2 = new CircleOptions()
//                .center(new LatLng(35.001,34.000))
//                .radius(200)
//                .zIndex(11)
//                .strokeColor(Color.BLUE)
//                ;
//        googleMap.addCircle(circleOptions2);





    }
}
