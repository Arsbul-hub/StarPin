package com.star_wormwood.bulavka.SearchScreen


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlaylistListener
import com.star_wormwood.bulavka.NavigationScreen.NavigationScreenFragment
import com.star_wormwood.bulavka.common.Adapters.OnClick
import com.star_wormwood.bulavka.common.Adapters.TrackAdapter
import com.star_wormwood.bulavka.common.Dialogs.PlayListDialog
import com.star_wormwood.bulavka.common.Items.PlayList
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.common.PlaylistListener

import com.star_wormwood.bulavka.common.search_music
import kotlinx.android.synthetic.main.search_fragment.view.*



class SearchScreenFragment(var adapter: TrackAdapter? = null, var selected: Boolean = false) :
    Fragment() {
    lateinit var old_item: View

    var prepearingAdapter = false
    var step = 0
    var start = 0
    var max = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "124")
        val fragment_view = inflater.inflate(R.layout.search_fragment, container, false)
        fragment_view.tracks_list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        //fragment_view.found_status.visibility = View.GONE
        if (adapter != null) {
            adapter = adapter!!
            fragment_view.tracks_list_view.adapter = adapter
        }


        //fragment_view.found_status.visibility = View.GONE
        if (selected) {
            fragment_view.navigation_bar.visibility = View.GONE
            //Managers.musicManager.pause()
            fragment_view.actions_bar.visibility = View.VISIBLE
        }
//        requireActivity().bar.on_new_track = object : OnNewTrackPlay {
//            override fun OnPlay(url: String, item: View) {
//                if (::old_item.isInitialized) {
//                    old_item.loader_bar.visibility = View.INVISIBLE
//                }
//                old_item = item
//
//                item.loader_bar.visibility = View.VISIBLE
//
//                //bar.open_bar(data.name, data.artist, data.avatar, data.url)
//                val t = Thread {
//                    //music_player().play(data.url)
//                    MusicPlayer.play(track_url = url)
//                    playing_state = true
//                    runOnUiThread {
//                        item.loader_bar.visibility = View.INVISIBLE
//
//                    }
//                }
//                t.start()
//                t.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 -> }
//            }
//        }

        fragment_view.back.setOnClickListener {
            Managers.fragmentManager.goToFragment(NavigationScreenFragment())
            fragment_view.tracks_list_view.removeAllViews()
            fragment_view.search_field.setText("")
        }
//        fragment_view.search_field.setOnTouchListener { view, event ->
//            val DRAWABLE_LEFT = 0
//
//            val DRAWABLE_RIGHT = 2
//
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                if (event.getRawX() >= (view.getRight() - fragment_view.search_field.compoundDrawables[DRAWABLE_LEFT].getBounds()
//                        .width())
//                ) {
//
//                    Managers.fragmentManager.goToFragment(NavigationScreenFragment())
//                    fragment_view.list_view.removeAllViews()
//                    fragment_view.search_field.setText("")
//
//                }
//                if (event.getRawX() >= (view.getRight() - fragment_view.search_field.compoundDrawables[DRAWABLE_RIGHT].getBounds()
//                        .width())
//                ) {
//
//                    val imm: InputMethodManager =
//                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(fragment_view.search_field.windowToken, 0)
//
//                    fragment_view.search_field.clearFocus()
//                    search(fragment_view)
//
//                }
//            }
//            fragment_view.search_field.requestFocus()
//
//            return@setOnTouchListener true
//        }

        fragment_view.tracks_list_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {

                    if (!prepearingAdapter && start < max) {
                        prepearingAdapter = true
                        start += step
                        Thread {

                            val d = search_music(fragment_view.search_field.text.toString(), start).first //search_music(view.search_field.text.toString(), context)

                            adapter!!.list = adapter!!.list.plus(d)

                            runOnUiThread {
                                fragment_view.tracks_list_view.adapter!!.notifyDataSetChanged()
                            }
                            prepearingAdapter = false
                        }.start()
                    }
                }
            }
        })
        fragment_view.search_field.setOnKeyListener { view1, keycode, keyevent ->
            if (keyevent.keyCode == KeyEvent.KEYCODE_ENTER) {

                search(fragment_view)


                val imm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(fragment_view.search_field.windowToken, 0)

                fragment_view.search_field.clearFocus()
                return@setOnKeyListener true
            }
            runOnUiThread { fragment_view.found_status.visibility = View.INVISIBLE }
            return@setOnKeyListener false
        }

        fragment_view.actions_bar.setOnMenuItemClickListener {

            if (it.itemId == R.id.open_playlist_choose_window) {
                val d = PlayListDialog(object : PlaylistListener {
                    override fun onClickAdd() {
                        Managers.fragmentManager.goToFragment(
                            EditPlayListFragment(
                                EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
                                    override fun onEdit(name: String) {
                                        Managers.fragmentManager.goToPrevious()

                                    }

                                    override fun onBack() {
                                        Managers.fragmentManager.goToPrevious()
                                    }
                                })
                        )
                    }

                    override fun onClick(playList: PlayList) {
                        for (track in adapter!!.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист ${playList.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            adapter!!.deselectAll()

                        }
                    }


                }).show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.share_music) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND

                val tracks_names: String =
                    adapter!!.getSelectedTracks()
                        .joinToString(separator = "\n", transform = { "${it.artist} - ${it.name}" })

                intent.putExtra(Intent.EXTRA_TEXT, tracks_names)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Поделиться музыкой"))

            }
            true
        }
        fragment_view.actions_bar.setNavigationOnClickListener {

            fragment_view.actions_bar.visibility = View.GONE

            fragment_view.navigation_bar.visibility = View.VISIBLE
            adapter!!.deselectAll()
            selected = false
        }
        return fragment_view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
    }

    fun search(view: View) {

        //view.search_loading.visibility = View.VISIBLE
        view.found_status.visibility = View.GONE
        view.connection_error_search.visibility = View.GONE
        view.tracks_list_view.visibility = View.GONE
        view.search_loading.visibility = View.VISIBLE


        val lt = Thread {
            val out = search_music(view.search_field.text.toString())
            step = out.second

            max = out.third

            if (out.first.isNotEmpty()) {
                runOnUiThread {

                    adapter = TrackAdapter(
                        out.first,
                        object : OnClick {
                            override fun onClickTrack(
                                tracks_list: List<Track>,
                                track: Track
                            ) {
                                Managers.musicManager.play(tracks_list, track)

                                //myDialogFragment.show(manager, "dialog")

                            }

                            override fun onChooseTrack(
                                tracks_list: List<Track>,
                                track: Track
                            ) {
                                view.actions_bar.visibility = View.VISIBLE
                                view.navigation_bar.visibility = View.GONE
                                selected = true
                            }

                            override fun onDeselectTracks(
                                tracks_list: List<Track>,
                                track: Track
                            ) {
                                view.actions_bar.visibility = View.GONE
                                view.navigation_bar.visibility = View.VISIBLE
                                selected = false

                            }
                        })


                    view.tracks_list_view.adapter = adapter


                    view.tracks_list_view.visibility = View.VISIBLE
                }
            } else {
                runOnUiThread { view.found_status.visibility = View.VISIBLE }
            }


            runOnUiThread { view.search_loading.visibility = View.GONE }
        }
        lt.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 ->
            view.connection_error_search.visibility = View.VISIBLE

            view.search_loading.visibility = View.GONE
        }
        lt.start()


    }
}