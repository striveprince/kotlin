package com.lifecycle.binding.inter.bind.data

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.Constant
import com.lifecycle.binding.R
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.inter.inflate.Recycler
import com.lifecycle.binding.util.findLayoutView

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 10:28
 * Email: 1033144294@qq.com
 */
interface DataBindRecycler<T, Binding : ViewDataBinding> : DataBinding<T, Binding>, Inflate, Recycler {

    override fun createView(context: Context, parent: ViewGroup?, convertView: View?): View {
        val layoutId = convertView?.getTag(R.id.inflate)?.let { (it as Inflate).layoutId() }
        val binding = convertView?.getTag(R.id.dataBinding).let {
            if (it is ViewDataBinding && layoutId() == layoutId) {
                it.setVariable(Constant.parse, this)
                it.setVariable(Constant.vm, t())
                it.executePendingBindings()
                it
            }else parse(t(),context,parent,false)
        }
        val view = binding.root
        view.setTag(R.id.inflate, this)
        view.setTag(R.id.dataBinding, binding)
        return view
    }


    override fun layoutId() = findLayoutView(javaClass).layout[layoutIndex()]
}