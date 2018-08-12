package com.example.android.bakeit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    ArrayList<Recipe> recipeArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView recipe_name;
        public TextView download_tv;
        public ImageView back_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            recipe_name=itemView.findViewById(R.id.recipe_name);
            download_tv=itemView.findViewById(R.id.download_tv);
            back_image=itemView.findViewById(R.id.recipe_thumbnail);
        }
    }
    private OnItemClicked onClick;
    public interface OnItemClicked{
        void onItemClick(int position);
        void onButtonClicked();
    }

    public RecipeAdapter(ArrayList<Recipe> recipeArrayList) {
        this.recipeArrayList = recipeArrayList;
    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, final int position) {
        Recipe rec=recipeArrayList.get(position);
        holder.recipe_name.setText(rec.recipeName);
        holder.back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
        holder.download_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onButtonClicked();
            }
        });

    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
}
