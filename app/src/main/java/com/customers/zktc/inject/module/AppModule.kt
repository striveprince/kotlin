package com.customers.zktc.inject.module

import android.content.Context
import android.content.res.Resources
import com.customers.zktc.inject.qualifier.context.AppContext
import com.customers.zktc.inject.scope.ApplicationScope
import com.customers.zktc.ui.ZktcApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app:ZktcApplication) {

    @AppContext
    @Provides
    @ApplicationScope
    internal fun getContext():Context{
        return app
    }

    @AppContext
    @Provides
    @ApplicationScope
    internal fun getApplication():ZktcApplication{
        return app
    }

    @AppContext
    @Provides
    @ApplicationScope
    internal fun provideResources():Resources{
        return app.resources
    }


}