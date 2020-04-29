package com.lifecycle.demo.ui.select.consult

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.PopupWindow
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.ui.select.consult.HomeConsultFragment.Companion.homeConsult
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.databinding.ViewGroupBindingAdapter
import com.lifecycle.binding.adapter.recycler.RecyclerAdapter
import com.lifecycle.binding.adapter.recycler.RecyclerMultiplexSelectAdapter
import com.lifecycle.binding.adapter.recycler.ReverseSpanSizeLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingFragment
import com.lifecycle.binding.util.*
import com.lifecycle.demo.R
import com.lifecycle.demo.base.recycler.RecyclerParse
import com.lifecycle.demo.base.util.popup
import com.lifecycle.demo.databinding.FragmentHomeConsultBinding
import com.lifecycle.demo.databinding.LayoutSwipeRecyclerViewBinding
import com.lifecycle.demo.ui.select.ExamParam
import com.lifecycle.demo.ui.select.SelectActivity.Companion.select
import com.lifecycle.demo.ui.select.popup.PopupRecyclerInflate
import com.lifecycle.demo.ui.select.popup.PopupSelect

@Route(path = homeConsult)
@LayoutView(layout = [R.layout.fragment_home_consult])
class HomeConsultFragment : DataBindingFragment<HomeConsultModel, FragmentHomeConsultBinding>(), IEvent<PopupSelect> {
    companion object {
        const val homeConsult = "$select/consult"
    }

    var recyclerBinding: LayoutSwipeRecyclerViewBinding? = null
    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        model.apply {
            grid.observer(owner) {
                adapter.array.put(R.layout.holder_home_list, if (it) 1 else 0)
                recyclerBinding?.run {
                    recyclerView.layoutManager = if (it) GridLayoutManager(context, 2) else LinearLayoutManager(context)
                    recyclerView.adapter = adapter as RecyclerAdapter
                    recyclerView.itemAnimator = null
                }
            }
        }
    }

    override fun parse(t: HomeConsultModel, context: Context, parent: ViewGroup?, attachToParent: Boolean): FragmentHomeConsultBinding {
        return super.parse(t, context, parent, attachToParent).apply { initRecyclerBinding(this,t) }
    }

    private fun initRecyclerBinding(it: FragmentHomeConsultBinding, t: HomeConsultModel) {
        ViewGroupBindingAdapter.apply {
            recyclerBinding = it.frameLayout.parse(RecyclerParse<ExamResultEntity>(), t)
                .apply { recyclerView.apply { layoutManager = LinearLayoutManager(context) }
                    lifecycleOwner = this@HomeConsultFragment }
        }
    }

    private var popupWindow: PopupWindow? = null

    fun onSelectClick(v: View) {
        popupWindow?.dismiss()
        popupWindow = showPopup(model.position.value ?: 0).apply {
            setOnDismissListener { popupDismiss() }
            if (model.position.value == 3) {
                width = screenWidth * 4 / 5
                height = screenHeight - dip(24)
                showAtLocation(binding.root.rootView, Gravity.TOP, screenWidth / 5, 0)
            } else showAsDropDown(v)
            showShadow(true)
        }
    }

    private fun popupDismiss() {
        showShadow(false)
        model.position.value = -1
    }

    private fun showShadow(boolean: Boolean = true) {
        if (model.position.value ?: 0 != 3) return
        val l = requireActivity().window.attributes.apply { alpha = if (boolean) 0.7f else 1f }
        l.alpha = if (boolean) 0.7f else 1f
        requireActivity().window.apply {
            if (boolean) addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            else clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }.attributes = l
    }

    private fun selectList(examParam: ExamParam) {
        popupWindow?.dismiss()
        model.params.value = examParam
    }


    lateinit var selectAdapter: RecyclerMultiplexSelectAdapter<PopupSelect>

    override fun setEvent(type: Int, e: PopupSelect, position: Int, view: View?): Any {
        if (view?.id == R.id.select_option)
            return false
        else if (view?.id == R.id.select_title) {
            val p = selectAdapter.adapterList.indexOf(e) + 1
            val l = model.arrayList(e)
            if ((view as CheckBox).isChecked) {
                model.list.removeAll(l)
                selectAdapter.removeList(p, l.size)
            } else {
                model.list.addAll(p, l)
                selectAdapter.addList(l, p)
            }
        }
        return true
    }

    private fun showPopup(position: Int): PopupWindow {
        val count = when {
            position < 2 -> 2
            position == 2 -> 1
            else -> position
        }
        selectAdapter = RecyclerMultiplexSelectAdapter()
        selectAdapter.array.put(R.id.select_option, position)
        selectAdapter.addEventAdapter(this)
        val manager = GridLayoutManager(requireActivity(), count)
        manager.spanSizeLookup = ReverseSpanSizeLookup(selectAdapter, count)
        val inflate = PopupRecyclerInflate(manager, selectAdapter) { selectList(model.params(list = it)) }
            .apply { httpData = { _, _ -> model.requireData(position) } }
        return popup(inflate)
    }

}