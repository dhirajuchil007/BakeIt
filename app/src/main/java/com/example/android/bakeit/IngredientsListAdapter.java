package com.example.android.bakeit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class IngredientsListAdapter extends ArrayAdapter<Ingredients> {
    public IngredientsListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public IngredientsListAdapter(@NonNull Context context, int resource, @NonNull List<Ingredients> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null)
        {
            view=LayoutInflater.from(getContext()).inflate(R.layout.ingredients_list_item,parent,false);
        }
        Ingredients i=getItem(position);
        TextView name=view.findViewById(R.id.ing_name);
        TextView quantity=view.findViewById(R.id.ing_quantity);
        name.setText(i.name);
        if(i.quantity>1)
        quantity.setText(i.quantity+" "+i.measure+"s");
        else
            quantity.setText(i.quantity+" "+i.measure);
        return view;

    }

}
