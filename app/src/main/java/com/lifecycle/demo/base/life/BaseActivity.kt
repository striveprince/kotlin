package com.lifecycle.demo.base.life

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.launcher.ARouter
import com.lifecycle.demo.R
import com.lifecycle.demo.base.life.viewmodel.BaseViewModel
import com.lifecycle.demo.base.util.applyKitKatTranslucency
import com.lifecycle.demo.base.util.rxBus
import com.lifecycle.demo.inject.component.ActivityComponent
import com.lifecycle.demo.inject.data.Api
import com.lifecycle.demo.inject.module.ActivityModule
import com.lifecycle.demo.ui.DemoApplication
import com.lifecycle.binding.base.view.SwipeBackLayout
import com.lifecycle.binding.inter.Init
import com.lifecycle.binding.inter.Parse
import com.lifecycle.binding.util.subscribeNormal
import org.jetbrains.anko.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<Model : ViewModel, B> : AppCompatActivity(), Parse<Model, B>,
    LifecycleInit<Model, ActivityComponent.Builder, Api> {
    @Inject
    lateinit var api: Api
    val model: Model by lazy { initModel() }

    open fun isSwipe(): Int = SwipeBackLayout.FROM_LEFT
    private lateinit var frameLayout: FrameLayout
    override fun owner() = this
    override fun getActivity() = this

    private var toolbarInflate = ToolbarInflate(toolbarIndex())
    private var usableHeightPrevious = 0
    private fun toolbarIndex() = 0
    fun rightText() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(savedInstanceState)
        applyKitKatTranslucency(this, android.R.color.transparent)
    }

    override fun initView(savedInstanceState: Bundle?) {
        DemoApplication.activityBuilder.let { builder ->
            if (builder == null) {
                val parseView = parseBaseView()
                rxBus<ActivityComponent.Builder>()
                    .subscribeNormal {
                        Timber.i("rxBusMain init")
                        frameLayout.removeAllViews()
                        frameLayout.addView(inject(it, savedInstanceState))
                    }
                addToSwipeLayout(parseView)
            } else {
                addToSwipeLayout(inject(builder, savedInstanceState))
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
            swipeBackLayout.addView(injectView, matchParent, matchParent)
        } else setContentView(injectView)
    }

    private fun parseBaseView(): View {
        return AnkoContext.create(this).apply {
            frameLayout =
                frameLayout { imageView { imageResource = R.drawable.ic_launcher_background } }
        }.view
    }

    override fun inject(builder: ActivityComponent.Builder, savedInstanceState: Bundle?): View {
        Timber.i("init base activity_video inject ${javaClass.simpleName}")
        ARouter.getInstance().inject(this)
        val component = builder.applyActivity(ActivityModule(this)).build()
        val method = ActivityComponent::class.java.getDeclaredMethod("inject", this::class.java)
        method.invoke(component, this)
        val view = createView(model, this)
        val root = initToolbar(toolbarInflate, view)
        initData(api, this, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent(view) }
        return root
    }

    private fun initToolbar(toolbarInflate: ToolbarInflate, view: View): View {
        return toolbarInflate.createView(this).let {
            if (it is ViewGroup) {
                it.apply {
                    for (index in 0..childCount) {
                        val toolView = getChildAt(index)
                        if (toolView is Toolbar) {
                            setSupportActionBar(toolView)
                            break
                        }
                    }
                    addView(view, childCount, ViewGroup.LayoutParams(matchParent, matchParent))
                }
            } else view
        }
    }

    override fun initModel(): Model {
        val clazz = javaClass.kotlin.supertypes[0].arguments[0].type!!.javaType as Class<Model>
        return ViewModelProviders.of(this)[clazz]
    }

    override fun t() = model

    @CallSuper
    override fun initData(api: Api, owner: LifecycleOwner, bundle: Bundle?) {
        if (model is Init<*>) (model as Init<Api>).initData(api, owner, bundle)
        if (model is BaseViewModel) (model as BaseViewModel).title.value = title
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

