package com.example.publicdataassignment;


import android.app.Activity;
import android.util.Log;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AirAPIHandler {
    private static AirAPIHandler instance = null;
    private String api_key;
    private String air_url;

    private AirAPIHandler(String api_key, String air_url) {
        if(instance == null) {
            instance = this;
        }
        this.api_key = api_key;
        this.air_url = air_url;
    }
    public static AirAPIHandler getInstance(Activity activity) {
        if(instance == null) {
            new AirAPIHandler.Builder(activity).build();
        }
        return instance;
    }

    public static class Builder {
        private String api_key;
        private String air_url;

        public Builder(Activity activity) {
            api_key = activity.getResources().getString(R.string.public_api_key);
            air_url = activity.getResources().getString(R.string.air_url);
        }

        public AirAPIHandler build() {
            return new AirAPIHandler(api_key, air_url);
        }
    }

    public AirAPIResponse requestAirAPI(String location) throws IOException, IndexOutOfBoundsException, NullPointerException {
        StringBuilder urlBuilder = new StringBuilder(air_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + api_key); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("stationName", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8")); /*측정소명*/
        urlBuilder.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8")); /*검색 조건 (년도별 : YEAR, 월별: MONTH, 일별 : DAILY)*/
        urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.3", "UTF-8")); /*검색 조건 (년도별 : YEAR, 월별: MONTH, 일별 : DAILY)*/
        URL url = new URL(urlBuilder.toString());
        Log.i("API-AIRAPI", "URL: " + urlBuilder.toString());
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
        String response = sb.toString();
        Log.i("API-AIRAPI", "Response code: " + conn.getResponseCode());
        Log.i("API-AIRAPI", "Result String: " + response);
        AirAPIModel model = parseResponse(response);
        AirAPIModel.Body.Item item = null;
        try {
            item = model.body.items.get(0);
        } catch (IndexOutOfBoundsException err) {
            return null;
        }
        ArrayList<String> names = new ArrayList<>();
        names.add("통합대기환경");
        names.add("일산화탄소");
        names.add("오존");
        names.add("이산화질소");
        names.add("pm10 미세먼지");
        names.add("pm25 미세먼지");

        ArrayList<Integer> grades = new ArrayList<>();
        grades.add(item.khaiGrade);
        grades.add(item.coGrade);
        grades.add(item.o3Grade);
        grades.add(item.no2Grade);
        grades.add(item.pm10Grade);
        grades.add(item.pm25Grade);

        ArrayList<String> values = new ArrayList<>();
        values.add(item.khaiValue);
        values.add(item.coValue);
        values.add(item.o3Value);
        values.add(item.no2Value);
        values.add(item.pm10Value);
        values.add(item.pm25Value);

        AirAPIResponse result = new AirAPIResponse(grades, values, names);
        return result;
    }

    public AirAPIModel parseResponse(String response) {
        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        GsonXml gsonXml = new GsonXmlBuilder()
                .setXmlParserCreator(parserCreator)
                .create();

        AirAPIModel result = gsonXml.fromXml(response, AirAPIModel.class);
        return result;
    }
}