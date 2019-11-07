package com.customers.zktc.base.adapter

import android.text.TextUtils
import android.widget.Filterable
import com.binding.model.adapter.widget.ListAdapter
import java.util.*
import kotlin.collections.ArrayList

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/7 11:47
 * Email: 1033144294@qq.com
 */
class FilterAdapter<E:Filter<*>>: ListAdapter<E>(),Filterable{
    override fun getFilter()=object :android.widget.Filter(){
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if(TextUtils.isEmpty(prefix)){
                results.values = holderList
                results.count = count
            }else{
                val values = ArrayList<E>()
                val prefixString = prefix.toString().toLowerCase(Locale.getDefault())
                for (e in holderList) {
                    if(e.compare(prefixString)) values.add(e)
                }
                results.values = values
                results.count = values.size
            }
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            holderList.clear()
            holderList.addAll(results.values as List<E>)
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}