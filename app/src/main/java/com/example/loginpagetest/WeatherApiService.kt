package com.example.loginpagetest

import com.example.loginpagetest.data.WeatherItem
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Observable<WeatherItem>
}
