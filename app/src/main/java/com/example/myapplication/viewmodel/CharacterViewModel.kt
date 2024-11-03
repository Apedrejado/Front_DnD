package com.example.myapplication.viewmodel

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.CharacterDeleteReceiver
import com.example.myapplication.R
import com.example.myapplication.database.CharacterDao
import com.example.myapplication.database.entities.CharacterEntity
import kotlinx.coroutines.launch
import org.example.utils.Calculator

class CharacterViewModel(private val characterDao: CharacterDao) : ViewModel() {

    // LiveData para observar a lista de personagens
    val characters: LiveData<List<CharacterEntity>> = characterDao.getAllCharacters()

    // LiveData para um único personagem
    private val _character = MutableLiveData<CharacterEntity?>()
    val character: LiveData<CharacterEntity?> get() = _character

    private val calculator = Calculator()


    // Método para inserir um personagem
    fun insertCharacter(context: Context, characterEntity: CharacterEntity) {
        viewModelScope.launch {
            try {
                Log.d("CharacterViewModel", "Iniciando inserção do personagem: ${characterEntity.name}")
                val characterId = characterDao.insertCharacter(characterEntity) // Captura o ID gerado
                Log.d("CharacterViewModel", "Personagem inserido com ID: $characterId")
                sendNotification(context, characterId.toInt(), characterEntity.name)
                Log.d("CharacterViewModel", "Notificação enviada com sucesso para o personagem: ${characterEntity.name}")
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao inserir personagem: ${e.message}")
            }
        }
    }


    // Método para obter um personagem por ID
    fun getCharacterById(characterId: Int) {
        viewModelScope.launch {
            try {
                _character.value = characterDao.getCharacterById(characterId) // Define o valor do LiveData
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao obter personagem: ${e.message}")
                _character.value = null // Define como null em caso de erro
            }
        }
    }

    // Método para deletar um personagem por ID
    fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            try {
                characterDao.deleteCharacter(characterId)
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao deletar personagem: ${e.message}")
            }
        }
    }

    // Método para deletar todos os personagens
    fun deleteAllCharacters() {
        viewModelScope.launch {
            try {
                characterDao.deleteAllCharacters()
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao deletar todos os personagens: ${e.message}")
            }
        }
    }


    fun insertCharacterAndGetId(characterEntity: CharacterEntity, callback: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                // Insere o personagem e captura o ID gerado
                val characterId = characterDao.insertCharacter(characterEntity)
                // Executa o callback com o ID gerado
                callback(characterId)
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao inserir personagem: ${e.message}")
            }
        }
    }


    private fun sendNotification(context: Context, characterId: Int, characterName: String?) {
        Log.d("NotificationDebug", "Iniciando envio da notificação para o personagem com ID: $characterId e Nome: $characterName")

        val notificationId = characterId
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (notificationManager != null) {
            Log.d("NotificationDebug", "NotificationManager obtido com sucesso.")

            // Intent para a ação de exclusão
            val deleteIntent = Intent(context, CharacterDeleteReceiver::class.java).apply {
                action = "DELETE_CHARACTER"
                putExtra("character_id", characterId)  // Envia o ID do personagem
                putExtra("notification_id", notificationId)  // Envia o ID da notificação para cancelar
            }
            val deletePendingIntent = PendingIntent.getBroadcast(
                context,
                characterId,
                deleteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Configura a notificação com o botão "Excluir"
            val notificationBuilder = NotificationCompat.Builder(context, "character_creation_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Personagem Criado")
                .setContentText("O personagem '$characterName' foi criado com sucesso!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.circle_button_background, "Excluir", deletePendingIntent)  // Botão de excluir
                .setAutoCancel(true)  // Remove a notificação quando o usuário a tocar
                .setDefaults(Notification.DEFAULT_ALL) // Define os sons e vibrações padrão

            Log.d("NotificationDebug", "Notificação configurada com o botão 'Excluir'. Enviando...")

            notificationManager.notify(notificationId, notificationBuilder.build())
            Log.d("NotificationDebug", "Notificação enviada com sucesso com ID: $notificationId")

        } else {
            Log.e("NotificationDebug", "Falha ao obter NotificationManager.")
        }
    }




            fun updateCharacter(characterEntity: CharacterEntity) {
        viewModelScope.launch {
            val constitutionModifier = calculator.calculateModifier(characterEntity.constitution)
            val hitPoints = calculator.calculateHitPoints(constitutionModifier)

            try {
                characterDao.updateCharacter(
                    id = characterEntity.id,
                    name = characterEntity.name,
                    race = characterEntity.race,
                    characterClass = characterEntity.characterClass,
                    alignment = characterEntity.alignment,
                    background = characterEntity.background,
                    strength = characterEntity.strength,
                    dexterity = characterEntity.dexterity,
                    constitution = characterEntity.constitution,
                    intelligence = characterEntity.intelligence,
                    wisdom = characterEntity.wisdom,
                    charisma = characterEntity.charisma,
                    hitPoints = hitPoints
                )
                Log.d("CharacterViewModel", "Personagem atualizado com sucesso: ${characterEntity.name}")
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Erro ao atualizar personagem: ${e.message}")
            }
        }
    }

}
