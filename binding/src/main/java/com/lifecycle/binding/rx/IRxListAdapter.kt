package com.lifecycle.binding.rx

import com.lifecycle.binding.IList
import io.reactivex.Observable

interface IRxListAdapter<E> : IList<E, Observable<Any>>