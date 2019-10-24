package com.customers.zktc.ui.mall.catagory

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.customers.zktc.base.cycle.BaseActivity
import com.customers.zktc.inject.component.ActivityComponent
import com.customers.zktc.ui.mall.catagory.MallCategoryActivity.Companion.category

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/24 11:39
 * Email: 1033144294@qq.com
 */
@Route(path = category)
class MallCategoryActivity : BaseActivity<MallCategoryModel>() {
    companion object {
        const val category = ActivityComponent.Config.zktc + "catagory"
    }
    @JvmField @Autowired var catagory_2: Int = 0
    @JvmField @Autowired var catagory_3: Int = 0
}