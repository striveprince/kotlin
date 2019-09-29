package com.customers.zktc.inject.module

import android.content.Context
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.customers.zktc.inject.qualifier.context.FragmentContext
import com.customers.zktc.inject.qualifier.manager.ChildFragmentManager
import com.customers.zktc.inject.qualifier.manager.SupportFragmentManager
import com.customers.zktc.inject.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class FragmentModule (val fragment:Fragment){

    @FragmentScope
    @FragmentContext
    @Provides
    fun provideFragmentContext():Context{
        return fragment.activity!!
    }

    @SupportFragmentManager
    @Provides
    @FragmentScope
    fun provideFragmentManager():FragmentManager{
        return fragment.fragmentManager!!
    }

    @ChildFragmentManager
    @Provides
    @FragmentScope
    fun provideChildFragmentManager():FragmentManager{
        return fragment.childFragmentManager
    }

    @FragmentScope
    @Provides
    internal fun provideDisplayMetrics(@FragmentContext context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

}