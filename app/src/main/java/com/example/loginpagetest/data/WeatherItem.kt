package com.example.loginpagetest.data

import com.google.gson.annotations.SerializedName

data class WeatherItem(@SerializedName("weather") val weather: List<WeatherItemOverview>,
                       @SerializedName("main") val main: WeatherItemMain,
                       @SerializedName("wind") val wind: WeatherItemWind,
                       @SerializedName("clouds") val clouds: WeatherItemClouds,
                       @SerializedName("dt") val dt: Int, // 計算時刻
                       @SerializedName("sys") val sys: WeatherItemSys,
                       @SerializedName("name") val name: String) { // 町の名前

    data class WeatherItemOverview(@SerializedName("main") val main: String, // 天気
                                   @SerializedName("description") val description: String, // 天気の詳細
                                   @SerializedName("icon") val icon: String) // 天気のアイコン（別にAPIコールする）

    data class WeatherItemMain(@SerializedName("temp") val temp: Double,
                               @SerializedName("feels_like") val feels_like: Double,
                               @SerializedName("temp_min") val temp_min: Double,
                               @SerializedName("temp_max") val temp_max: Double,
                               @SerializedName("pressure") val pressure: Int,
                               @SerializedName("humidity") val humidity: Int)

    data class WeatherItemWind(@SerializedName("speed") val speed: Double, // 風速
                               @SerializedName("deg") val deg: Int) // 風向

    data class WeatherItemClouds(@SerializedName("all") val all: Int) // 曇りのパーセンテージ

    data class WeatherItemSys(@SerializedName("sunrise") val sunrise: Int, // 日の出の時間（わかんないけど）
                              @SerializedName("sunset") val sunset: Int) // 日の沈む時間（わかんないけど）


}
