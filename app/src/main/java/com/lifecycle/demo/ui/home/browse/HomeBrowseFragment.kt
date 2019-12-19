package com.lifecycle.demo.ui.home.browse

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.base.life.anko.AnkoFragment
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.ui.home.browse.HomeBrowseFragment.Companion.browse
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout

@Route(path = browse)
class HomeBrowseFragment :AnkoFragment<HomeBrowseModel>(){
    companion object{ const val  browse = FragmentComponent.Config.fragment+"browse" }

    override fun parse(t: HomeBrowseModel, context: Context): AnkoContext<Context> {
        return AnkoContext.Companion.create(context).apply { frameLayout() }
    }
}