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
            public String so2Value;
            public String coValue;
            public String o3Value;
            public String no2Value;
            public String pm10Value;
            public String pm10Value24;
            public String pm25Value;
            public String pm25Value24;
            public String khaiValue;
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
