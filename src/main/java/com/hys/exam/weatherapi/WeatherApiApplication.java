package com.hys.exam.weatherapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);

        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("WEATHER_API_KEY");
        String apiUrl = dotenv.get("WEATHER_API_URL");

        // API 키와 URL을 사용하는 로직
        System.out.println("API Key: " + apiKey);
        System.out.println("API URL: " + apiUrl);
    }
}
