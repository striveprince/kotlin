package com.lifecycle.binding.adapter.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapterFactory(): CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        return LiveDataCallAdapter<LiveData<*>>(getParameterUpperBound(0, returnType as ParameterizedType))
    }
}
class LiveDataCallAdapter<R>(val type: Type):CallAdapter<R, LiveData<R>>{
    override fun adapt(call: Call<R>): LiveData<R> {
        val flag = AtomicBoolean(false)
        return liveData{  }
    }

    override fun responseType(): Type {
       return type
    }
}