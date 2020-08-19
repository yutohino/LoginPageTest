package com.example.loginpagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginpagetest.databinding.ActivityMainBinding
import com.example.loginpagetest.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {
    lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
    }

    override fun onResume() {
        super.onResume()

        val list = createList()

        binding.rv.setHasFixedSize(true)
        val adapter = PlaceItemAdapter(list)
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = linearLayoutManager
        adapter.notifyDataSetChanged()
    }

    fun createList(): List<PlaceData> {
        var list = ArrayList<PlaceData>()
        list.add(PlaceData("Tokyo"))
        list.add(PlaceData("Kyoto"))
        list.add(PlaceData("Osaka"))
        list.add(PlaceData("Fukuoka"))
        return list
    }
}
