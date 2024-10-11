package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import character.Attributes
import org.example.races.Dwarf
import org.example.races.Elf
import org.example.races.RaceStrategy
import org.example.races.Undead
import org.example.utils.Calculator

class SecondActivity : AppCompatActivity() {

    private var forca = 8
    private var destreza = 8
    private var constituicao = 8
    private var inteligencia = 8
    private var sabedoria = 8
    private var carisma = 8

    private var pontosDisponiveis = 27

    private lateinit var valueTextViewForca: TextView
    private lateinit var valueTextViewDestreza: TextView
    private lateinit var valueTextViewConstituicao: TextView
    private lateinit var valueTextViewInteligencia: TextView
    private lateinit var valueTextViewSabedoria: TextView
    private lateinit var valueTextViewCarisma: TextView
    private lateinit var valueTextViewPontosDisponiveis: TextView

    private lateinit var buttonDecrementForca: Button
    private lateinit var buttonIncrementForca: Button
    private lateinit var buttonDecrementDestreza: Button
    private lateinit var buttonIncrementDestreza: Button
    private lateinit var buttonDecrementConstituicao: Button
    private lateinit var buttonIncrementConstituicao: Button
    private lateinit var buttonDecrementInteligencia: Button
    private lateinit var buttonIncrementInteligencia: Button
    private lateinit var buttonDecrementSabedoria: Button
    private lateinit var buttonIncrementSabedoria: Button
    private lateinit var buttonDecrementCarisma: Button
    private lateinit var buttonIncrementCarisma: Button
    private lateinit var buttonConcluir: Button
    private val costTable = mapOf(
        8 to 0, 9 to 1, 10 to 2, 11 to 3, 12 to 4, 13 to 5, 14 to 7, 15 to 9
    )

    private lateinit var selectedRace: RaceStrategy
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val characterName = intent.getStringExtra("CHARACTER_NAME")
        val raceName = intent.getStringExtra("RACE")
        val characterClass = intent.getStringExtra("CLASS")
        val alignment1 = intent.getStringExtra("ALIGNMENT1")
        val background = intent.getStringExtra("BACKGROUND")

        selectedRace = when (raceName) {
            "Elf" -> Elf()
            "Dwarf" -> Dwarf()
            "Undead" -> Undead()
            else -> null
        } ?: throw IllegalArgumentException("Raça inválida")

        val initialAttributes = Attributes(8, 8, 8, 8, 8, 8) // Atributos iniciais


        forca = initialAttributes.strength
        destreza = initialAttributes.dexterity
        constituicao = initialAttributes.constitution
        inteligencia = initialAttributes.intelligence
        sabedoria = initialAttributes.wisdom
        carisma = initialAttributes.charisma

        initializeViews()
        updateAllValues()
        updateAllButtonStates()

        configureListeners()

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("forca", forca )
            resultIntent.putExtra("destreza", destreza)
            resultIntent.putExtra("constituicao", constituicao)
            resultIntent.putExtra("inteligencia", inteligencia)
            resultIntent.putExtra("sabedoria", sabedoria)
            resultIntent.putExtra("carisma", carisma)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        buttonConcluir.setOnClickListener {

            val finalAttributes = Attributes(forca, destreza, constituicao, inteligencia, sabedoria, carisma)

            val modifiedAttributes = selectedRace.applyRaceBonuses(finalAttributes)

            val intent = Intent(this, CharacterSheetActivity::class.java).apply {
                // Envie os atributos modificados com os bônus raciais aplicados.
                putExtra("forca", modifiedAttributes.strength)
                putExtra("destreza", modifiedAttributes.dexterity)
                putExtra("constituicao", modifiedAttributes.constitution)
                putExtra("inteligencia", modifiedAttributes.intelligence)
                putExtra("sabedoria", modifiedAttributes.wisdom)
                putExtra("carisma", modifiedAttributes.charisma)

                putExtra("nome", characterName)
                putExtra("raca", raceName)
                putExtra("classe", characterClass)
                putExtra("alinhamento1", alignment1)
                putExtra("background", background)
            }
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        valueTextViewForca = findViewById(R.id.valueTextViewForca)
        valueTextViewDestreza = findViewById(R.id.valueTextViewDestreza)
        valueTextViewConstituicao = findViewById(R.id.valueTextViewConstituicao)
        valueTextViewInteligencia = findViewById(R.id.valueTextViewInteligencia)
        valueTextViewSabedoria = findViewById(R.id.valueTextViewSabedoria)
        valueTextViewCarisma = findViewById(R.id.valueTextViewCarisma)
        valueTextViewPontosDisponiveis = findViewById(R.id.textViewPontosRestantes)

        buttonDecrementForca = findViewById(R.id.buttonDecrementForca)
        buttonIncrementForca = findViewById(R.id.buttonIncrementForca)
        buttonDecrementDestreza = findViewById(R.id.buttonDecrementDestreza)
        buttonIncrementDestreza = findViewById(R.id.buttonIncrementDestreza)
        buttonDecrementConstituicao = findViewById(R.id.buttonDecrementConstituicao)
        buttonIncrementConstituicao = findViewById(R.id.buttonIncrementConstituicao)
        buttonDecrementInteligencia = findViewById(R.id.buttonDecrementInteligencia)
        buttonIncrementInteligencia = findViewById(R.id.buttonIncrementInteligencia)
        buttonDecrementSabedoria = findViewById(R.id.buttonDecrementSabedoria)
        buttonIncrementSabedoria = findViewById(R.id.buttonIncrementSabedoria)
        buttonDecrementCarisma = findViewById(R.id.buttonDecrementCarisma)
        buttonIncrementCarisma = findViewById(R.id.buttonIncrementCarisma)

        buttonConcluir = findViewById(R.id.buttonConcluir)

        buttonConcluir.isEnabled = false
    }

