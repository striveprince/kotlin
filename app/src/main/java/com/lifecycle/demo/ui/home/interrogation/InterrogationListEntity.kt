package com.lifecycle.demo.ui.home.interrogation

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.utils.MapUtils.isNotEmpty
import com.lifecycle.binding.inter.bind.DataBeanInflate
import com.lifecycle.demo.R
import com.lifecycle.demo.base.util.ARouterUtil
import com.lifecycle.demo.inject.data.net.bean.InterrogationBean
import com.lifecycle.binding.rotate.TimeEntity
import com.lifecycle.binding.rotate.TimeUtil
import com.lifecycle.binding.inter.bind.data.DataBindInflate
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.inter.inflate.Diff
import com.lifecycle.binding.inter.inflate.Recycler

@LayoutView(layout = [R.layout.holder_interrogation])
class InterrogationListEntity(bean: InterrogationBean) : DataBeanInflate<InterrogationBean, ViewDataBinding>(bean), Diff,Recycler,TimeEntity {
    var t:RecyclerView.ViewHolder? = null
    val state = MutableLiveData(interrogationState())
//    val timeRemaining = MutableLiveData(calculationTime(bean.end_time))
    val timeRemaining = MutableLiveData(calculationTime("end_time"))
    val imgVisibility = MutableLiveData(View.VISIBLE)
//    val imgVisibility = MutableLiveData(if (bean.evaluate?.score in 1..5) View.VISIBLE else View.GONE)
    val content = MutableLiveData(getContent(bean))
    val notifyRed = MutableLiveData<Int>(R.drawable.shape_circle8)
//    val age = MutableLiveData<String>(bean.exam.age.toString())
    val age = MutableLiveData("12")
//    val sex = MutableLiveData<String>(bean.exam.sex)
    val sex = MutableLiveData("bean.exam.sex")

    override fun attach(t: RecyclerView.ViewHolder) {
        TimeUtil.add(this)
    }

    override fun detached(t: RecyclerView.ViewHolder) {
        TimeUtil.remove(this)
    }

    override fun getTurn() {
//        timeRemaining.value = calculationTime(bean.end_time)
    }

    private fun getContent(bean: InterrogationBean): String {
//        bean.dialogues.apply {
//            if (isNotEmpty()) {
//                last().messages.apply {
//                    if (isNotEmpty())
//                        last().apply {
//                            return when (type) {
//                                1 -> "[${content.replace("{DoctorName}", "您")
//                                    .replace("医生", "您")
//                                    .replace("{PatientName}", "病患")}]"
//                                2 -> "[图片]"
//                                3 -> "[语音]"
//                                else -> bean.description
//                            }
//                        }
//                }
//            }
//        }
//        return bean.description
        return "bean.description"
    }
//
    val img = MutableLiveData(R.mipmap.rate1__ic
//            when (bean.evaluate?.score) {
//                1 -> R.mipmap.rate1__ic
//                2 -> R.mipmap.rate2__ic
//                3 -> R.mipmap.rate3__ic
//                4 -> R.mipmap.rate4__ic
//                5 -> R.mipmap.rate5__ic
//                else -> R.color.transparent
//            }
    )

    fun onItemClick(v: View) {
        ARouterUtil.interrogationDetail("bean.id")
    }

    private fun interrogationState(): CharSequence {
        return "投诉退款"
//        return when (bean.state) {
//            -20 -> "投诉退款"
//            -10 -> "已退诊"
//            -1, -5, -30, -40 -> "已取消"
//            5 -> "待接诊"
//            10, 20 -> "问诊中"
//            30 -> "已完成"
//            40 -> "已评价"
//            else -> ""
//        }
    }

    private fun calculationTime(endTime: String): CharSequence{
        return ""
    }
}