package com.lifecycle.rx

import com.lifecycle.binding.IList
import io.reactivex.Observable

interface IListAdapter<E> : IList<E, Observable<Any>>