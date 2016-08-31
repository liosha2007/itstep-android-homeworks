package edu.android.homework_12.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author liosha (26.08.2016).
 */
public class Day {
    public final String dayicon;
    public final String daytitle;
    public final String dayfcttext;
    public final String nighticon;
    public final String nighttitle;
    public final String nightfcttext;

    public final String date;
    public final String high;
    public final String low;
    public final String conditions;
    public final String maxwind;
    public final String maxwinddir;
    public final String avewind;
    public final String avewinddir;

    public Day(String dayicon, String daytitle, String dayfcttext, String nighticon, String nighttitle, String nightfcttext, String date, String high, String low, String conditions, String maxwind, String maxwinddir, String avewind, String avewinddir) {
        this.dayicon = dayicon;
        this.daytitle = daytitle;
        this.dayfcttext = dayfcttext;
        this.nighticon = nighticon;
        this.nighttitle = nighttitle;
        this.nightfcttext = nightfcttext;
        this.date = date;
        this.high = high;
        this.low = low;
        this.conditions = conditions;
        this.maxwind = maxwind;
        this.maxwinddir = maxwinddir;
        this.avewind = avewind;
        this.avewinddir = avewinddir;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
