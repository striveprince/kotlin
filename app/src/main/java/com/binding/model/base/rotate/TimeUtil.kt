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


class TimeUtil: Runnable {
    private val handler = Handler()
    init {
        handler.postDelayed(this, 200)
        TimeUtil.handler = handler
    }

    override fun run() {
        for (timeEntity in hashMap.keys) {
            if (hashMap[timeEntity]!!) timeEntity.getTurn()
        }
        val duration = 1000 - System.currentTimeMillis() % 1000
        //        Timber.i("current time:%1d,duration:%2d",System.currentTimeMillis(),duration);
        handler.postDelayed(this, duration)
    }


    fun switching(timeEntity: TimeEntity, state: Int) {
        if ((state == 0) xor hashMap[timeEntity]!!) {
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
        fun destroy() {
            hashMap.clear()
            handler.removeCallbacksAndMessages(null)
        }
        lateinit var handler :Handler
        val instance = TimeUtil()
        private val hashMap = HashMap<TimeEntity, Boolean>()

        fun add(timeEntity: TimeEntity) {
            hashMap[timeEntity] = true
        }
    }
}
