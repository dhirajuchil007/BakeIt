package com.example.android.bakeit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.bakeit.MainActivity.TAG;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipceViewHolder> {
    public class RecipceViewHolder extends RecyclerView.ViewHolder{
        public TextView stepDesc;
        public TextView stepNumber;

        public RecipceViewHolder(View itemView) {
            super(itemView);
            stepDesc=itemView.findViewById(R.id.step_desc);
            stepNumber=itemView.findViewById(R.id.step_number_tv);

        }
    }
   private OnRecipleClicked  onRecipleClicked;
    public interface OnRecipleClicked{
        void onClick(int position);
    }

ArrayList<Steps> mStepsArrayList;
    private Context mContext;

    @NonNull
    @Override
    public RecipceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item,parent,false);

        return new RecipceViewHolder(itemView);


    }

    public RecipeStepsAdapter(ArrayList<Steps> mStepsArrayList,Context context) {
        this.mStepsArrayList = mStepsArrayList;
        mContext=context;


    }

    @Override
    public void onBindViewHolder(@NonNull RecipceViewHolder holder, final int position) {
        holder.stepDesc.setText(mStepsArrayList.get(position).shortDesc);
        holder.stepNumber.setText(mContext.getString(R.string.steps)+String.valueOf(mStepsArrayList.get(position).id+1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipleClicked.onClick(position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mStepsArrayList.size();
    }

    public void setOnRecipleClicked(OnRecipleClicked onRecipleClicked) {
        
        this.onRecipleClicked = onRecipleClicked;
    }
}
