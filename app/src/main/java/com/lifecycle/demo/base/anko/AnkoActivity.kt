package com.lifecycle.demo.base.anko

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.life.BaseActivity
import com.lifecycle.demo.base.anko.inter.AnkoParse
import org.jetbrains.anko.AnkoContext

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 11:40
 * Email: 1033144294@qq.com
 */
abstract class AnkoActivity<Model : ViewModel> : BaseActivity<Model, AnkoContext<Context>>(),
    AnkoParse<Model, AnkoContext<Context>>