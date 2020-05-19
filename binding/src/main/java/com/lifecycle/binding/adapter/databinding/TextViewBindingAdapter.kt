package com.lifecycle.binding.adapter.databinding

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.ObservableField
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lifecycle.binding.R
import com.lifecycle.binding.adapter.databinding.inter.AfterTextChangedError
import com.lifecycle.binding.adapter.databinding.inter.Observer
import com.lifecycle.binding.util.observe
import com.lifecycle.binding.util.observer

/**
 * Created by arvin on 2018/1/17.
 */
@SuppressLint("RestrictedApi")
object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:drawableBottom")
    fun setDrawableBottom(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableBottom(
            view, ContextCompat.getDrawable(view.context, drawable)
        )
    }

    @JvmStatic
    @BindingAdapter("android:drawableLeft")
    fun setDrawableLeft(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableLeft(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:error", event = "android:errorAttrChanged")
    fun getTextError(view: TextView): CharSequence? {
        return view.error
    }

    @JvmStatic
    @BindingAdapter(value = ["android:checkError", "android:errorAttrChanged"], requireAll = false)
    fun setTextError(view: TextView, after: AfterTextChangedError?, errorAttrChanged: InverseBindingListener?) {
        val newValue: TextWatcher?
        newValue = if (after == null && errorAttrChanged == null) {
            null
        } else {
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    after?.checkError(s).let {
                        errorAttrChanged?.onChange()
                        if (view is TextInputEditText) {
                            view.getInputLayout().error = it
                        } else {
                            view.error = it
                        }
                    }
                }
            }
        }
        ListenerUtil.trackListener(view, newValue, R.id.textError)?.let {
            view.removeTextChangedListener(it)
        }
        newValue?.let { view.addTextChangedListener(it) }
    }

    @JvmStatic
    fun TextInputEditText.getInputLayout(): TextInputLayout {
        TextInputEditText::class.java.getDeclaredMethod("getTextInputLayout").let {
            it.isAccessible = true
            return it.invoke(this) as TextInputLayout
        }
    }


    @JvmStatic
    @BindingAdapter("android:drawableRight")
    fun setDrawableRight(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableRight(
            view,
            ContextCompat.getDrawable(view.context, drawable)
        )
    }

    @JvmStatic
    @BindingAdapter("android:drawableTop")
    fun setDrawableTop(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableTop(view, ContextCompat.getDrawable(view.context, drawable))
    }

    @JvmStatic
    @BindingAdapter("android:drawableStart")
    fun setDrawableStart(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableStart(view, ContextCompat.getDrawable(view.context, drawable))
    }

    @BindingAdapter("android:drawableEnd")
    fun setDrawableEnd(view: TextView, drawable: Int) {
        androidx.databinding.adapters.TextViewBindingAdapter.setDrawableEnd(view, ContextCompat.getDrawable(view.context, drawable))
    }
}

fun TextView.textChange(function: (String) -> Unit): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            function(s.toString())
        }
    }.apply { addTextChangedListener(this) }
}

fun TextView.bindChange(s: Observer<String>):TextWatcher{
    s.observer { text = it }
    return textChange { if (it !=s.get())s.set(it) }
}

fun TextView.bindChange(s: ObservableField<String>):TextWatcher{
    s.observe { text =  it }
    return textChange { if (it !=s.get())s.set(it) }
}

fun TextView.bindChange(owner: LifecycleOwner, s: MutableLiveData<String>):TextWatcher{
    s.observer(owner) { text =  it }
    return textChange { if (it !=s.value)s.value = it }
}
