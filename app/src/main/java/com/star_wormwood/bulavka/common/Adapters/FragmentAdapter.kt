package com.star_wormwood.bulavka.common.Adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
@SuppressLint("StaticFieldLeak")
var last_adapter: Any? = null
class FragmentAdapter(adapter: FragmentActivity, val listFragments: List<Fragment>): FragmentStateAdapter(adapter) {

    override fun getItemCount(): Int {
        return listFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragments[position]
    }

}