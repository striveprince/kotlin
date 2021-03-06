package com.lifecycle.binding.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.lifecycle.binding.R
import kotlin.math.abs

/**
 * Created by GongWen on 17/8/24.
 */

class SwipeBackLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        const val FROM_LEFT = 1
        const val FROM_NO = 0
        const val FROM_RIGHT = 1 shl 1
        const val FROM_TOP = 1 shl 2
        const val FROM_BOTTOM = 1 shl 3
    }

    @IntDef(
        FROM_NO,
        FROM_LEFT,
        FROM_TOP,
        FROM_RIGHT,
        FROM_BOTTOM
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class DirectionMode

    var directionMode = FROM_LEFT
        set(direction) {
            field = direction
            mDragHelper.setEdgeTrackingEnabled(direction)
        }

    private val mDragHelper: ViewDragHelper by lazy { ViewDragHelper.create(this, 1f, DragHelperCallback()).apply { setEdgeTrackingEnabled(directionMode) } }
    private val mDragContentView: View by lazy { getChildAt(0).apply { if (background == null) setBackgroundColor(Color.WHITE) } }
    private val innerScrollView: View? by lazy { SwipeBackUtil.findAllScrollViews(this) }

    private var swipeWidth: Int = 0
    private var swipeHeight: Int = 0

    private val mTouchSlop: Int by lazy { mDragHelper.touchSlop }
    private var swipeBackFraction: Float = 0.toFloat()
    var isSwipeFromEdge = false
    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()

    private var leftOffset = 0
    private var topOffset = 0
    private var autoFinishedVelocityLimit = 2000f
    var onSwipeBackListener: ((View?, Float) -> Unit) = { _, swipeBackFraction1 -> this.alpha = 1 - swipeBackFraction1 }
    var onSwipeFinishedListener: ((View?, Boolean) -> Unit) = { _, isEnd -> if (isEnd) finish() }

    private var touchedEdge = ViewDragHelper.INVALID_POINTER

    private val isSwipeEnabled: Boolean
        get() {
            if (isSwipeFromEdge) {
                when (directionMode) {
                    FROM_LEFT -> return touchedEdge == ViewDragHelper.EDGE_LEFT
                    FROM_TOP -> return touchedEdge == ViewDragHelper.EDGE_TOP
                    FROM_RIGHT -> return touchedEdge == ViewDragHelper.EDGE_RIGHT
                    FROM_BOTTOM -> return touchedEdge == ViewDragHelper.EDGE_BOTTOM
                }
            }
            return true
        }


    init {
        setWillNotDraw(false)
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout)
        directionMode = a.getInt(R.styleable.SwipeBackLayout_directionMode, directionMode)
        isSwipeFromEdge = a.getBoolean(R.styleable.SwipeBackLayout_isSwipeFromEdge, isSwipeFromEdge)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        if (childCount != 1) {
            throw IllegalStateException("SwipeBackLayout must contains only one direct child.")
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth: Int = View.resolveSize(mDragContentView.measuredWidth, widthMeasureSpec) + paddingLeft + paddingRight
        val measuredHeight: Int = View.resolveSize(mDragContentView.measuredHeight, heightMeasureSpec) + paddingTop + paddingBottom
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return
        val left = paddingLeft + leftOffset
        val top = paddingTop + topOffset
        val right = left + mDragContentView.measuredWidth
        val bottom = top + mDragContentView.measuredHeight
        mDragContentView.layout(left, top, right, bottom)
        if (changed) {
            swipeWidth = width
            swipeHeight = height
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX
                downY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> if (innerScrollView != null && SwipeBackUtil.contains(innerScrollView!!, downX, downY)) {
                val distanceX = abs(ev.rawX - downX)
                val distanceY = abs(ev.rawY - downY)
                return when (directionMode) {
                    FROM_LEFT, FROM_RIGHT -> if (distanceY > mTouchSlop && distanceY > distanceX) super.onInterceptTouchEvent(ev) else defaultHandleEvent(ev)
                    FROM_TOP, FROM_BOTTOM -> if (distanceX > mTouchSlop && distanceX > distanceY) super.onInterceptTouchEvent(ev) else defaultHandleEvent(ev)
                    else -> defaultHandleEvent(ev)
                }
            }
        }
        return defaultHandleEvent(ev)
    }

    private fun defaultHandleEvent(ev: MotionEvent) = mDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) ViewCompat.postInvalidateOnAnimation(this)
    }

    fun smoothScrollToX(finalLeft: Int) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, paddingTop)) ViewCompat.postInvalidateOnAnimation(this)
    }

    fun smoothScrollToY(finalTop: Int) {
        if (mDragHelper.settleCapturedViewAt(paddingLeft, finalTop)) ViewCompat.postInvalidateOnAnimation(this)
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === mDragContentView
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            leftOffset = if (isSwipeEnabled) {
                if (directionMode == FROM_LEFT && !SwipeBackUtil.canViewScrollRight(innerScrollView, downX, downY, false)) {
                    left.coerceAtLeast(paddingLeft).coerceAtMost(swipeWidth)
                } else if (directionMode == FROM_RIGHT && !SwipeBackUtil.canViewScrollLeft(innerScrollView, downX, downY, false)) {
                    left.coerceAtLeast(-swipeWidth).coerceAtMost(paddingRight)
                } else paddingLeft
            } else paddingLeft
            return leftOffset
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            topOffset = if (isSwipeEnabled) {
                if (directionMode == FROM_TOP && !SwipeBackUtil.canViewScrollUp(innerScrollView, downX, downY, false)) {
                    top.coerceAtLeast(paddingTop).coerceAtMost(swipeHeight)
                } else if (directionMode == FROM_BOTTOM && !SwipeBackUtil.canViewScrollDown(innerScrollView, downX, downY, false)) {
                    top.coerceAtLeast(-swipeHeight).coerceAtMost(paddingBottom)
                } else paddingTop
            } else paddingTop
            return topOffset
        }

        override fun onViewPositionChanged(changedView: View, l: Int, t: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, l, t, dx, dy)
            val left = abs(l)
            val top = abs(t)
            when (directionMode) {
                FROM_LEFT, FROM_RIGHT -> swipeBackFraction = 1.0f * left / swipeWidth
                FROM_TOP, FROM_BOTTOM -> swipeBackFraction = 1.0f * top / swipeHeight
            }
            onSwipeBackListener.invoke(mDragContentView, swipeBackFraction)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            topOffset = 0
            leftOffset = 0
            if (isSwipeEnabled) {
                if (backJudgeBySpeed(xvel, yvel)) {
                    when (directionMode) {
                        FROM_LEFT -> smoothScrollToX(swipeWidth)
                        FROM_TOP -> smoothScrollToY(swipeHeight)
                        FROM_RIGHT -> smoothScrollToX(-swipeWidth)
                        FROM_BOTTOM -> smoothScrollToY(-swipeHeight)
                    }
                } else {
                    when (directionMode) {
                        FROM_LEFT, FROM_RIGHT -> smoothScrollToX(paddingLeft)
                        FROM_BOTTOM, FROM_TOP -> smoothScrollToY(paddingTop)
                    }
                }
            }
            touchedEdge = ViewDragHelper.INVALID_POINTER
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (state == ViewDragHelper.STATE_IDLE) {
                if (swipeBackFraction == 0f) {
                    onSwipeFinishedListener(mDragContentView, false)
                } else if (swipeBackFraction == 1f) {
                    onSwipeFinishedListener(mDragContentView, true)
                }
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return swipeWidth
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return swipeHeight
        }

        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
            super.onEdgeTouched(edgeFlags, pointerId)
            touchedEdge = edgeFlags
        }
    }

    fun finish() {
        (context as Activity).onBackPressed()
    }

    private fun backJudgeBySpeed(xvel: Float, yvel: Float): Boolean {
        when (directionMode) {
            FROM_LEFT -> return xvel > autoFinishedVelocityLimit
            FROM_TOP -> return yvel > autoFinishedVelocityLimit
            FROM_RIGHT -> return xvel < -autoFinishedVelocityLimit
            FROM_BOTTOM -> return yvel < -autoFinishedVelocityLimit
        }
        return false
    }
}
