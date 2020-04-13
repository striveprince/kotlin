package com.lifecycle.demo.inject.module

import android.content.Context
import android.content.res.Resources
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.BuildConfig
import com.lifecycle.demo.ui.DemoApplication
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.Module
import dagger.Provides
import timber.log.Timber
import java.io.File


@Module
class AppModule(val app: DemoApplication) {
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context).setDrawableSize(20f) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context).setDrawableSize(20f) }
        ARouter.init(app)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
            Timber.plant(Timber.DebugTree())
        }
    }

    @Provides
    internal fun getContext(): Context {
        return app
    }

    @Provides
    internal fun getApplication(): DemoApplication {
        return app
    }

    @Provides
    internal fun provideResources(): Resources {
        return app.resources
    }


    @Provides
    internal fun provideFileDirs(): File {
        return app.filesDir.absoluteFile
    }
}