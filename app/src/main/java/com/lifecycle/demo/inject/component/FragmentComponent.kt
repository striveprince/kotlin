package com.lifecycle.demo.inject.component

import com.lifecycle.demo.inject.component.ActivityComponent.Config.tomtaw
import com.lifecycle.demo.inject.module.FragmentModule
import com.lifecycle.demo.inject.scope.FragmentScope
import com.lifecycle.demo.ui.home.browse.HomeBrowseFragment
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment
import com.lifecycle.demo.ui.home.interrogation.list.InterrogationListFragment
import com.lifecycle.demo.ui.home.message.HomeMessageFragment
import com.lifecycle.demo.ui.home.mine.HomeMineFragment
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
@FragmentScope
interface FragmentComponent {
    object Config {
        const val fragment = tomtaw + "fragment/"
    }
    @Subcomponent.Builder
    interface Builder {
        fun applyFragmentModule(fragmentModule: FragmentModule): Builder
        fun build(): FragmentComponent
    }

}