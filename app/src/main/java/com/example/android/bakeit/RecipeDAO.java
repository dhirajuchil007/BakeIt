package com.example.android.bakeit;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Query("Select * from recipe Order By id")
    List<Recipe> getAllRecipies();

    @Query("select * from recipe where id=:id")
           Recipe getRecipeFromID(int id);

    @Insert
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);
}
