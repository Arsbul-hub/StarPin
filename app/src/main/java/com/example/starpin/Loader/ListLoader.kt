package com.example.starpin.Loader



import android.app.Activity
import android.view.View
import com.example.starpin.*
import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.list_fragment.view.*
import kotlinx.android.synthetic.main.main_activity.*


class UserPlayLists{
    fun load(fragment_view: View, activity: Activity){
        fragment_view.loading.visibility = View.VISIBLE
        fragment_view.list_view.visibility = View.GONE
        fragment_view.connection_error.visibility = View.GONE
        screens.list_screen.load_list = object : Load {
            override fun load_list_items(fragment_view: View) {
                UserPlayLists().load(fragment_view, activity)

            }

        }




        fragment_view.list_view.adapter =
            PlayListAdapter(ArrayList(User.user_manager.play_lists.values), object : OnClickList {
                override fun onOpen(playList: PlayList) {
                    TracksInPlayLists().load(fragment_view, activity, playList)
                    //requireActivity().fragment_view.setCurrentItem(4, true)
                }

            })
        fragment_view.list_view.visibility = View.VISIBLE
        fragment_view.loading.visibility = View.GONE

    }

}
class TracksInPlayLists {
    fun load(fragment_view: View, activity: Activity, playList: PlayList){

        fragment_view.loading.visibility = View.VISIBLE
        fragment_view.list_view.visibility = View.GONE
        fragment_view.connection_error.visibility = View.GONE

        screens.list_screen.load_list = object : Load {
            override fun load_list_items(fragment_view: View) {
                TracksInPlayLists().load(fragment_view, activity, playList)

            }

        }
        val lt = Thread {

            activity.runOnUiThread {
                fragment_view.list_view.adapter = TrackAdapter(playList.tracks.toMutableList(), object : OnClick {
                    override fun onClickTrack(
                        tracks_list: MutableList<Track>,
                        track_index: Int,
                        item: View
                    ) {
                        activity.bar.open(tracks_list, track_index, item)
                    }

                })
                fragment_view.list_view.visibility = View.VISIBLE
                fragment_view.loading.visibility = View.GONE

            }
        }

        lt.start()
    }
}
class TracksInTop {
    fun load(fragment_view: View, activity: Activity){
        fragment_view.loading.visibility = View.VISIBLE
        fragment_view.list_view.visibility = View.GONE
        fragment_view.connection_error.visibility = View.GONE
        screens.list_screen.load_list = object : Load {
            override fun load_list_items(fragment_view: View) {
                TracksInTop().load(fragment_view, activity)

            }

        }

        val lt = Thread {
            val p = get_top().toMutableList()
            activity.runOnUiThread {
                fragment_view.list_view.adapter = TrackAdapter(p, object : OnClick {
                    override fun onClickTrack(
                        tracks_list: MutableList<Track>,
                        track_index: Int,
                        item: View
                    ) {
                        activity.bar.open(tracks_list, track_index, item)
                    }

                })
                fragment_view.list_view.visibility = View.VISIBLE
                fragment_view.loading.visibility = View.GONE

            }
        }
        lt.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 ->
            fragment_view.connection_error.visibility = View.VISIBLE
            fragment_view.loading.visibility = View.GONE
        }
        lt.start()


    }
}