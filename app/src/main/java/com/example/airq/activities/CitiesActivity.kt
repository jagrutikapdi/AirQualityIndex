package com.example.airq.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.airq.adapter.CitiesAdapter
import com.example.airq.R
import com.example.airq.Status
import com.example.airq.model.Data
import com.example.airq.viewmodel.CityViewModel
import kotlinx.android.synthetic.main.activity_cities.*

class CitiesActivity : AppCompatActivity() {

    private lateinit var cityViewModel: CityViewModel
    private lateinit var adapter: CitiesAdapter
    private var dataList = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_Light)
        setContentView(R.layout.activity_cities)
        init()
        loadDataFromSocket()
    }

    private fun loadDataFromSocket() {
        if (::cityViewModel.isInitialized) {
            apply {
                cityViewModel.status.observe(this, { status: Status? ->
                    when (status) {
                        Status.DONE -> {
                            progressBar.visibility = View.GONE
                            rvCities.visibility = View.VISIBLE
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            rvCities.visibility = View.GONE
                        }
                        Status.FETCHING -> {
                            progressBar.visibility = View.VISIBLE
                            rvCities.visibility = View.GONE
                        }
                    }
                })
                cityViewModel.response.observe(this, { response: List<Data>? ->
                    if (response != null) {
                        adapter.updateItems(response.toMutableList())
                    }
                })
            }
        }
    }

    private fun init() {
        adapter = CitiesAdapter(dataList,this)
        rvCities.adapter = adapter
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        cityViewModel.setActivityContext(this)
        cityViewModel.startSocket()
    }

    override fun onDestroy() {
        cityViewModel.webSocket?.close(1000,"Socket Closed !!")
        super.onDestroy()
    }

}