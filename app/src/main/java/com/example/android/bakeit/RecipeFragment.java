package com.example.android.bakeit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.android.bakeit.MainActivity.TAG;

public class RecipeFragment extends Fragment implements RecipeStepsAdapter.OnRecipleClicked {
ArrayList<Steps> steps;
ArrayList<Ingredients> ingredientsArrayList;
public static final String BACK_TAG="tag";
public static final String LIST_STEPS="steplist";
    Bundle bundle;
private boolean mTwoPane;

    public RecipeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.recipe_fragment,container,false);
        Log.d(TAG, "onCreateView: ");
         bundle=getArguments();
        mTwoPane=bundle.getBoolean(RecipeSteps.TWO_PANE);


        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());

        RecyclerView recipeSteps=(RecyclerView)view.findViewById(R.id.recipe_recycler_view);
        recipeSteps.setLayoutManager(layoutManager);
        if(savedInstanceState!=null)
            steps=savedInstanceState.getParcelableArrayList(LIST_STEPS);
        RecipeStepsAdapter recipeStepsAdapter=new RecipeStepsAdapter(steps,getContext());

        recipeSteps.setAdapter(recipeStepsAdapter);
        recipeStepsAdapter.setOnRecipleClicked(RecipeFragment.this);


    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
        Log.d(TAG, "setSteps: "+steps.get(1).description);
    }

    public void setIngredientsArrayList(ArrayList<Ingredients> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
    }

    @Override
    public void onClick(int position) {
        //Toast.makeText(getContext(),"test",Toast.LENGTH_SHORT).show();

        StepsFragment stepsFragment=new StepsFragment();
        stepsFragment.setVideoLink(steps.get(position).videoUrl);
        stepsFragment.setLongDesc(steps.get(position).description);
        stepsFragment.setId(steps.get(position).id);
        stepsFragment.setArguments(bundle);
        FragmentManager fragmentManager=getFragmentManager();
        if(mTwoPane)
            fragmentManager.beginTransaction().replace(R.id.step_viewer,stepsFragment).commit();
        else
        fragmentManager.beginTransaction().replace(R.id.recipe_steps,stepsFragment).addToBackStack(BACK_TAG).commit();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STEPS,steps);

    }
}
