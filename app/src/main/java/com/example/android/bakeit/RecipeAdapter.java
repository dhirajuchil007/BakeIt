package com.example.android.bakeit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.android.bakeit.RecipeSteps.TAG;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    ArrayList<Recipe> recipeArrayList;
    Context mContext;
    List<Recipe>temp;
    SharedPreferences sharedPreferences;

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
        void onButtonClicked(Recipe recipe);
    }

    public RecipeAdapter(ArrayList<Recipe> recipeArrayList,Context context) {
        this.recipeArrayList = recipeArrayList;
        mContext=context;
    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item,parent,false);
        ImageView back=view.findViewById(R.id.recipe_thumbnail);
        Picasso.get().load(R.drawable.baking).fit().into(back);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, final int position) {
        Recipe rec=recipeArrayList.get(position);
       // boolean isOffline=checkIfRecipeOffline(rec.id);
//        if(isOffline) {
//            Log.d(TAG, "onBindViewHolder: is present");
//            holder.download_tv.setText(mContext.getString(R.string.download_done_string));
//        }
        Set defaultSet=new HashSet();
sharedPreferences=mContext.getSharedPreferences(mContext.getString(R.string.download_shared_pref),Context.MODE_PRIVATE);
        Set<String> set=sharedPreferences.getStringSet(MainActivity.DOWNLOAD_SET,defaultSet);
        if(set.contains(Integer.toString(rec.id)))
            holder.download_tv.setText(mContext.getString(R.string.download_done_string));
        else
            holder.download_tv.setText(mContext.getString(R.string.download_button_text));
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
                onClick.onButtonClicked(recipeArrayList.get(position));
            }
        });

    }
//    public boolean checkIfRecipeOffline(final int id){
//
//        final AppDatabase mDb=AppDatabase.getsInstance(mContext);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//              temp = mDb.recipieDao().getRecipeFromID(id);
//
//
//
//            }
//        });
//
//    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }
}
