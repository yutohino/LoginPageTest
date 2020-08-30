package com.example.loginpagetest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpagetest.databinding.ItemPlaceBinding

class PlaceItemAdapter(var dataList: List<PlaceData>, val activity: WeatherActivity) : RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>() {

    lateinit var listener: Listener

    interface Listener {
        fun onRequest(cityName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPlaceBinding>(LayoutInflater.from(parent.context), R.layout.item_place, parent, false)
        listener = activity
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textPlace.text = dataList[position].placeTitle


        holder.itemView.setOnClickListener {
            listener.onRequest(dataList[position].cityName)
        }
    }

    class ViewHolder constructor(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)
}
