package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.SecondActivity

class CharacterFormActivity : AppCompatActivity() {

    private lateinit var etCharacterName: EditText
    private lateinit var spinnerRace: Spinner
    private lateinit var spinnerClass: Spinner
    private lateinit var spinnerAlignment1: Spinner
    private lateinit var spinnerAlignment2: Spinner
    private lateinit var spinnerBackground: Spinner
    private lateinit var btnNext: Button

    private val races = arrayOf("Elf", "Undead", "Dwarf")
    private val classes = arrayOf("Wizard", "Warrior")
    private val alignments = arrayOf(
        "Lawful Good", "Neutral Good", "Chaotic Good",
        "Lawful Neutral", "True Neutral", "Chaotic Neutral",
        "Lawful Evil", "Neutral Evil", "Chaotic Evil"
    )
    private val backgrounds = arrayOf("Noble", "Soldier", "Outlander")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_form)

        etCharacterName = findViewById(R.id.etCharacterName)
        spinnerRace = findViewById(R.id.spinnerRace)
        spinnerClass = findViewById(R.id.spinnerClass)
        spinnerAlignment1 = findViewById(R.id.spinnerAlignment1)

        spinnerBackground = findViewById(R.id.spinnerBackground)
        btnNext = findViewById(R.id.btnNext)

        // Configurando os adaptadores dos spinners
        setupSpinner(spinnerRace, races)
        setupSpinner(spinnerClass, classes)
        setupSpinner(spinnerAlignment1, alignments)
        setupSpinner(spinnerBackground, backgrounds)

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

        // Passar dados para a SecondActivity
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("CHARACTER_NAME", name)
            putExtra("RACE", race)
            putExtra("CLASS", characterClass)
            putExtra("ALIGNMENT1", alignment1)

            putExtra("BACKGROUND", background)
        }
        startActivity(intent)
    }
}
