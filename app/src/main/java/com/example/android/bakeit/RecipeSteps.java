package com.example.android.bakeit;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

public class RecipeSteps extends AppCompatActivity implements RecipeStepsAdapter.OnRecipleClicked{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        Gson gson=new Gson();
        String recString=getIntent().getStringExtra(MainActivity.RECIPE_JSON);
        Recipe recipe=gson.fromJson(recString,Recipe.class);
        setTitle(recipe.recipeName);
        RecipeFragment recipeFragment=new RecipeFragment();
        recipeFragment.setIngredientsArrayList(recipe.ingredientsList);
        recipeFragment.setSteps(recipe.stepsList);
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(savedInstanceState==null)
        fragmentManager.beginTransaction().add(R.id.recipe_steps,recipeFragment).commit();
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this,"text",Toast.LENGTH_SHORT);
    }
}
