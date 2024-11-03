package com.example.myapplication

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.views.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Inicializa o MediaPlayer com o arquivo de som
        mediaPlayer = MediaPlayer.create(this, R.raw.splash_sound)
        mediaPlayer.start() // Começa a tocar o som

        val textViewName = findViewById<TextView>(R.id.textViewName)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        textViewName.startAnimation(slideUp)

        // Redirecionar para a próxima Activity após a animação
        slideUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Inicie a próxima Activity aqui
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish() // Finaliza a SplashActivity
                mediaPlayer.release() // Libera o MediaPlayer após terminar
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release() // Libera o MediaPlayer se não for mais necessário
        }
    }
}
