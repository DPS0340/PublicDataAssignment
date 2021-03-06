package com.example.publicdataassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MAIN", "Showing loading screen..");
        Activity activity = this;
        ProgressBar progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent destIntent = new Intent(activity, ShowActivity.class);
                startActivity(destIntent);
            }
        });
        setTimeoutToSelectActivity(0.8);
    }

    private void setTimeoutToSelectActivity(double sec) {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("MAIN", "Loading is done!");
                        goSelectActivity();
                    }
                },
                (int)(sec * 1000));
    }

    private void goSelectActivity() {
        Intent destIntent = new Intent(this, SelectActivity.class);
        startActivity(destIntent);
    }

}