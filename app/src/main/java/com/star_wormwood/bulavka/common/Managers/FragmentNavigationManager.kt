package com.star_wormwood.bulavka.common.Managers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.star_wormwood.bulavka.R


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
        transaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_up);
        transaction.replace(containerId, previousFragment).commit()
        currentFragment = previousFragment
    }
}