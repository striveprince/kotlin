package com.lifecycle.demo.ui.select

import android.os.Bundle
import android.view.View
import android.widget.PopupWindow
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.ActivityHomeBinding
import com.lifecycle.demo.inject.bean.CommonDataBean
import com.lifecycle.demo.ui.select.SelectActivity.Companion.select
import com.lifecycle.demo.ui.select.fragment.HomeListFragment.Companion.homeList
import com.lifecycle.demo.ui.select.popup.PopupRecyclerInflate
import com.lifecycle.demo.ui.select.popup.SelectOption
import com.lifecycle.binding.adapter.recycler.OrderSpanSizeLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.base.util.*
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw
import com.lifecycle.rx.adapter.RecyclerSelectAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

@Route(path = select)
@LayoutView(layout = [R.layout.activity_select])
class SelectActivity : DataBindingActivity<SelectModel, ActivityHomeBinding>() {
    companion object {
        const val select = tomtaw + "select"
    }

    private val list = ArrayList<SelectOption>()
    private val dataList = ArrayList<SelectOption>()
    private val patientList = ArrayList<SelectOption>()
    private val requestList = ArrayList<SelectOption>()
    private val serviceList = ArrayList<SelectOption>()

    private var window: PopupWindow? = null


    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        showFragment(supportFragmentManager, homeList)
    }


    private fun requireData(position: Int, offset: Int, state: Int): Single<List<SelectOption>> {
        return if (list.isEmpty()) api.netApi.httpApi.commonData().restful()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { convertList(position, it) }
            .flatMap { selectOption(position) }
        else selectOption(position)
    }

    private fun selectOption(position: Int): Single<List<SelectOption>> {
        return when (position) {
            0 -> Single.just(dataList)
            1 -> Single.just(patientList)
            2 -> Single.just(requestList)
            3 -> Single.just(serviceList)
            else -> Single.just(list)
        }
    }

    private fun convertList(position: Int, it: CommonDataBean): Single<List<SelectOption>> {
        addDataList(getString(R.string.type_seek), it.data_source, dataList)
        addDataList(getString(R.string.type_inspect), it.patient_class, patientList)
        addDataList(getString(R.string.type_date), it.request_dept, requestList)
        addDataList(getString(R.string.type_system), it.service_sect, serviceList)
        return selectOption(position)
    }

    private fun addDataList(type: String, map: Map<String, String>, dataList: MutableList<SelectOption>) {
        for (s in map.entries) dataList.add(SelectOption(s.key, type))
        if (dataList.isNotEmpty()) {
            list.add(SelectOption(type))
            list.addAll(dataList)
        }
    }

    private fun showFragment(fm: FragmentManager, route: String) {
        fm.beginTransaction().run {
            val fragment = ARouterUtil.findFragmentByTag(fm, route)
            if (!fragment.isAdded) add(R.id.home_frame_layout, fragment, route)
            show(fragment)
            commitAllowingStateLoss()
        }
    }

    fun onSelectClick(v: View) {
        model.position.value?.let {
            window?.dismiss()
            window = showPopup(it) { model.position.value = -1 }
                .apply { showAsDropDown(v) }
        }
    }

    private fun showPopup(it: Int, dismiss: () -> Unit = {}): PopupWindow {
        val adapter = RecyclerSelectAdapter<SelectOption>()
        val manager = GridLayoutManager(requireActivity(), 4)
        manager.spanSizeLookup = OrderSpanSizeLookup(adapter, 4)
        val inflate = PopupRecyclerInflate(manager, adapter) {
            selectList(ExamParam(it))
        }.apply { httpData = { offset, state -> requireData(it, offset, state) } }
        return popup(inflate).apply {
            setOnDismissListener {
                dismiss()
                inflate.destroy()
            }
        }
    }

    private fun selectList(examParam: ExamParam) {
        model.params.value = examParam
        window?.dismiss()
    }
}