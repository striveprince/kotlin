package com.binding.model.base.rotate

import android.os.Handler

import java.util.HashMap

/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：11:04
 * modify developer：  admin
 * modify time：11:04
 * modify remark：
 *
 * @version 2.0
 */


class TimeUtil private constructor() : Runnable {

    init {
        handler.postDelayed(this, 1000)
    }

    override fun run() {
        for (timeEntity in hashMap.keys) {
            if (hashMap[timeEntity]!!) timeEntity.getTurn()
        }
        val duration = 1000 - System.currentTimeMillis() % 1000
        //        Timber.i("current time:%1d,duration:%2d",System.currentTimeMillis(),duration);
        handler.postDelayed(this, duration)
    }

    fun add(timeEntity: TimeEntity) {
        hashMap[timeEntity] = true
    }

    fun switching(timeEntity: TimeEntity, state: Int) {
        if ((state == 0) xor hashMap[timeEntity]!!) {
            val b =
                if (state == 0) hashMap.put(timeEntity, true) else hashMap.put(timeEntity, false)
        }
    }

    fun remove(timeEntity: TimeEntity?) {
        if (timeEntity == null) return
        handler.postDelayed({ hashMap.remove(timeEntity) }, 400)
    }

    fun destroy() {
        hashMap.clear()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val handler = Handler()
        private val hashMap = HashMap<TimeEntity, Boolean>()
        val instance = TimeUtil()
    }
}
