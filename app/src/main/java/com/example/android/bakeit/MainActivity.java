package com.example.android.bakeit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakeit.IdlingResource.SimpleIdlingResource;
import com.example.android.bakeit.utils.NetworkUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.OnItemClicked
{
    public static final String TAG="mainactivity";
    private final static String BAKE_QUERY="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String RECIPE_JSON="recipies";
    public static final String DOWNLOAD_SET="downloadset";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE =100;
    Set defaultSet;
RecyclerView recipeRecyclerView;
RecipeAdapter recipeAdapter;
ArrayList<Recipe> recipeArrayList;
AppDatabase mDb;
SharedPreferences sharedPreferences;
IdlingResource mIdlingResource;
TextView invisibleText;


    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDb=AppDatabase.getsInstance(this);
        setSupportActionBar(toolbar);
        recipeArrayList=new ArrayList<>();
        defaultSet=new HashSet();

sharedPreferences=this.getSharedPreferences(getString(R.string.download_shared_pref), Context.MODE_PRIVATE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        try {
            new GetRecipes().execute(new URL(BAKE_QUERY));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       // ImageView test=(ImageView)findViewById(R.id.test_image_view);

        recipeRecyclerView=findViewById(R.id.recipe_recyclerview);


       // test.setImageBitmap(bt);
        if(getResources().getString(R.string.landscape).equals("yes"))
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        else

        recipeRecyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext()));

invisibleText=findViewById(R.id.invisible_text);
getIdlingResource();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;

            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(this,RecipeSteps.class);
        Gson gson=new Gson();
        Log.d(TAG, "onItemClick: ");
        String recString=gson.toJson(recipeArrayList.get(position));
        intent.putExtra(RECIPE_JSON,recString);
        startActivity(intent);

    }


    @Override
    public void onButtonClicked(final Recipe recipe) {

    Set <String>set=sharedPreferences.getStringSet(DOWNLOAD_SET,defaultSet);
    SharedPreferences.Editor editor=sharedPreferences.edit();
        Log.d(TAG, "onButtonClicked: "+set.toString());
    if(!set.contains(Integer.toString(recipe.id))) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                mDb.recipieDao().insert(recipe);
            }
        }).start();
        set.add(Integer.toString(recipe.id));
        editor.putStringSet(DOWNLOAD_SET,set);
        editor.commit();
        recipeAdapter.notifyDataSetChanged();
    }
    else
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDb.recipieDao().delete(recipe);
            }
        }).start();
        set.remove(Integer.toString(recipe.id));
        editor.putStringSet(DOWNLOAD_SET,set);
        editor.commit();
        recipeAdapter.notifyDataSetChanged();

    }


    }
    public void setAdapterFromDB() throws JSONException{


        List<Recipe> recipeList=mDb.recipieDao().getAllRecipies();
        if(recipeList==null||recipeList.size()==0)
        {runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invisibleText.setVisibility(View.VISIBLE);
                invisibleText.setText(getString(R.string.invisible_text));
            }
        });

        }
        else {
            Iterator<Recipe> itr = recipeList.iterator();
            //    Log.d(TAG, "setAdapterFromDB: "+recipeList.size());
            while (itr.hasNext()) {
                Recipe recipe = itr.next();
                ArrayList<Ingredients> ing = createINGArrayList(new JSONArray(recipe.ingredientsJson));
                ArrayList<Steps> step = createStepArrayList(new JSONArray(recipe.stepJson));
                recipeArrayList.add(new Recipe(ing, recipe.id, recipe.recipeName, step, recipe.servings, recipe.image, recipe.stepJson, recipe.ingredientsJson));


            }
            recipeAdapter = new RecipeAdapter(recipeArrayList, this);
            Log.d(TAG, "setAdapterFromDB: " + recipeArrayList.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recipeRecyclerView.setAdapter(recipeAdapter);
                    recipeAdapter.setOnClick(MainActivity.this);

                }
            });


        }

    }
    public ArrayList<Ingredients> createINGArrayList(JSONArray ingArray) throws JSONException{
        ArrayList<Ingredients> ing=new ArrayList<>();
        for(int j=0;j<ingArray.length();j++)
        {
            ing.add(new Ingredients(ingArray.getJSONObject(j).getString("ingredient")
                    ,ingArray.getJSONObject(j).getString("measure")
                    ,ingArray.getJSONObject(j).getDouble("quantity")));
        }
        return ing;

    }
    public ArrayList<Steps> createStepArrayList(JSONArray steps)throws JSONException{
        ArrayList<Steps> ste=new ArrayList<>();
        for(int k=0;k<steps.length();k++)
        {
            ste.add(new Steps(k,
                    steps.getJSONObject(k).getString("shortDescription")
                    ,steps.getJSONObject(k).getString("description"),
                    steps.getJSONObject(k).getString("videoURL"),
                    steps.getJSONObject(k).getString("thumbnailURL")));
        }
        return ste;

    }


    class GetRecipes extends AsyncTask<URL,Void,ArrayList<Recipe>>{

        @Override
        protected ArrayList<Recipe> doInBackground(URL... urls) {
            URL serachURL=urls[0];
            ArrayList<Recipe> recipes=new ArrayList();
            String results=null;
            if(isOnline())
            {
                try {
                    results= NetworkUtils.getResponseFromHttpUrl(serachURL);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else return null;

            if(results!=null)
            {
                try {
                    JSONArray jsonArray=new JSONArray(results);
                    Log.d(TAG, "doInBackground: "+jsonArray);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        //Log.d(TAG, "doInBackground: "+jsonArray.length());
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        int id=jsonObject.getInt("id");
                        String name=jsonObject.getString("name");
                        int servings=jsonObject.getInt("servings");
                        String image=jsonObject.getString("image");
                        JSONArray ingArray=jsonObject.getJSONArray("ingredients");
                        JSONArray steps=jsonObject.getJSONArray("steps");
                        ArrayList<Ingredients> ing=createINGArrayList(ingArray);
                        ArrayList<Steps> ste=createStepArrayList(steps);


                        recipes.add(new Recipe(ing,id,name,ste,servings,image,steps.toString(),ingArray.toString()));
                        Log.d(TAG, "doInBackground: "+steps.toString());
                      //  Log.d(TAG, "doInBackground: "+name);


                    }
                    return recipes;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                Log.e("Json", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. ",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;


        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {

            if(recipes!=null)
            {
                recipeArrayList=recipes;
                recipeAdapter=new RecipeAdapter(recipes,MainActivity.this);
                recipeRecyclerView.setAdapter(recipeAdapter);
                recipeAdapter.setOnClick(MainActivity.this);
            }
            else{
                Toast toast=Toast.makeText(MainActivity.this,getString(R.string.internet_error_message),Toast.LENGTH_SHORT);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                setAdapterFromDB();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                toast.show();
            }
        }
    }
}
