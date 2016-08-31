package edu.android.homework_12.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liosha (26.08.2016).
 */
public class Weather {
    public final List<Day> days = new ArrayList<>(10);
    public final String country;
    public final String city;
    public final String lat;
    public final String lon;

    public Weather(String country, String city, String lat, String lon) {
        this.country = country;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
