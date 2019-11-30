package com.example.brian.mapje;

import androidx.fragment.app.FragmentActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.view.textclassifier.TextLinks;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

    List<InfoWindowData> monumenten;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        monumenten = new ArrayList<>();
        //====================LOCATIE OPVRAGEN========================
        // Construct a GeoDataClient.
        RequestQueue gerequest=Volley.newRequestQueue(this); //REST API voor THIS activity

        String URL = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek3/MapServer/207/query?where=1%3D1&outFields=Naam,BeschermingDetails&outSR=4326&f=json";
        //BELANGRIJK!!!
        //copy de code in http://jsonviewer.stack.hu om de structuur van deze json te zien

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            Log.i("Rest response", response.toString()); //Dit werkt
                        try {
                            JSONArray jsonFeatureArray = response.getJSONArray("features"); //features is de grootste table waar we in moeten zoeken (zie jsonviewer.stack.hu)
                            for(int i = 0; i < jsonFeatureArray.length(); i++)
                            {
                                InfoWindowData object= new InfoWindowData();
                                JSONObject feature = jsonFeatureArray.getJSONObject(i); //we vragen elk object op
                                //============== ATTRIBUTES OPVRAGEN MET DE NAAM EN DE BESCHRIJVING ========================
                                JSONObject attributeObject = feature.getJSONObject("attributes"); //in features steken atrributes (zie jsonviewer.stack.hu)
                                    String Naam = attributeObject.getString("Naam"); //met elks hun naam (zie jsonviewer.stack.hu)
                                if(Naam != "huis") //ik wil geen 100 000 huizen
                                {
                                    Log.i("Het monument", Naam);
                                    String BeschermingDetails = attributeObject.getString("BeschermingDetails");
                                    Log.i("Details", BeschermingDetails);
                                    //============== GEO OPVRAGEN  ========================
                                    JSONObject GeoObject = feature.getJSONObject("geometry");
                                    JSONArray VerzamelArray = GeoObject.getJSONArray("rings");
                                    JSONArray EersteGeoArray = VerzamelArray.getJSONArray(0).getJSONArray(0);

                                    Double Long = EersteGeoArray.getDouble(1);
                                    Log.i("Long", String.valueOf(Long));
                                    Double Lat = EersteGeoArray.getDouble(0);
                                    Log.i("Lat", String.valueOf(Lat));

                                    object.setNaam(Naam);
                                    object.setBeschrijving(BeschermingDetails);
                                    object.setLongitude(Long);
                                    object.setLatitude(Lat);
                                    monumenten.add(object);

                                    Log.i("Hey object",monumenten.get(i).getNaam());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        gerequest.add(objectRequest);

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
        LatLng steen = new LatLng(51.2227238, 4.3973637);

        MarkerOptions markerOptionsMas = new MarkerOptions();
        markerOptionsMas.position(mas)
                .title("MAS ANTWERPEN")
                .snippet("Het mas is een museum over de stad en de haven van Antwerpen Het heeft 10 verdiepingen en een indrukwekkende architectuur. De prijs voor een kaartje is dan ook niet te duur. Rijmen is fijn maar bezoeken zou plezanter zijn")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED));

        InfoWindowData masinfo = new InfoWindowData();
        masinfo.setImage("masfoto");
        masinfo.setHotel("Hotel : kei goei hotels in de buurt");


        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        Marker m = mMap.addMarker(markerOptionsMas);
        m.setTag(masinfo);
        m.showInfoWindow();



        Log.i("Size op dit moment", String.valueOf(monumenten.size())); //Volgens logs: 0???
        for (int i = 0; i < monumenten.size(); i++)
        {
            LatLng objLocation = new LatLng(monumenten.get(i).getLatitude(), monumenten.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(objLocation).title(monumenten.get(i).getNaam()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).snippet(monumenten.get(i).getNaam()));
        }


        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.addMarker(new MarkerOptions().position(mas).title("Dit is het mas"));
        //mMap.addMarker(new MarkerOptions().position(steen).title("Dit is het steen").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).snippet("Dit is nog wa meer zever"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mas));
    }

    @Override
    public void onInfoWindowClick(Marker marker) { //zelf nog toe te voegen bij extends https://youtu.be/v4BrNgTEI6E?t=622

    }


}
