package com.customers.zktc.inject.module

import android.content.res.Resources
import com.customers.zktc.ui.ZktcApplication
import dagger.Module

@Module
class AppModule(val app:ZktcApplication) {
    internal fun getApplication():ZktcApplication{
        return app
    }

    internal fun provideResources():Resources{
        return app.resources
    }


}