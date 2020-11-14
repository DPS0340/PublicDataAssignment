package com.example.publicdataassignment;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoactionAPIHandler {
    private String api_key;
    private String api_secret;

    private LoactionAPIHandler(String api_key, String api_secret) {
        this.api_key = api_key;
        this.api_secret = api_secret;
    }

    public static class Builder {
        private String api_key;
        private String api_secret;

        public Builder(Activity activity) {
            api_key = activity.getResources().getString(R.string.naver_api_key);
            api_secret = activity.getResources().getString(R.string.naver_api_secret);
        }

        public LoactionAPIHandler build() {
            return new LoactionAPIHandler(api_key, api_secret);
        }
    }

    public String requestLocationAPI(String location) throws UnsupportedEncodingException, IOException {
    }
}
