package com.lifecycle.binding.inter.inflate

import androidx.recyclerview.widget.RecyclerView

interface Recycler:Attach<RecyclerView.ViewHolder>,Detached<RecyclerView.ViewHolder>{

}