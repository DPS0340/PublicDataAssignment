package com.example.publicdataassignment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.io.IOException;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        AirAPIHandler airAPIHandler = new AirAPIHandler.Builder(this).build();
        Intent currentIntent = getIntent();
        String gu = currentIntent.getStringExtra("gu");
        String dong = currentIntent.getStringExtra("dong");
        AirAPIResponse response = null;
        int overAllstatus;
        try {
            response = airAPIHandler.requestAirAPI(gu);
        } catch (IOException err) {
        }
        if (response == null) {
            if (dong != null && !dong.equals("")) {
                try {
                    response = airAPIHandler.requestAirAPI(dong);
                } catch (IOException err) {
                    String errString = Log.getStackTraceString(err);
                    Log.e("API-AIRAPI", errString);
                    Toast.makeText(ShowActivity.this, "통신에 오류가 생겼습니다.", Toast.LENGTH_SHORT).show();                }
            }
            overAllstatus = 0;
            if(response != null) {
                overAllstatus = response.getOverallStatus();
            }
        } else {
            overAllstatus = response.getOverallStatus();
        }
        initLayout(gu, overAllstatus, response);
    }

    private void initLayout(String gu, int status, AirAPIResponse response) {
        TextView todayText = findViewById(R.id.todayIsText);
        if (status != 0) {
            todayText.setText(String.format(todayText.getText().toString(), gu, parseStatus(status)));
        } else {
            todayText.setText(String.format("%s 검색에 오류가 발생했습니다.", gu));
        }
        int faceID = parseFace(status);
        int colorID = parseColor(status);
        setFace(faceID);
        setColor(colorID);
        if (status != 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout root = findViewById(R.id.statusLayout);
            LinearLayout horizontal = new LinearLayout(this);
            horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < response.getNames().size(); i++) {
                int partStatus = response.getGrades().get(i);
                String partValue = response.getValues().get(i);
                String partName = response.getNames().get(i);
                View inflated = inflater.inflate(R.layout.layout_part_air_status, null, false);
                setInflatedData(inflated, partStatus, partValue, partName);
                inflated.invalidate();
                horizontal.addView(inflated);
                horizontal.invalidate();
                if (i == 2) {
                    root.addView(horizontal);
                    root.invalidate();
                    horizontal = new LinearLayout(this);
                    horizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
            }
            root.addView(horizontal);
            root.invalidate();
        }
    }

    private void setInflatedData(View inflated, int partStatus, String partValue, String partName) {
        ImageView icon = inflated.findViewById(R.id.icon);
        TextView name = inflated.findViewById(R.id.name);
        TextView value = inflated.findViewById(R.id.value);
        int faceId = parseFace(partStatus);
        icon.setImageResource(faceId);
        name.setText(String.format(name.getText().toString(), partName, parseStatus(partStatus)));
        value.setText(String.format(value.getText().toString(), partValue));
    }

    private int parseColor(int status) {
        int colorId;
        switch (status) {
            case DayStatus.VERY_SATISFIED:
                colorId = R.color.very_satisfied;
                break;
            case DayStatus.SATISFIED:
                colorId = R.color.satisfied;
                break;
            case DayStatus.DISSATISFIED:
                colorId = R.color.dissatisfied;
                break;
            case DayStatus.VERY_DISSATISFIED:
            default:
                colorId = R.color.very_dissatisfied;
                break;
        }
        return colorId;
    }

    private int parseFace(int status) {
        int faceId;
        switch (status) {
            case DayStatus.VERY_SATISFIED:
                faceId = R.drawable.ic_baseline_sentiment_very_satisfied_24;
                break;
            case DayStatus.SATISFIED:
                faceId = R.drawable.ic_baseline_sentiment_satisfied_alt_24;
                break;
            case DayStatus.DISSATISFIED:
                faceId = R.drawable.ic_baseline_sentiment_dissatisfied_24;
                break;
            case DayStatus.VERY_DISSATISFIED:
            default:
                faceId = R.drawable.ic_baseline_sentiment_very_dissatisfied_24;
                break;
        }
        return faceId;
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
