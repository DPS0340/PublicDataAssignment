package com.example.publicdataassignment;

public class LocationModel {
    private String gu;
    private String dong;

    public LocationModel(String gu, String dong) {
        this.gu = gu;
        this.dong = dong;
    }

    public String getGu() {
        return gu;
    }

    public String getDong() {
        return dong;
    }
}
