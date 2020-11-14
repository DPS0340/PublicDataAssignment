package com.example.publicdataassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.naver.maps.map.LocationSource;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        requestPermissions();
        setMainNetwork();

        GpsOnlyLocationSource gpsOnlyLocationSource = new GpsOnlyLocationSource(this);
        GpsListener gpsListener = new GpsListener();
        ReverseGeocoder reverseGeocoder = new ReverseGeocoder.Builder(this).build();
        gpsOnlyLocationSource.activate((LocationSource.OnLocationChangedListener) gpsListener);
        Button currentLocationButton = findViewById(R.id.currentLocationButton);
        Button anotherLocationButton = findViewById(R.id.anotherLocationButton);

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = gpsListener.getLatitude();
                double longitude = gpsListener.getLongitude();
                String dong = null;
                try {
                    dong = reverseGeocoder.requestReverseGeoApi((float)latitude, (float)longitude);
                } catch (Exception err) {
                    String errString = Log.getStackTraceString(err);
                    Log.e("API-GEOAPI", errString);
                    Toast.makeText(SelectActivity.this, "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                goShowActivity(dong);
            }
        });
        anotherLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_location);
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        Button dialogNoButton = dialog.findViewById(R.id.dialogNoButton);
        Button dialogYesButton = dialog.findViewById(R.id.dialogYesButton);
        EditText dialogDongName = dialog.findViewById(R.id.dialogDongName);
        Activity activity = this;
        dialogNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dong = dialogDongName.getText().toString();
                if (dong.equals("")) {
                    Toast.makeText(activity, "동 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                goShowActivity(dong);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void goShowActivity(String dong) {
        Intent destIntent = new Intent(this, ShowActivity.class);
        destIntent.putExtra("dong", dong);
        startActivity(destIntent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(permissions, 0);
    }


    private void setMainNetwork() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}