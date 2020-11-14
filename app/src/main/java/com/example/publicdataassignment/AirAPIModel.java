package com.example.publicdataassignment;

import java.util.ArrayList;

public class AirAPIModel {
    public class Header {
        public String resultCode;
        public String resultMsg;
    }

    public class Body {
        public class Item {
            public String dataTime;
            public String mangName;
            public double so2Value;
            public double coValue;
            public double o3Value;
            public double no2Value;
            public double pm10Value;
            public double pm10Value24;
            public double pm25Value;
            public double pm25Value24;
            public double khaiValue;
            public int khaiGrade;
            public int coGrade;
            public int o3Grade;
            public int no2Grade;
            public int pm10Grade;
            public int pm25Grade;
            public int pm10Grade1h;
            public int pm25Grade1h;
        }

        public ArrayList<Item> items;
        public int numOfRows;
        public int pageNo;
        public int totalCount;
    }

    public Header header;
    public Body body;

}
