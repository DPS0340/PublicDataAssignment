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

    private GpsOnlyLocationSource gpsOnlyLocationSource;
    private GpsListener gpsListener;
    private ReverseGeocoder reverseGeocoder;
    private Button currentLocationButton;
    private Button anotherLocationButton;
    private Button mapButton;
    private SelectActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        requestPermissions();
        activity = this;

        gpsOnlyLocationSource = new GpsOnlyLocationSource(this);
        gpsListener = new GpsListener();
        reverseGeocoder = new ReverseGeocoder.Builder(this).build();
        currentLocationButton = findViewById(R.id.currentLocationButton);
        anotherLocationButton = findViewById(R.id.anotherLocationButton);
        mapButton = findViewById(R.id.mapButton);

        gpsOnlyLocationSource.activate((LocationSource.OnLocationChangedListener) gpsListener);


        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float latitude = gpsListener.getLatitude();
                float longitude = gpsListener.getLongitude();
                LocationModel locationModel = new LocationModel(latitude, longitude);
                if (latitude == -1 || longitude == -1) {
                    Log.e("API-GEOAPI", "not Initialized");
                    Toast.makeText(SelectActivity.this, "로딩 중입니다. 잠시만 기다려 주세요..", Toast.LENGTH_SHORT).show();
                    return;
                }
                new ReverseGeocodeTask(activity).execute(locationModel);
            }
        });
        anotherLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMapActivity();
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
        EditText dialogDongName = dialog.findViewById(R.id.dialogGuName);
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
                String gu = dialogDongName.getText().toString();
                if (gu.equals("")) {
                    Toast.makeText(activity, "동 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                gu = gu.trim();
                dialog.dismiss();
                goShowActivity(gu);
            }
        });
        dialog.show();
    }

    public void goMapActivity() {
        Intent destIntent = new Intent(this, MapActivity.class);
        startActivity(destIntent);
    }

    public void goShowActivity(String gu) {
        Intent destIntent = new Intent(this, ShowActivity.class);
        destIntent.putExtra("gu", gu);
        startActivity(destIntent);
    }

    public void goShowActivity(AddressModel data) {
        Intent destIntent = new Intent(this, ShowActivity.class);
        destIntent.putExtra("gu", data.getGu());
        destIntent.putExtra("dong", data.getDong());
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

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}