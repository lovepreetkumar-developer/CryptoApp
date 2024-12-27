package com.lnadeem.app.models

data class HourlyWeatherResponse(
    val city: CityModel,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherResponse>,
    val message: Int
)

data class CityModel(
    val coord: CoordModel,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class RainModel(
    val `3h`: Double
)