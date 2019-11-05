package com.binding.model.adapter.recycler

import android.view.View
import android.widget.Checkable
import com.binding.model.containsList
import com.binding.model.inflate.inter.Check
import com.binding.model.inflate.obj.EventType

class RecyclerSelectAdapter<E : Check<*>>
constructor(private val max: Int = Int.MAX_VALUE) : RecyclerAdapter<E>() {

    private val enable = 1
    private val check = 2
    private val select = 3
    val checkList = ArrayList<E>()

    override fun setList(position: Int, es: List<E>, type: Int): Boolean {
        when (type) {
            EventType.select -> for (e in es) select(e, !checkList.contains(e), isPush(e))
            else -> return super.setList(position, es, type)
        }
        return true
    }


    override fun setIEntity(position: Int, e: E, type: Int, view: View?): Boolean {
        return when (type) {
            EventType.select -> select(position, e, view)
            else -> super.setIEntity(position, e, type, view)
        }
    }

    fun select(position: Int, e: E, v: View?): Boolean {
        var p = position
        return if (v != null) {
            if (holderList.isEmpty()) return false
            if (!containsList(p, holderList)) p = 0
            val inE = holderList[p]
            when (inE.getCheckType()) {
                enable -> select(inE, !v.isEnabled, isPush(inE))
                this.check -> if (v is Checkable) {
                    select(inE, (v as Checkable).isChecked, isPush(inE))
                } else false
                select -> select(inE, v.isSelected, isPush(inE))
                else -> select(inE, !checkList.contains(inE), isPush(inE))
            }
        } else select(e, !checkList.contains(e), isPush(e))
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
                inE.check(success)
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
                } else break
            }
        } else
            for (i in checkList.indices.reversed()) {
                val e = checkList[i]
                select(e, false, isPush(e))
            }
    }

}