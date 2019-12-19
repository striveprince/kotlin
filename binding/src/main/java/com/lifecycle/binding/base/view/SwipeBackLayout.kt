package com.lifecycle.binding.base.view

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

    var directionMode =
        FROM_LEFT
        set(direction) {
            field = direction
            mDragHelper.setEdgeTrackingEnabled(direction)
        }

    private val mDragHelper: ViewDragHelper
    private var mDragContentView: View? = null
    private var innerScrollView: View? = null

    private var swipeWidth: Int = 0
    private var swipeHeight: Int = 0

    private val mTouchSlop: Int
    private var swipeBackFraction: Float = 0.toFloat()
    var isSwipeFromEdge = false
    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()

    private var leftOffset = 0
    private var topOffset = 0
    var autoFinishedVelocityLimit = 2000f
    private var onSwipeBackListener: ((View?, Float) -> Unit)? = null
    private var onSwipeFinishedListener: ((View?, Boolean) -> Unit)? = null

    fun setOnSwipeBackListener(onSwipeBackListener: ((View?, Float) -> Unit)) {
        this.onSwipeBackListener = onSwipeBackListener
    }

    fun setOnSwipeFinishedListener(onSwipeFinishedListener: ((View?, Boolean) -> Unit)) {
        this.onSwipeFinishedListener = onSwipeFinishedListener
    }

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
        mDragHelper = ViewDragHelper.create(this, 1f, DragHelperCallback())
        mDragHelper.setEdgeTrackingEnabled(directionMode)
        mTouchSlop = mDragHelper.touchSlop
        setOnSwipeFinishedListener { _, isEnd -> if (isEnd) finish() }
        setOnSwipeBackListener { _, swipeBackFraction1 -> this.alpha = 1 - swipeBackFraction1 }
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
        if (childCount > 1) {
            throw IllegalStateException("SwipeBackLayout must contains only one direct child.")
        }
        var defaultMeasuredWidth = 0
        var defaultMeasuredHeight = 0
        val measuredWidth: Int
        val measuredHeight: Int
        if (childCount > 0) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            mDragContentView = getChildAt(0)
            if (mDragContentView!!.background == null) mDragContentView!!.setBackgroundColor(Color.WHITE)
            defaultMeasuredWidth = mDragContentView!!.measuredWidth
            defaultMeasuredHeight = mDragContentView!!.measuredHeight
        }
        measuredWidth = View.resolveSize(defaultMeasuredWidth, widthMeasureSpec) + paddingLeft + paddingRight
        measuredHeight = View.resolveSize(defaultMeasuredHeight, heightMeasureSpec) + paddingTop + paddingBottom
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return
        val left = paddingLeft + leftOffset
        val top = paddingTop + topOffset
        val right = left + mDragContentView!!.measuredWidth
        val bottom = top + mDragContentView!!.measuredHeight
        mDragContentView!!.layout(left, top, right, bottom)
        if (changed) {
            swipeWidth = width
            swipeHeight = height
        }
        innerScrollView =
            SwipeBackUtil.findAllScrollViews(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        MotionEvent#getAction()
//        when (MotionEventCompat.getActionMasked(ev)) {
        when (ev.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.rawX
                downY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> if (innerScrollView != null && SwipeBackUtil.contains(
                    innerScrollView!!,
                    downX,
                    downY
                )
            ) {
                val distanceX = Math.abs(ev.rawX - downX)
                val distanceY = Math.abs(ev.rawY - downY)
                if (directionMode == FROM_LEFT || directionMode == FROM_RIGHT) {
                    if (distanceY > mTouchSlop && distanceY > distanceX) {
                        return super.onInterceptTouchEvent(ev)
                    }
                } else if (directionMode == FROM_TOP || directionMode == FROM_BOTTOM) {
                    if (distanceX > mTouchSlop && distanceX > distanceY) {
                        return super.onInterceptTouchEvent(ev)
                    }
                }
            }
        }
        val handled = mDragHelper.shouldInterceptTouchEvent(ev)
        return handled || super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun smoothScrollToX(finalLeft: Int) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, paddingTop)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun smoothScrollToY(finalTop: Int) {
        if (mDragHelper.settleCapturedViewAt(paddingLeft, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === mDragContentView
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            leftOffset = paddingLeft
            if (isSwipeEnabled) {
                if (directionMode == FROM_LEFT && !SwipeBackUtil.canViewScrollRight(
                        innerScrollView,
                        downX,
                        downY,
                        false
                    )
                ) {
                    leftOffset = Math.min(Math.max(left, paddingLeft), swipeWidth)
                } else if (directionMode == FROM_RIGHT && !SwipeBackUtil.canViewScrollLeft(
                        innerScrollView,
                        downX,
                        downY,
                        false
                    )
                ) {
                    leftOffset = Math.min(Math.max(left, -swipeWidth), paddingRight)
                }
            }
            return leftOffset
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            topOffset = paddingTop
            if (isSwipeEnabled) {
                if (directionMode == FROM_TOP && !SwipeBackUtil.canViewScrollUp(
                        innerScrollView,
                        downX,
                        downY,
                        false
                    )
                ) {
                    topOffset = Math.min(Math.max(top, paddingTop), swipeHeight)
                } else if (directionMode == FROM_BOTTOM && !SwipeBackUtil.canViewScrollDown(
                        innerScrollView,
                        downX,
                        downY,
                        false
                    )
                ) {
                    topOffset = Math.min(Math.max(top, -swipeHeight), paddingBottom)
                }
            }
            return topOffset
        }

        override fun onViewPositionChanged(changedView: View, l: Int, t: Int, dx: Int, dy: Int) {
            var left = l
            var top = t
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            left = abs(left)
            top = abs(top)
            when (directionMode) {
                FROM_LEFT, FROM_RIGHT -> swipeBackFraction = 1.0f * left / swipeWidth
                FROM_TOP, FROM_BOTTOM -> swipeBackFraction = 1.0f * top / swipeHeight
            }
            onSwipeBackListener?.invoke(mDragContentView, swipeBackFraction)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            topOffset = 0
            leftOffset = topOffset
            if (!isSwipeEnabled) {
                touchedEdge = ViewDragHelper.INVALID_POINTER
                return
            }
            touchedEdge = ViewDragHelper.INVALID_POINTER
            val isBackToEnd = backJudgeBySpeed(xvel, yvel)
            if (isBackToEnd) {
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

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if (state == ViewDragHelper.STATE_IDLE) {
                if (swipeBackFraction == 0f) {
                    onSwipeFinishedListener?.invoke(mDragContentView, false)
                } else if (swipeBackFraction == 1f) {
                    onSwipeFinishedListener?.invoke(mDragContentView, true)
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
