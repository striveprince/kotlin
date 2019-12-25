package com.lifecycle.demo.ui.home.interrogation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.demo.base.util.OnPageChange
import com.lifecycle.demo.base.util.viewModel
import com.lifecycle.demo.databinding.FragmentHomeIntrrogationBinding
import com.lifecycle.demo.inject.component.FragmentComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation
import com.lifecycle.binding.adapter.databinding.TabLayoutBindingAdapter
import com.lifecycle.binding.adapter.pager.FragmentOpenPager2Adapter
import com.lifecycle.binding.adapter.pager.impl.FragmentPager2Adapter
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.util.observer

@Route(path = interrogation)
@LayoutView(layout = [R.layout.fragment_home_intrrogation])
class HomeInterrogationFragment : DataBindingFragment<HomeInterrogationModel, FragmentHomeIntrrogationBinding>() {
    companion object {
        const val interrogation = FragmentComponent.Config.fragment + "interrogation"
    }

    val pager2Adapter by lazy {
        FragmentPager2Adapter<HomeInterrogationEntity>(childFragmentManager, lifecycle)
            .apply { addList(model.items) }
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        binding.apply {
            viewPager.offscreenPageLimit = 3
            (activity as AppCompatActivity).viewModel<HomeModel>().apply {
                newCount.observer(owner()) { tabLayout.getTabAt(1)?.text = R.string.new_interrogation.stringRes(it) }
                waitCount.observer(owner()) { tabLayout.getTabAt(2)?.text = R.string.wait_interrogation.stringRes(it) }
                allCount.observer(owner()) { tabLayout.getTabAt(0)?.text = R.string.all_interrogation.stringRes(it) }
            }
//            model.position.observer(owner()) {
//                TabLayoutBindingAdapter.setPosition(tabLayout, it)
//                viewPager.currentItem = it
//            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

/* anko view
    override fun parse(t: HomeInterrogationModel, context: Context) =
        AnkoContext.create(context).apply {
            verticalLayout {
                backgroundColorResource = R.color.back_ground
                topPadding = dip(24)
                frameLayout {
                    backgroundColorResource = R.color.windowBackground
                    padding = dip(10)
                    textView(R.string.task_interrogation) {
                        textSize = 20f
                        paint.isFakeBoldText = true
                    }.lparams(wrapContent, matchParent, gravity = Gravity.CENTER)
                    textView(R.string.wait_for_event){
                        gravity = Gravity.CENTER
                    }.lparams(wrapContent, matchParent, gravity = Gravity.END)
                }
                val tabLayout = themedTabLayout(R.style.TabLayoutStyle) {
                    for (index in 0..2) {
                        val e = t.items[index]
                        newTab().apply {
                            text = App.string(e.strRes, t.newCount.value!! + t.waitCount.value!!)
                            e.tab = this
                            addTab(e.tab)
                        }
                    }
                    t.apply {
                        newCount.observer(getLifecycleOwner()) {
                            items[1].apply { tab.apply { text = App.string(strRes, it) } }
                            items[0].apply { tab.apply { text = App.string(strRes, waitCount.value!! + it) } }
                        }
                        waitCount.observer(getLifecycleOwner()) {
                            items[2].apply { tab.apply { text = App.string(strRes, it) } }
                            items[0].apply { tab.apply { text = App.string(strRes, t.newCount.value!! + it) } }
                        }
                    }
                    tabTextColors = colorState(R.color.tab_color)
                    addOnTabSelectedListener(t)
                    lparams(matchParent, dip(48))
                }
                viewPager2 {
                    id = R.id.view_page
                    registerOnPageChangeCallback(OnPageChange(t))
                    lparams(matchParent, matchParent)
                    pager2Adapter.addList(es = t.items)
                    t.position.observer(getLifecycleOwner()) {
                        TabLayoutBindingAdapter.setScrollPosition(tabLayout, it)
                        currentItem = it
                    }
                    adapter = pager2Adapter
                }
            }
        }*/
