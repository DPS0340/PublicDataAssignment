package com.example.publicdataassignment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        AirAPIHandler airAPIHandler = new AirAPIHandler.Builder(this).build();
        Intent currentIntent = getIntent();
        String dong = currentIntent.getStringExtra("gu");
        int status = 0;
        try {
            status = airAPIHandler.requestAirAPI(dong);
        } catch (IOException err) {
            String errString = Log.getStackTraceString(err);
            Log.e("API-AIRAPI", errString);
            Toast.makeText(ShowActivity.this, "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();
        }
        initLayout(dong, status);
    }

    private void initLayout(String gu, int status) {
        TextView todayText = findViewById(R.id.todayIsText);
        todayText.setText(String.format("오늘의 %s의 날씨는 %s 입니다.", gu, parseStatus(status)));
        int faceID;
        int colorID;
        switch (status) {
            case DayStatus.VERY_SATISFIED:
                faceID = R.drawable.ic_baseline_sentiment_very_satisfied_24;
                colorID = R.color.very_satisfied;
                break;
            case DayStatus.SATISFIED:
                faceID = R.drawable.ic_baseline_sentiment_satisfied_alt_24;
                colorID = R.color.satisfied;
                break;
            case DayStatus.DISSATISFIED:
                faceID = R.drawable.ic_baseline_sentiment_dissatisfied_24;
                colorID = R.color.dissatisfied;
                break;
            case DayStatus.VERY_DISSATISFIED:
                faceID = R.drawable.ic_baseline_sentiment_very_dissatisfied_24;
                colorID = R.color.very_dissatisfied;
                break;
            default:
                faceID = 0;
                colorID = 0;
                break;
        }
        setFace(faceID);
        setColor(colorID);
    }

    private String parseStatus(int status) {
        switch (status) {
            case DayStatus.VERY_SATISFIED:
                return "매우 좋음";
            case DayStatus.SATISFIED:
                return "좋음";
            case DayStatus.DISSATISFIED:
                return "나쁨";
            case DayStatus.VERY_DISSATISFIED:
                return "매우 나쁨";
            default:
                return "";
        }
    }

    private void setColor(int colorID) {
        ConstraintLayout layout = findViewById(R.id.showLayout);
        layout.setBackgroundColor(getResources().getColor(colorID, getTheme()));
    }

    private void setFace(int faceID) {
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(faceID);
    }
}
