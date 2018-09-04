package com.example.android.bakeit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {
    static  AppDatabase mDb;
    public static final String CURRENT_ING_ID="idofcurrenting";
static String id;

   static Recipe recipe;

    static void updateAppWidget(final Context context,final AppWidgetManager appWidgetManager,
                              final  int appWidgetId) {
        Log.d(TAG, "AppWidget: "+appWidgetId);

       mDb=AppDatabase.getsInstance(context);
        CharSequence widgetText = IngredientsWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
       // Log.d(TAG, "updateWidget: "+widgetText);
        final SharedPreferences preferences=context.getSharedPreferences(IngredientsWidgetConfigureActivity.PREFS_NAME,Context.MODE_PRIVATE);
        id= preferences.getString(IngredientsWidgetConfigureActivity.ING_REC_ID,"0");
        Log.d(TAG, "updateAppWidget: "+id);
        new Thread(new Runnable() {
            @Override
            public void run() {
              recipe    =mDb.recipieDao().getRecipeFromID(Integer.parseInt(id));
              if(recipe==null)
                  return;
                try {

                    ArrayList<Ingredients> ing=IngredientsWidgetConfigureActivity.createINGArrayList(new JSONArray(recipe.ingredientsJson));



                Intent prevIng=new Intent(context,HandleIngSwitch.class);
              SharedPreferences preferences=context.getSharedPreferences(IngredientsWidgetConfigureActivity.PREFS_NAME,Context.MODE_PRIVATE);
             String ingr= preferences.getString(CURRENT_ING_ID,"0");
                    Log.d(TAG, "runit: "+ingr);

                    String ingredient=ing.get(Integer.parseInt(ingr)).name;
                prevIng.setAction(HandleIngSwitch.ACTION_PREV_ING);
                prevIng.putExtra(IngredientsWidgetConfigureActivity.ING_REC_ID,appWidgetId);
                    PendingIntent prevPendingIntent=PendingIntent.getService(context,0,prevIng,PendingIntent.FLAG_UPDATE_CURRENT);
                Intent nestIng=new Intent(context,HandleIngSwitch.class);
                nestIng.setAction(HandleIngSwitch.ACTION_NEXT_ING);
                nestIng.putExtra(IngredientsWidgetConfigureActivity.ING_REC_ID,appWidgetId);
                PendingIntent  nextPendingIntent=PendingIntent.getService(context,0,nestIng,PendingIntent.FLAG_UPDATE_CURRENT);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
                views.setTextViewText(R.id.appwidget_text, recipe.recipeName);
                views.setTextViewText(R.id.ingredient_name,ingredient);
                views.setOnClickPendingIntent(R.id.prev_button,prevPendingIntent);
                views.setOnClickPendingIntent(R.id.next_button,nextPendingIntent);


                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    public static void updateIngWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }
}

