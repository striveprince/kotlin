package com.customers.zktc.ui.home.page

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binding.model.App
import com.customers.zktc.R

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/25 9:22
 * Email: 1033144294@qq.com
 */
class HomePageDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val tenDp = App.toPx(6)
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val position = parent.getChildAdapterPosition(view)
        val index = layoutManager.spanSizeLookup.getSpanIndex(position, spanCount)
        val itemSpan = layoutManager.spanSizeLookup.getSpanSize(position)
        if (view.id == R.id.home_page_layout) {
            if (itemSpan != spanCount) {
                if (index == 0)
                    outRect.set(tenDp, 0, 0, 0)
                 else if (index + itemSpan == spanCount)
                    outRect.set(0, 0, tenDp, 0)
            }else{
                outRect.set(tenDp,0,tenDp,0)
            }
        }
    }
}