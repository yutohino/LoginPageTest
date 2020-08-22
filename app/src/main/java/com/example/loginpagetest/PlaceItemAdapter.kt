package com.example.loginpagetest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.loginpagetest.databinding.ItemPlaceBinding

class PlaceItemAdapter(var dataList: List<PlaceData>) : RecyclerView.Adapter<PlaceItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPlaceBinding>(LayoutInflater.from(parent.context), R.layout.item_place, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textPlace.text = dataList[position].placeTitle

        // TODO：タップした際にAPIリクエストを送る。
    }

    class ViewHolder constructor(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)
}
