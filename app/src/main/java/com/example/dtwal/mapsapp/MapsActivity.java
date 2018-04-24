package com.example.dtwal.mapsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Point> pointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pointList = new ArrayList<Point>();

        //Take JSON data and convert it to JSON objects
        InputStream is = getResources().openRawResource(R.raw.trip);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        try {
            JSONObject jo = new JSONObject(jsonString);

            JSONArray jsonArray = jo.getJSONArray("points");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPoint = jsonArray.getJSONObject(i);
                Point point = new Point();
                point.setLongitude(jsonPoint.getDouble("longitude"));
                point.setLatitude(jsonPoint.getDouble("latitude"));
                pointList.add(point);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions rectOptions = new PolylineOptions();

        LatLng pointStart = new LatLng(pointList.get(0).getLatitude(), pointList.get(0).getLongitude());
        LatLng pointEnd = new LatLng(pointList.get(pointList.size() - 1).getLatitude(), pointList.get(pointList.size() - 1).getLongitude());

        //Choose points to draw
        for (int i = 0; i < pointList.size(); i++) {
            LatLng newPoint = new LatLng(pointList.get(i).getLatitude(), pointList.get(i).getLongitude());
            rectOptions.add(newPoint);
        }

        // Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(pointStart).title("Beginning marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointStart, 10));


        mMap.addMarker(new MarkerOptions().position(pointEnd).title("Ending marker"));

    }

}
