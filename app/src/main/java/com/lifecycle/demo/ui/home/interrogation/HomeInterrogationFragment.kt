package com.lifecycle.demo.ui.home.interrogation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.rx.adapter.RxFragmentPager2Adapter
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.binding.util.observer
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.viewModel
import com.lifecycle.demo.databinding.FragmentHomeIntrrogationBinding
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.HomeModel
import com.lifecycle.demo.ui.home.interrogation.HomeInterrogationFragment.Companion.interrogation

@Route(path = interrogation)
@LayoutView(layout = [R.layout.fragment_home_intrrogation])
class HomeInterrogationFragment : DataBindingFragment<HomeInterrogationModel, FragmentHomeIntrrogationBinding>() {
    companion object {
        const val interrogation = "$home/interrogation"
    }

    val pager2Adapter by lazy {
        RxFragmentPager2Adapter<HomeInterrogationEntity>(childFragmentManager, lifecycle)
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
        }
    }
}