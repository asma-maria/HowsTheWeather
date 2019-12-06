package com.sh4dow.climate;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    // TODO: Declare the member variables here

    private String mTemperature;
    private int mCondition;
    private String mCity;
    private String mIconName;
    private String mHumidity;
    private String mPressure;
    private String mWindSpeed;
    private String minTemp;
    private String maxTemp;
    private String conditionName;
    private String mPollution;
    private String mMainPollutant;
    private String mcountry;
    private String mWindDirection;
    private String mTimeStamp;
    private String mIcon;

    public static WeatherDataModel fromJSON(JSONObject jsonObject){
        WeatherDataModel weatherData = new WeatherDataModel();
        try {
            weatherData.mCity = jsonObject.getString("name");
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);

            weatherData.conditionName = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedTemp = (int)Math.rint(tempResult);
            weatherData.mTemperature = Integer.toString(roundedTemp);

            weatherData.mHumidity = Integer.toString(jsonObject.getJSONObject("main").getInt("humidity"));
            weatherData.mPressure = Double.toString(jsonObject.getJSONObject("main").getDouble("pressure"));
            weatherData.mWindSpeed = Double.toString(jsonObject.getJSONObject("wind").getDouble("speed"));

            double tempMinTemp = jsonObject.getJSONObject("main").getInt("temp_min") - 273.15;
            int roundedMinTemp = (int)Math.rint(tempMinTemp);
            weatherData.minTemp = Integer.toString(roundedMinTemp);

            double tempMaxTemp = jsonObject.getJSONObject("main").getInt("temp_max") - 273.15;
            int roundedMaxTemp = (int)Math.rint(tempMaxTemp);
            weatherData.maxTemp = Integer.toString(roundedMaxTemp);

            return weatherData;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static WeatherDataModel fromJSONPollution(JSONObject jsonObject){
        WeatherDataModel weatherData = new WeatherDataModel();
        try{
            weatherData.mCity = jsonObject.getJSONObject("data").getString("city");
            weatherData.mcountry = jsonObject.getJSONObject("data").getString("country");
            weatherData.mTimeStamp = jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getString("ts");

            //weather
            weatherData.mHumidity = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("hu"));
            weatherData.mPressure = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("pr"));
            weatherData.mTemperature = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("tp"));
            weatherData.mWindDirection = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("wd"));
            weatherData.mWindSpeed = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("ws"));
            String tempIcon = jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getString("ic");
            weatherData.mIcon = setWeatherIcon(tempIcon);

            // pollution
            weatherData.mPollution = Integer.toString(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getInt("aqius"));
            weatherData.mMainPollutant = jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getString("aqius");

            return weatherData;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }


    }

    private static String setWeatherIcon(String ic){
        if(ic.equals("01d")){
            return "clear_sky_day";
        }else if(ic.equals("01n")){
            return "clear_sky_night";
        }else if(ic.equals("02d")){
            return "few._clouds_day";
        }else if(ic.equals("02n")){
            return "few_clouds_night";
        }else if(ic.equals("03d")){
            return "scattered_clouds";
        }else if(ic.equals("04d")){
            return "broken_clouds";
        }else if(ic.equals("09d")){
            return "shower_rain";
        }else if(ic.equals("10d")){
            return "rain_day_time";
        }else if(ic.equals("10n")){
            return "rain_night_time";
        }else if(ic.equals("11d")){
            return "thunderstorm";
        }else if(ic.equals("13d")){
            return "snow";
        }else if(ic.equals("50d")){
            return "mist";
        }
        return "clear_sky_night";
    }


    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }
        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature + "°";
    }

    public String getmCity() {
        return mCity;
    }

    public String getmIconName() {
        return mIconName;
    }

    public String getmHumidity() {
        return mHumidity+"% hpa";
    }

    public String getmPressure() {
        return mPressure + " psi";
    }

    public String getmWindSpeed() {
        return mWindSpeed+" m/s";
    }

    public String getMinTemp() {
        return minTemp+ "°";
    }

    public String getMaxTemp() {
        return maxTemp+ "°";
    }

    public String getConditionName() {
        return conditionName;
    }

    public String getmPollution() {
        return mPollution;
    }

    public String getMcountry() {
        return mcountry;
    }

    public String getmWindDirection() {
        return mWindDirection;
    }

    public String getmTimeStamp() {
        return mTimeStamp;
    }

    public String getmMainPollutant() {
        return mMainPollutant;
    }

    public String getmIcon() {
        return mIcon;
    }
}
