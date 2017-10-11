package com.example.meena.robotrition;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Robotrition extends AppCompatActivity implements FatSecretSearch.CommunicationChannel,SearchBar.CommChan,DisplayInfo.NewChan,FatSecretGet.Ditalits,NutriFacts.BackNewChan, TodaysHistory.NewTwoChan, FavsFrags.NewThreeChan, RecepiesFragment.JustFo, FirstLook.GoFirst, ProfileFrag.ProfileChange, AboutFrag.AboutInterface, RecFrag.RecInterface{

    FragmentTransaction fragmentTransaction;
    final FragmentManager fragmentManager= getSupportFragmentManager();
    ArrayList<ArrayList> todaysHist= new ArrayList<>();
    ArrayList<String> foodNames= new ArrayList<>();
    ArrayList<String> foodIds= new ArrayList<>();
    ArrayList<ArrayList> favorites= new ArrayList<>();
    ArrayList<String> favorites_names= new ArrayList<>();
    ArrayList<String> favorites_ids= new ArrayList<>();
    ArrayList<String> profile_info= new ArrayList<>();
    int calorie_goal_main=2000;
    int started=0;
    Bundle savedState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_file);
        savedState=savedInstanceState;
        if (savedInstanceState==null){
            FirstLook firstLook= new FirstLook(this);
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.changinFrag_id,firstLook);
            fragmentTransaction.commit();
        }
        else {
            todaysHist= (ArrayList<ArrayList>) savedInstanceState.get("history");
            favorites= (ArrayList<ArrayList>)savedInstanceState.get("favorites");
            foodNames= (ArrayList<String>) savedInstanceState.get("foodnames");
            foodIds= (ArrayList<String>) savedInstanceState.get("foodids");
            favorites_names= (ArrayList<String>) savedInstanceState.get("favoritesnames");
            favorites_ids= (ArrayList<String>) savedInstanceState.get("favoritesids");
            goBack();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_food) {
            SearchBar searchBar= new SearchBar();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.changinFrag_id,searchBar);
            fragmentTransaction.commit();
        }

        if (id==R.id.get_recs){
            RecFrag recFrag= new RecFrag(this,profile_info);
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.changinFrag_id,recFrag);
            fragmentTransaction.commit();
        }
        if (id==R.id.add_fav){
            FavsFrags favsFrags= new FavsFrags(this,favorites,favorites_names,favorites_ids,0);
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.changinFrag_id,favsFrags);
            fragmentTransaction.commit();
        }
        if (id==R.id.recipies_id){

        }
        if (id==R.id.profile_menu){
            bringUpProfile();
        }
        if (id==R.id.about_id) {
            AboutFrag aboutFrag= new AboutFrag();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.changinFrag_id,aboutFrag);
            fragmentTransaction.commit();

        }
        return super.onOptionsItemSelected(item);

    }
    public long findID(String givenName){
        long id=33691;

        return id;
    }

    @Override
    public void sendInfo(ArrayList<ArrayList> types) {
        DisplayInfo displayInfo= new DisplayInfo(this,types);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id,displayInfo);
        fragmentTransaction.commit();
    }

    @Override
    public void giveNameToSearch(String string) {
        FatSecretSearch search = new FatSecretSearch(string, Robotrition.this, 1);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id,search);
        fragmentTransaction.commit();
    }


    @Override
    public void getDetails(ArrayList list,String foodsad, String food_asdf) {
        NutriFacts nutriFacts= new NutriFacts(list,foodsad, food_asdf,this);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id,nutriFacts);
        fragmentTransaction.commit();
    }

    @Override
    public void addedItem(ArrayList list,String string,String fId) {
        Log.d("arrayListaddedItem",list+"");
        todaysHist.add(list);
        foodNames.add(string);
        foodIds.add(fId);
        goBack();
    }

    @Override
    public void goBack() {
        TodaysHistory todaysHistory= new TodaysHistory(this,todaysHist,foodNames,foodIds,0);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id, todaysHistory);
        fragmentTransaction.commit();
        started=1;
    }

    @Override
    public void goalSet(ArrayList<String> info) {
        profile_info=info;
    }

    @Override
    public void addToFavs(ArrayList list1,String s,String i) {
        for (int a=0;a<favorites_ids.size();a++)
        {
            if (favorites_ids.get(a).equals(i)){
                Toast.makeText(Robotrition.this,"This is already in your favorites!",Toast.LENGTH_LONG).show();
                return;
            }
        }
        favorites_ids.add(i);
        favorites_names.add(s);
        favorites.add(list1);
        Toast.makeText(Robotrition.this,"Added To Favorites",Toast.LENGTH_LONG).show();
    }

    public void callFatGet(long l, Context d) {
        FatSecretGet fatSecretGet = new FatSecretGet(l, d);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id, fatSecretGet);
        fragmentTransaction.commit();
    }

    @Override
    public void removeFav(int position) {
        favorites.remove(position);
        favorites_names.remove(position);
        favorites_ids.remove(position);

        FavsFrags favsFrags= new FavsFrags(this,favorites,favorites_names,favorites_ids,1);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id, favsFrags);
        fragmentTransaction.commit();
    }

    @Override
    public void removeItem(int position) {
        todaysHist.remove(position);
        foodNames.remove(position);
        foodIds.remove(position);

        TodaysHistory todaysHistory= new TodaysHistory(this,todaysHist,foodNames,foodIds,1);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.changinFrag_id, todaysHistory);
        fragmentTransaction.commit();
    }

 /*   @Override
    protected void onStop() {
        if (timesCalled==0) {
            Log.d("lifecycle","calling");
            onSaveInstanceState(savedState);
            Log.d("lifecycle","donecalling");
        }
        super.onStop();
        Log.d("lifecycle", "stop");
    }*/

 @Override
 public void bringUpProfile() {
     ProfileFrag profileFrag= new ProfileFrag(this,profile_info);
     fragmentTransaction= fragmentManager.beginTransaction();
     fragmentTransaction.replace(R.id.changinFrag_id,profileFrag);
     fragmentTransaction.commit();
 }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "destroy");

    }
    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("lifecycle","saveing");
        if (started==1) {
            outState.putSerializable("history", todaysHist);
            outState.putSerializable("favorites", favorites);
            outState.putStringArrayList("foodnames", foodNames);
            outState.putStringArrayList("foodids", foodIds);
            outState.putStringArrayList("favoritesnames", favorites_names);
            outState.putStringArrayList("favoritesids", favorites_ids);
            //Log.d("lifecycle","saved: "+outState);
        }
        super.onSaveInstanceState(outState);
    }*/

}
//figure out how to get the results to display on screen
//add quantity to foods
//add name
