package com.customers.zktc.inject.module

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.customers.zktc.inject.qualifier.context.ActivityContext
import com.customers.zktc.inject.qualifier.manager.ActivityFragmentManager
import com.customers.zktc.inject.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityContext
    @Provides
    @ActivityScope
    internal fun provideActivity(): Context {
        return activity
    }

    @ActivityScope
    @ActivityFragmentManager
    @Provides
    internal fun provideFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    @ActivityScope
    internal fun provideDisplayMetrics(): DisplayMetrics {
        return activity.resources.displayMetrics
    }

    @Provides
    @ActivityScope
    internal fun provideLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }


}