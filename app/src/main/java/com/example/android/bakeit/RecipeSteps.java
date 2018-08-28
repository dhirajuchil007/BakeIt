package com.example.android.bakeit;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

public class RecipeSteps extends AppCompatActivity implements RecipeStepsAdapter.OnRecipleClicked{
private boolean mTwoPane=false;
public static final String TAG="RecipeSteps";
public static final String TWO_PANE="twopane";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        if(findViewById(R.id.tab_linear_layout)!=null)
        {
            mTwoPane=true;
        }
        Bundle bundle=new Bundle();
        bundle.putBoolean(TWO_PANE,mTwoPane);
        Gson gson=new Gson();
        String recString=getIntent().getStringExtra(MainActivity.RECIPE_JSON);
        Recipe recipe=gson.fromJson(recString,Recipe.class);
        setTitle(recipe.recipeName);
        RecipeFragment recipeFragment=new RecipeFragment();
        recipeFragment.setIngredientsArrayList(recipe.ingredientsList);
        recipeFragment.setSteps(recipe.stepsList);
        recipeFragment.setArguments(bundle);
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(savedInstanceState==null)
        fragmentManager.beginTransaction().add(R.id.recipe_steps,recipeFragment).commit();


        if(mTwoPane)
        {
            Log.d(TAG, "onCreate: recipestep"+recipe.stepsList.get(0).description);
            StepsFragment stepsFragment=new StepsFragment();
            stepsFragment.setId(0);
            stepsFragment.setLongDesc(recipe.stepsList.get(0).description);
            stepsFragment.setVideoLink(recipe.stepsList.get(0).videoUrl);

            stepsFragment.setArguments(bundle);
            stepsFragment.setStepList(recipe.stepsList);
            fragmentManager.beginTransaction().add(R.id.step_viewer,stepsFragment).commit();


        }
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(this,"text",Toast.LENGTH_SHORT);
    }

}
