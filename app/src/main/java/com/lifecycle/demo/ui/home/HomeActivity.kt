package com.lifecycle.demo.ui.home

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.anko.AnkoActivity
import com.lifecycle.demo.inject.component.ActivityComponent
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.lifecycle.binding.base.view.SwipeBackLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 9:51
 * Email: 1033144294@qq.com
 */
@Route(path = HomeActivity.home)
class HomeActivity : AnkoActivity<HomeModel>() {
    companion object {
        const val home = ActivityComponent.Config.tomtaw + "home"
        const val fragmentIndex = "route"
        const val savedInstance = "savedInstance"
    }

    override fun isSwipe() = SwipeBackLayout.FROM_NO

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(fragmentIndex, model.beforeIndex)
        outState.putBundle(savedInstance, model.fragmentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        model.restoreFragmentState(savedInstanceState)
    }

    override fun parse(t: HomeModel, context: Context): AnkoContext<Context> {
        return AnkoContext.create(this).apply {
            this.verticalLayout {
                frameLayout {
                    id = R.id.home_frame_layout
                    backgroundColorResource = R.color.colorAccent
                }.lparams(matchParent, wrapContent, weight = 1f)
                bottomNavigationView {
                    backgroundColorResource = R.color.colorAccent
                    labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
                    itemTextColor = ContextCompat.getColorStateList(context, R.color.tab_color)
                    itemIconTintList = ContextCompat.getColorStateList(context, R.color.tab_color)
                    itemBackgroundResource = android.R.color.white
                    inflateMenu(R.menu.home_tab)
                    setOnNavigationItemSelectedListener(model)
                    lparams(matchParent, dip(48))
                }
            }
        }
    }
}