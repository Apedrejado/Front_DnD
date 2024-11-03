package com.example.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import character.Attributes
import com.example.myapplication.R
import com.example.myapplication.database.CharacterDatabase
import com.example.myapplication.database.entities.CharacterEntity
import com.example.myapplication.viewmodel.CharacterViewModel
import com.example.myapplication.viewmodel.CharacterViewModelFactory
import main.kotlin.character.Character
import org.example.utils.Calculator

class CharacterSheetActivity : AppCompatActivity() {

    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(CharacterDatabase.getDatabase(application).characterDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charactersheet)

        val calculator = Calculator()
        val character = createCharacterFromIntent(calculator)
        displayCharacterInfo(character, calculator)

        val characterEntity = createCharacterEntity(character, calculator.calculateHitPoints(calculator.calculateModifier(character.baseAttributes.constitution)))
        val characterId = characterEntity.id

        val deleteButton: Button = findViewById(R.id.deleteCharacterButton)
        deleteButton.setOnClickListener {
            if (character.id != -1) {
                Log.d("CharacterSheetActivity", "Tentando excluir o personagem com ID: ${character.id}")

                // Confirmação antes de excluir
                AlertDialog.Builder(this)
                    .setTitle("Confirmar Exclusão")
                    .setMessage("Você realmente deseja excluir o personagem '${character.name}'?")
                    .setPositiveButton("Sim") { dialog, which ->
                        characterViewModel.deleteCharacter(character.id)
                        Log.d("CharacterSheetActivity", "Personagem com ID: ${character.id} excluído com sucesso.")
                        finish() // Finaliza a activity após a exclusão
                    }
                    .setNegativeButton("Não", null)
                    .show()
            } else {
                Log.e("CharacterSheetActivity", "ID do personagem inválido para exclusão. ID: ${character.id}")
            }
        }

        val backButton: Button = findViewById(R.id.buttonBackToMain)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        val alterarButton: Button = findViewById(R.id.buttonAlterar)
        alterarButton.setOnClickListener {
            Log.d("CharacterSheetActivity", "Atributos enviados para o update:" +
                    "\nID: ${character.id}" +
                    "\nNome: ${character.name}" +
                    "\nRaça: ${character.race.raceName}" +
                    "\nClasse: ${character.characterClass.className}" +
                    "\nAlinhamento: ${character.alignment}" +
                    "\nBackground: ${character.background}" +
                    "\nForça: ${character.baseAttributes.strength}" +
                    "\nDestreza: ${character.baseAttributes.dexterity}" +
                    "\nConstituição: ${character.baseAttributes.constitution}" +
                    "\nInteligência: ${character.baseAttributes.intelligence}" +
                    "\nSabedoria: ${character.baseAttributes.wisdom}" +
                    "\nCarisma: ${character.baseAttributes.charisma}")

            val intent = Intent(this, CharacterFormActivity::class.java).apply {
                putExtra("id", character.id)
                putExtra("nome", character.name)
                putExtra("raca", character.race.raceName)
                putExtra("classe", character.characterClass.className)
                putExtra("alinhamento1", character.alignment)
                putExtra("background", character.background)
                putExtra("forca", character.baseAttributes.strength)
                putExtra("destreza", character.baseAttributes.dexterity)
                putExtra("constituicao", character.baseAttributes.constitution)
                putExtra("inteligencia", character.baseAttributes.intelligence)
                putExtra("sabedoria", character.baseAttributes.wisdom)
                putExtra("carisma", character.baseAttributes.charisma)
            }
            startActivity(intent)
        }

    }

    private fun createCharacterFromIntent(calculator: Calculator): Character {
        val name = intent.getStringExtra("nome") ?: "Desconhecido"
        val race = intent.getStringExtra("raca") ?: "Desconhecido"
        val characterClass = intent.getStringExtra("classe") ?: "Desconhecido"
        val alignment1 = intent.getStringExtra("alinhamento1") ?: "Desconhecido"
        val background = intent.getStringExtra("background") ?: "Desconhecido"

        val id = intent.getIntExtra("id", -1)
        Log.d("CharacterSheetActivity", "ID recebido: $id")

        val baseAttributes = Attributes(
            strength = intent.getIntExtra("forca", 0),
            dexterity = intent.getIntExtra("destreza", 0),
            constitution = intent.getIntExtra("constituicao", 0),
            intelligence = intent.getIntExtra("inteligencia", 0),
            wisdom = intent.getIntExtra("sabedoria", 0),
            charisma = intent.getIntExtra("carisma", 0)
        )

        val selectedRace = calculator.getRaceStrategy(race)
        val selectedClass = calculator.getCharacterClass(characterClass)

        return Character(id, name, selectedRace, selectedClass, alignment1, background, baseAttributes)
    }

    private fun displayCharacterInfo(character: Character, calculator: Calculator) {
        findViewById<TextView>(R.id.tvCharacterName).text = "Nome: ${character.name}"
        findViewById<TextView>(R.id.tvCharacterRaceClass).text = "Raça: ${character.race.raceName} | Classe: ${character.characterClass.className}"
        findViewById<TextView>(R.id.tvCharacterAlignment).text = "Alinhamento: ${character.alignment}"
        findViewById<TextView>(R.id.tvCharacterBackground).text = "Background: ${character.background}"

        findViewById<TextView>(R.id.tvStrength).text = "Força: ${character.baseAttributes.strength} (${calculator.calculateModifier(character.baseAttributes.strength)})"
        findViewById<TextView>(R.id.tvDexterity).text = "Destreza: ${character.baseAttributes.dexterity} (${calculator.calculateModifier(character.baseAttributes.dexterity)})"
        findViewById<TextView>(R.id.tvConstitution).text = "Constituição: ${character.baseAttributes.constitution} (${calculator.calculateModifier(character.baseAttributes.constitution)})"
        findViewById<TextView>(R.id.tvIntelligence).text = "Inteligência: ${character.baseAttributes.intelligence} (${calculator.calculateModifier(character.baseAttributes.intelligence)})"
        findViewById<TextView>(R.id.tvWisdom).text = "Sabedoria: ${character.baseAttributes.wisdom} (${calculator.calculateModifier(character.baseAttributes.wisdom)})"
        findViewById<TextView>(R.id.tvCharisma).text = "Carisma: ${character.baseAttributes.charisma} (${calculator.calculateModifier(character.baseAttributes.charisma)})"
        findViewById<TextView>(R.id.tvHitPoints).text = "Pontos de Vida: ${calculator.calculateHitPoints(calculator.calculateModifier(character.baseAttributes.constitution))}"
    }

    private fun createCharacterEntity(character: Character, hitPoints: Int): CharacterEntity {
        return CharacterEntity(
            name = character.name,
            race = character.race.raceName,
            characterClass = character.characterClass.className,
            alignment = character.alignment,
            background = character.background,
            strength = character.baseAttributes.strength,
            dexterity = character.baseAttributes.dexterity,
            constitution = character.baseAttributes.constitution,
            intelligence = character.baseAttributes.intelligence,
            wisdom = character.baseAttributes.wisdom,
            charisma = character.baseAttributes.charisma,
            hitPoints = hitPoints
        )
    }
}
