package eu.braincluster.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import eu.braincluster.weatherapp.data.WeatherResult
import eu.braincluster.weatherapp.databinding.ActivityMainBinding
import eu.braincluster.weatherapp.network.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    private lateinit var weatherApi: WeatherApi

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRetrofit()

        binding.buttonGetWeather.setOnClickListener {
            getWeatherData()
        }
    }

    private fun initializeRetrofit()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    private fun getWeatherData()
    {
        weatherApi.getWeatherData(
            binding.editTextCityName.text.toString(),
            "metric",
            "c3e97034b71a111ecef34734046ff5dd"
        )
            .enqueue(object : Callback<WeatherResult>
            {
                override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>)
                {
                    Log.i("WEATHERDATA", response.body()?.weather.toString())
                }

                override fun onFailure(call: Call<WeatherResult>, ex: Throwable)
                {
                    Log.e("WEATHERDATA", ex.message.toString())
                }
            })
    }
}

