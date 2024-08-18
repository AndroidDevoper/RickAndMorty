package com.example.rickandmorty.data.remote.favorite

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: FavoriteCharacter)

    @Delete
    suspend fun delete(character: FavoriteCharacter)

    @Query("SELECT * FROM favorite_characters")
    fun getAllFavorites(): List<FavoriteCharacter>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_characters WHERE id = :characterId)")
    suspend fun isFavorite(characterId: Int): Boolean
}