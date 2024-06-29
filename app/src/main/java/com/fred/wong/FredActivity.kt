// 301199984

package com.fred.wong

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setting the title to Hands-On Test 1 COMP-304 401 instead of app name (FredWong_COMP304_001_Hands-On_Test1_S24)
        // since app name title is too long and gets cut off
        supportActionBar?.title = getString(R.string.actionBar_main_title_truncated)

        // Retrieve both the ImageButton and Button controls
        val imageButtonMain: ImageButton = findViewById(R.id.imageButtonMain)
        val buttonMain: Button = findViewById(R.id.buttonMain)

        // Set OnClickListeners for both button types so second activity can be reached regardless of user choice
        imageButtonMain.setOnClickListener {
            val intent = Intent(this, WongActivity::class.java)
            startActivity(intent)
        }
        buttonMain.setOnClickListener {
            val intent = Intent(this, WongActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.overall_menu, menu)
        return true
    }
}