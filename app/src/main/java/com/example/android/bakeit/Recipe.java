package com.example.android.bakeit;

import java.util.ArrayList;

public class Recipe {
    ArrayList<Ingredients> ingredientsList;
    int id;
    String recipeName;
    ArrayList<Steps> stepsList;
    int servings;
    String image;

    public Recipe(ArrayList<Ingredients> ingredientsList, int id, String recipeName, ArrayList<Steps> stepsList, int servings, String image) {
        this.ingredientsList = ingredientsList;
        this.id = id;
        this.recipeName = recipeName;
        this.stepsList = stepsList;
        this.servings = servings;
        this.image = image;
    }
}
