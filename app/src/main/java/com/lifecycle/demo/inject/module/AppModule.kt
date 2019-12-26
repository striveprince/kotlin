package com.lifecycle.demo.inject.module

import android.content.Context
import android.content.res.Resources
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.BR
import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.inject.qualifier.AppContext
import com.lifecycle.demo.inject.scope.ApplicationScope
import com.lifecycle.demo.ui.DemoApplication
import com.lifecycle.binding.Constant
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.Module
import dagger.Provides
import timber.log.Timber
import java.io.File


@Module(subcomponents = [ActivityComponent::class, FragmentComponent::class])
class AppModule(val app: DemoApplication) {
    init {
        ARouter.init(app)
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context).setDrawableSize(20f) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context).setDrawableSize(20f) }
//        app.initYealinkSdk()

        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
            Timber.plant(Timber.DebugTree())
        }
    }

    @AppContext
    @Provides
    @ApplicationScope
    internal fun getContext(): Context {
        return app
    }

    @Provides
    @ApplicationScope
    internal fun getApplication(): DemoApplication {
        return app
    }

    @Provides
    @ApplicationScope
    internal fun provideResources(): Resources {
        return app.resources
    }


    @Provides
    @ApplicationScope
    internal fun provideFileDirs(): File {
        return app.filesDir.absoluteFile
    }
}