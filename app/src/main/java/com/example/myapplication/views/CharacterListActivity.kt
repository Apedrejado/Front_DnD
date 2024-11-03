package com.example.myapplication.views

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.CharacterAdapter
import com.example.myapplication.database.CharacterDatabase
import com.example.myapplication.viewmodel.CharacterViewModel
import com.example.myapplication.viewmodel.CharacterViewModelFactory

class CharacterListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private lateinit var deleteAllButton: Button // Declaração do botão

    private val characterViewModel: CharacterViewModel by viewModels {
        CharacterViewModelFactory(CharacterDatabase.getDatabase(application).characterDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        // Configurações do RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observa a lista de personagens e atualiza o adapter
        characterViewModel.characters.observe(this) { allCharacters ->
            adapter = CharacterAdapter(this, allCharacters)
            recyclerView.adapter = adapter
        }

        // Configuração do botão "Deletar Todos"
        deleteAllButton = findViewById(R.id.button_delete_all)
        deleteAllButton.setOnClickListener {
            characterViewModel.deleteAllCharacters()
        }
    }
}
