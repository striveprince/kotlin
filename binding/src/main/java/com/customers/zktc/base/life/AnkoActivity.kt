package com.customers.zktc.base.life

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.inter.anko.AnkoParse
import org.jetbrains.anko.AnkoContext

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 11:40
 * Email: 1033144294@qq.com
 */
abstract class AnkoActivity<Model:ViewModel> :AppCompatActivity(),AnkoParse<Model, AnkoContext<Context>>{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(createView(model(),this))
    }

//    abstract fun model():Model

}