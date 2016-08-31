package edu.android.homework_12;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liosha (27.08.2016).
 */
public class Utils {
    private static Map<String, Integer> ICONS = new HashMap<String, Integer>(){{
        put("chanceflurries", R.drawable.chanceflurries);
        put("chancerain", R.drawable.chancerain);
        put("chancesleet", R.drawable.chancesleet);
        put("chancesnow", R.drawable.chancesnow);
        put("chancetstorms", R.drawable.chancetstorms);
        put("clear", R.drawable.clear);
        put("cloudy", R.drawable.cloudy);
        put("flurries", R.drawable.flurries);
        put("fog", R.drawable.fog);
        put("hazy", R.drawable.hazy);
        put("mostlycloudy", R.drawable.mostlycloudy);
        put("mostlysunny", R.drawable.mostlysunny);
        put("nt_chanceflurries", R.drawable.nt_chanceflurries);
        put("nt_chancerain", R.drawable.nt_chancerain);
        put("nt_chancesleet", R.drawable.nt_chancesleet);
        put("nt_chancesnow", R.drawable.nt_chancesnow);
        put("nt_chancetstorms", R.drawable.nt_chancetstorms);
        put("nt_clear", R.drawable.nt_clear);
        put("nt_cloudy", R.drawable.nt_cloudy);
        put("nt_flurries", R.drawable.nt_flurries);
        put("nt_fog", R.drawable.nt_fog);
        put("nt_hazy", R.drawable.nt_hazy);
        put("nt_mostlycloudy", R.drawable.nt_mostlycloudy);
        put("nt_mostlysunny", R.drawable.nt_mostlysunny);
        put("nt_partlycloudy", R.drawable.nt_partlycloudy);
        put("nt_partlysunny", R.drawable.nt_partlysunny);
        put("nt_rain", R.drawable.nt_rain);
        put("nt_sleet", R.drawable.nt_sleet);
        put("nt_snow", R.drawable.nt_snow);
        put("nt_sunny", R.drawable.nt_sunny);
        put("nt_tstorms", R.drawable.nt_tstorms);
        put("partlycloudy", R.drawable.partlycloudy);
        put("partlysunny", R.drawable.partlysunny);
        put("rain", R.drawable.rain);
        put("sleet", R.drawable.sleet);
        put("snow", R.drawable.snow);
        put("sunny", R.drawable.sunny);
        put("tstorms", R.drawable.tstorms);
    }};

    public static int resourceIdByName(String name) {
        return ICONS.get(name);
    }

    public static Connection openConnection(String connectionString) throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
}
