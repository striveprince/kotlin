package com.lifecycle.demo.ui.home.mine

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.coroutines.adapter.RecyclerSelectAdapter
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.popupRecycler
import com.lifecycle.demo.databinding.FragmentHomeMineBinding
import com.lifecycle.demo.ui.home.HomeActivity.Companion.home
import com.lifecycle.demo.ui.home.mine.HomeMineFragment.Companion.mine
import io.reactivex.Single
import timber.log.Timber

@Route(path = mine)
@LayoutView(layout = [R.layout.fragment_home_mine])
class HomeMineFragment : DataBindingFragment<HomeMineModel, FragmentHomeMineBinding>() {
    companion object {
        const val mine = "$home/mine"
    }


    val list = ArrayList<HomeMineEntity>()

    fun onButtonClick(view: View) {
        val adapter = RecyclerSelectAdapter<HomeMineEntity>()
        val inflate = PopupRecyclerInflate(requireActivity(), adapter).apply {
            httpData = { _, _ ->
                if (list.isEmpty()) {
                    list.addAll(arrayListOf(
                        HomeMineEntity(),
                        HomeMineEntity(),
                        HomeMineEntity(),
                        HomeMineEntity()))
                    Single.just(list)
                } else {
                    Single.just(list)
                }

            }
        }
        popupRecycler(inflate)
            .apply {
                setOnDismissListener {
                    Timber.i("size = ${adapter.selectList.size}")
                }
            }
            .showAsDropDown(view)
    }
}