package com.example.publicdataassignment;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ReverseGeocoder {
    private static ReverseGeocoder instance = null;
    private String api_key;
    private String api_secret;
    private String reverse_geo_url;
    private ReverseGeocoder(String api_key, String api_secret, String reverse_geo_url) {
        if(instance == null) {
            instance = this;
        }
        this.api_key = api_key;
        this.api_secret = api_secret;
        this.reverse_geo_url = reverse_geo_url;
    }
    public static ReverseGeocoder getInstance(Activity activity) {
        if(instance == null) {
            new ReverseGeocoder.Builder(activity).build();
        }
        return instance;
    }

    public static class Builder {
        private String api_key;
        private String api_secret;
        private String reverse_geo_url;

        public Builder(Activity activity) {
            api_key = activity.getResources().getString(R.string.naver_api_key);
            api_secret = activity.getResources().getString(R.string.naver_api_secret);
            reverse_geo_url = activity.getResources().getString(R.string.naver_reverse_geo_url);
        }

        public ReverseGeocoder build() {
            return new ReverseGeocoder(api_key, api_secret, reverse_geo_url);
        }
    }

    public AddressModel requestReverseGeoApi(float latitude, float longitude) throws IOException, ArrayIndexOutOfBoundsException {
        StringBuilder urlBuilder = new StringBuilder(reverse_geo_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode("coordsToaddr", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("coords", "UTF-8") + "=" + URLEncoder.encode(String.format("%f,%f", longitude, latitude), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("output", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("orders", "UTF-8") + "=" + URLEncoder.encode("legalcode", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("X-NCP-APIGW-API-KEY-ID", "UTF-8") + "=" + URLEncoder.encode(api_key, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("X-NCP-APIGW-API-KEY", "UTF-8") + "=" + URLEncoder.encode(api_secret, "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        int resCode = conn.getResponseCode();
        if (200 <= resCode && resCode <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String jsonString = sb.toString();
        Gson gson = new Gson();
        Log.i("GEOAPI", "latitude: " + latitude);
        Log.i("GEOAPI", "longitude: " + longitude);
        Log.i("GEOAPI", "Json String: " + jsonString);
        Log.i("GEOAPI", "Response code: " + conn.getResponseCode());
        GeocodeModel data = gson.fromJson(jsonString, GeocodeModel.class);
        String gu = data.results.get(0).region.area2.name;
        String dong = data.results.get(0).region.area3.name;
        AddressModel result = new AddressModel(gu, dong);
        Log.i("GEOAPI", "gu: " + gu);
        Log.i("GEOAPI", "dong: " + dong);
        return result;
    }
}
