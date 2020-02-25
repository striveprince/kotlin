package com.lifecycle.demo.inject.module

import android.content.Context
import androidx.room.Room
import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.data.database.DatabaseApi
import com.lifecycle.demo.inject.data.database.DatabaseApi.Companion.DATABASE_NAME
import com.lifecycle.demo.inject.data.map.MapApi
import com.lifecycle.demo.inject.data.net.HttpApi
import com.lifecycle.demo.inject.data.net.NetApi
import com.lifecycle.demo.inject.data.net.converter.JsonConverterFactory
import com.lifecycle.demo.inject.data.oss.OssApi
import com.lifecycle.demo.inject.data.preference.PreferenceApi
import com.lifecycle.demo.inject.interceptor.NetInterceptor
import com.lifecycle.demo.inject.qualifier.AppContext
import com.lifecycle.demo.inject.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Module()
class DataModule {
    init {
        Timber.i("DataModule init")
    }
    @Provides
    @ApplicationScope
    internal fun provideNetApi(okHttpClient: OkHttpClient): HttpApi {
        return Retrofit.Builder()
            .baseUrl("https://api.host.cn")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(JsonConverterFactory())
            .callFactory(okHttpClient)
            .build().create(HttpApi::class.java)
    }

    @Provides
    @ApplicationScope
    internal fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(NetInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
        } else OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetInterceptor())
            .build()
    }

    @Provides
    @ApplicationScope
    internal fun provideDataBaseApi(@AppContext context: Context): DatabaseApi {
        return Room
            .databaseBuilder(context, DatabaseApi::class.java, DATABASE_NAME)
//            .createFromFile()
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @Provides
    @ApplicationScope
    internal fun provideMapApi(@AppContext context: Context): MapApi {
        return MapApi(context)
    }

    @Provides
    @ApplicationScope
    internal fun provideOssApi(@AppContext context: Context, httpApi: HttpApi): OssApi {
        return OssApi(context, httpApi)
    }

    @Provides
    @ApplicationScope
    internal fun providePreferenceApi(@AppContext context: Context): PreferenceApi {
        return PreferenceApi(context)
    }

    @Provides
    @ApplicationScope
    internal fun provideApi(@AppContext context: Context, httpApi: HttpApi, databaseApi: DatabaseApi, mapApi: MapApi, ossApi: OssApi, preferenceApi: PreferenceApi): Api {
        return Api(context, NetApi(httpApi), databaseApi, mapApi, ossApi, preferenceApi)
    }

}
