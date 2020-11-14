package com.example.publicdataassignment;


import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;

public class AirAPIHandler {
    private String api_key;
    private String air_url;

    private AirAPIHandler(String api_key, String air_url) {
        this.api_key = api_key;
        this.air_url = air_url;
    }

    public static class Builder {
        private String api_key;
        private String air_url;

        public Builder(Activity activity) {
            api_key = activity.getResources().getString(R.string.api_key);
            air_url = activity.getResources().getString(R.string.air_url);
        }

        public AirAPIHandler build() {
            return new AirAPIHandler(api_key, air_url);
        }
    }

    public String requestAirAPI(String location) throws UnsupportedEncodingException, IOException {
        StringBuilder urlBuilder = new StringBuilder(air_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode(api_key, "UTF-8")); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("stationName", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8")); /*측정소명*/
        urlBuilder.append("&" + URLEncoder.encode("searchCondition", "UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8")); /*검색 조건 (년도별 : YEAR, 월별: MONTH, 일별 : DAILY)*/
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
        String result = sb.toString();
        Log.i("AIRAPI", "Response code: " + conn.getResponseCode());
        Log.i("AIRAPI", "Result: " + result);
        return result;
    }

}