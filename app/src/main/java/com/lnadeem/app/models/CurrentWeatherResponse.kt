package com.lnadeem.app.models

data class WeatherResponse(
    val base: String,
    val clouds: CloudsModel,
    val cod: Int,
    val coord: CoordModel,
    val dt: Int,
    val id: Int,
    val main: MainModel,
    val name: String,
    val sys: SysModel,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherModel>,
    val wind: WindModel,
    val rain: RainModel,
    val pop: Double,
    val dt_txt: String,
)

data class CloudsModel(
    val all: Int
)

data class CoordModel(
    val lat: Double,
    val lon: Double
)

data class MainModel(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class SysModel(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int,
    val pod: String
)

data class WeatherModel(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class WindModel(
    val deg: Int,
    val speed: Double,
    val gust: Double,
)