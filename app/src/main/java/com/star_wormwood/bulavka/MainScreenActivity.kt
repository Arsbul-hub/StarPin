package com.star_wormwood.bulavka


import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.color.DynamicColors
import com.star_wormwood.bulavka.NavigationScreen.NavigationScreenFragment
import com.star_wormwood.bulavka.common.Managers.FragmentNavigationManager
import com.star_wormwood.bulavka.common.Managers.MusicManager
import com.star_wormwood.bulavka.common.Managers.UserManager
import kotlinx.android.synthetic.main.main_activity.*


object User {

    lateinit var pref: SharedPreferences
    val user_manager = UserManager()
}

object Managers {
    @SuppressLint("StaticFieldLeak")
    val musicManager = MusicManager()
    lateinit var fragmentManager: FragmentNavigationManager
}

@SuppressLint("StaticFieldLeak")
object Screens {
    lateinit var activity: MainScreenActivity
    val main_screen = NavigationScreenFragment()


}


object activityObjects {
    lateinit var intent: Intent
}

class MainScreenActivity : AppCompatActivity() {
    lateinit var old_item: View
    var data = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Screens.activity = this

        //
        //Log.e("11222", play_lists.toString())
        User.pref = getSharedPreferences("User", Context.MODE_PRIVATE)
        User.user_manager.loadUserData()
        Managers.fragmentManager =
            FragmentNavigationManager(fragmentView.id, supportFragmentManager)
        Managers.fragmentManager.goToFragment(NavigationScreenFragment())

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                Screens.activity,
                arrayOf(READ_PHONE_STATE),
                111
            )
        }

        Log.e("state123", Managers.musicManager.playingState.toString())
        if (Managers.musicManager.playingState != MusicManager.NOT_PREPARED) {
            Managers.musicManager.updateBar()
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            getPhoneDetail()
//    }


}