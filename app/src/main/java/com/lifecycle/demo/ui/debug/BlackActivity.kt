package com.lifecycle.demo.ui.debug

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.lifecycle.binding.inter.bind.annotation.LayoutView
import com.lifecycle.binding.life.binding.data.DataBindingActivity
import com.lifecycle.binding.rx.viewmodel.RxLifeViewModel
import com.lifecycle.binding.util.observer
import com.lifecycle.demo.R
import com.lifecycle.demo.databinding.ActivityBlankBinding

@LayoutView(layout = [R.layout.activity_blank])
class BlackActivity : DataBindingActivity<RxLifeViewModel, ActivityBlankBinding>() {
    val position = MutableLiveData(0)
    private val navController: LiveData<NavController> = MutableLiveData()

    override fun initData(owner: LifecycleOwner, bundle: Bundle?) {
        super.initData(owner, bundle)
        val ids = arrayListOf(R.id.blankFragment, R.id.blankFragment1, R.id.blankFragment2, R.id.blankFragment3, R.id.blankFragment)
        position.observer(owner) {
            binding.frameLayoutBlank.findNavController().navigate(ids[it])
        }
        setup(
            supportFragmentManager, R.id.nav_host_fragment_container,
            intent,
            arrayListOf(R.navigation.home, R.navigation.blank, R.navigation.chat, R.navigation.mine)
        )
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.value?.navigateUp() ?: false
    }

    private fun setup(manager: FragmentManager, containerId: Int, intent: Intent?, ids: ArrayList<Int>) {
        ids.forEachIndexed { index, id ->
            val fragmentTag = getFragmentTag(index)
            val navHostFragment = manager.obtainNavHostFragment(containerId, fragmentTag,id)
        }
    }

    private fun getFragmentTag(index: Int): String {
        return "${index}"
    }

    private fun FragmentManager.obtainNavHostFragment(containerId: Int, fragmentTag: String, id: Int): NavHostFragment {
       return findFragmentByTag(fragmentTag) as? NavHostFragment ?: NavHostFragment.create(id).apply {
            beginTransaction().add(containerId,this,fragmentTag).commitNow()
        }
    }
}
