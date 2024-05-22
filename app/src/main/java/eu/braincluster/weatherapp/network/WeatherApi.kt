package eu.braincluster.weatherapp.network

import eu.braincluster.weatherapp.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// HOST: http://api.openweathermap.org
// PATH: /data/2.5/weather
// Query / URL params: ?q=Szentes&units=metric&appid=c3e97034b71a111ecef34734046ff5dd

interface WeatherApi
{
    @GET("data/2.5/weather")
    fun getWeatherData(@Query("q") city: String,
                       @Query("units") units: String,
                       @Query("appid") appid: String): Call<WeatherResult>
}
