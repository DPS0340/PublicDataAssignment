package com.example.publicdataassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
                    dong = reverseGeocoder.requestReverseGeoApi(latitude, longitude);
                } catch (Exception err) {
                    String errString = Log.getStackTraceString(err);
                    Log.e("SELECTACTIVITY", errString);
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
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.dialog_location);
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
                String text = dialogDongName.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(activity, "동 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                goShowActivity(text);
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
}