package com.example.brian.mapje;

import androidx.fragment.app.FragmentActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.view.textclassifier.TextLinks;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private boolean IsReady = false;


    List<InfoWindowData> monumenten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        monumenten = (List<InfoWindowData>) intent.getSerializableExtra("LIST");
        //monumenten = new ArrayList<>();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng mas = new LatLng(51.2289, 4.4048203);

        MarkerOptions markerOptionsMas = new MarkerOptions();
        markerOptionsMas.position(mas)
                .title("MAS ANTWERPEN")
                .snippet("Het mas is een museum over de stad en de haven van Antwerpen Het heeft 10 verdiepingen en een indrukwekkende architectuur. De prijs voor een kaartje is dan ook niet te duur. Rijmen is fijn maar bezoeken zou plezanter zijn")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED));

        InfoWindowData masinfo = new InfoWindowData();
        //masinfo.setImage("masfoto");
       // masinfo.setHotel("Hotel : kei goei hotels in de buurt");


        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        Marker m = mMap.addMarker(markerOptionsMas);
        m.setTag(masinfo);
        m.showInfoWindow();


        //============ MARKERS ADDEN =====================

       AddMarkers();



        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(mas).title("Dit is het mas"));
        //mMap.addMarker(new MarkerOptions().position(steen).title("Dit is het steen").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).snippet("Dit is nog wa meer zever"));
        float zoomLevel = 10.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mas, zoomLevel ));
    }

    @Override
    public void onInfoWindowClick(Marker marker) { //zelf nog toe te voegen bij extends https://youtu.be/v4BrNgTEI6E?t=622

        Toast toast = Toast.makeText(getApplicationContext(),
                "klik klik",
                Toast.LENGTH_LONG);

    }

    public void AddMarkers()
    {
        Log.i("Size op dit moment", String.valueOf(monumenten.size())); //19 test markers
        if (monumenten.size() != 0)
        {
            for (int i = 0; i < 20; i++)
            {
                LatLng objLocation = new LatLng(monumenten.get(i).getLatitude(), monumenten.get(i).getLongitude());
                // InfoWindowData objectInfo = new InfoWindowData();
                //objectInfo.setHotel(monumenten.get(i).getBeschrijving());
                //CustomInfoWindowGoogleMap ObjectInfoWindow = new CustomInfoWindowGoogleMap(this);
                //mMap.setInfoWindowAdapter(ObjectInfoWindow);
                mMap.addMarker(new MarkerOptions().position(objLocation).title(monumenten.get(i).getNaam())); //no rocket science
                //o.setTag(objectInfo);
                //o.showInfoWindow();
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "error",
                    Toast.LENGTH_LONG);

            toast.show();
        }
    }


    public void Zoek(View view)
    {


    }
}
