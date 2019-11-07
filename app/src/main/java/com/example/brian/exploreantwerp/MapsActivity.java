package com.example.brian.exploreantwerp;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //================ MARKERS ===============================
        LatLng mas = new LatLng(51.2289, 4.4048203);
        LatLng steen = new LatLng(51.2227238, 4.3973637);
        LatLng kathedraal = new LatLng( 51.2202678, 4.4015157);
        LatLng redstarline = new LatLng(51.233028, 4.403362);
        LatLng stadsfeestzaal = new LatLng(51.2171618, 4.4111768);
        LatLng skyline = new LatLng(51.2227238,4.4212203);
        LatLng spoornoord = new LatLng(51.2300422,4.42346680111632);
        LatLng handelsbeurs = new LatLng(51.21934845,4.40615870763359);
        LatLng grotemarkt = new LatLng(51.22117115,4.40035205671144);
        LatLng station = new LatLng(51.21608275,4.42103807023019);
        LatLng zoo = new LatLng(51.2163428,4.4235414);
        LatLng opera = new LatLng( 51.2188832,4.4157408);
        LatLng cinema = new LatLng(51.2460256,4.4158764);
        LatLng kaaienzuid = new LatLng(51.2121175,4.389851);
        LatLng droogdokkenpark = new LatLng(51.2360092,4.40175368513583);


        //tip??? waardes verhuizen naar mapje values???

        //om een custom icoontje aan te maken => res => drawable (right click) => add image asset (kies zelf derna je favo icon en geef een deftige naam)


        mMap.addMarker(new MarkerOptions().position(mas).title("Dit is het mas"));
        mMap.addMarker(new MarkerOptions().position(steen).title("Dit is het steen").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uitleg"));
        mMap.addMarker(new MarkerOptions().position(kathedraal).title("Dit is de kathedraal").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));
        mMap.addMarker(new MarkerOptions().position(redstarline).title("Dit is redstarline").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));
        mMap.addMarker(new MarkerOptions().position(stadsfeestzaal).title("Dit is de stadsfeestzaal").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));
        mMap.addMarker(new MarkerOptions().position(skyline).title("Dit is de skyline").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));
        mMap.addMarker(new MarkerOptions().position(spoornoord).title("Dit is spoornoord").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));
        mMap.addMarker(new MarkerOptions().position(handelsbeurs).title("Dit is de handelsbeurs").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_testicoontje)).snippet("Dit is nog wa meer uileg"));

         //tip tip tip: Arrays???

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mas));
    }
}
