package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.database.entities.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Query("SELECT * FROM character_table ORDER BY id ASC")
    fun getAllCharacters(): LiveData<List<CharacterEntity>>

    @Query("SELECT * FROM character_table WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity?

    @Query("DELETE FROM character_table WHERE id = :characterId")
    suspend fun deleteCharacter(characterId: Int): Int

    @Query("DELETE FROM character_table")
    suspend fun deleteAllCharacters(): Int

    @Query("UPDATE character_table SET name = :name, race = :race, characterClass = :characterClass, alignment = :alignment, background = :background, strength = :strength, dexterity = :dexterity, constitution = :constitution, intelligence = :intelligence, wisdom = :wisdom, charisma = :charisma, hitPoints = :hitPoints WHERE id = :id")
    suspend fun updateCharacter(
        id: Int,
        name: String,
        race: String,
        characterClass: String,
        alignment: String,
        background: String,
        strength: Int,
        dexterity: Int,
        constitution: Int,
        intelligence: Int,
        wisdom: Int,
        charisma: Int,
        hitPoints: Int
    )


}
