package com.hys.exam.weatherapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WeatherConfiguration {

    private final String apiKey;
    private final String apiUrl;

    public WeatherConfiguration() {
        Dotenv dotenv = Dotenv.configure().load();
        this.apiKey = dotenv.get("WEATHER_API_KEY");
        this.apiUrl = dotenv.get("WEATHER_API_URL");
    }

}
