package com.example.android.bakeit;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey
    int id;
    String recipeName;
    int servings;
    String image;
    String stepJson;
    String ingredientsJson;
    @Ignore
    ArrayList<Steps> stepsList;
    @Ignore
    ArrayList<Ingredients> ingredientsList;
    @Ignore
    public Recipe(ArrayList<Ingredients> ingredientsList, int id, String recipeName, ArrayList<Steps> stepsList, int servings, String image,String stepJson,String ingredientsJson) {

        this.ingredientsList = ingredientsList;
        this.id = id;
        this.recipeName = recipeName;
        this.stepsList = stepsList;
        this.servings = servings;
        this.image = image;
        this.stepJson=stepJson;
        this.ingredientsJson=ingredientsJson;
    }

    public Recipe(int id, String recipeName, int servings, String image, String stepJson, String ingredientsJson) {
        this.id = id;
        this.recipeName = recipeName;
        this.servings = servings;
        this.image = image;
        this.stepJson = stepJson;
        this.ingredientsJson = ingredientsJson;
    }
    public Recipe(int id,String recipeName,ArrayList<Ingredients> ingredientsList){
        this.id=id;
        this.recipeName=recipeName;
        this.ingredientsList=ingredientsList;
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public String getStepJson() {
        return stepJson;
    }

    public String getIngredientsJson() {
        return ingredientsJson;
    }

    public ArrayList<Steps> getStepsList() {
        return stepsList;
    }

    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStepJson(String stepJson) {
        this.stepJson = stepJson;
    }

    public void setIngredientsJson(String ingredientsJson) {
        this.ingredientsJson = ingredientsJson;
    }

    public void setStepsList(ArrayList<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public void setIngredientsList(ArrayList<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }
}
