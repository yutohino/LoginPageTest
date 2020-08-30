package com.example.loginpagetest

import com.example.loginpagetest.data.WeatherItem
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Single<WeatherItem>
}
