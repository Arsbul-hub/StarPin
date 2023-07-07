package com.star_wormwood.bulavka.common.Managers

import android.util.Log
import com.star_wormwood.bulavka.common.Items.PlayList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.User
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type


class DataManager {
    val gson = Gson()
    var commitBuffer = mutableListOf<MutableMap<String, Any>>()
    fun savePlayLists(listType: String, data: MutableList<PlayList>) {
        val editor = User.pref.edit()
//        val fos = FileOutputStream("t.tmp")
//        val oos = ObjectOutputStream(fos)
//
////        oos.writeInt(12345)
////        oos.writeObject("Today")
//        oos.writeObject(data)
//
//        oos.close()

////        for (list in data.values){
//            for (track in list.tracks){
//
//            }
//        }
        editor.putString(listType, gson.toJson(data).toString())
        editor.apply()


        //val file_steam: FileOutputStream = op
    }

    fun getPlayList(listType: String): MutableList<PlayList> {
        var out =
            mutableListOf<PlayList>() //JSONObject(User.pref.getString("PlayLists", ""))
        val data = User.pref.getString(listType, "")
        if (data!!.isNotEmpty()) {
            val json_data = JSONArray(data)

            // if (name == "CreatedPlayLists") {
            for (n in 0 until json_data.length()) {
                var pl = json_data[n]
                out.add(gson.fromJson(pl.toString(), PlayList::class.java))

            }
            //}
        }

        //Log.e("123", gson.fromJson(User.pref.getString("PlayLists", ""), PlayList::class.java).toString())
        return out
    }

    fun getBuffer(): MutableList<MutableMap<Int, Any>> {
        var out: MutableList<MutableMap<Int, Any>> = mutableListOf<MutableMap<Int, Any>>()
        val data = User.pref.getString("commitBuffer", "")
        if (data!!.isNotEmpty()) {
            val json_data = JSONArray(data)
            out =
                gson.fromJson(data, object : TypeToken<MutableList<MutableMap<Int, Any>>>() {}.type)
        }
        Log.e("List", out.toString())

        return out
    }

    fun addToBuffer(type: String, data: Any) {
        val editor = User.pref.edit()
        commitBuffer.add(mutableMapOf(type to data))
        Log.e("Gson", gson.toJson(commitBuffer))
        editor.putString("commitBuffer", gson.toJson(commitBuffer))
        editor.apply()
    }

}

class UserManager {
    companion object {
        const val CREATE_PLAYLIST = "0"
        const val ADD_TRACK_TO_USER_PLAYLIST = "1"
        const val ADD_TRACK_TO_SERVICE_PLAYLIST = "2"
        const val REMOVE_TRACK_FROM_USER_PLAYLIST = "1"
        const val REMOVE_TRACK_FROM_SERVICE_PLAYLIST = "4"
        const val REMOVE_PLAYLIST = "5"

    }

    var servicePlayLists = mutableListOf<PlayList>()
    var createdPlayLists = mutableListOf<PlayList>()

    val dataManager = DataManager()
    fun getCreatedPlaylist(name: String): PlayList? {
        for (playlist in createdPlayLists) {
            if (playlist.name == name) {
                return playlist
            }
        }
        return null
    }

    fun getServicePlaylist(name: String): PlayList? {
        for (playlist in servicePlayLists) {
            if (playlist.name == name) {
                return playlist
            }
        }
        return null
    }

    fun loadUserData() {

        val savedUserPlaylists = dataManager.getPlayList("CreatedPlayLists")
        val savedServicePlaylists = dataManager.getPlayList("ServicePlayLists")
        dataManager.getBuffer()
        if (savedServicePlaylists.isEmpty()) {
            servicePlayLists.add(PlayList(name = "Понравившиеся"))

        } else {
            servicePlayLists = savedServicePlaylists
        }



        createdPlayLists = savedUserPlaylists


    }

    fun addToServicePlayList(list_name: String, track: Track) {

        val playlist = getServicePlaylist(list_name)
        playlist?.add(track)
        dataManager.savePlayLists("ServicePlayLists", servicePlayLists)
        dataManager.addToBuffer(
            ADD_TRACK_TO_SERVICE_PLAYLIST,
            mutableMapOf("list_name" to list_name, "track" to track)
        )




    }

    fun removeFromServicePlayList(list_name: String, track: Track) {

        getServicePlaylist(list_name)?.tracks?.remove(track)


        dataManager.savePlayLists("ServicePlayLists", servicePlayLists)
        dataManager.addToBuffer(
            REMOVE_TRACK_FROM_SERVICE_PLAYLIST,
            mutableMapOf("list_name" to list_name, "track" to track)
        )

    }

    fun addToPlayList(list_name: String, track: Track) {
        val playlist = getCreatedPlaylist(list_name)
        playlist?.add(track)

        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
        dataManager.addToBuffer(
            ADD_TRACK_TO_USER_PLAYLIST,
            mutableMapOf("list_name" to list_name, "track" to track)
        )

    }

    fun createPlayList(list_name: String, tracksList: MutableList<Track> = mutableListOf()) {

        if (getCreatedPlaylist(list_name) == null) {

            createdPlayLists.add(PlayList(name = list_name))
            getCreatedPlaylist(list_name)?.tracks = tracksList
        }

        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
        dataManager.addToBuffer(CREATE_PLAYLIST, list_name)

    }

    fun renamePlayList(new_name: String, current_name: String) {
        getCreatedPlaylist(current_name)?.name = new_name
        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
    }

    fun removePlaylist(list_name: String) {
        if (getCreatedPlaylist(list_name) != null) {

            createdPlayLists.remove(getCreatedPlaylist(list_name))
        }
        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
        dataManager.addToBuffer(REMOVE_PLAYLIST, list_name)


    }

    fun removeFromPlayList(list_name: String, track: Track) {
        getCreatedPlaylist(list_name)?.tracks?.remove(track)

        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
        dataManager.addToBuffer(
            REMOVE_TRACK_FROM_USER_PLAYLIST,
            mutableMapOf("list_name" to list_name, "track" to track)
        )

    }
//    fun addCommit(name: String, )

}