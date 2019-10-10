package com.binding.model.inflate

import androidx.databinding.ViewDataBinding
import com.binding.model.adapter.GridInflate

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/10 13:17
 * Email: 1033144294@qq.com
 */
abstract class ViewGridInflate<Binding:ViewDataBinding> :ViewInflate<Binding>(),GridInflate<Binding> {

}