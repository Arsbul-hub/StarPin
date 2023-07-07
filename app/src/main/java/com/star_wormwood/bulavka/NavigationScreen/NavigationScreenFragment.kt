package com.star_wormwood.bulavka.NavigationScreen

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.star_wormwood.bulavka.LikedTracksScreen.LikedTracksFragment
import com.star_wormwood.bulavka.MainScreen.MainScreenFragment
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.Screens
import com.star_wormwood.bulavka.common.Managers.FragmentNavigationManager
import kotlinx.android.synthetic.main.navigation_fragment.view.*


class NavigationScreenFragment( // Весь конструктор нужен для того, чтоб я мог указывать аргументы классов, например при множемтвеннном выделении
    private val mainScreen: Fragment = MainScreenFragment(),
    private val likedScreen: Fragment = LikedTracksFragment(),
    private val selectedItem: Int = R.id.main_sreen
) : Fragment() {
    lateinit var old_item: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.navigation_fragment, container, false)

        val fragmentManager = FragmentNavigationManager(
            view.navigation_view.id,
            requireActivity().supportFragmentManager
        )
        val mapFragments: MutableMap<Int, Fragment> = mutableMapOf(
            R.id.main_sreen to mainScreen,
            R.id.liked_screen to likedScreen
        )

        fragmentManager.goToFragment(mapFragments[selectedItem]!!)
        view.navigation_bar.selectedItemId = selectedItem
        view.navigation_bar.setOnItemSelectedListener {

            fragmentManager.goToFragment(mapFragments[it.itemId]!!)

            return@setOnItemSelectedListener true
        }

        return view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
    }

}