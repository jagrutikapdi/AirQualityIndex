package com.example.airq.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.airq.R
import com.example.airq.model.Data
import com.example.airq.viewmodel.CityViewModel
import kotlinx.android.synthetic.main.layout_aqi_circle.*
import kotlinx.android.synthetic.main.layout_location_text.*
import java.text.DecimalFormat


class CityDetailsActivity : AppCompatActivity() {

    //Data
    private lateinit var cityViewModel: CityViewModel
    var data: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme_Light)
        setContentView(R.layout.activity_city_details)
        if (intent != null)
            data = intent.getSerializableExtra("Data") as Data
        locationTextView.text = data?.city
        updateUI(data!!)
        init()
        loadDataFromSocket()
    }


    private fun init() {
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        cityViewModel.setActivityContext(this)
        cityViewModel.startSocket()
    }

    private fun loadDataFromSocket() {
        if (::cityViewModel.isInitialized) {
            apply {
                cityViewModel.response.observe(this, { response: List<Data>? ->
                    if (response != null) {
                        findCity(response)
                    }
                })
            }
        }
    }

    private fun findCity(response: List<Data>) {
        for (item in response) {
            if (data?.city.equals(item.city)) {

//                aqiTextView.text = roundOffDecimal(item.aqi).toString()
                updateUI(item)
                break
            }
        }
    }

    private fun updateUI(item: Data) {
        val colorId =
            when {
                item.aqi.toInt() in 0..50 -> {
                    airQuality.text = this.getString(R.string.good)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.scaleGood))
                    ContextCompat.getDrawable(this, R.drawable.circle_good)
                }
                item.aqi.toInt() in 51..100 -> {
                    airQuality.text = this.getString(R.string.satisfactory)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.satisfactory))
                    ContextCompat.getDrawable(this, R.drawable.circle_satisfactory)
                }
                item.aqi.toInt() in 101..200 -> {
                    airQuality.text = this.getString(R.string.moderate)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.scaleModerate))
                    ContextCompat.getDrawable(this, R.drawable.circle_moderate)
                }

                item.aqi.toInt() in 201..300 -> {
                    airQuality.text = this.getString(R.string.poor)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.poor))
                    ContextCompat.getDrawable(this, R.drawable.circle_poor)
                }
                item.aqi.toInt() in 301..400 -> {
                    airQuality.text = this.getString(R.string.very_poor)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.very_poor))
                    ContextCompat.getDrawable(this, R.drawable.circle_very_poor)
                }
                item.aqi.toInt() in 401..500 -> {
                    airQuality.text = this.getString(R.string.sereve)
                    airQuality.setTextColor(ContextCompat.getColor(this, R.color.severe))
                    ContextCompat.getDrawable(this, R.drawable.circle_servere)
                }
                else -> {

                    ContextCompat.getDrawable(this, R.drawable.circle_good)
                }
            }
        aqi_background.setImageDrawable(colorId)
    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        return df.format(number).toDouble()
    }

    override fun onDestroy() {
        cityViewModel.webSocket?.close(1000, "Socket Closed !!")
        super.onDestroy()
    }
}