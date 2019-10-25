package com.customers.zktc.ui.home.page

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binding.model.App
import com.customers.zktc.R
import timber.log.Timber

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/25 9:22
 * Email: 1033144294@qq.com
 */
class HomePageDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (R.id.floor_picture_layout == view.id) {
            val tenDp = App.toPx(6)
            val layoutManager = parent.layoutManager as GridLayoutManager
            val spanCount = layoutManager.spanCount
            val itemCount = layoutManager.childCount
//            layoutManager.
            layoutManager.findContainingItemView(view)?.let {
                Timber.i("view: left=${it.left},right=${it.right},${it.contentDescription}")
            }
        }
    }
}
//            (view as ViewGroup).lef
//            val from = App.getScreenWidth() - tenDp
//            Timber.i("view: left=${view.left},right=${view.right},${view.contentDescription}")
//            if (view.x.toInt() == 0|| view.left == tenDp) {
//                outRect.set(tenDp, 0,0,0)
//            }else if (view.y.toInt() == from||view.right== App.getScreenWidth()) {
//                outRect.set(0,0, tenDp, 0)
//            }
//            Timber.i("rect: left=${outRect.left},right=${outRect.right},in=${from}..${App.getScreenWidth()}")
