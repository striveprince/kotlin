package com.lifecycle.demo.ui.debug.blank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.lifecycle.demo.R

class BlankFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blank, container, false)?.apply {
            findViewById<TextView>(R.id.hello).setOnClickListener {
                findNavController().navigate(R.id.blankFragment1)
            }
        }
    }
}