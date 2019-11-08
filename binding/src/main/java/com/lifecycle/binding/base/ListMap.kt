package com.lifecycle.binding.base

class ListMap<K, V> : HashMap<K, ArrayList<V>>() {
    fun put(key: K, value: V) {
        val list = get(key)
        if (list == null) {
            put(key, arrayListOf(value))
        } else {
            list.add(value)
        }
    }

    fun remove(key: K, value: V) {
        val list = get(key)
        if (list == null) {
            remove(key)
        } else {
            list.remove(value)
            if(list.isEmpty())remove(key)
        }
    }
}