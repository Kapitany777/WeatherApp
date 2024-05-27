package eu.braincluster.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import eu.braincluster.weatherapp.data.WeatherResult
import eu.braincluster.weatherapp.databinding.ActivityMainBinding
import eu.braincluster.weatherapp.network.WeatherApi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
        val timeoutMsec = 1000L;

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(timeoutMsec, TimeUnit.MILLISECONDS)
            .readTimeout(timeoutMsec, TimeUnit.MILLISECONDS)
            .writeTimeout(timeoutMsec, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
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
                    val weatherResult = response.body()

                    val description = weatherResult?.weather?.get(0)?.description
                    val currentTemp = weatherResult?.main?.temp
                    val minTemp = weatherResult?.main?.temp_min
                    val maxTemp = weatherResult?.main?.temp_max

                    if (description != null)
                    {
                        binding.textViewResult.text = """
                        Description: $description
                        Current temp: $currentTemp
                        Min temp: $minTemp
                        Max temp: $maxTemp
                        """.trimIndent()
                    }
                    else
                    {
                        binding.textViewResult.text = "Data not found error!"
                    }
                }

                override fun onFailure(call: Call<WeatherResult>, ex: Throwable)
                {
                    binding.textViewResult.text = ex.message.toString()
                }
            })
    }
}
