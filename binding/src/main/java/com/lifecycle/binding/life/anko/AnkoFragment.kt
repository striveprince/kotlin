package com.lifecycle.binding.life.anko

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lifecycle.binding.life.BaseFragment
import com.lifecycle.binding.inter.anko.AnkoParse
import org.jetbrains.anko.AnkoContext

/**
 * Company:
 * Description:
 * Author: created by ArvinWang on 2019/11/15 11:41
 * Email: 1033144294@qq.com
 */
abstract class AnkoFragment<Model:ViewModel> : BaseFragment<Model, AnkoContext<Context>>(),
    AnkoParse<Model,AnkoContext<Context>>