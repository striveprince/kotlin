package com.customers.zktc.ui.demo

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.customers.zktc.R
import com.google.android.material.tabs.TabLayout
import com.lifecycle.binding.inter.anko.AnkoParse
import com.lifecycle.binding.util.tabLayout
import org.jetbrains.anko.*

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 9:51
 * Email: 1033144294@qq.com
 */
class DemoActivity : AppCompatActivity(), AnkoParse<DemoModel, AnkoContext<Context>>
, TabLayout.OnTabSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val demoModel = getModel()
        setContentView(createView(demoModel, this))
    }

    private fun getModel() = ViewModelProviders.of(this)[DemoModel::class.java]
    fun context(): AppCompatActivity = this

    override fun parse(t: DemoModel, context: Context): AnkoContext<Context> {
        return AnkoContext.create(this).apply {
            verticalLayout {
                frameLayout { id = R.id.home_frame_layout }
                    .lparams(matchParent, wrapContent, 1f)
                tabLayout {
                    orientation = LinearLayout.HORIZONTAL
                    for (index in 0..5){
                        addTab(HomeEntity(index).tab)
                    }
                    addOnTabSelectedListener(this@DemoActivity)
                }
                    .lparams(matchParent, dip(50))
            }
        }
    }

    private fun checkFragment(position: Int) {

    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {

    }
}


//        if (position < 0 || position >= fragments.size || position == currentPosition) return
//        val ft = fragmentManager.beginTransaction()
//        if (currentPosition >= 0) {
//            val beforeFragment = fragments[currentPosition].getItem(position, binding.frameLayout)
//            ft.hide(beforeFragment)
//        }
//        val fragment = fragments[position].getItem(position, binding.frameLayout)
//        if (!fragment.isAdded) ft.add(R.id.frameLayout, fragment)
//        ft.show(fragment)
//        ft.commitAllowingStateLoss()
//        currentPosition = position