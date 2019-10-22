package com.binding.model.adapter.recycler

import android.view.View
import android.widget.Checkable
import androidx.databinding.ViewDataBinding
import com.binding.model.containsList
import com.binding.model.inflate.inter.Recycler
import com.binding.model.inflate.obj.EventType

class RecyclerSelectAdapter<E : Recycler<in ViewDataBinding>>
constructor(val max: Int = Int.MAX_VALUE) : RecyclerAdapter<E>() {

    private val ENABLE = 1
    private val CHECK = 2
    private val SELECT = 3
    val checkList = ArrayList<E>()

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        when (type) {
            EventType.select -> for (e in es) select(e, !checkList.contains(e), isPush(e))
            else -> return super.setList(position, es, type)
        }
        return true
    }


    override fun setIEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        when (type) {
            EventType.select -> return select(position, e, view)
            else -> return super.setIEntity(position, e, type, view)
        }
    }

    fun select(position: Int, e: E, v: View?): Boolean {
        var position = position
        var e = e
        if (v != null) {
            if (holderList.isEmpty()) return false
            if (!containsList(position, holderList)) position = 0
            e = holderList[position]
            when (e.getCheckType()) {
                ENABLE -> return select(e, !v.isEnabled, isPush(e))
                CHECK -> return if (v is Checkable) {
                    select(e, (v as Checkable).isChecked, isPush(e))
                } else
                    false
                SELECT -> return select(e, v.isSelected, isPush(e))
                else -> return select(e, !checkList.contains(e), isPush(e))
            }
        } else
            return select(e, !checkList.contains(e), isPush(e))
    }

    private fun isPush(e: E): Boolean {
        val status = e.checkWay() and 1
        return status == 1
    }

    private fun select(inE: E, check: Boolean, push: Boolean): Boolean {
        var success = true
        if (!check) {
            if (!isTakeBack(inE))
                inE.check(true)
            else {
                inE.check(false)
                checkList.remove(inE)
            }
        } else {
            if (push) {
                inE.check(true)
                checkList.add(inE)
                while (checkList.size > max) {
                    val out = checkList.removeAt(0)
                    out.check(false)
                }
            } else {
                success = add(inE)
                if (success)
                    inE.check(true)
                else
                    inE.check(false)
            }

        }
        return success
    }

    private fun add(inE: E): Boolean {
        val success = checkList.size < max
        return success && checkList.add(inE)
    }


    private fun isTakeBack(e: E): Boolean {
        val status = e.checkWay() shr 1 and 1
        return status == 1
    }


    fun check(position: Int, check: Boolean) {
        if (containsList(position, holderList)) {
            val e = holderList[position]
            select(e, check, isPush(e))
        }
    }

    fun checkAll(check: Boolean) {
        if (check) {
            for (e in holderList) {
                if (checkList.size < max) {
                    select(e, true, isPush(e))
                } else
                    break
            }
        } else
            for (i in checkList.indices.reversed()) {
                val e = checkList[i]
                select(e, false, isPush(e))
            }
    }

}