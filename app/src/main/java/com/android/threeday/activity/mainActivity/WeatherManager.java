package com.android.threeday.activity.mainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.widget.Toast;

import com.android.threeday.R;
import com.android.threeday.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by user on 2014/12/26.
 */
public class WeatherManager {
    private final long LOCATION_UPDATE_TIME = 12 * 60 * 60 * 1000;
    private final long WEATHER_UPDATE_TIME = 6 * 60 * 60 * 1000;
    private final long WEATHER_PAST_TIME = 24 * 60 * 60 * 1000;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private Weather mTodayWeather;
    private Weather mTomorrowWeather;
    private WeatherLoadListener mWeatherLoadListener;
    private Handler mHandler = new Handler();
    private LocationManager mLocationManager;
    private Location mLocation;

    private boolean mIsWeatherAvailable;

    public WeatherManager(Context context){
        this.mContext = context;
        this.mSharedPreferences = this.mContext.getSharedPreferences(Util.PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.mTodayWeather = new Weather();
        this.mTomorrowWeather = new Weather();
    }

    public void checkWeather( ){
        long lastWeatherTime = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_WEATHER_UPDATE_TIME, 0);
        Time now = new Time();
        now.setToNow();
        long nowMills = now.toMillis(false);
        if(nowMills - lastWeatherTime > WEATHER_UPDATE_TIME){
            if(isNetworkAvailable()){
                checkLocation();
                return;
            }
        }
        checkPastWeather(lastWeatherTime, nowMills);
    }

    public boolean isWeatherAvailable( ){
        return this.mIsWeatherAvailable;
    }

    public void setWeatherLoadListener(WeatherLoadListener weatherLoadListener){
        this.mWeatherLoadListener = weatherLoadListener;
    }

