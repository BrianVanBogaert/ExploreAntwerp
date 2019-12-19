package com.example.brian.mapje;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap;
    private int AantalBezocht = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    List<InfoWindowData> monumenten;
    private DatabaseHelper mDB;


    static SharedPreferences settings;
    static SharedPreferences.Editor editor;
    CustomInfoWindowGoogleMap customInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        customInfoWindow = new CustomInfoWindowGoogleMap(this);
        Intent intent = getIntent();
        monumenten = (List<InfoWindowData>) intent.getSerializableExtra("LIST");
        mDB = new DatabaseHelper(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onBackPressed() {
        Intent StappenActivity = new Intent(this, StappenActivity.class);
        startActivity(StappenActivity);
    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        Setstyle();
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        mMap.setOnMyLocationChangeListener(this);
        enableMyLocationIfPermitted();
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        LatLng mas = new LatLng(51.2289, 4.4048203);


//        MarkerOptions markerOptionsMas = new MarkerOptions();
//        markerOptionsMas.position(mas)
//                .title("MAS ANTWERPEN")
//                .snippet("Het mas is een museum over de stad en de haven van Antwerpen Het heeft 10 verdiepingen en een indrukwekkende architectuur. De prijs voor een kaartje is dan ook niet te duur. Rijmen is fijn maar bezoeken zou plezanter zijn")
//                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED));
//
//        InfoWindowData masinfo = new InfoWindowData();
        //masinfo.setImage("masfoto");
       // masinfo.setHotel("Hotel : kei goei hotels in de buurt");

//        Marker m = mMap.addMarker(markerOptionsMas);
//        m.setTag(masinfo);
//        m.showInfoWindow();


        //========================================== MARKERS ADDEN ============================================

       AddMarkers();

        //mMap.addMarker(new MarkerOptions().position(steen).title("Dit is het steen").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).snippet("Dit is nog wa meer zever"));
        float zoomLevel = 12.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mas, zoomLevel ));
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
       LatLng ClickPos = marker.getPosition();
       String ClickPosString = String.valueOf(ClickPos);            //output: lat/lng:(xxxxxxxx,yyyyyy);
        String replacedPos = ClickPosString.replace("lat/lng:","");      //gooit alle brol weg
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+replacedPos));
        startActivity(intent);
    }

    public void AddMarkers()
    {
        Log.i("Size op dit moment", String.valueOf(monumenten.size())); //19 test markers
        if (monumenten.size() != 0)
        {
            for (int i = 0; i < monumenten.size() ; i++)
            {
                LatLng objLocation = new LatLng(monumenten.get(i).getLatitude(), monumenten.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(objLocation)
                        .title(monumenten.get(i).getNaam()))
                        .setTag(monumenten.get(i))
                ;
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Oops, foutje! Herstart de app",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

// ==================================== LOCATIE TONEN (en permit) http://bit.do/fkRs3 ==================================================

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

// ==================================== CHECK COLISSIONS ==================================================

    @Override
    public void onMyLocationChange(Location location)
    {
        Cursor res = mDB.getAllData();
        String score="";
        Location target = new Location("target");
        for (int i = 0; i < monumenten.size(); ++i)
        {
            target.setLatitude(monumenten.get(i).getLatitude());
            target.setLongitude(monumenten.get(i).getLongitude());

            if(location.distanceTo(target) < 500 && monumenten.get(i).getAlBezocht() == false)
            {
                Log.i("HEY GE BENT VLAKBIJ", "een monument");
                AantalBezocht++;


                monumenten.get(i).setAlbezocht(true);
                TextView bottomtext = (TextView) findViewById(R.id.bottomtext);


                bottomtext.setText(AantalBezocht + " monumenten bezocht");
                mDB.updateVisitedMonuments(AantalBezocht);
            }
        }

        // de string score kunt ge nu in een textview zetten als ge wilt
        while (res.moveToNext()){
            score = ("Score :" +res.getString(2) );
        }

    }



    // ================================ STYLING ====================================

    private void Setstyle()
    {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle)); //eigen custom JSON style voor google maps

            if (!success) {
                Log.e("Error", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Error", "can't find style" , e);
        }

    }

}
