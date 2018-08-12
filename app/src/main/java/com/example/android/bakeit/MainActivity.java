package com.example.android.bakeit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RecipeAdapter.OnItemClicked
{
    public static final String TAG="mainactivity";
    private final static String BAKE_QUERY="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final String RECIPE_JSON="recipies";
RecyclerView recipeRecyclerView;
RecipeAdapter recipeAdapter;
ArrayList<Recipe> recipeArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            new GetRecipes().execute(new URL(BAKE_QUERY));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
       // ImageView test=(ImageView)findViewById(R.id.test_image_view);

        recipeRecyclerView=findViewById(R.id.recipe_recyclerview);


       // test.setImageBitmap(bt);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recipeRecyclerView.setLayoutManager(layoutManager);




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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private boolean isOnline() {
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
        String recString=gson.toJson(recipeArrayList.get(position));
        intent.putExtra(RECIPE_JSON,recString);
        startActivity(intent);

    }

    @Override
    public void onButtonClicked() {
        Toast.makeText(this,"download clicked",Toast.LENGTH_SHORT).show();
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
                        ArrayList<Ingredients> ing=new ArrayList();
                        ArrayList<Steps> ste=new ArrayList();
                        for(int j=0;j<ingArray.length();j++)
                        {
                            ing.add(new Ingredients(ingArray.getJSONObject(j).getString("ingredient")
                                    ,ingArray.getJSONObject(j).getString("measure")
                                    ,ingArray.getJSONObject(j).getDouble("quantity")));
                        }
                        for(int k=0;k<steps.length();k++)
                        {
                            ste.add(new Steps(steps.getJSONObject(k).getInt("id"),
                                    steps.getJSONObject(k).getString("shortDescription")
                                    ,steps.getJSONObject(k).getString("description"),
                                    steps.getJSONObject(k).getString("videoURL"),
                                    steps.getJSONObject(k).getString("thumbnailURL")));
                        }
                        recipes.add(new Recipe(ing,id,name,ste,servings,image));
                        Log.d(TAG, "doInBackground: "+name);


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
                recipeAdapter=new RecipeAdapter(recipes);
                recipeRecyclerView.setAdapter(recipeAdapter);
                recipeAdapter.setOnClick(MainActivity.this);
            }
            else{
                Toast toast=Toast.makeText(MainActivity.this,getString(R.string.internet_error_message),Toast.LENGTH_SHORT);

                toast.show();
            }
        }
    }
}
