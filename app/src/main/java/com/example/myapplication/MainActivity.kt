package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botão para criar personagem
        val buttonToCreateCharacter = findViewById<Button>(R.id.buttonCreateCharacter)



        // Navega para a CharacterFormActivity
        buttonToCreateCharacter.setOnClickListener {
            val intent = Intent(this, CharacterFormActivity::class.java)
            startActivity(intent)
        }


    }
}
