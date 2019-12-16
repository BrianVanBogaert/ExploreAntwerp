package com.example.brian.mapje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends AppCompatActivity
{
    List<InfoWindowData> monumenten;
    ImageView img;
    TextView Title;
    TextView Beschrijving;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        ConstraintLayout rl = (ConstraintLayout) findViewById(R.id.background);
        rl.setBackgroundColor(Color.RED);

        Animation FadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        img= (ImageView) findViewById(R.id.logo);
        img.setImageResource(R.drawable.logo);
        img.startAnimation(FadeIn);

        // ======================================== CONNECTION CHECK + OPHALEN DATA ====================================================================
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Log.i("HOERA","CONNECTED");
            DataOphalen();
        }
        else
        {
            connected = false;
            Intent OopsIntent = new Intent(this, OopsActivity.class);
            Intent NoConnectionIntent = new Intent(this, NoConnectionActivity.class);
            startActivity(NoConnectionIntent);
        }
    }

    // ======================================== KLIK LADEN ====================================================================
    public void LoadMap(View view)
    {
        Button LaadKnop =  findViewById(R.id.loadButton);
        LaadKnop.setBackground(getDrawable(R.drawable.circle_button_clicked));

        Animation FadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        img.startAnimation(FadeOut);
        Beschrijving = (TextView) findViewById(R.id.beschrijving);
        Beschrijving.startAnimation(FadeOut);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("LIST", (Serializable) monumenten); //hier geven we de lijst van monumenten mee aan de volgende activity
        startActivity(intent);
    }

    private void DataOphalen()
    {
        monumenten = new ArrayList<>();
        //====================LOCATIE OPVRAGEN========================
        RequestQueue gerequest=Volley.newRequestQueue(this); //REST API voor THIS activity
        //BELANGRIJK!!!
        //copy de code in http://jsonviewer.stack.hu om de structuur van deze json te zien

        String URL = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek3/MapServer/207/query?where=1%3D1&outFields=Naam,BeschermingDetails,Bescherming,STATUS,shape_Area&outSR=4326&f=json";
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
                                Double Oppervlakte = attributeObject.getDouble("shape_Area");
                                if( Oppervlakte > 4000 && !Naam.matches("huis")) //ik wil de grootste monumenten en geen huizen
                                {

                                    Log.i("Het monument", Naam);
                                    String BeschermingDetails = attributeObject.getString("BeschermingDetails");
                                    Log.i("Details", BeschermingDetails);
                                    //============== GEO OPVRAGEN  ========================
                                    JSONObject GeoObject = feature.getJSONObject("geometry");
                                    JSONArray VerzamelArray = GeoObject.getJSONArray("rings");
                                    JSONArray EersteGeoArray = VerzamelArray.getJSONArray(0).getJSONArray(0);

                                    Double Long = EersteGeoArray.getDouble(0);
                                    Log.i("Long", String.valueOf(Long));
                                    Double Lat = EersteGeoArray.getDouble(1);
                                    Log.i("Lat", String.valueOf(Lat));

                                    object.setNaam(Naam);
                                    object.setBeschrijving(BeschermingDetails);
                                    object.setLongitude(Long);
                                    object.setLatitude(Lat);
                                    monumenten.add(object);
                                   // Log.i("Hey object",monumenten.get(i).getNaam());
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
    }

    public void webKlik(View view)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.visitantwerpen.be"));
        startActivity(browserIntent);
    }

    public void toerismeKlik(View view)
    {
        Uri gmmIntentUri = Uri.parse("geo:51.2194475,4.4024643?q=Toeristische Dienst");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
