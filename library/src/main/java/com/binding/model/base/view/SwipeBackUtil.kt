package com.binding.model.base.view

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

/**
 * Created by GongWen on 17/8/25.
 */

object SwipeBackUtil {
    fun canViewScrollUp(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y)) {
            defaultValueForNull
        } else ViewCompat.canScrollVertically(mView, -1)
    }

    fun canViewScrollDown(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y)) {
            defaultValueForNull
        } else
            mView.canScrollVertically(1)
//            ViewCompat.canScrollVertically(mView, 1)
    }

    fun canViewScrollRight(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y)) {
            defaultValueForNull
        } else
//            ViewCompat.canScrollHorizontally(mView, -1)
            mView.canScrollHorizontally(-1)
    }

    fun canViewScrollLeft(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y)) {
            defaultValueForNull
        } else
//            ViewCompat.canScrollHorizontally(mView, 1)
            mView.canScrollHorizontally(1)
    }


    fun findAllScrollViews(mViewGroup: ViewGroup): View? {
        for (i in 0 until mViewGroup.childCount) {
            var mView: View? = mViewGroup.getChildAt(i)
            if (mView!!.visibility != View.VISIBLE) {
                continue
            }
            if (isScrollableView(mView)) {
                return mView
            }
            if (mView is ViewGroup) {
                mView = findAllScrollViews((mView as ViewGroup?)!!)
                if (mView != null) {
                    return mView
                }
            }
        }
        return null
    }

    fun isScrollableView(mView: View): Boolean {
        return (mView is ScrollView
                || mView is HorizontalScrollView
                || mView is NestedScrollView
                || mView is AbsListView
                || mView is RecyclerView
                || mView is ViewPager
                || mView is WebView)
    }

    fun contains(mView: View, x: Float, y: Float): Boolean {
        val localRect = Rect()
        mView.getGlobalVisibleRect(localRect)
        return localRect.contains(x.toInt(), y.toInt())
    }

}
