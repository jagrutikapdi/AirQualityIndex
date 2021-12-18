package com.airq.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.airq.DataDiffCallback
import com.example.airq.R
import com.example.airq.activities.CityDetailsActivity
import com.example.airq.model.Data
import java.text.DecimalFormat


class CitiesAdapter(private var searchResults: MutableList<Data>,internal var context:Activity) : RecyclerView.Adapter<CitiesAdapter.SearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.adapter_cities
                , parent, false)

        return SearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult = searchResults[position]
        holder.stationName.text = searchResult.city
        holder.stationAQI.text = roundOffDecimal(searchResult.aqi).toString()
        var airQuality:String? = null
        val colorId =
            when {
                searchResult.aqi.toInt() in 0..50 -> {
                    airQuality = context.getString(R.string.good)
                    ContextCompat.getColor(context, R.color.scaleGood)
                }
                searchResult.aqi.toInt() in 51..100 -> {
                    airQuality = context.getString(R.string.satisfactory)
                    ContextCompat.getColor(context, R.color.satisfactory)
                }
                searchResult.aqi.toInt() in 101..200 -> {
                    airQuality = context.getString(R.string.moderate)
                    ContextCompat.getColor(context, R.color.scaleModerate)
                }

                searchResult.aqi.toInt() in 201..300 -> {
                    airQuality = context.getString(R.string.poor)
                    ContextCompat.getColor(context, R.color.poor)
                }
                searchResult.aqi.toInt() in 301..400 -> {
                    airQuality = context.getString(R.string.very_poor)
                    ContextCompat.getColor(context, R.color.very_poor)
                }
                searchResult.aqi.toInt() in 401..500 -> {
                    airQuality = context.getString(R.string.sereve)
                    ContextCompat.getColor(context, R.color.severe)
                }
                else -> {
                    ContextCompat.getColor(context, R.color.scaleGood)
                }
            }
        holder.airQuality.setText( airQuality)
        holder.itemBg.setBackgroundColor( colorId)
        holder.itemView.setOnClickListener {
            var intent =Intent(context, CityDetailsActivity::class.java)
            intent.putExtra("Data",searchResults[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    fun updateItems(data: MutableList<Data>) {

        val diffResult = DiffUtil.calculateDiff(DataDiffCallback(data, data))
        diffResult.dispatchUpdatesTo(this)

        searchResults.clear()
        searchResults = data
        notifyDataSetChanged()
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        return df.format(number).toDouble()
    }

    inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById(R.id.stationName)
        val stationAQI: TextView = itemView.findViewById(R.id.stationAQI)
        val airQuality: TextView = itemView.findViewById(R.id.airQuality)
        val itemBg: ImageView = itemView.findViewById(R.id.itemBg)
    }
}