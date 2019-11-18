package com.customers.zktc.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.customers.zktc.R
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import com.lifecycle.binding.inter.anko.AnkoParse
import com.lifecycle.binding.util.bottomNavigationView
import com.lifecycle.binding.util.observer
import com.lifecycle.binding.util.tabLayout
import org.jetbrains.anko.*
/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 9:51
 * Email: 1033144294@qq.com
 */
class HomeActivity : AppCompatActivity(), AnkoParse<HomeModel, AnkoContext<Context>> {
    private var beforeIndex = -1
    private val demoModel: HomeModel by lazy{ ViewModelProviders.of(this)[HomeModel::class.java] }
    private val homeEntities = ArrayList<HomeEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(this)[HomeModel::class.java]
        for (index in 0..5) homeEntities.add(HomeEntity(index))
        setContentView(createView(demoModel, this))
        demoModel.currentIndex.observer(this){ checkFragment(it) }
    }

    fun context(): AppCompatActivity = this

    override fun parse(t: HomeModel, context: Context): AnkoContext<Context> {
        return AnkoContext.create(this).apply {
            verticalLayout {
                frameLayout { id = R.id.home_frame_layout }
                    .lparams(matchParent, wrapContent, 1f)
                tabLayout {
                    orientation = LinearLayout.HORIZONTAL
                    tabMode = MODE_FIXED
                    for (homeEntity in homeEntities) addTab(homeEntity.tab())
                    addOnTabSelectedListener(demoModel)
                }.lparams(matchParent, dip(50))
                bottomNavigationView{
//                    itemTextColor =
//                    ColorStateList
                    itemIconTintList = resources.getColorStateList(R.color.tab_color)
                    inflateMenu(R.menu.home_tab)

                }.lparams(matchParent,dip(50))
            }
        }
    }

    private fun checkFragment(position: Int) {
        if(position<0||position>=homeEntities.size||position == beforeIndex)return
        val ft = supportFragmentManager.beginTransaction()
        if(beforeIndex>=0) ft.hide(homeEntities[beforeIndex].fragment)
        val fragment = homeEntities[position].fragment
        if(!fragment.isAdded)ft.add(R.id.home_frame_layout,fragment)
        ft.show(fragment)
        ft.commitAllowingStateLoss()
        beforeIndex = position
    }

}

