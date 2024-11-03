package com.example.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class CharacterFormActivity : AppCompatActivity() {

    private lateinit var etCharacterName: EditText
    private lateinit var spinnerRace: Spinner
    private lateinit var spinnerClass: Spinner
    private lateinit var spinnerAlignment1: Spinner
    private lateinit var spinnerBackground: Spinner
    private lateinit var btnNext: Button

    private val races = arrayOf("Elf", "Undead", "Dwarf")
    private val classes = arrayOf("Wizard", "Fighter")
    private val alignments = arrayOf(
        "Lawful Good", "Neutral Good", "Chaotic Good",
        "Lawful Neutral", "True Neutral", "Chaotic Neutral",
        "Lawful Evil", "Neutral Evil", "Chaotic Evil"
    )
    private val backgrounds = arrayOf("Noble", "Soldier", "Outlander")
    private var characterId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_form)

        etCharacterName = findViewById(R.id.etCharacterName)
        spinnerRace = findViewById(R.id.spinnerRace)
        spinnerClass = findViewById(R.id.spinnerClass)
        spinnerAlignment1 = findViewById(R.id.spinnerAlignment1)
        spinnerBackground = findViewById(R.id.spinnerBackground)
        btnNext = findViewById(R.id.btnNext)

        setupSpinner(spinnerRace, races)
        setupSpinner(spinnerClass, classes)
        setupSpinner(spinnerAlignment1, alignments)
        setupSpinner(spinnerBackground, backgrounds)

        // Verifica se os dados do personagem foram passados para edição
        characterId = intent.getIntExtra("id", -1).takeIf { it != -1 }
        if (characterId != null) {
            // Preenche os campos com os dados do personagem existente
            etCharacterName.setText(intent.getStringExtra("nome"))

            val race = intent.getStringExtra("raca")
            val className = intent.getStringExtra("classe")
            val alignment = intent.getStringExtra("alinhamento1")
            val background = intent.getStringExtra("background")

            race?.let {
                val position = races.indexOf(it)
                if (position >= 0) spinnerRace.setSelection(position)
            }

            className?.let {
                val position = classes.indexOf(it)
                if (position >= 0) spinnerClass.setSelection(position)
            }

            alignment?.let {
                val position = alignments.indexOf(it)
                if (position >= 0) spinnerAlignment1.setSelection(position)
            }

            background?.let {
                val position = backgrounds.indexOf(it)
                if (position >= 0) spinnerBackground.setSelection(position)
            }
        }

        btnNext.setOnClickListener { onNextButtonClick() }
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun onNextButtonClick() {
        val name = etCharacterName.text.toString()
        val race = spinnerRace.selectedItem.toString()
        val characterClass = spinnerClass.selectedItem.toString()
        val alignment1 = spinnerAlignment1.selectedItem.toString()
        val background = spinnerBackground.selectedItem.toString()

        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("CHARACTER_NAME", name)
            putExtra("RACE", race)
            putExtra("CLASS", characterClass)
            putExtra("ALIGNMENT1", alignment1)
            putExtra("BACKGROUND", background)
            characterId?.let { putExtra("ID", it) }
        }
        startActivity(intent)
    }
}
