package com.star_wormwood.bulavka

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.star_wormwood.bulavka.common.Adapters.PlayListAdapter


//import kotlinx.android.synthetic.main.play_list_fragment.view.*


class PlayListsFragment : Fragment() {

    lateinit var fragment_view: View

    var set_name = ""
    lateinit var current_adapter: PlayListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        fragment_view = inflater.inflate(R.layout.liked_fragment, container, false)
//        fragment_view.navigation_bar.title.text = "Плейлисты"
//        fragment_view.list_view.addItemDecoration(VerticalSpaceItemDecoration(20))
//        fragment_view.list_view.layoutManager =
//            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
//
//        if (User.user_manager.createdPlayLists.isNotEmpty()) {
//            fragment_view.empty_status.visibility = View.GONE
//            fragment_view.list_view.adapter = PlayListAdapter(
//                User.user_manager.createdPlayLists.values.toMutableList(),
//                object : onClickList {
//                    override fun onClick(playList: PlayList) {
//                        Log.e("play", playList.toString())
//                        Managers.fragmentManager.goToFragment(TracksFragment(playList))
//
//                    }
//
//                },
//                true
//            )
//        } else {
//            fragment_view.empty_status.visibility = View.VISIBLE
//        }
//        fragment_view.add.visibility = View.VISIBLE
//        fragment_view.add.setOnClickListener {
//            Managers.fragmentManager.goToFragment(
//                com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment(
//                    com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment.MODE_CREATE
//                ) {
//                    Managers.fragmentManager.goToFragment(PlayListsFragment())
//                })
//        }
//        fragment_view.back.setOnClickListener {
//
//            Managers.fragmentManager.goToFragment(NavigationScreenFragment())
//
//        }


        return fragment_view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        enterTransition = inflater.inflateTransition(R.transition.fade)
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)

    }
}