package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitDetail;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitsAdapter;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.StackProduits;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class containing methods to receive items marked as "favorite" and display them using a ListView.
 * Contains methods to delete multiple items simultaneously as well as warn users when they don't
 * have any items in their favorite list.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 10/06/2016
 * @release_date : 29/07/2016
 * @see : ProductDetail.java
 */
public class Favoris extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**fields*/
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ListView produitFavorisListView;
    private TextView txtEmptyFavoritesList;
    private List<StackProduits> listProduits = new ArrayList<>();
    private ProduitsAdapter adapterFavoris;

    /**
     * Method used to initialize the current activity, by inflating the activity's UI and interacting with
     * implemented widgets in the UI. Used to get and set the toolbar, (a floating action button), the
     * drawer which is used as the side menu, a navigation view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        /**get ListView reference*/
        produitFavorisListView = (ListView) findViewById(R.id.favoritesList);
        txtEmptyFavoritesList = (TextView) findViewById(R.id.content_favorite_emptylist);

        /**getting back data from shared preferences*/
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Gson gson = new Gson();

         /**getting back favorites*/
        Set<String> myJson = mPrefs.getStringSet("listJson2", new HashSet<String>());
        if (myJson.isEmpty() && listProduits.isEmpty()) {
            produitFavorisListView.setAdapter(null);
            txtEmptyFavoritesList.setText(getString(R.string.content_favoris_empty_list));
            txtEmptyFavoritesList.setVisibility(View.VISIBLE);
            //Log.i("INFO", "No items"); todo - log info: no items
        }
        else if (myJson.isEmpty() && listProduits != null) {
            adapterFavoris.notifyDataSetChanged();
            adapterFavoris = new ProduitsAdapter(getApplicationContext(), -1, listProduits);
            produitFavorisListView.setAdapter(adapterFavoris);
            txtEmptyFavoritesList.setVisibility(View.GONE);
        }
        else{
            //for each where we get back values from sting set, then convert to product
            for (String id : myJson) {
                StackProduits savedProduct = gson.fromJson(id, StackProduits.class);
                //savedProduct.setIsAddedAsFav("1");
                listProduits.add(savedProduct);
            }
            adapterFavoris = new ProduitsAdapter(getApplicationContext(), -1, listProduits);
            produitFavorisListView.setAdapter(adapterFavoris);
        }

        /**Set the click listener to launch the browser when a row is clicked.*/
        produitFavorisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intentProduitFavorisDetail = new Intent(Favoris.this, ProduitDetail.class);
                //StackProduits ProduitFavoris = ProduitsXmlPullParser.getStackProduitFromFile(Favoris.this).get(pos);
                StackProduits ProduitFavoris = adapterFavoris.getItem(pos);
                intentProduitFavorisDetail.putExtra("produit", ProduitFavoris);
                startActivity(intentProduitFavorisDetail);
            }
        });

        /**handle multiple item selection for deletion*/
        produitFavorisListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        produitFavorisListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener(){
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.favorite_menu, menu);
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;   //done
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_fav:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapterFavoris.getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                StackProduits selecteditem = adapterFavoris.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapterFavoris.remove(selecteditem);
                                adapterFavoris.notifyDataSetChanged();
                                if (listProduits.isEmpty()) {
                                    if(txtEmptyFavoritesList != null){
                                        txtEmptyFavoritesList.setText(getString(R.string.content_favoris_empty_list));
                                        txtEmptyFavoritesList.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                        //save after modifications
                        String getProduct;
                        Set<String> stringListProductSave = new HashSet<>();
                        Gson gsonSave = new Gson();
                        for(int i=0; i<listProduits.size();i++){
                            getProduct = gsonSave.toJson(listProduits.get(i));
                            stringListProductSave.add(getProduct);
                        }
                        SharedPreferences.Editor prefsEditorSave = mPrefs.edit();
                        prefsEditorSave.putStringSet("listJson2", stringListProductSave);
                        prefsEditorSave.apply();
                        adapterFavoris.notifyDataSetChanged();
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapterFavoris.removeSelection();
            }
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = produitFavorisListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                adapterFavoris.toggleSelection(position);
            }
        });

        /**toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**drawer*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**navigationView*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /***********************************************************************************************
     * NAVIGATION MENU METHODS                                                                     *
     ***********************************************************************************************/
    /**
     * Method used to close the drawer (main menu on the left side of the UI) when the back of the
     * UI is touched (screen part other than the drawer itself).
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    /**
     * Method used to inflate the menu once it's been created.
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);   //adds items to the action bar if it is present.
        return true;
    }
    /**
     * Method used to handle action bar item clicks here. The action bar will automatically handle clicks on the
     Home/Up button, so long as a parent activity is specified in AndroidManifest.xml.
     * @param item
     *      instance of MenuItem
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_mentions_legales) {
            startActivity(new Intent(Favoris.this, MentionsLegales.class));
        }
        else if(id == R.id.action_a_propos){
            startActivity(new Intent(Favoris.this, APropos.class));
        }
        else if(id == R.id.action_aide){
            startActivity(new Intent(Favoris.this, Aide.class));
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Method used to start new activities when items from the menu are clicked.
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();  // Handle navigation view item clicks here.
        if (id == R.id.nav_ventes) {
            startActivity(new Intent(Favoris.this, Ventes.class));
        }
        else if (id == R.id.nav_accueil) {
            startActivity(new Intent(Favoris.this, Home.class));
        }
        else if (id == R.id.nav_locations) {
            startActivity(new Intent(Favoris.this, Locations.class));
        }
        else if (id == R.id.nav_recherche) {
            startActivity(new Intent(Favoris.this, Recherche.class));
        }
        else if (id == R.id.nav_favoris) {
            //do nothing = close drawer
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(Favoris.this, Contact.class));
        }
        else if (id == R.id.nav_social) {
            startActivity(new Intent(Favoris.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
