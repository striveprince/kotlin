package com.lifecycle.binding.life

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.App
import com.lifecycle.binding.R
import com.lifecycle.binding.base.view.SwipeBackLayout
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.inter.inflate.Inflate
import com.lifecycle.binding.util.rxBus
import com.lifecycle.binding.util.subscribeNormal
import com.lifecycle.binding.viewmodel.LifeViewModel
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<Model : ViewModel, B> : AppCompatActivity(), Parse<Model, B>,

    LifecycleInit<Model> {
    val model: Model by lazy { initModel() }
    var toolbarIndex = 0

    open fun isSwipe(): Int = SwipeBackLayout.FROM_LEFT
    override fun owner() = this
    override fun getActivity() = this
    private var usableHeightPrevious = 0
    fun rightText() = ""
    override fun t() = model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.startCreate(this)
        initView(savedInstanceState)
    }

    fun initView(savedInstanceState: Bundle?) {
        val layout = if (!App.init) {
            startView().apply {
                rxBus<Boolean>()
                    .filter{it}
                    .subscribeNormal {
                        removeAllViews()
                        addView(inject(savedInstanceState))
                    }
            }
        } else inject(savedInstanceState)
        setContentView(layout)
    }

    override fun inject(savedInstanceState: Bundle?): View {
        val view = createView(model, this)
        initData(this,savedInstanceState)
        return initToolbar(toolbarView(), view)
    }

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        model.let { if(it is LifeViewModel)it.initData(this,bundle) }
    }
    open fun toolbarView(): ViewGroup {
        return if (App.toolbarList.isEmpty()) FrameLayout(this).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        else App.toolbarList[toolbarIndex].createView(this) as ViewGroup
    }

    private fun initToolbar(viewGroup: ViewGroup, injectView: View): View {
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
    }

    private fun addToSwipeLayout(injectView: View) {
        return if (isSwipe() != SwipeBackLayout.FROM_NO) {
            setContentView(R.layout.activity_base)
            val swipeBackLayout = findViewById<SwipeBackLayout>(R.id.swipe_back_layout)
            swipeBackLayout.directionMode = isSwipe()
            val imageView: View = findViewById(R.id.iv_shadow)
            swipeBackLayout.setOnSwipeBackListener { _, f -> imageView.alpha = 1 - f }
            swipeBackLayout.addView(injectView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        } else setContentView(injectView)
    }

    open fun startView(): FrameLayout {
        return FrameLayout(this)
    }

    override fun initModel(): Model {
        val clazz = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<Model>
        return ViewModelProviders.of(this)[clazz]
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

    fun onBackClick(view: View) {
        onBackPressed()
    }

    fun onRightClick(view: View) {
    }

}

