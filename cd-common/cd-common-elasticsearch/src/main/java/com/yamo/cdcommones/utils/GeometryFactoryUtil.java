package com.yamo.cdcommones.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class GeometryFactoryUtil {
    private WKTReader reader;
    private static GeometryFactory instance = null;

    public static synchronized GeometryFactory getInstance() {
        if (instance == null) {
            instance = new GeometryFactory();
        }
        return instance;
    }

    public void getReader() {
        reader = new WKTReader();
    }

    public Geometry buildGeo(String str) {
        try {
            if (reader == null) {
                reader = new WKTReader();
            }
            return reader.read(str);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("buildGeometry Error",e);
        }
    }
}

