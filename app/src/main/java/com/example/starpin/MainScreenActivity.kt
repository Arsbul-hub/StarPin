package com.example.starpin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_activity.*



class MainScreenActivity : AppCompatActivity() {
    lateinit var old_item: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        fragment_view.adapter = FragmentAdapter(this, listOf(MainScreenFragment(), SearchScreenFragment()))
        fragment_view.isUserInputEnabled = false
    }

}