package com.customers.zktc.inject.module

import android.content.Context
import com.customers.zktc.BuildConfig
import com.customers.zktc.inject.data.Api
import com.customers.zktc.inject.data.database.DatabaseApi
import com.customers.zktc.inject.data.map.MapApi
import com.customers.zktc.inject.data.net.NetApi
import com.customers.zktc.inject.data.oss.OssApi
import com.customers.zktc.inject.data.preference.PreferenceApi
import com.customers.zktc.inject.interceptor.NetInterceptor
import com.customers.zktc.inject.qualifier.context.AppContext
import com.customers.zktc.inject.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
class DataModule {



    @Provides
    @ApplicationScope
    internal fun provideNetApi(okHttpClient: OkHttpClient,netInterceptor: NetInterceptor): NetApi {
        val client = okHttpClient.newBuilder()
            .addInterceptor(netInterceptor)
            .build()
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ApiHost)
            .addConverterFactory(MoshiConverterFactory.create())
//            .addConverterFactory(Json.asConverterFactory(contentType))
//            .addConverterFactory(serializationConverterFactory(contentType,Json))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .callFactory(client)
            .build().create(NetApi::class.java)
    }

    @Provides
    @ApplicationScope
    internal fun provideOkHttpClient(): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor())
            //                .cache(new Cache(new File(""),1024*1024*20))
            .build().newBuilder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return httpClientBuilder.build()
    }

//    @Provides
//    @ApplicationScope
//    internal fun provideOssClient(@AppContext context: Context,netApi: NetApi): OSSClient {
//        val credentialProvider1 = OssSignCredentialProvider(netApi)
//        val conf = ClientConfiguration()
//        conf.connectionTimeout = 15 * 1000
//        conf.socketTimeout = 15 * 1000
//        conf.maxConcurrentRequest = 5
//        conf.maxErrorRetry = 2
//        return OSSClient(context, BuildConfig.endpoint, credentialProvider1)
//    }


    @Provides
    @ApplicationScope
    internal fun provideDataBaseApi():DatabaseApi{
        return DatabaseApi()
    }

    @Provides
    @ApplicationScope
    internal fun provideMapApi(@AppContext context: Context):MapApi{
        return MapApi(context)
    }

    @Provides
    @ApplicationScope
    internal fun provideOssApi(@AppContext context: Context,netApi: NetApi): OssApi {
        return OssApi(context,netApi)
    }


    @Provides
    @ApplicationScope
    internal fun providePreferenceApi(@AppContext context: Context): PreferenceApi {
        return PreferenceApi(context)
    }

    @Provides
    @ApplicationScope
    internal  fun provideApi(@AppContext context: Context,netApi: NetApi,databaseApi:DatabaseApi,mapApi:MapApi,ossApi: OssApi,preferenceApi: PreferenceApi):Api{
        return Api(context,netApi,databaseApi,mapApi,ossApi,preferenceApi)
    }
}
