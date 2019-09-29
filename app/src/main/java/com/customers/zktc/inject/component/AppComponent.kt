package com.customers.zktc.inject.component

import com.customers.zktc.inject.module.AppModule
import com.customers.zktc.inject.module.DataModule
import com.customers.zktc.inject.scope.ApplicationScope
import com.customers.zktc.ui.ZktcApplication
import dagger.Component

@Component(modules = [AppModule::class,DataModule::class])
//@Component(modules = [AppModule::class, NetWorkModule::class, DataModule::class, LocationModule::class, LocalDataModule::class])
@ApplicationScope
interface AppComponent {
    fun inject(application: ZktcApplication)
}