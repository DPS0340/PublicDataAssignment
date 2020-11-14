package com.example.publicdataassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

        GpsOnlyLocationSource gpsOnlyLocationSource = new GpsOnlyLocationSource(this);
        GpsListener gpsListener = new GpsListener();
        ReverseGeocoder reverseGeocoder = new ReverseGeocoder.Builder(this).build();
        gpsOnlyLocationSource.activate((LocationSource.OnLocationChangedListener) gpsListener);
        Button currentLocationButton = findViewById(R.id.currentLocationButton);
        Button anotherLocationButton = findViewById(R.id.anotherLocationButton);

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = gpsListener.latitude;
                double longitude = gpsListener.longitude;
                String dong = null;
                try {
                    dong = reverseGeocoder.requestReverseGeoApi(latitude, longitude);
                } catch (Exception ignored) {
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
}