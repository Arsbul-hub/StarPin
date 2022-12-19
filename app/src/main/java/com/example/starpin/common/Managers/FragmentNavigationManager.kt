package com.example.starpin.common.Managers

import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.starpin.R
import com.example.starpin.Screens


class FragmentNavigationManager(
    val containerId: Int,
    val supportFragmentManager: FragmentManager?) {

    lateinit var previousFragment: Fragment
    lateinit var currentFragment: Fragment

    fun goToFragment(fragment: Fragment, animation_enter: Int = R.anim.fade_in, animation_exit: Int = R.anim.fade_out) {

        if (::currentFragment.isInitialized) {
            previousFragment = currentFragment
        }
        val transaction = supportFragmentManager!!.beginTransaction()

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.setCustomAnimations(animation_enter, animation_exit)
        //transaction.addToBackStack(null);
        transaction.replace(containerId, fragment).commit()

        currentFragment = fragment
    }
    fun goToPrevious() {
        val transaction = supportFragmentManager!!.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
        transaction.replace(containerId, previousFragment).commit()
        currentFragment = previousFragment
    }
}