    private boolean isNetworkAvailable( ){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setWeather(boolean moveOneDay, long todayTimeMills){
        String todayWeather;
        String todayTemperature;
        String tomorrowWeather;
        String tomorrowTemperature;
        if(moveOneDay){
            todayWeather = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER2_WEATHER, null);
            todayTemperature = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER2_TEMPERATURE, null);
            tomorrowWeather = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER3_WEATHER, null);
            tomorrowTemperature = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER3_TEMPERATURE, null);
        }else{
            todayWeather = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER1_WEATHER, null);
            todayTemperature = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER1_TEMPERATURE, null);
            tomorrowWeather = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER2_WEATHER, null);
            tomorrowTemperature = this.mSharedPreferences.getString(Util.PREFERENCE_KEY_WEATHER2_TEMPERATURE, null);
        }
        this.mTodayWeather.setTime(todayTimeMills);
        this.mTodayWeather.setWeather(todayWeather);
        this.mTodayWeather.setTemperature(todayTemperature);
        this.mTomorrowWeather.setTime(todayTimeMills + Util.A_DAY_IN_MILLIS);
        this.mTomorrowWeather.setWeather(tomorrowWeather);
        this.mTomorrowWeather.setTemperature(tomorrowTemperature);

        this.mIsWeatherAvailable = true;
    }

    public String getTodayWeather( ){
        return this.mTodayWeather.getWeather();
    }

    public String getTodayTemperature( ){
        return this.mTodayWeather.getTemperature();
    }

    public String getTomorrowWeather( ){
        return this.mTomorrowWeather.getWeather();
    }

    public String getTomorrowTemperature( ){
        return this.mTomorrowWeather.getTemperature();
    }

    private void checkPastWeather( ){
        long lastWeatherTime = this.mSharedPreferences.getLong(Util.PREFERENCE_KEY_WEATHER_UPDATE_TIME, 0);
        Time now = new Time();
        now.setToNow();
        checkPastWeather(lastWeatherTime, now.toMillis(false));
    }

    private void checkPastWeather(long lastWeatherTimeMills, long nowMills){
        if(nowMills - lastWeatherTimeMills > WEATHER_PAST_TIME){
            this.mIsWeatherAvailable = false;
        }else{
            Time time = new Time();
            Time now = new Time();
            now.setToNow();
            time.set(lastWeatherTimeMills);
            if(time.year == now.year){
                if(time.yearDay == now.yearDay){
                    setWeather(false, lastWeatherTimeMills);
                }else if(time.yearDay == now.yearDay - 1){
                    setWeather(true, lastWeatherTimeMills + Util.A_DAY_IN_MILLIS);
                }else{
                    this.mIsWeatherAvailable = false;
                }
            }else if(time.year == now.year - 1 && now.yearDay == 0){
                setWeather(true, lastWeatherTimeMills + Util.A_DAY_IN_MILLIS);
            }else{
                this.mIsWeatherAvailable = false;
            }

            if(mWeatherLoadListener != null){
                this.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mIsWeatherAvailable){
                            mWeatherLoadListener.onWeatherLoadSuccess();
                        } else{
                            mWeatherLoadListener.onWeatherLoadFail();
                        }
                    }
                });
            }
        }
    }

    private void requestWeather( ){
        new WeatherAsyncTask().execute();
    }

    private void checkLocation( ){
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        this.mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Time time = new Time();
        time.setToNow();
        if(this.mLocation != null){
            if(time.toMillis(false) - this.mLocation.getTime() < LOCATION_UPDATE_TIME){
                requestWeather();
                return;
            }
        }
        this.mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(this.mLocation != null){
            if(time.toMillis(false) - this.mLocation.getTime() > LOCATION_UPDATE_TIME){
                updateLocation( );
            }else{
                requestWeather();
            }
        }else{
            updateLocation();
        }
    }

    private void updateLocation( ){
        if(this.mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            this.mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLocation = location;
                    mLocationManager.removeUpdates(this);
                    requestWeather();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }else{
            checkPastWeather();
            if(!this.mIsWeatherAvailable){
                Toast.makeText(this.mContext, R.string.location_disable, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class WeatherAsyncTask extends AsyncTask{
        private final String mWeatherUrl1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
        private final String mWeatherUrl2 = "&output=json&mcode=" + Util.WEATHER_BAIDU_MCODE + "&ak=" + Util.WEATHER_BAIDU_APPKEY;

        @Override
        protected Object doInBackground(Object[] params) {
            checkWeather();
            return null;
        }

        private void saveWeatherData(String cityName, JSONArray weatherData) throws JSONException {
            JSONObject todayJSON = weatherData.getJSONObject(0);
            JSONObject tomorrowJSON = weatherData.getJSONObject(1);
            JSONObject tomorrowAfterJSON = weatherData.getJSONObject(2);
            Time now = new Time();
            now.setToNow();
            long nowMills = now.toMillis(false);
            mTodayWeather.setTime(nowMills);
            mTodayWeather.setWeather(todayJSON.getString("weather"));
            mTodayWeather.setTemperature(todayJSON.getString("temperature"));
            mTomorrowWeather.setTime(nowMills);
            mTomorrowWeather.setWeather(tomorrowJSON.getString("weather"));
            mTomorrowWeather.setTemperature(tomorrowJSON.getString("temperature"));
            mSharedPreferences.edit().putString(Util.PREFERENCE_KEY_WEATHER_CITY_NAME, cityName)
                    .putLong(Util.PREFERENCE_KEY_WEATHER_UPDATE_TIME, nowMills).
                    putString(Util.PREFERENCE_KEY_WEATHER1_WEATHER, mTodayWeather.getWeather()).
                    putString(Util.PREFERENCE_KEY_WEATHER1_TEMPERATURE, mTodayWeather.getTemperature()).
                    putString(Util.PREFERENCE_KEY_WEATHER2_WEATHER, mTomorrowWeather.getWeather()).
                    putString(Util.PREFERENCE_KEY_WEATHER2_TEMPERATURE, mTomorrowWeather.getTemperature()).
                    putString(Util.PREFERENCE_KEY_WEATHER3_WEATHER, tomorrowAfterJSON.getString("weather")).
                    putString(Util.PREFERENCE_KEY_WEATHER3_TEMPERATURE, tomorrowAfterJSON.getString("temperature")).commit();
        }

        private void checkWeather( ){
            if(mLocation != null){
                boolean isGetSuccess = false;
                InputStream inputStream = null;
                try {
                    Geocoder geocoder = new Geocoder(mContext);
                    List<Address> list = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                    String location = URLEncoder.encode(list.get(0).getLocality(), "utf-8");
                    String urlString = this.mWeatherUrl1 + location + this.mWeatherUrl2;
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    int resultCode = httpURLConnection.getResponseCode();
                    if(resultCode != -1){
                        inputStream = httpURLConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String string;
                        StringBuilder builder = new StringBuilder();
                        while((string = reader.readLine()) != null){
                            builder.append(string);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(builder.toString());
                        if(jsonObject.getString("status").equals("success")){
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            JSONObject weatherJsonObject = jsonArray.getJSONObject(0);
                            String cityName = weatherJsonObject.getString("currentCity");
                            JSONArray weatherData = weatherJsonObject.getJSONArray("weather_data");
                            saveWeatherData(cityName, weatherData);
                            isGetSuccess = true;
                            mIsWeatherAvailable = true;
                            if(mWeatherLoadListener != null){
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWeatherLoadListener.onWeatherLoadSuccess();
                                    }
                                });
                            }
                        }
                    }

                } catch (Exception e) {

                }finally {
                    if(!isGetSuccess){
                        checkPastWeather();
                    }
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    private class Weather{
        private long mTime;
        private String mTemperature;
        private String mWeather;

        public void setTime(long time){
            this.mTime = time;
        }

        public long getTime( ){
            return this.mTime;
        }

        public void setTemperature(String temperature){
            this.mTemperature = temperature;
        }

        public String getTemperature( ){
            return this.mTemperature;
        }

        public void setWeather(String weather){
            this.mWeather = weather;
        }

        public String getWeather( ){
            return this.mWeather;
        }

    }

    public interface WeatherLoadListener{
        public void onWeatherLoadSuccess( );
        public void onWeatherLoadFail( );
    }
}
