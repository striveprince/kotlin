package com.lifecycle.binding.life

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.R
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.util.lifeModel
import com.lifecycle.binding.view.SwipeBackLayout

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<Model : ViewModel, B> : AppCompatActivity(), Parse<Model, B>,

    LifecycleInit<Model> {
    val model: Model by lazy { initModel() }
    var toolbarIndex = Int.MAX_VALUE

    open fun isSwipe(): Int = SwipeBackLayout.FROM_LEFT
    override fun owner() = this
    override fun requireActivity() = this
    private var usableHeightPrevious = 0
    fun rightText() = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        val injectView = if (!AppLifecycle.initFinish) {
            startView().apply { waitFinish(savedInstanceState) }
        } else inject(savedInstanceState)
        if (isSwipe() != SwipeBackLayout.FROM_NO) {
            setContentView(R.layout.activity_base)
            val swipeBackLayout = findViewById<SwipeBackLayout>(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            val imageView: View = findViewById(R.id.iv_shadow)
            swipeBackLayout.onSwipeBackListener = { _, f -> imageView.alpha = 1 - f }
            swipeBackLayout.addView(injectView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        } else setContentView(injectView)
    }


    override fun inject(savedInstanceState: Bundle?): View {
        val injectView = createView(model, this)
        initData(this, savedInstanceState)
        if(injectView.searchToolbar()==null){
            if (AppLifecycle.toolbarList.run { isNotEmpty()&& toolbarIndex<size }){
                val toolbar = AppLifecycle.toolbarList[toolbarIndex].createView(this) as ViewGroup
                return LinearLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    orientation = LinearLayout.VERTICAL
                    addView(toolbar,MATCH_PARENT, WRAP_CONTENT)
                    addView(injectView, MATCH_PARENT, MATCH_PARENT)
                    toolbar.searchToolbar()
                }
            }
        }
        return injectView
    }

    open fun ViewGroup.waitFinish(savedInstanceState: Bundle?) {
        AppLifecycle.appInit = {
            removeAllViews()
            addView(inject(savedInstanceState))
        }
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        model.let { if (it is Init) it.initData(this, bundle) }
    }


    open fun initToolbar(it: Toolbar) {

    }

    fun View.searchToolbar(): Toolbar? {
        return when(this){
            is Toolbar-> this.also { setSupportActionBar(it) }.also { initToolbar(it) }
            is ViewGroup-> loopToolbar()
            else-> null
        }
    }

    private fun ViewGroup.loopToolbar(): Toolbar?{
        for (index in 0 until childCount)
            return getChildAt(index).searchToolbar()
        return null
    }

    private fun addToSwipeLayout(injectView: View) {
        return if (isSwipe() != SwipeBackLayout.FROM_NO) {
            setContentView(R.layout.activity_base)
            val swipeBackLayout = findViewById<SwipeBackLayout>(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            val imageView: View = findViewById(R.id.iv_shadow)
            swipeBackLayout.onSwipeBackListener = { _, f -> imageView.alpha = 1 - f }
            swipeBackLayout.addView(injectView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        } else setContentView(injectView)
    }

    open fun startView(): ViewGroup = FrameLayout(this)

    override fun initModel(clazz: Class<Model>): Model {
        return lifeModel(clazz)
    }

    fun onBackClick(view: View) {
        onBackPressed()
    }

    fun onRightClick(view: View) {
    }

}


/*
    open fun toolbarView(): ViewGroup {
        return if (AppLifecycle.toolbarList.isEmpty()) FrameLayout(this).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        else AppLifecycle.toolbarList[toolbarIndex].createView(this) as ViewGroup
    }
        return if (viewGroup is Toolbar) {
            setSupportActionBar(viewGroup)
            LinearLayout(viewGroup.context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                orientation = LinearLayout.VERTICAL
                addView(viewGroup)
                addView(injectView)
            }
        } else {
            viewGroup.apply {
                for (index in 0..childCount) {
                    val v = getChildAt(index)
                    if (v is Toolbar) {
                        setSupportActionBar(v)
                        break
                    }
                }
                addView(injectView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            }
        }
    private fun initToolbar(injectView: View): View {
        injectView.searchToolbar()?.let { setSupportActionBar(it) }
        return injectView
    }

     open fun possiblyResizeChildOfContent(injectView: View) {
        val usableHeightNow = computeUsableHeight(injectView)
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = injectView.parentHeight()
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            injectView.let {
                it.layoutParams.height =
                    if (heightDifference > usableHeightSansKeyboard / 4) scrollUp(
                        injectView,
                        usableHeightSansKeyboard,
                        heightDifference
                    )
                    else usableHeightSansKeyboard
                it.requestLayout()
            }
        }
        usableHeightPrevious = usableHeightNow
    }

    private fun View.parentHeight(): Int {
        return if (parent is ViewGroup) (parent as ViewGroup).measuredHeight
        else measuredHeight
    }

    private fun scrollUp(
        injectView: View,
        usableHeightSansKeyboard: Int,
        heightDifference: Int
    ): Int {
        val height = usableHeightSansKeyboard - heightDifference
        if (injectView is ScrollView) {
            val childHeight = injectView.getChildAt(0).height
            injectView.apply { postDelayed({ scrollTo(0, childHeight - height) }, 10) }
        }
        return height
    }

    private fun computeUsableHeight(injectView: View): Int {
        val r = Rect()
        injectView.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }
*/