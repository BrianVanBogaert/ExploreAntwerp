package com.example.brian.mapje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class NoConnectionActivity extends AppCompatActivity
{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Animation FadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);
        TextView Fout = findViewById(R.id.fout);
        Fout.startAnimation(FadeIn);
    }

    public void Herstart(View view)
    {
        Intent Herstart = new Intent(this, LoadActivity.class);
        startActivity(Herstart);
    }
}
