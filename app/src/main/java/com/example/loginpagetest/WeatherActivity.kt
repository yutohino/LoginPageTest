package com.example.loginpagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginpagetest.data.WeatherItem
import com.example.loginpagetest.databinding.ActivityMainBinding
import com.example.loginpagetest.databinding.ActivityWeatherBinding
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

class WeatherActivity : AppCompatActivity(), PlaceItemAdapter.Listener {

    val compositeDisposable = CompositeDisposable()

    lateinit var binding: ActivityWeatherBinding

    lateinit var weatherApiService: WeatherApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
    }

    override fun onResume() {
        super.onResume()

        val list = createList()

        binding.rv.setHasFixedSize(true)
        val adapter = PlaceItemAdapter(list, this)
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = linearLayoutManager
        adapter.notifyDataSetChanged()

        weatherApiService = Provider.provideWeatherApiService()!!
    }

    fun createList(): List<PlaceData> {
        val list = ArrayList<PlaceData>()
        list.add(PlaceData("札幌", "sapporo"))
        list.add(PlaceData("新潟", "nigata"))
        list.add(PlaceData("仙台", "sendai"))
        list.add(PlaceData("東京", "tokyo"))
        list.add(PlaceData("名古屋", "nagoya"))
        list.add(PlaceData("金沢", "kanazawa"))
        list.add(PlaceData("京都", "kyoto"))
        list.add(PlaceData("大阪", "osaka"))
        list.add(PlaceData("広島", "hiroshima"))
        list.add(PlaceData("高知", "kochi"))
        list.add(PlaceData("福岡", "fukuoka"))
        list.add(PlaceData("那覇", "naha"))
        return list
    }

    override fun onRequest(cityName: String) {
        val weatherDisposable: Disposable = weatherApiService.getWeather(cityName, OpenWeatherMapApi.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<WeatherItem>() {
                override fun onSuccess(t: WeatherItem) {
                    setText(t)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@WeatherActivity, "通信エラーが発生しました", Toast.LENGTH_SHORT).show()
                }

            })
        compositeDisposable.add(weatherDisposable)
    }

    fun setText(item : WeatherItem) {
        Log.d("testas", item.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
