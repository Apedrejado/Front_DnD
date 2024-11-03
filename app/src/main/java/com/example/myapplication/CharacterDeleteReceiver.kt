package com.example.myapplication

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.CharacterDatabase
import com.example.myapplication.viewmodel.CharacterViewModel
import com.example.myapplication.viewmodel.CharacterViewModelFactory

class CharacterDeleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val characterId = intent?.getLongExtra("character_id", -1L)
        val notificationId = intent?.getIntExtra("notification_id", -1)

        if (characterId != null && characterId != -1L && notificationId != null && notificationId != -1) {
            // Obtenha o CharacterDao a partir do CharacterDatabase
            val characterDao = CharacterDatabase.getDatabase(context).characterDao()

            // Crie a ViewModelFactory
            val factory = CharacterViewModelFactory(characterDao)

            // Use o contexto da aplicação para obter a ViewModel
            val viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application).create(CharacterViewModel::class.java)

            // Chame o método de exclusão
            viewModel.deleteCharacter(characterId.toInt())

            // Remova a notificação
            NotificationManagerCompat.from(context).cancel(notificationId)
            Log.d("CharacterDeleteReceiver", "Notificação cancelada com ID: $notificationId e personagem deletado com ID: $characterId")
        } else {
            Log.e("CharacterDeleteReceiver", "ID do personagem ou da notificação inválido.")
        }
    }
}
