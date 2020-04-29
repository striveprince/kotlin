package com.lifecycle.demo.ui.select

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.PopupWindow
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.lifecycle.binding.IEvent
import com.lifecycle.binding.adapter.databinding.ViewGroupBindingAdapter.inflate
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.ActivityHomeBinding
import com.lifecycle.demo.inject.bean.CommonDataBean
import com.lifecycle.demo.ui.select.SelectActivity.Companion.select
import com.lifecycle.demo.ui.select.fragment.HomeListFragment.Companion.homeList
import com.lifecycle.demo.ui.select.popup.PopupRecyclerInflate
import com.lifecycle.demo.ui.select.popup.PopupSelect
import com.lifecycle.binding.adapter.recycler.RecyclerMultiplexSelectAdapter
import com.lifecycle.binding.adapter.recycler.ReverseSpanSizeLookup
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.demo.base.util.*
import com.lifecycle.demo.databinding.ActivitySelectBinding
import com.lifecycle.demo.ui.DemoApplication.Companion.tomtaw
import com.lifecycle.demo.ui.select.popup.SelectOption
import com.lifecycle.demo.ui.select.popup.SelectTitle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

@Route(path = select)
@LayoutView(layout = [R.layout.activity_select])
class SelectActivity : DataBindingActivity<SelectModel, ActivitySelectBinding>() , IEvent<PopupSelect> {
    companion object {
        const val select = tomtaw + "select"
    }

    private val list = ArrayList<PopupSelect>()
    private val dataList = ArrayList<PopupSelect>()
    private val patientList = ArrayList<PopupSelect>()
    private val serviceList = ArrayList<PopupSelect>()
    private val dateList = ArrayList<PopupSelect>()
    private val requestList = ArrayList<PopupSelect>()

    private var window: PopupWindow? = null


    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        adapter = RecyclerMultiplexSelectAdapter()
        val manager = GridLayoutManager(requireActivity(), 4)
        manager.spanSizeLookup = ReverseSpanSizeLookup(adapter, 4)
        val inflate = PopupRecyclerInflate(manager,adapter).apply { httpData = { _, _ -> requireData(3) } }
        binding.inflate = inflate
//        binding.homeFrameLayout.inflate(inflate)
//        showFragment(supportFragmentManager, homeList)
    }

    private fun requireData(position: Int): Single<List<PopupSelect>> {
        return if (list.isEmpty()) api.netApi.httpApi.commonData().restful()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { convertList(position, it) }
            .flatMap { selectOption(position) }
        else selectOption(position)
    }

    private fun addDateList(string: String) {
        val dates = mutableListOf(
            SelectOption(getString(R.string.date_not_limit)),
            SelectOption(getString(R.string.day_one)).apply { checked.set(true) },
            SelectOption(getString(R.string.week_one)),
            SelectOption(getString(R.string.month_one)),
            SelectOption(getString(R.string.year_one))
        )
        for (date in dates) date.date = "date"
        dateList.addAll(dates)
        list.add(SelectTitle(string))
        list.addAll(dateList)
    }


    private fun selectOption(position: Int): Single<List<PopupSelect>> {
        return when (position) {
            0 -> Single.just(dataList)
            1 -> Single.just(patientList)
            2 -> Single.just(dateList)
            else -> Single.just(list)
        }
    }

    private fun convertList(position: Int, it: CommonDataBean): Single<List<PopupSelect>> {
        addDataList(getString(R.string.type_seek), it.data_source, dataList)
        addDataList(getString(R.string.type_inspect), it.patient_class, patientList)
        addDateList(getString(R.string.type_date))
        addDataList(getString(R.string.type_system), it.service_sect, serviceList)
        return selectOption(position)
    }


    private fun addDataList(type: String, map: Map<String, String>, dataList: MutableList<PopupSelect>) {
        for (s in map.entries) dataList.add(SelectOption(s.key,getString(R.string.date)))
        if (dataList.isNotEmpty()) {
            list.add(SelectTitle(type))
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


    private fun arrayList(e: PopupSelect): ArrayList<PopupSelect> {
        return when (e.type) {
            getString(R.string.type_seek) -> dataList
            getString(R.string.type_inspect) -> patientList
            getString(R.string.type_system) -> serviceList
            else -> dateList
        }
    }


    lateinit var adapter: RecyclerMultiplexSelectAdapter<PopupSelect>
    override fun setEvent(type: Int, e: PopupSelect, position: Int, view: View?): Any {
        if (view?.id == R.id.select_option) {
            return false
        }
        else if (view?.id == R.id.select_title) {
            val p = adapter.adapterList.indexOf(e) + 1
            val l = arrayList(e)
            if ((view as CheckBox).isChecked) {
                list.removeAll(l)
                adapter.removeList(p, l.size)
            } else {
                list.addAll(p, l)
                adapter.addList(l, p)
            }
        }
        return true
    }


    private fun showPopup(position: Int, dismiss: () -> Unit = {}): PopupWindow {
        adapter = RecyclerMultiplexSelectAdapter<PopupSelect>()
        val manager = GridLayoutManager(requireActivity(), 4)
        manager.spanSizeLookup = ReverseSpanSizeLookup(adapter, 4)
        adapter.addEventAdapter(this)
        val inflate = PopupRecyclerInflate(manager, adapter) { selectList(ExamParam(it)) }
            .apply { httpData = { _, _ -> requireData(position) } }
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