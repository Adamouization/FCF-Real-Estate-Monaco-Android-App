package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Favoris;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Home;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Locations;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Ventes;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.Downloader;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitDetail;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitsAdapter;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitsXmlPullParser;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.StackProduits;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;

import java.io.FileNotFoundException;

/**
 * Class containing methods to display the search results. If the search is null, a small message
 * informs the user his search wasn't successful.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 29/06/2016
 * @release_date : 29/07/2016
 * @see : Recherche.java
 */
public class RechercheListViewResult extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * fields
     */
    //navigation
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //url
    private String XMLurlRecherche = "";
    //pass string to RechercheListViewResult.java 's activity
    public static String MY_PREFS = "MY_PREFS";
    private SharedPreferences mySharedPreferences;
    int prefMode = Activity.MODE_PRIVATE;
    //view
    private ListView rechercheResultListView;
    private String txtNoResult = "";
    //adapter
    private ProduitsAdapter mAdapter;

    /**
     * Method used to initialize the current activity, by inflating the activity's UI and interacting with
     * implemented widgets in the UI. Used to get and set the toolbar, (a floating action button), the
     * drawer which is used as the side menu, a navigation view.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**loading animation + set xml layout view*/
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_recherche_listview_result);

        /**get references*/
        rechercheResultListView = (ListView) findViewById(R.id.recherche_result_ListView);

        /**initialize text*/
        txtNoResult = getString(R.string.content_search_result_empty);

        /**sharedpreference - get back url from the search*/
        mySharedPreferences = getSharedPreferences(MY_PREFS, prefMode);
        XMLurlRecherche = mySharedPreferences.getString("urlSearchKey", null);
        //Log.i("final url retrieved =", XMLurlRecherche); //todo log: final search url retrieved

        /**Launches product detail on any item click*/
        rechercheResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentProduitRechercheDetail = new Intent(RechercheListViewResult.this, ProduitDetail.class);
                StackProduits ProduitRecherche = ProduitsXmlPullParser.getStackProduitFromFile(RechercheListViewResult.this).get(position);
                intentProduitRechercheDetail.putExtra("produit", ProduitRecherche);
                startActivity(intentProduitRechercheDetail);
            }
        });

        /**
         * If network is available download the XML from the Internet.
         * If not then try to use the local file from last time.
         */
        if (isNetworkAvailable()) {
            //Log.i("StackProduits", "starting download Task"); todo started downloading task
            SitesDownloadTask download = new SitesDownloadTask();
            download.execute();
        } else {
            mAdapter = new ProduitsAdapter(getApplicationContext(), -1, ProduitsXmlPullParser.getStackProduitFromFile(RechercheListViewResult.this));
            rechercheResultListView.setAdapter(mAdapter);
        }

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

    /**
     * Helper method to determine if Internet connection is available.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method used to inflate the menu once it's been created.
     *
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
     * Home/Up button, so long as a parent activity is specified in AndroidManifest.xml.
     *
     * @param item instance of MenuItem
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_mentions_legales) {
            startActivity(new Intent(RechercheListViewResult.this, MentionsLegales.class));
        } else if (id == R.id.action_a_propos) {
            startActivity(new Intent(RechercheListViewResult.this, APropos.class));
        } else if (id == R.id.action_aide) {
            startActivity(new Intent(RechercheListViewResult.this, Aide.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to start new activities when items from the menu are clicked.
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();  // Handle navigation view item clicks here.
        if (id == R.id.nav_ventes) {
            startActivity(new Intent(RechercheListViewResult.this, Ventes.class));
        } else if (id == R.id.nav_accueil) {
            startActivity(new Intent(RechercheListViewResult.this, Home.class));
        } else if (id == R.id.nav_locations) {
            startActivity(new Intent(RechercheListViewResult.this, Locations.class));
        } else if (id == R.id.nav_favoris) {
            startActivity(new Intent(RechercheListViewResult.this, Favoris.class));
        } else if (id == R.id.nav_recherche) {
            startActivity(new Intent(RechercheListViewResult.this, Recherche.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(RechercheListViewResult.this, Contact.class));
        } else if (id == R.id.nav_social) {
            startActivity(new Intent(RechercheListViewResult.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/


    /**
     * AsyncTask that will download the xml file for us and store it locally.
     * After the download is done we'll parse the local file.
     */
    private class SitesDownloadTask extends AsyncTask<Void, Void, Void> {

        /**
         * Method calling the DownloadFromUrl method in Downloaded.java.
         *
         * @param arg0
         * @return
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            //Download the file
            try {
                Downloader.DownloadFromUrl(XMLurlRecherche, openFileOutput("StackSites.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgressRechercheResults);
        @Override
        protected void onPreExecute(){
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        /**
         * Method setting up the adapter of the listView.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            //setup our Adapter and set it to the ListView.
            mAdapter = new ProduitsAdapter(RechercheListViewResult.this, -1, ProduitsXmlPullParser.getStackProduitFromFile(RechercheListViewResult.this));
            if(mAdapter.getCount() == 0){
                Toast.makeText(RechercheListViewResult.this, txtNoResult, Toast.LENGTH_LONG).show();
            }
            rechercheResultListView.setAdapter(mAdapter);
            linlaHeaderProgress.setVisibility(View.GONE);
            setProgressBarIndeterminateVisibility(false);
            //Log.i("StackProduits", "adapter size = " + mAdapter.getCount()); todo adapter size
        }
    }
}
