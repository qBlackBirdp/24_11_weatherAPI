package com.hys.exam.weatherapi.controler;

import com.hys.exam.weatherapi.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(defaultValue = "서울") String city, Model model) {
        String jsonResponse = weatherService.getWeeklyWeather(city);
        Map<String, Object> currentWeather = weatherService.getCurrentWeather(jsonResponse);
        List<Map<String, Object>> weeklyForecast = weatherService.getWeeklyForecast(jsonResponse);

        model.addAttribute("city", city); // 검색한 도시 이름 추가
        model.addAttribute("currentWeather", currentWeather);
        model.addAttribute("weeklyForecast", weeklyForecast);

        return "weather"; // Thymeleaf 템플릿 이름 반환
    }
}
