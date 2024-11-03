package com.hys.exam.weatherapi.service;

import com.hys.exam.weatherapi.config.WeatherConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final WeatherConfiguration config;
    private final RestTemplate restTemplate;

    public WeatherService(WeatherConfiguration config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public double[] getCoordinates(String city) {
        String geocodingUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", city, config.getApiKey());
        String response = restTemplate.getForObject(geocodingUrl, String.class);

        // JSON 배열로 파싱하여 위도와 경도 추출
        JSONArray jsonArray = new JSONArray(response);
        JSONObject location = jsonArray.getJSONObject(0);
        double lat = location.getDouble("lat");
        double lon = location.getDouble("lon");

        return new double[]{lat, lon};
    }

    // 주간 날씨 데이터를 가져오는 메소드
    public String getWeeklyWeather(String city) {
        double[] coordinates = getCoordinates(city);
        String url = String.format("%s?lat=%f&lon=%f&exclude=minutely,hourly,alerts&units=metric&appid=%s",
                config.getApiUrl(), coordinates[0], coordinates[1], config.getApiKey());
        return restTemplate.getForObject(url, String.class);
    }

    // 현재 날씨 정보를 가져오는 메소드
    public Map<String, Object> getCurrentWeather(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject current = jsonObject.getJSONObject("current");
        JSONObject weather = current.getJSONArray("weather").getJSONObject(0);

        Map<String, Object> currentWeather = new HashMap<>();
        currentWeather.put("temperature", current.getDouble("temp"));
        currentWeather.put("feels_like", current.getDouble("feels_like"));
        currentWeather.put("description", weather.getString("description"));
        currentWeather.put("humidity", current.getInt("humidity"));
        currentWeather.put("clouds", current.getInt("clouds"));
        currentWeather.put("wind_speed", current.getDouble("wind_speed"));
        currentWeather.put("uvi", current.getDouble("uvi"));

        return currentWeather;
    }

    // 주간 날씨 예보 정보를 가져오는 메소드
    public List<Map<String, Object>> getWeeklyForecast(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray daily = jsonObject.getJSONArray("daily");

        List<Map<String, Object>> weeklyForecast = new ArrayList<>();

        for (int i = 0; i < daily.length(); i++) {
            JSONObject day = daily.getJSONObject(i);
            JSONObject temp = day.getJSONObject("temp");
            JSONObject weather = day.getJSONArray("weather").getJSONObject(0);

            Map<String, Object> dailyWeather = new HashMap<>();
            dailyWeather.put("date", day.getLong("dt"));  // UNIX 타임스탬프
            dailyWeather.put("temp_max", temp.getDouble("max"));
            dailyWeather.put("temp_min", temp.getDouble("min"));
            dailyWeather.put("description", weather.getString("description"));
            dailyWeather.put("pop", day.optDouble("pop", 0));  // 강수 확률
            dailyWeather.put("uvi", day.getDouble("uvi"));

            weeklyForecast.add(dailyWeather);
        }

        return weeklyForecast;
    }
}