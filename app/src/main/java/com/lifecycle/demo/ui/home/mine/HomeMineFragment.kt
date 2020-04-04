package com.lifecycle.demo.ui.home.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.popupRecycler
import com.lifecycle.demo.databinding.FragmentHomeMineBinding
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine
import io.reactivex.Single

@Route(path = mine)
@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineFragment : DataBindingFragment<HomeMineModel, FragmentHomeMineBinding>() {
    companion object {
        const val mine = "$home/mine"
    }

    fun onButtonClick(view: View) {
        val inflate = PopupRecyclerInflate<HomeMineEntity>(requireActivity()).apply {
            httpData = {_,_-> Single.just(arrayListOf(
                HomeMineEntity(),
                HomeMineEntity(),
                HomeMineEntity(),
                HomeMineEntity()
            )) }
        }
        popupRecycler(inflate).showAsDropDown(view)
    }
}