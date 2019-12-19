package com.example.brian.mapje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StappenActivity extends AppCompatActivity implements SensorEventListener,StepListener {

    private StappenTeller simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private ProgressBar progressBar;
    private TextView txtStapProgress,txtStap,txtCalo,txtBezocht;
    private double totalKilometer,totalCalo;
    private DatabaseHelper mDB;
    private int numSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stappen);

        progressBar = findViewById(R.id.progressBar);
        txtStapProgress = findViewById(R.id.txtStapProgress);
        txtStap = findViewById(R.id.txtstappen);
        txtCalo = findViewById(R.id.txtCalo);
        txtBezocht = findViewById(R.id.txtBezocht);

        ConstraintLayout constraintLayout = findViewById(R.id.stappenLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable)constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        mDB = new DatabaseHelper(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StappenTeller();
        simpleStepDetector.registerListener(this);

        sensorManager.registerListener(StappenActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }


    public void viewData(View view){
        Cursor res = mDB.getAllData();
        if (res.getCount()==0){
            showMessage("Error","Niks gevonden");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("ID :" +res.getString(0) +"\n");
            buffer.append("Stappen :" +res.getString(1) +"\n");
            buffer.append("Score :" +res.getString(2) +"\n");
            buffer.append("Calorieën :" +res.getString(3) +"\n");
        }
        showMessage("Data",buffer.toString());
    }


    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void BackToMap(View v)
    {
        Intent LoadActivity = new Intent(this, LoadActivity.class);
        startActivity(LoadActivity);
    }

    @Override
    public void step(long timeNs)
    {
        numSteps++;
        progressBar.setProgress(numSteps);
        String stringstap = Integer.toString(numSteps);
        txtStapProgress.setText(stringstap);
        totalKilometer = (numSteps*0.0008);
        String stringkilo= String.format("%.2f", totalKilometer);
        txtStap.setText(stringkilo +" KM");
        totalCalo = 0.03455*numSteps;
        String stringcalo= String.format("%.2f", totalCalo);
        txtCalo.setText(stringcalo +" Caloriën");
        mDB.updateStapData(numSteps,(int)totalCalo);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}

