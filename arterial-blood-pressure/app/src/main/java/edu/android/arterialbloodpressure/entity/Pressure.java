package edu.android.arterialbloodpressure.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liosha (09.09.2016).
 */
public class Pressure {
    public final Date date;
    public final int up;
    public final int down;

    public Pressure(Date date, int up, int down) {
        this.date = date;
        this.up = up;
        this.down = down;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd (HH:mm:ss):   ");
        return dateFormat.format(date) + "\t" + up + "/" + down;
    }
}
