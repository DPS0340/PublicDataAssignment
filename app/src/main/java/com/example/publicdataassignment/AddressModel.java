package com.example.publicdataassignment;

public class AddressModel {
    private String gu;
    private String dong;

    public AddressModel(String gu, String dong) {
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
