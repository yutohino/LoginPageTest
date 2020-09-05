package com.example.loginpagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.loginpagetest.data.WeatherItem
import com.example.loginpagetest.databinding.ActivityWeatherBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherActivity : AppCompatActivity(), PlaceItemAdapter.Listener {

    lateinit var binding: ActivityWeatherBinding

    lateinit var weatherApiService: WeatherApiService

    val compositeDisposable = CompositeDisposable()
    var currentCityName: String? = null

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

    /**
     * 地名と地名のパラメータのリストを作成・取得
     *
     * @return 地名と地名のパラメータのリスト
     */
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

    /**
     * タップしたリストの地名パラメータを用いてAPIコールの実行、成功時の処理・失敗時の処理まで
     *
     * @param cityName タップしたリストの地名のパラメータ
     */
    override fun onRequest(cityName: String) {
        if (currentCityName == cityName) {
            return
        }
        currentCityName = cityName

        // TODO: 遅延処理でストリームを打ち消す
        val weatherDisposable: Disposable = weatherApiService.getWeather(cityName, OpenWeatherMapApi.API_KEY)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<WeatherItem>() {
                override fun onSuccess(t: WeatherItem) {
                    setText(t)
                    val progressCircle = CircularProgressDrawable(this@WeatherActivity)
                    progressCircle.strokeWidth = 10f
                    progressCircle.centerRadius = 30f
                    progressCircle.start()
                    val option: RequestOptions = RequestOptions()
                        .placeholder(progressCircle)
                        .error(R.drawable.error)
                    Glide.with(this@WeatherActivity)
                        .setDefaultRequestOptions(option)
                        .load("https://openweathermap.org/img/wn/${t.weather[0].icon}@2x.png")
                        .into(binding.iconWeather)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@WeatherActivity, "通信エラーが発生しました", Toast.LENGTH_SHORT).show()
                }
            })
        compositeDisposable.add(weatherDisposable)
    }

    /**
     * パースされたJSONデータをTextViewに表示
     *
     * @param item WeatherItemクラスからパースしたJSONデータを使う
     */
    fun setText(item: WeatherItem) {
        binding.tvCity.text = item.name // 町の名前
        binding.tvWeather.text = "${item.weather[0].main}：${item.weather[0].description}（雲率${item.clouds.all}％）" // 天気・詳細・曇り率
        binding.tvTimeCalculation.text = "計算時刻：${item.dt}" // 計算時刻
        binding.tvTemp.text = "気温：${item.main.temp}" // 気温
        binding.tvFeelsLikeTemp.text = "体感温度：${item.main.feels_like}" // 体感温度
        binding.tvTempMin.text = "最低気温：${item.main.temp_min}" // 最低気温
        binding.tvTempMax.text = "最高気温：${item.main.temp_max}" // 最高気温
        binding.tvPressure.text = "気圧：${item.main.pressure}" // 気圧
        binding.tvHumidity.text = "湿度：${item.main.humidity}" // 湿度
        binding.tvSpeed.text = "風速：${item.wind.speed}" // 風速
        binding.tvDegree.text = "風向：${item.wind.deg}" // 風向
        binding.tvSunrise.text = "日の出：${item.sys.sunrise}" // 日の出
        binding.tvSunset.text = "日の入り：${item.sys.sunset}" // 日の入り
        binding.tvJson.text = "送られてきたJSONデータ：\n${item}" // 送られてきたJSONデータ
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
