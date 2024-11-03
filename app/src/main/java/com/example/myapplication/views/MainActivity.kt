package com.example.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.services.ForegroundService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonToCreateCharacter = findViewById<Button>(R.id.buttonCreateCharacter)
        buttonToCreateCharacter.setOnClickListener {
            val intent = Intent(this, CharacterFormActivity::class.java)
            startActivity(intent)
        }

        val buttonToCharacterList = findViewById<Button>(R.id.buttonShowCharacterList)
        buttonToCharacterList.setOnClickListener {
            val intent = Intent(this, CharacterListActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonStartForegroundService).setOnClickListener {
            val serviceIntent = Intent(this, ForegroundService::class.java)
            startService(serviceIntent)
        }


    }
}
