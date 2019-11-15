package com.customers.zktc.base.life

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.anko.AnkoParse
import org.jetbrains.anko.AnkoContext

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 11:41
 * Email: 1033144294@qq.com
 */
abstract class AnkoFragment<Model:ViewModel> :Fragment(),AnkoParse<Model,AnkoContext<Context>> {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}