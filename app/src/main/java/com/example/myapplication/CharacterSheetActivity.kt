package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import character.Attributes
import main.kotlin.character.Character // Ajuste do pacote
import org.example.utils.Calculator

class CharacterSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charactersheet)
        val calculator = Calculator()

        // Recuperar os dados enviados pela Intent
        val name = intent.getStringExtra("nome") ?: "Unknown"
        val race = intent.getStringExtra("raca") ?: "Unknown"
        val characterClass = intent.getStringExtra("classe") ?: "Unknown"
        val alignment1 = intent.getStringExtra("alinhamento1") ?: "Unknown"
        val background = intent.getStringExtra("background") ?: "Unknown"

        val strength = intent.getIntExtra("forca", 0)
        val dexterity = intent.getIntExtra("destreza", 0)
        val constitution = intent.getIntExtra("constituicao", 0)
        val intelligence = intent.getIntExtra("inteligencia", 0)
        val wisdom = intent.getIntExtra("sabedoria", 0)
        val charisma = intent.getIntExtra("carisma", 0)

        // Criar o objeto Attributes
        val baseAttributes = Attributes(
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma
        )

        // Usar métodos auxiliares para obter as instâncias de RaceStrategy e CharacterClass
        val selectedRace = calculator.getRaceStrategy(race)
        val selectedClass = calculator.getCharacterClass(characterClass)

        // Criar o objeto Character
        val character = Character(
            name = name,
            race = selectedRace,
            characterClass = selectedClass,
            alignment = alignment1,
            background = background,
            baseAttributes = baseAttributes
        )

        // Calcular os pontos de vida
        val hitPoints = calculator.calculateHitPoints(calculator.calculateModifier(constitution))

        // Exibir os dados básicos do personagem (Nome, Raça, Classe, etc.)
        findViewById<TextView>(R.id.tvCharacterName).text = "Name: ${character.name}"
        findViewById<TextView>(R.id.tvCharacterRaceClass).text = "Race: ${character.race.raceName} | Class: ${character.characterClass.className}"
        findViewById<TextView>(R.id.tvCharacterAlignment).text = "Alignment: ${character.alignment}"
        findViewById<TextView>(R.id.tvCharacterBackground).text = "Background: ${character.background}"

        // Exibir os atributos com os modificadores
        findViewById<TextView>(R.id.tvStrength).text = "Strength: ${character.baseAttributes.strength} (Modifier: ${calculator.calculateModifier(character.baseAttributes.strength)})"
        findViewById<TextView>(R.id.tvDexterity).text = "Dexterity: ${character.baseAttributes.dexterity} (Modifier: ${calculator.calculateModifier(character.baseAttributes.dexterity)})"
        findViewById<TextView>(R.id.tvConstitution).text = "Constitution: ${character.baseAttributes.constitution} (Modifier: ${calculator.calculateModifier(character.baseAttributes.constitution)})"
        findViewById<TextView>(R.id.tvIntelligence).text = "Intelligence: ${character.baseAttributes.intelligence} (Modifier: ${calculator.calculateModifier(character.baseAttributes.intelligence)})"
        findViewById<TextView>(R.id.tvWisdom).text = "Wisdom: ${character.baseAttributes.wisdom} (Modifier: ${calculator.calculateModifier(character.baseAttributes.wisdom)})"
        findViewById<TextView>(R.id.tvCharisma).text = "Charisma: ${character.baseAttributes.charisma} (Modifier: ${calculator.calculateModifier(character.baseAttributes.charisma)})"
        findViewById<TextView>(R.id.tvHitPoints).text = "Hit Points: $hitPoints"
    }
}
