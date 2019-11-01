package com.customers.zktc.ui.user.sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.launcher.ARouter
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.binding.model.subscribeNormal
import com.customers.zktc.R
import com.customers.zktc.base.cycle.BaseFragment
import com.customers.zktc.databinding.ActivitySignBinding
import com.customers.zktc.inject.data.preference.user.UserEntity
import com.customers.zktc.inject.qualifier.manager.ActivityFragmentManager
import com.customers.zktc.ui.Constant
import com.customers.zktc.ui.loginEvent
import com.customers.zktc.ui.user.sign.SignParams
import com.customers.zktc.ui.receiveSignEvent
import javax.inject.Inject

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/12 11:44
 * Email: 1033144294@qq.com
 */
@LayoutView(layout = [R.layout.activity_sign])
class SignModel
@Inject constructor(@ActivityFragmentManager private val fragmentManager: FragmentManager) :
    ViewModel<SignActivity, ActivitySignBinding>() {
    private var currentPath: String = ""

    override fun attachView(savedInstanceState: Bundle?, t: SignActivity) {
        super.attachView(savedInstanceState, t)
        showFragment(t.path)
        receiveSignEvent().subscribeNormal(t, { showFragment(it.path, it.signParams) })
    }

    private fun getFragment(path: String): BaseFragment<*> {
        return ARouter.getInstance().build(path).navigation() as BaseFragment<*>
    }

    fun onCloseClick(v: View) {
        onBackPress()
    }




    private fun showFragment(path: String, signParams: SignParams = SignParams()) {
        val fragment = getFragment(path)
        val ft = fragmentManager.beginTransaction()
        val beforeFragment = fragmentManager.findFragmentByTag(currentPath) as BaseFragment<*>?
        beforeFragment?.let { ft.hide(it) }
        if (!fragment.isAdded) ft.add(R.id.frameLayout, fragment, path)
        val bundle = Bundle()
        bundle.putParcelable(Constant.params, signParams)
        fragment.arguments = bundle
        ft.show(fragment)
        ft.commitAllowingStateLoss()
        currentPath = path
    }
}