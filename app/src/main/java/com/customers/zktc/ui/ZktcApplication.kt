package com.customers.zktc.ui

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.binding.model.App
import com.customers.zktc.BR
import com.customers.zktc.BuildConfig
import com.customers.zktc.R
import com.customers.zktc.inject.component.AppComponent
import com.customers.zktc.inject.component.DaggerAppComponent
import com.customers.zktc.inject.module.AppModule
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import timber.log.Timber


class ZktcApplication : MultiDexApplication() {

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    companion object {
        private var application: ZktcApplication? = null
        var component: AppComponent? = null
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        component?.inject(this)
        ARouter.init(this)
        App(this)
        if(BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
            Timber.plant(Timber.DebugTree())
        }

    }
}