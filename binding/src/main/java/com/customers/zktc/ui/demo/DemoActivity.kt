package com.customers.zktc.ui.demo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lifecycle.binding.inter.anko.AnkoParse
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.verticalLayout

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/11/15 9:51
 * Email: 1033144294@qq.com
 */
class DemoActivity : AppCompatActivity(), AnkoParse<DemoModel, AnkoContext<Context>> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val demoModel = getModel()

        setContentView(createView(demoModel,this))
    }

    private fun getModel() = ViewModelProviders.of(this)[DemoModel::class.java]

    override fun parse(t: DemoModel, context: Context): AnkoContext<Context> {
        return AnkoContext.create(this).apply {
            verticalLayout {
                button {
                    t.name.observe(this@DemoActivity, Observer{ text = it })
                    onClick{ t.click()}
                }
            }
        }
    }

}

