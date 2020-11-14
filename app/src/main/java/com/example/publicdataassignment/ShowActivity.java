package com.example.publicdataassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        AirAPIHandler airAPIHandler = new AirAPIHandler.Builder(this).build();
        Intent currentIntent = getIntent();
        String dong = currentIntent.getStringExtra("dong");
        try {
            airAPIHandler.requestAirAPI(dong);
        } catch (IOException err) {
            String errString = Log.getStackTraceString(err);
            Log.e("API-AIRAPI", errString);
            Toast.makeText(ShowActivity.this, "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initLayout() {

    }
}
