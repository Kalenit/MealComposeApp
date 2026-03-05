package com.example.themealapp.injection

import android.content.Context
import com.example.themealapp.api.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDir = context.cacheDir
        return Cache(cacheDir, 10L * 1024 * 1024) // 10 MB cache [web:1][web:5]
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(): Interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.HOURS) // Adjust as needed
                .build()
            return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .removeHeader("Pragma")
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, cacheInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(cacheInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * caching
     * authentication header
     * logging
     * certificate pinning
     */

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMealApi(retrofit: Retrofit): MealApi {
        return retrofit.create(MealApi::class.java)
    }
}