package com.example.android.bakeit;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class HandleIngSwitch extends IntentService {
    public static final String ACTION_PREV_ING="previousingredient";
    public static final String ACTION_NEXT_ING="nextingredient";
    AppDatabase mDb;

    public HandleIngSwitch() {
        super("plantwateringservice");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action=intent.getAction();

        int id=intent.getIntExtra(IngredientsWidgetConfigureActivity.ING_REC_ID,0);
        Log.d(TAG, "appwidget: "+id);
        if(action.equals(ACTION_PREV_ING))
            prevIngredient(id);
        else if (action.equals(ACTION_NEXT_ING))
            nextIngredient(id);


    }

    private void nextIngredient(final int appwidgetid) {
        mDb=AppDatabase.getsInstance(getApplicationContext());


        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences=getSharedPreferences(IngredientsWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE);
                int recid=Integer.parseInt(preferences.getString(IngredientsWidgetConfigureActivity.ING_REC_ID,"0"));
                Recipe recipe=mDb.recipieDao().getRecipeFromID(recid);
                String currentID=preferences.getString(IngredientsWidget.CURRENT_ING_ID,"0");
                Log.d(TAG, "currentid"+recid);
                try {
                    ArrayList<Ingredients> ing=IngredientsWidgetConfigureActivity.createINGArrayList(new JSONArray(recipe.ingredientsJson));
                    String ingid=  preferences.getString(IngredientsWidget.CURRENT_ING_ID,"0");
                    if(Integer.parseInt(ingid)<ing.size()-1)
                        ingid=String.valueOf(Integer.parseInt(ingid)+1);

                    preferences.edit().putString(IngredientsWidget.CURRENT_ING_ID,ingid).apply();
                    AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(getApplicationContext());
                    int [] appWdigetIds=appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(),IngredientsWidget.class));
                    Log.d(TAG, ""+appWdigetIds.length);
                    IngredientsWidget.updateIngWidget(getApplicationContext(),appWidgetManager,appWdigetIds);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    public void prevIngredient(final int appwigetid){
        mDb=AppDatabase.getsInstance(getApplicationContext());


        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences=getSharedPreferences(IngredientsWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE);
                int recid=Integer.parseInt(preferences.getString(IngredientsWidgetConfigureActivity.ING_REC_ID,"0"));
                Recipe recipe=mDb.recipieDao().getRecipeFromID(recid);
                Log.d(TAG, "recid: "+recid);
                String currentID=preferences.getString(IngredientsWidget.CURRENT_ING_ID,"0");
                Log.d(TAG, "currentid"+currentID);
                try {
                    ArrayList<Ingredients> ing=IngredientsWidgetConfigureActivity.createINGArrayList(new JSONArray(recipe.ingredientsJson));
                  String ingid=  preferences.getString(IngredientsWidget.CURRENT_ING_ID,"0");
                  if(Integer.parseInt(ingid)!=0)
                  ingid=String.valueOf(Integer.parseInt(ingid)-1);

                   preferences.edit().putString(IngredientsWidget.CURRENT_ING_ID,ingid).apply();
                   AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(getApplicationContext());
                   int [] appWdigetIds=appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(),IngredientsWidget.class));
                    Log.d(TAG, "appwidgetids "+appWdigetIds.length);
                   IngredientsWidget.updateIngWidget(getApplicationContext(),appWidgetManager,appWdigetIds);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }
}
