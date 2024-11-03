package com.hys.exam.weatherapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDay {
    private String date;
    private double temperature;
    private String description;
}
