package com.example.loginpagetest

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Provider {

    companion object {
        var weatherApiService: WeatherApiService? = null

        fun provideWeatherApiService(): WeatherApiService? {
            if (weatherApiService == null) {
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create()) // コンバーター
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RxJavaでSchedulerの設定
                    .build()
                weatherApiService = retrofit.create(WeatherApiService::class.java)
            }

            return weatherApiService
        }
    }
}
