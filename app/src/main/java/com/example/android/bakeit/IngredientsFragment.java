package com.example.android.bakeit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {
    ArrayList<Ingredients> ingredientsArrayList;
    public static final String ING_LIST="ingredientslist";
    private TextView ingredientsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.ingredient_fragment,container,false);
        return view;

    }

    public void setIngredientsArrayList(ArrayList<Ingredients> ingredientsArrayList) {
        this.ingredientsArrayList = ingredientsArrayList;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null)
            ingredientsArrayList=savedInstanceState.getParcelableArrayList(ING_LIST);
        ListView listView=view.findViewById(R.id.ing_list_view);
        IngredientsListAdapter ingredientsListAdapter=new IngredientsListAdapter(getContext(),R.layout.ingredients_list_item,ingredientsArrayList);
        listView.setAdapter(ingredientsListAdapter);






    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ING_LIST,ingredientsArrayList);
    }
}