    private fun configureListeners() {
        buttonDecrementForca.setOnClickListener {
            val currentCost = costTable[forca] ?: 0
            if (forca > 8 + selectedRace.getStrengthBonus()) { // Não permite menos que 8 + bônus de força
                forca--
                val newCost = costTable[forca] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementForca.setOnClickListener {
            val currentCost = costTable[forca] ?: 0
            if (pontosDisponiveis > 0 && forca < 15) { // Máximo permitido de 15
                forca++
                val newCost = costTable[forca] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonDecrementDestreza.setOnClickListener {
            val currentCost = costTable[destreza] ?: 0
            if (destreza > 8 + selectedRace.getDexterityBonus()) { // Não permite menos que 8 + bônus de destreza
                destreza--
                val newCost = costTable[destreza] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementDestreza.setOnClickListener {
            val currentCost = costTable[destreza] ?: 0
            if (pontosDisponiveis > 0 && destreza < 15) { // Máximo permitido de 15
                destreza++
                val newCost = costTable[destreza] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonDecrementConstituicao.setOnClickListener {
            val currentCost = costTable[constituicao] ?: 0
            if (constituicao > 8 + selectedRace.getConstitutionBonus()) { // Não permite menos que 8 + bônus de constituição
                constituicao--
                val newCost = costTable[constituicao] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementConstituicao.setOnClickListener {
            val currentCost = costTable[constituicao] ?: 0
            if (pontosDisponiveis > 0 && constituicao < 15) { // Máximo permitido de 15
                constituicao++
                val newCost = costTable[constituicao] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonDecrementInteligencia.setOnClickListener {
            val currentCost = costTable[inteligencia] ?: 0
            if (inteligencia > 8 + selectedRace.getIntelligenceBonus()) { // Não permite menos que 8 + bônus de inteligência
                inteligencia--
                val newCost = costTable[inteligencia] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementInteligencia.setOnClickListener {
            val currentCost = costTable[inteligencia] ?: 0
            if (pontosDisponiveis > 0 && inteligencia < 15) { // Máximo permitido de 15
                inteligencia++
                val newCost = costTable[inteligencia] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonDecrementSabedoria.setOnClickListener {
            val currentCost = costTable[sabedoria] ?: 0
            if (sabedoria > 8 + selectedRace.getWisdomBonus()) { // Não permite menos que 8 + bônus de sabedoria
                sabedoria--
                val newCost = costTable[sabedoria] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementSabedoria.setOnClickListener {
            val currentCost = costTable[sabedoria] ?: 0
            if (pontosDisponiveis > 0 && sabedoria < 15) { // Máximo permitido de 15
                sabedoria++
                val newCost = costTable[sabedoria] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonDecrementCarisma.setOnClickListener {
            val currentCost = costTable[carisma] ?: 0
            if (carisma > 8 + selectedRace.getCharismaBonus()) { // Não permite menos que 8 + bônus de carisma
                carisma--
                val newCost = costTable[carisma] ?: 0
                pontosDisponiveis += (currentCost - newCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }

        buttonIncrementCarisma.setOnClickListener {
            val currentCost = costTable[carisma] ?: 0
            if (pontosDisponiveis > 0 && carisma < 15) { // Máximo permitido de 15
                carisma++
                val newCost = costTable[carisma] ?: 0
                pontosDisponiveis -= (newCost - currentCost)
                updateAllValues()
                updateAllButtonStates()
            }
        }
    }

    private fun updateAllValues() {
        valueTextViewForca.text = forca.toString()
        valueTextViewDestreza.text = destreza.toString()
        valueTextViewConstituicao.text = constituicao.toString()
        valueTextViewInteligencia.text = inteligencia.toString()
        valueTextViewSabedoria.text = sabedoria.toString()
        valueTextViewCarisma.text = carisma.toString()
        valueTextViewPontosDisponiveis.text = pontosDisponiveis.toString()
    }

        private fun updateAllButtonStates() {
        buttonDecrementForca.isEnabled = forca > 8 + selectedRace.getStrengthBonus()
        buttonIncrementForca.isEnabled = pontosDisponiveis > 0 && forca < 15

        buttonDecrementDestreza.isEnabled = destreza > 8 + selectedRace.getDexterityBonus()
        buttonIncrementDestreza.isEnabled = pontosDisponiveis > 0 && destreza < 15

        buttonDecrementConstituicao.isEnabled = constituicao > 8 + selectedRace.getConstitutionBonus()
        buttonIncrementConstituicao.isEnabled = pontosDisponiveis > 0 && constituicao < 15

        buttonDecrementInteligencia.isEnabled = inteligencia > 8 + selectedRace.getIntelligenceBonus()
        buttonIncrementInteligencia.isEnabled = pontosDisponiveis > 0 && inteligencia < 15

        buttonDecrementSabedoria.isEnabled = sabedoria > 8 + selectedRace.getWisdomBonus()
        buttonIncrementSabedoria.isEnabled = pontosDisponiveis > 0 && sabedoria < 15

        buttonDecrementCarisma.isEnabled = carisma > 8 + selectedRace.getCharismaBonus()
        buttonIncrementCarisma.isEnabled = pontosDisponiveis > 0 && carisma < 15

        buttonConcluir.isEnabled = pontosDisponiveis == 0
    }
}
