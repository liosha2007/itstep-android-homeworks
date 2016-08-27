package edu.android.homework_11.tasks;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.android.homework_11.MainActivity;
import edu.android.homework_11.model.Day;
import edu.android.homework_11.model.Weather;

/**
 * @author liosha (26.08.2016).
 */
public class NetworkTask extends GenericTask<String, Void, Weather> {
    public static final String URL = "http://api.wunderground.com/api/5e3264a16de5b83a/geolookup/conditions/forecast10day/lang:RU/q/%s.json";

    public NetworkTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Weather doInBackground(String... strings) {
        String city = strings[0];
        try {
            String url = String.format(Locale.US, URL, city != null && !city.isEmpty() ? city : "autoip");
            java.net.URL client = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) client.openConnection();
            final InputStream inputStream = urlConnection.getInputStream();
            final String jsonData = IOUtils.toString(inputStream, Charset.forName("utf-8"));
            inputStream.close();

            JSONObject jsonRoot = new JSONObject(jsonData);
            final JSONObject jsonLocation = jsonRoot.getJSONObject("location");
            final JSONObject jsonForecast = jsonRoot.getJSONObject("forecast");

            final Weather weather = new Weather(
                    jsonLocation.getString("country_name"),
                    jsonLocation.getString("city"),
                    jsonLocation.getString("lat"),
                    jsonLocation.getString("lon")
            );
            final JSONObject jsonTxtForecast = jsonForecast.getJSONObject("txt_forecast");
            final JSONObject jsonSimpleForecast = jsonForecast.getJSONObject("simpleforecast");
            final JSONArray jsonForecastday = jsonTxtForecast.getJSONArray("forecastday");

            final JSONArray jsonSimpleForecastday = jsonSimpleForecast.getJSONArray("forecastday");

            for (int n = 0, m = 0; n < jsonSimpleForecastday.length(); n++, m++) {

                final JSONObject jsonDay = (JSONObject) jsonForecastday.get(m);
                final String dayicon = jsonDay.getString("icon");
                final String daytitle = jsonDay.getString("title");
                final String dayfcttext = jsonDay.getString("fcttext_metric");

                final JSONObject jsonNight = (JSONObject) jsonForecastday.get(++m);
                final String nighticon = jsonNight.getString("icon");
                final String nighttitle = jsonNight.getString("title");
                final String nightfcttext = jsonNight.getString("fcttext_metric");

                final JSONObject jsonDayDetails = (JSONObject) jsonSimpleForecastday.get(n);
                final String day = jsonDayDetails.getJSONObject("date").getString("day");
                final String month = jsonDayDetails.getJSONObject("date").getString("month");
                final String year = jsonDayDetails.getJSONObject("date").getString("year");
                final String weekday = jsonDayDetails.getJSONObject("date").getString("weekday");
                final String highCelsius = jsonDayDetails.getJSONObject("high").getString("celsius");
                final String lowCelsius = jsonDayDetails.getJSONObject("low").getString("celsius");
                final String conditions = jsonDayDetails.getString("conditions");
                final String maxWindKph = jsonDayDetails.getJSONObject("maxwind").getString("kph");
                final String maxWindDir = jsonDayDetails.getJSONObject("maxwind").getString("dir");
                final String aveWindKph = jsonDayDetails.getJSONObject("avewind").getString("kph");
                final String aveWindDir = jsonDayDetails.getJSONObject("avewind").getString("dir");

                final String date = String.format(Locale.US, "%2s.%2s.%s", day, month, year).replaceAll(" ", "0");

                final Day dayObj = new Day(
                        dayicon, daytitle, dayfcttext,
                        nighticon, nighttitle, nightfcttext,
                        date, highCelsius, lowCelsius, conditions, maxWindKph, maxWindDir, aveWindKph, aveWindDir);
                weather.days.add(dayObj);
            }
            return weather;
        } catch (Exception e) {
            Log.d("MY", "Can't download the weather!", e);
            cancel(false);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        ref.get().onDataDownloaded(weather);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        ref.get().onCantLoad();
    }
}
