package com.customers.zktc.ui.mall.catagory

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.binding.model.annoation.LayoutView
import com.binding.model.inflate.model.ViewModel
import com.customers.zktc.R
import com.customers.zktc.databinding.ActivityMallCategoryBinding
import com.customers.zktc.inject.qualifier.manager.ActivityFragmentManager
import timber.log.Timber
import javax.inject.Inject

@LayoutView(R.layout.activity_mall_category)
class MallCategoryModel
@Inject constructor(@ActivityFragmentManager private val fragmentManager: FragmentManager) :
    ViewModel<MallCategoryActivity,ActivityMallCategoryBinding>() {
    override fun attachView(savedInstanceState: Bundle?, t: MallCategoryActivity) {
        super.attachView(savedInstanceState, t)
        Timber.i("t.catagory_3 = ${t.catagory_3}")
        Timber.i("t.catagory_2 = ${t.catagory_2}")
    }


}
