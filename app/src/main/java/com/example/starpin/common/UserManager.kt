package com.example.starpin

import android.util.Log
import com.example.starpin.common.PlayList


class DataManager {

}

class UserManager {

    var play_lists = mutableMapOf<String, PlayList>()
    val data_manager = DataManager()
    init {

        newPlayList("Понравившиеся")
        Log.e("123", "123")
    }

    fun addToPlayList(list_name: String, track: Track) {
        if (play_lists.contains(list_name)) {
            play_lists[list_name]!!.tracks.add(track)

        } else {
            newPlayList(list_name)
            addToPlayList(list_name, track)

        }
    }

    fun newPlayList(list_name: String) {

        if (!play_lists.contains(list_name)) {

            play_lists[list_name] = PlayList(name = list_name)
        }
    }
    fun deleteFromPlayList(list_name: String, track: Track){
        if (play_lists[list_name]!!.tracks.contains(track)){
            play_lists[list_name]!!.tracks.remove(track)
        }

    }
}