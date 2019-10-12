package com.customers.zktc.base.view

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.customers.zktc.R

import java.util.ArrayList

class VerificationCodeView
@JvmOverloads constructor(paramContext: Context, paramAttributeSet: AttributeSet? = null, paramInt: Int = 0) :
    RelativeLayout(paramContext, paramAttributeSet, paramInt) {
    var editText: EditText? = null
        private set

    private var inputData: String? = null
    private var codeListener:((String)->Unit)? = null
//    var http: ((Int, Int) -> Single<R>)? = null
    private var tvBgFocus = R.drawable.verification_code_et_bg_focus
    private var tvBgNormal = R.drawable.verification_code_et_bg_normal
    private var tvHeight = 45
    private val tvList = ArrayList<TextView>()
    private var tvMarginRight = 10
    private var tvTextColor: Int = 0
    private var tvTextSize = 8.0f
    private var tvWidth = 45
    private var vCodeLength = 6

    init {
        val typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.VerificationCodeView)
        this.vCodeLength = typedArray.getInteger(R.styleable.VerificationCodeView_vCodeDataLength, 6)
        this.tvTextColor = typedArray.getColor(R.styleable.VerificationCodeView_vCodeTextColor, resources.getColor(R.color.black))
        this.tvTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vCodeTextSize, TypedValue.applyDimension(2, 8.0f, resources.displayMetrics).toInt()).toFloat()
        this.tvWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vCodeWidth, TypedValue.applyDimension(1, 45.0f, resources.displayMetrics).toInt())
        this.tvHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vCodeHeight, TypedValue.applyDimension(1, 45.0f, resources.displayMetrics).toInt())
        this.tvMarginRight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeView_vCodeMargin, TypedValue.applyDimension(1, 10.0f, resources.displayMetrics).toInt())
        this.tvBgNormal = typedArray.getResourceId(R.styleable.VerificationCodeView_vCodeBackgroundNormal, R.drawable.verification_code_et_bg_normal)
        this.tvBgFocus = typedArray.getResourceId(R.styleable.VerificationCodeView_vCodeBackgroundFocus, R.drawable.verification_code_et_bg_focus)
        typedArray.recycle()
        init()
    }

    private fun clearsFocus() {
        for (b in 0 until this.vCodeLength)
            this.tvList[b].setBackgroundResource(this.tvBgNormal)
    }

    private fun init() {
        initTextView()
        initEditText()
        clearsFocus()
    }

    private fun initEditText() {
        this.editText = EditText(context)
        addView(this.editText)
        val layoutParams = this.editText!!.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = -1
        layoutParams.height = this.tvHeight
        this.editText!!.layoutParams = layoutParams
        this.editText!!.imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED
        this.editText!!.isCursorVisible = false
        this.editText!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(this.vCodeLength))
        this.editText!!.inputType = InputType.TYPE_CLASS_NUMBER
        this.editText!!.textSize = 0.0f
        this.editText!!.setBackgroundResource(0)
        this.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(param1Editable: Editable) {
                codeListener?.invoke(param1Editable.toString())
            }

            override fun beforeTextChanged(param1CharSequence: CharSequence, param1Int1: Int, param1Int2: Int, param1Int3: Int) {}

            override fun onTextChanged(param1CharSequence: CharSequence?, param1Int1: Int, param1Int2: Int, param1Int3: Int) {
                var paramInt2 = 0
                var paramInt1 = 0
                if (param1CharSequence != null && !TextUtils.isEmpty(param1CharSequence.toString())) {
                    inputData = param1CharSequence.toString()
                    if (inputData!!.length == vCodeLength) {
                        tvSetFocus(vCodeLength - 1)
                    } else {
                        tvSetFocus(inputData!!.length)
                    }
                    while (paramInt1 < inputData!!.length) {
                        val str = inputData
                        paramInt2 = paramInt1 + 1
                        tvList[paramInt1].text =
                            str!!.substring(paramInt1, paramInt2)
                        paramInt1 = paramInt2
                    }
                    paramInt1 = inputData!!.length
                    while (paramInt1 < vCodeLength) {
                        tvList[paramInt1].text = ""
                        paramInt1++
                    }
                } else {
                    tvSetFocus(0)
                    paramInt1 = paramInt2
                    while (paramInt1 < vCodeLength) {
                        tvList[paramInt1].text = ""
                        paramInt1++
                    }
                }
            }
        })
        this.editText!!.setOnFocusChangeListener { param1View, hasFocus ->
            val str = editText!!.text.toString().trim { it <= ' ' }
            if (hasFocus) {
                if (TextUtils.isEmpty(str)) {
                    tvSetFocus(0)
                } else if (str.length == vCodeLength) {
                    tvSetFocus(vCodeLength - 1)
                } else {
                    tvSetFocus(str.length)
                }
            } else {
                clearsFocus()
            }
        }
    }

    private fun initTextView() {
        val linearLayout = LinearLayout(context)
        addView(linearLayout)
        val layoutParams = linearLayout.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = -1
        layoutParams.height = -2
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER
        for (b in 0 until this.vCodeLength) {
            val textView = TextView(context)
            textView.isAllCaps = false
            linearLayout.addView(textView)
            val layoutParams1 = textView.layoutParams as LinearLayout.LayoutParams
            layoutParams1.width = this.tvWidth
            layoutParams1.height = this.tvHeight
            if (b == this.vCodeLength - 1) {
                layoutParams1.rightMargin = 0
            } else {
                layoutParams1.rightMargin = this.tvMarginRight
            }
            textView.setBackgroundResource(this.tvBgNormal)
            textView.gravity = Gravity.CENTER
            textView.setTextSize(0, this.tvTextSize)
            textView.setTextColor(this.tvTextColor)
            textView.isAllCaps = false
            this.tvList.add(textView)
        }
    }

    //获取键盘触点样式
    fun tvSetFocus(paramInt: Int) {
        tvSetFocus(this.tvList[paramInt])
    }

    private fun tvSetFocus(paramTextView: TextView) {
        for (b in 0 until this.vCodeLength)
            this.tvList[b].setBackgroundResource(this.tvBgNormal)
        paramTextView.setBackgroundResource(this.tvBgFocus)
    }

    fun setInputData(paramString: String) {
        this.inputData = paramString
        this.editText!!.setText(paramString)
        if (TextUtils.isEmpty(paramString)) {
            clearsFocus()
        }
    }
}