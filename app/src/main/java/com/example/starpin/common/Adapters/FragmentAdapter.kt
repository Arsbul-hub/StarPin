package com.example.starpin.common.Adapters

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
@SuppressLint("StaticFieldLeak")
var last_adapter: Any? = null
class FragmentAdapter(adapter: FragmentActivity, val list_fragments: List<Fragment>): FragmentStateAdapter(adapter) {

    override fun getItemCount(): Int {
        return list_fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return list_fragments[position]
    }

}