package com.example.android.bakeit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class RecipeWidgetAdapter extends ArrayAdapter<Recipe> {
    public RecipeWidgetAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public RecipeWidgetAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view= LayoutInflater.from(getContext()).inflate(R.layout.recipe_widget_conf_list_item,parent,false);
        }
        TextView tv=view.findViewById(R.id.recipe_widget_conf_tv);
        Recipe r=getItem(position);
        Log.d(TAG, "getView: "+r.recipeName);
        tv.setText(r.recipeName);

        return view;

    }
}

