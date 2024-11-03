package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.database.entities.CharacterEntity
import com.example.myapplication.views.CharacterSheetActivity


class CharacterAdapter(private val context: Context, private val characters: List<CharacterEntity>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val buttonCharacter: Button = itemView.findViewById(R.id.buttonCharacter)

        fun bind(character: CharacterEntity) {
            buttonCharacter.text = character.name
            buttonCharacter.setOnClickListener {
                val intent = Intent(context, CharacterSheetActivity::class.java).apply {
                    putExtra("id", character.id)
                    putExtra("nome", character.name)
                    putExtra("raca", character.race)
                    putExtra("classe", character.characterClass)
                    putExtra("alinhamento1", character.alignment)
                    putExtra("background", character.background)
                    putExtra("forca", character.strength)
                    putExtra("destreza", character.dexterity)
                    putExtra("constituicao", character.constitution)
                    putExtra("inteligencia", character.intelligence)
                    putExtra("sabedoria", character.wisdom)
                    putExtra("carisma", character.charisma)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_list_item, parent, false)
        return CharacterViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return characters.size
    }
}
