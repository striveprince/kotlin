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
        handler.postDelayed(this, 50)
        TimeUtil.handler = handler
    }

    override fun run() {
        for (timeEntity in hashMap.keys) {
            if (hashMap[timeEntity]!!) timeEntity.getTurn()
        }
        val duration = 1000 - System.currentTimeMillis() % 1000
        handler.postDelayed(this, duration)
    }



    companion object {
        private val instance=TimeUtil()
        fun destroy() {
            hashMap.clear()
            handler.removeCallbacksAndMessages(null)
        }
        lateinit var handler :Handler
        fun switching(timeEntity: TimeEntity, state: Int) {
            val b = state==0
            if (b xor hashMap[timeEntity]!!) {
                if (b) hashMap.put(timeEntity, true)
                else hashMap.put(timeEntity, false)
            }
        }

        private val hashMap = HashMap<TimeEntity, Boolean>()

        fun add(timeEntity: TimeEntity) {
            hashMap[timeEntity] = true
        }

        fun remove(timeEntity: TimeEntity?) {
            if (timeEntity == null) return
            handler.postDelayed({ hashMap.remove(timeEntity) }, 400)
        }

    }
}
