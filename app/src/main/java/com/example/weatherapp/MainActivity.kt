package com.example.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

////896e365bf763f1daf9bcd9c9da77e743
////https://api.openweathermap.org/data/2.5/weather?q=raipur&appid=896e365bf763f1daf9bcd9c9da77e743
class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy{
     ActivityMainBinding.inflate(layoutInflater)

}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        fetchWeatherInfo("raipur")
        searchWeather()


    }
    private fun searchWeather(){
        val searchCity = binding.search
        searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherInfo(query)
                }
                return true

                }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherInfo( city: String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(ApiInterface::class.java)

        val response =
            retrofit.getWeatherInfo(city, "896e365bf763f1daf9bcd9c9da77e743", "metric")

        response.enqueue(object : Callback<MyWeather> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<MyWeather>, response: Response<MyWeather>) {
                toast()
                val responseBody = response.body()
                if (responseBody != null) {


                    binding.location.text = city
                    binding.temparature.text = responseBody.main.temp.toString() + " °C"
                    binding.minimumTemp.text = responseBody.main.temp_min.toString() + " °C"
                    binding.maximumTemp.text = responseBody.main.temp_max.toString() + " °C"
                    binding.condition.text = responseBody.main.feels_like.toString()
                    val date = date()
                    val day =day(System.currentTimeMillis())
                    binding.humidity.text = responseBody.main.humidity.toString()
                    binding.windSpeed.text = responseBody.wind.speed.toString()
                    binding.WeatherCondition.text = responseBody.weather[0].main.toString()
                    binding.sunRise.text = time(responseBody.sys.sunrise.toLong())
                    binding.sunSet.text =time( responseBody.sys.sunset.toLong())
                    binding.sea.text = responseBody.main.pressure.toString()

                    setImgBg(binding.WeatherCondition.text.toString())



                }


            }

            override fun onFailure(call: Call<MyWeather>, t: Throwable) {
                Log.d("hero", "failed")
            }

        })


    }

    fun toast() {
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show()

    }

   fun day(timestamp : Long):String{
       val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
       return sdf.format((Date()))
   }
    fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH:mm",Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
    fun date() :String{
        val sdt = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdt.format((Date()))
    }

    fun setImgBg(condition : String) {
        when(condition.lowercase()){
            "clear","sunny","clear sky" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.animation.setAnimation(R.raw.animatedsun)
            }
            "partly clouds","clouds","overcast","mist","foggy"->{
                binding.root.setBackgroundResource(R.drawable.cloud_background)
                binding.animation.setAnimation(R.raw.cloud)
            }
            "light rain","drizzle","moderate rain","showers","heavy rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.animation.setAnimation(R.raw.rain)
            }
            "light snow","moderate snow","heavy snow","blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.animation.setAnimation(R.raw.snow)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.animation.setAnimation(R.raw.animatedsun)
            }
        }
        binding.animation.playAnimation()
    }
}