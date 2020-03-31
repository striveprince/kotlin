package com.lifecycle.binding.view

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

/**
 * Created by GongWen on 17/8/25.
 */

object SwipeBackUtil {
    fun canViewScrollUp(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(
                mView,
                x,
                y
            )
        ) {
            defaultValueForNull
        } else
            mView.canScrollVertically(-1)
    }

    fun canViewScrollDown(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y))  defaultValueForNull  else mView.canScrollVertically(1)
    }

    fun canViewScrollRight(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean): Boolean {
        return if (mView == null || !contains(mView, x, y)) defaultValueForNull  else mView.canScrollHorizontally(-1)
    }

    fun canViewScrollLeft(mView: View?, x: Float, y: Float, defaultValueForNull: Boolean)=
         if (mView == null || !contains(mView, x, y)) defaultValueForNull
        else mView.canScrollHorizontally(1)


    fun findAllScrollViews(mViewGroup: ViewGroup): View? {
        for (i in 0 until mViewGroup.childCount) {
            val view: View = mViewGroup.getChildAt(i)
            if (view.visibility != View.VISIBLE) continue
            if (isScrollableView(view)) return view
            if (view is ViewGroup) {
                return findAllScrollViews((view as ViewGroup?)!!)
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
