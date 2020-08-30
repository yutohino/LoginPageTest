package com.example.loginpagetest.data

import com.google.gson.annotations.SerializedName

data class WeatherItem(@SerializedName("coord") val coord: WeatherItemCoord,
                       @SerializedName("weather") val weather: List<WeatherItemOverview>,
                       @SerializedName("base") val base: String,
                       @SerializedName("main") val main: WeatherItemMain,
                       @SerializedName("visibility") val visibility: Int,
                       @SerializedName("wind") val wind: WeatherItemWind,
                       @SerializedName("clouds") val clouds: WeatherItemClouds,
                       @SerializedName("dt") val dt: Int,
                       @SerializedName("sys") val sys: WeatherItemSys,
                       @SerializedName("timezone") val timezone: Int,
                       @SerializedName("id") val id: Int, @SerializedName("name") val name: String,
                       @SerializedName("cod") val cod: Int) {

    data class WeatherItemCoord(@SerializedName("lon") val lon: Double,
                                @SerializedName("lat") val lat: Double)

    data class WeatherItemOverview(@SerializedName("id") val id: Int,
                                   @SerializedName("main") val main: String,
                                   @SerializedName("description") val description: String,
                                   @SerializedName("icon") val icon: String)

    data class WeatherItemMain(@SerializedName("temp") val temp: Double,
                               @SerializedName("feels_like") val feels_like: Double,
                               @SerializedName("temp_min") val temp_min: Double,
                               @SerializedName("temp_max") val temp_max: Double,
                               @SerializedName("pressure") val pressure: Int,
                               @SerializedName("humidity") val humidity: Int)

    data class WeatherItemWind(@SerializedName("speed") val speed: Double,
                               @SerializedName("deg") val deg: Int)

    data class WeatherItemClouds(@SerializedName("all") val all: Int)

    data class WeatherItemSys(@SerializedName("type") val type: Int,
                              @SerializedName("id") val id: Int,
                              @SerializedName("country") val country: String,
                              @SerializedName("sunrise") val sunrise: Int,
                              @SerializedName("sunset") val sunset: Int)


}
