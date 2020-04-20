package com.lifecycle.demo.ui.home.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.adapter.recycler.RecyclerSelectAdapter
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.popup
import com.lifecycle.demo.databinding.FragmentHomeMineBinding
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Route(path = mine)
@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineFragment : DataBindingFragment<HomeMineModel, FragmentHomeMineBinding>() {
    companion object { const val mine = "$home/mine" }

    val list = ArrayList<HomeMineEntity>()

    fun onButtonClick(view: View) {
        val adapter = RecyclerSelectAdapter<HomeMineEntity>()
        val inflate = PopupRecyclerInflate(requireActivity(), adapter).apply {
            httpData = { _, _ -> flow { emit(ArrayList<HomeMineEntity>()) } }
        }
        popup(inflate)
            .apply { setOnDismissListener { Timber.i("size = ${adapter.selectList.size}") } }
            .showAsDropDown(view)
    }
}