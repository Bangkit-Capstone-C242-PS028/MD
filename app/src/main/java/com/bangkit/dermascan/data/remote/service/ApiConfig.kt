package com.bangkit.dermascan.data.remote.service

import UserPreference
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.bangkit.dermascan.BuildConfig
//import com.bangkit.dermascan.dataArticles.local.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig(
    private val userPreference: UserPreference,
    private val context: Context
) {
    private fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                val token = runBlocking { userPreference.getToken().first() }
                val req = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                Log.d("Token", "Token saat ini: Bearer $token")
                chain.proceed(req)
            } catch (e: Exception) {
                Log.e("Interceptor", "Error getting token", e)
                chain.proceed(chain.request())
            }
        }
    }

    private fun getCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Cache-Control", "public, max-age=60")
                .build()
        }
    }

    private fun getOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!isNetworkAvailable(context)) {
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=${60 * 60 * 24 * 7}")
                    .build()
            }
            chain.proceed(request)
        }
    }

    @SuppressLint("ServiceCast")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private val cache by lazy {
        Cache(context.cacheDir, CACHE_SIZE)
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getAuthInterceptor())
            .addInterceptor(getCacheInterceptor())
            .addInterceptor(getOfflineCacheInterceptor())
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
        private const val IS_DEBUG = true
        private const val TIMEOUT_SECONDS = 30L
        private const val CACHE_SIZE = 10 * 1024 * 1024L

        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (IS_DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}