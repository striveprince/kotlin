package com.lifecycle.binding.inflate

import androidx.databinding.ViewDataBinding
import com.lifecycle.binding.inflate.inter.Entity

/**
 * Company: 中科同创
 * Description:
 * Author: created by ArvinWang on 2019/10/30 11:12
 * Email: 1033144294@qq.com
 */
open class ViewEntity<Bean,Binding:ViewDataBinding>(override val bean: Bean):Entity<Bean,Binding>,ViewInflate<Binding>()