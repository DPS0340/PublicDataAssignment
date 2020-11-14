package com.example.publicdataassignment;

import com.naver.maps.geometry.Coord;

import java.util.ArrayList;

public class GeocodeData {
    public class Status {
        public int code;
        public String name;
        public String message;
    }
    public class Result {
        public String name;
        public class Code {
            public String id;
            public String type;
            public String mappingId;
        }
        public class Region {
            public class Area {
                public String name;
                public class Coords {
                    public class Center {
                        public String crs;
                        public float x;
                        public float y;
                    }
                    public Center center;
                }
                public Coords Coords;
            }
            public Area area1;
            public Area area2;
            public Area area3;
            public Area area4;
        }
        public Code code;
        public Region region;
    }
    public Status status;
    public Result result;
}
