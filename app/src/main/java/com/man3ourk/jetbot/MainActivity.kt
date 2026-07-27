package com.man3ourk.jetbot

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textStatus = findViewById<TextView>(R.id.textStatus)
        textStatus.text = getString(R.string.app_ready)
    }
}
