package com.example.starpin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(adapter: FragmentActivity, val list_fragments: List<Fragment>): FragmentStateAdapter(adapter) {
    override fun getItemCount(): Int {
        return list_fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return list_fragments[position]
    }
}