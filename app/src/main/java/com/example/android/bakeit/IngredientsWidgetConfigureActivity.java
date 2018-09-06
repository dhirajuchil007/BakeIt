package com.example.android.bakeit;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends Activity {

    public static final String PREFS_NAME = "com.example.android.bakeit.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String RECIPE_NAME="recipename";
    public static final String ING_LIST="inglist";
    public static final String ING_REC_ID="recipeid";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    AppDatabase mDb;
    ListView listView;
    List<Recipe> recipeList;
    ArrayList<Recipe> recipeArrayList;
    TextView errorMsg;



    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = IngredientsWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);


        setContentView(R.layout.ingredients_widget_configure);
        errorMsg=findViewById(R.id.tv_error_msg_widget);
        listView =findViewById(R.id.ingredients_chooser_for_widget);
         mDb=AppDatabase.getsInstance(this);
         recipeArrayList=new ArrayList<>();
         new Thread(new Runnable() {
             @Override
             public void run() {
                 recipeList=mDb.recipieDao().getAllRecipies();
                 Iterator <Recipe> itr=recipeList.iterator();
                 while (itr.hasNext())
                 {
                     Recipe recipe= itr.next();
                     try {
                         ArrayList<Ingredients> ing=createINGArrayList(new JSONArray(recipe.ingredientsJson));
                         recipeArrayList.add(new Recipe(recipe.id,recipe.recipeName,ing));
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }


             }
         }).start();
        RecipeWidgetAdapter recipeWidgetAdapter=new RecipeWidgetAdapter(IngredientsWidgetConfigureActivity.this,R.id.ingredients_chooser_for_widget,recipeArrayList);
        listView.setAdapter(recipeWidgetAdapter);
        if(recipeArrayList==null||recipeArrayList.size()==0)
        {
            errorMsg.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(IngredientsWidgetConfigureActivity.this);
                IngredientsWidget.updateAppWidget(IngredientsWidgetConfigureActivity.this, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                Recipe res=recipeArrayList.get(position);
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                resultValue.putExtra(RECIPE_NAME,res.recipeName);
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList(ING_LIST,res.ingredientsList);

                //resultValue.putParcelableArrayListExtra(ING_LIST,res.ingredientsList);
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                resultValue.putExtra(ING_LIST,bundle);
                Set set=new HashSet(res.ingredientsList);
                SharedPreferences.Editor prefs = IngredientsWidgetConfigureActivity.this.getSharedPreferences(PREFS_NAME, 0).edit();

                                prefs.putString(ING_REC_ID,String.valueOf(res.id)).apply();


                setResult(RESULT_OK, resultValue);
                finish();

            }
        });




        //findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

//        mAppWidgetText.setText(loadTitlePref(IngredientsWidgetConfigureActivity.this, mAppWidgetId));
    }

    public static ArrayList<Ingredients> createINGArrayList(JSONArray ingArray) throws JSONException {
        ArrayList<Ingredients> ing=new ArrayList<>();
        for(int j=0;j<ingArray.length();j++)
        {
            ing.add(new Ingredients(ingArray.getJSONObject(j).getString("ingredient")
                    ,ingArray.getJSONObject(j).getString("measure")
                    ,ingArray.getJSONObject(j).getDouble("quantity")));
        }
        return ing;

    }
}

