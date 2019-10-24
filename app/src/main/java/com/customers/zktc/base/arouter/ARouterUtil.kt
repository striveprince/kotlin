package com.customers.zktc.base.arouter

import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.binding.model.Config
import com.customers.zktc.R
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.home.HomeActivity
import com.customers.zktc.ui.user.sign.SignActivity
import com.customers.zktc.ui.user.sign.login.LoginFragment
import timber.log.Timber

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/9 14:45
 * Email: 1033144294@qq.com
 */
object ARouterUtil {
    private fun build(path:String,bundle: Bundle=Bundle()):Postcard{
        return ARouter.getInstance()
            .build(path)
            .withTransition(R.anim.push_right_in,R.anim.push_left_out)
            .with(bundle)
    }

    fun login(path:String = LoginFragment.login){
        build(SignActivity.sign)
            .withString(Constant.path,path)
            .navigation()
    }

    fun start() {
        build(HomeActivity.home)
            .navigation()
    }

    private fun convert(path: String):Uri{
        val uri = Uri.parse(path.replace("com.tongchuang.budai://","zktc://com.tongchuang.budai/zktc/"))
        Timber.i("${uri.scheme}://${uri.host}${uri.path}?${uri.encodedQuery}")
        return uri
    }

    fun homeNavigation(path: String, title: String){
        ARouter.getInstance()
            .build(convert(path))
            .withTransition(R.anim.push_right_in,R.anim.push_left_out)
            .withString(Config.title,title)
            .navigation()
    }
}