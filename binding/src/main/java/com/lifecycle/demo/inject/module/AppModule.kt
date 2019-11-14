package com.lifecycle.demo.inject.module

import android.content.Context
import android.content.res.Resources
import com.lifecycle.demo.inject.qualifier.context.AppContext
import com.lifecycle.demo.inject.scope.ApplicationScope
import com.lifecycle.demo.ui.DemoApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app: DemoApplication) {

    @AppContext
    @Provides
    @ApplicationScope
    internal fun getContext():Context{
        return app
    }

    @AppContext
    @Provides
    @ApplicationScope
    internal fun getApplication(): DemoApplication {
        return app
    }

    @AppContext
    @Provides
    @ApplicationScope
    internal fun provideResources():Resources{
        return app.resources
    }


}