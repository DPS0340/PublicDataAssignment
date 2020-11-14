package com.example.publicdataassignment;

import android.util.Log;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class AirAPIResponse {
    private ArrayList<Integer> grades;
    private ArrayList<Double> values;
    private ArrayList<String> names;
    private int overallStatus;

    public AirAPIResponse(ArrayList<Integer> grades, ArrayList<Double> values, ArrayList<String> names) {
        // 딥 카피 사용
        this.grades = new ArrayList<>(grades);
        this.values = new ArrayList<>(values);
        this.names = new ArrayList<>(names);
        overallStatus = this.grades.get(0);
        Log.i("API-AIRAPI", "Result grades: " + grades.toString());
        Log.i("API-AIRAPI", "Result values: " + values.toString());
        Log.i("API-AIRAPI", "Result names: " + names.toString());
        Log.i("API-AIRAPI", "Result overall status: " + overallStatus);
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public ArrayList<Double> getValues() {
        return values;
    }
    public ArrayList<String> getNames() {
        return names;
    }

    public int getOverallStatus() {
        return overallStatus;
    }

}
