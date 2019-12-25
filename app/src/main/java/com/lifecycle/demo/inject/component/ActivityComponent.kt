package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.module.ActivityModule
import com.lifecycle.demo.inject.scope.ActivityScope
import com.lifecycle.demo.ui.home.HomeActivity
import com.lifecycle.demo.ui.interrogation.detail.InterrogationDetailActivity
import com.lifecycle.demo.ui.start.StartActivity
import com.lifecycle.demo.ui.user.sign.login.SignInActivity
import com.lifecycle.demo.ui.user.sign.up.SignUpActivity
import com.lifecycle.demo.ui.video.VideoActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent{
    object Config{ const val tomtaw = "/tomtaw/" }
//    fun inject(activity_video: AnkoActivity<out ViewModel>)
//    fun inject(activity: StartActivity)
//    fun inject(activity: SignInActivity)
//    fun inject(activity: SignUpActivity)
//    fun inject(activity: HomeActivity)
//    fun inject(activity: InterrogationDetailActivity)
//    fun inject(activity: VideoActivity)

    @Subcomponent.Builder
    interface Builder{
        fun applyActivity(activityModule: ActivityModule):Builder
        fun build():ActivityComponent
    }



}