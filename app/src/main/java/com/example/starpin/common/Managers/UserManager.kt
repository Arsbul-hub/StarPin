package com.example.starpin

import com.example.starpin.common.PlayList
import com.google.gson.Gson
import org.json.JSONObject


class DataManager {
    val gson = Gson()
    fun savePlayLists(listType: String, data: MutableMap<String, PlayList>) {
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

    fun getPlayList(listType: String): MutableMap<String, PlayList> {
        var out =
            mutableMapOf<String, PlayList>() //JSONObject(User.pref.getString("PlayLists", ""))
        val data = User.pref.getString(listType, "")
        if (data!!.isNotEmpty()) {
            val json_data = JSONObject(data)

            // if (name == "CreatedPlayLists") {
            for (n in json_data.keys()) {
                out[n] = gson.fromJson(json_data[n].toString(), PlayList::class.java)

            }
            //}
        }

        //Log.e("123", gson.fromJson(User.pref.getString("PlayLists", ""), PlayList::class.java).toString())
        return out
    }
}

class UserManager {
    var servicePlayLists = mutableMapOf<String, PlayList>()
    var createdPlayLists = mutableMapOf<String, PlayList>()

    val dataManager = DataManager()
    fun loadUserData() {
        val savedUserPlaylists = dataManager.getPlayList("CreatedPlayLists")
        val savedServicePlaylists = dataManager.getPlayList("ServicePlayLists")
        if (savedServicePlaylists.isEmpty()) {
            servicePlayLists["Понравившиеся"] = PlayList(name="Понравившиеся")

        } else {
            servicePlayLists = savedServicePlaylists
        }



            createdPlayLists = savedUserPlaylists


    }

    fun addToServicePlayList(list_name: String, track: Track) {


        if (track !in servicePlayLists[list_name]!!.tracks) {
            servicePlayLists[list_name]!!.tracks.add(track)
        }


        dataManager.savePlayLists("ServicePlayLists", servicePlayLists)

    }

    fun removeFromServicePlayList(list_name: String, track: Track) {
        if (track in servicePlayLists[list_name]!!.tracks) {
            servicePlayLists[list_name]!!.tracks.remove(track)
        }

        dataManager.savePlayLists("ServicePlayLists", servicePlayLists)

    }

    fun addToPlayList(list_name: String, track: Track) {

        if (createdPlayLists.contains(list_name)) {
            if (track !in createdPlayLists[list_name]!!.tracks) {
                createdPlayLists[list_name]!!.tracks.add(track)
            }

        } else {
            newPlayList(list_name)
            addToPlayList(list_name, track)

        }
        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)

    }

    fun newPlayList(list_name: String) {

        if (list_name !in createdPlayLists) {

            createdPlayLists[list_name] = PlayList(name = list_name)
        }
        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
    }

    fun removePlaylist(list_name: String) {
        if (list_name in createdPlayLists) {

            createdPlayLists.remove(list_name)
        }
        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)
    }

    fun removeFromPlayList(list_name: String, track: Track) {
        if (track in createdPlayLists[list_name]!!.tracks) {
            createdPlayLists[list_name]!!.tracks.remove(track)
        }

        dataManager.savePlayLists("CreatedPlayLists", createdPlayLists)

    }
}