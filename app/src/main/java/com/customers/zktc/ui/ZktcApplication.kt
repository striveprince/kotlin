package com.customers.zktc.ui

import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.binding.model.App
import com.binding.model.busPost
import com.binding.model.subscribeNormal
import com.customers.zktc.BR
import com.customers.zktc.BuildConfig
import com.customers.zktc.R
import com.customers.zktc.inject.component.AppComponent
import com.customers.zktc.inject.component.DaggerAppComponent
import com.customers.zktc.inject.module.AppModule
import com.customers.zktc.inject.module.DataModule
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class ZktcApplication : MultiDexApplication() {
    companion object {var component: AppComponent? = null}

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(android.R.color.white, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            ARouter.openDebug()
            ARouter.openLog()
            Timber.plant(Timber.DebugTree())
        }
        ARouter.init(this)
        App(this)
        Single.just(this)
            .subscribeOn(Schedulers.newThread())
            .subscribeNormal ({ application ->
                component = DaggerAppComponent.builder()
                    .dataModule(DataModule())
                    .appModule(AppModule(application)).build()
                component!!.inject(application)
                busPost(component!!)
            })
    }
}