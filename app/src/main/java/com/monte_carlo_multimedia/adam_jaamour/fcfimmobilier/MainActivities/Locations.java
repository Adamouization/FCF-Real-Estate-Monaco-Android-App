package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.Downloader;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitDetail;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitsAdapter;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.ProduitsXmlPullParser;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits.StackProduits;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;

import java.io.FileNotFoundException;

/**
 * Class containing the methods to handle the display of all products up for sale.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 10/06/2016
 * @release_date : 29/07/2016
 * @see : ProductDetail.java
 */
public class Locations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * fields
     */
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ProduitsAdapter mAdapter;
    private ListView produitLocationList;
    private Button buttonDate, buttonPrix, buttonArea;
    private Drawable imgArrowDown, imgArrowUp;
    private String XMLurl;  //string storing the link to needed xml feed
    private boolean countReOrder = true;

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
        setContentView(R.layout.activity_locations);

        //Log.i("package name", getApplicationContext().getPackageName()); //todo log: get package name

        /**get references*/
        produitLocationList = (ListView) findViewById(R.id.produitsList);

        /**default url*/
        XMLurl = getString(R.string.url_produit_location);

        /**initialize arrow drawables*/
        imgArrowDown = getResources().getDrawable(R.drawable.ic_keyboard_arrow_down);
        imgArrowUp = getResources().getDrawable(R.drawable.ic_keyboard_arrow_up);

        /**Set the click listener to launch the browser when a row is clicked.*/
        produitLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Intent intentProduitLocationDetail = new Intent(Locations.this, ProduitDetail.class);
                StackProduits ProduitLocation = ProduitsXmlPullParser.getStackProduitFromFile(Locations.this).get(pos);
                intentProduitLocationDetail.putExtra("produit", ProduitLocation);
                startActivity(intentProduitLocationDetail);
            }
        });

        /**
         * If network is available download the XML from the Internet.
         * If not then try to use the local file from last time.
         */
        if (isNetworkAvailable()) {
            //Log.i("StackProduits", "starting download Task"); todo - log started downloading task
            SitesDownloadTask download = new SitesDownloadTask();
            download.execute();
        } else {
            mAdapter = new ProduitsAdapter(getApplicationContext(), -1, ProduitsXmlPullParser.getStackProduitFromFile(Locations.this));
            produitLocationList.setAdapter(mAdapter);
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

        /**call button/text listeners*/
        onClickTextDateListener();
        onClickTextPrixListener();
        onClickTextAreaListener();
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
     * START OF BUTTON LISTENERS                                                                   *
     ***********************************************************************************************/
    /**
     * Method used to rearrange the ListView in terms of date.
     */
    public void onClickTextDateListener() {
        buttonDate = (Button) findViewById(R.id.button_txt_date_order);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!XMLurl.equals(getString(R.string.url_produit_location_date_desc))) {
                    XMLurl = getString(R.string.url_produit_location_date_desc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_date_desc), Toast.LENGTH_SHORT).show();
                } else if (!XMLurl.equals(getString(R.string.url_produit_location_date_asc))) {
                    XMLurl = getString(R.string.url_produit_location_date_asc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_date_asc), Toast.LENGTH_SHORT).show();
                }
                if (XMLurl.equals(getString(R.string.url_produit_location_date_asc))) {
                    buttonDate.setCompoundDrawablesWithIntrinsicBounds(imgArrowDown, null, null, null);
                } else if (XMLurl.equals(getString(R.string.url_produit_location_date_desc))) {
                    buttonDate.setCompoundDrawablesWithIntrinsicBounds(imgArrowUp, null, null, null);
                }
                buttonDate.setTypeface(null, Typeface.BOLD);
                buttonPrix.setTypeface(null, Typeface.NORMAL);
                buttonArea.setTypeface(null, Typeface.NORMAL);
            }
        });
    }
    /**
     * Method used to rearrange the ListView in terms of price.
     */
    public void onClickTextAreaListener() {
        buttonArea = (Button) findViewById(R.id.button_txt_prix_order);
        buttonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!XMLurl.equals(getString(R.string.url_produit_location_prix_desc))) {
                    XMLurl = getString(R.string.url_produit_location_prix_desc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_price_desc), Toast.LENGTH_SHORT).show();
                } else if (!XMLurl.equals(getString(R.string.url_produit_location_prix_asc))) {
                    XMLurl = getString(R.string.url_produit_location_prix_asc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_price_asc), Toast.LENGTH_SHORT).show();
                }
                if (XMLurl.equals(getString(R.string.url_produit_location_prix_asc))) {
                    buttonArea.setCompoundDrawablesWithIntrinsicBounds(imgArrowUp, null, null, null);
                } else if (XMLurl.equals(getString(R.string.url_produit_location_prix_desc))) {
                    buttonArea.setCompoundDrawablesWithIntrinsicBounds(imgArrowDown, null, null, null);
                }
                buttonDate.setTypeface(null, Typeface.NORMAL);
                buttonPrix.setTypeface(null, Typeface.NORMAL);
                buttonArea.setTypeface(null, Typeface.BOLD);
            }
        });
    }
    /**
     * Method used to rearrange the ListView in terms of area.
     */
    public void onClickTextPrixListener() {
        buttonPrix = (Button) findViewById(R.id.button_txt_area_order);
        buttonPrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!XMLurl.equals(getString(R.string.url_produit_location_area_desc))) {
                    XMLurl = getString(R.string.url_produit_location_area_desc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_area_desc), Toast.LENGTH_SHORT).show();
                } else if (!XMLurl.equals(getString(R.string.url_produit_location_area_asc))) {
                    XMLurl = getString(R.string.url_produit_location_area_asc);
                    SitesDownloadTask download = new SitesDownloadTask();
                    download.execute();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Locations.this, getString(R.string.toast_area_asc), Toast.LENGTH_SHORT).show();
                }
                if (XMLurl.equals(getString(R.string.url_produit_location_area_asc))) {
                    buttonPrix.setCompoundDrawablesWithIntrinsicBounds(imgArrowUp, null, null, null);
                } else if (XMLurl.equals(getString(R.string.url_produit_location_area_desc))) {
                    buttonPrix.setCompoundDrawablesWithIntrinsicBounds(imgArrowDown, null, null, null);
                }
                buttonDate.setTypeface(null, Typeface.NORMAL);
                buttonPrix.setTypeface(null, Typeface.BOLD);
                buttonArea.setTypeface(null, Typeface.NORMAL);
            }
        });
    }
    /***********************************************************************************************
     * END OF BUTTON LISTENERS                                                                     *
     ***********************************************************************************************/




    /***********************************************************************************************
     * START OF INNER CLASS FOR POPULATING LISTVIEW                                                *
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
                Downloader.DownloadFromUrl(XMLurl, openFileOutput("StackSites.xml", Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        LinearLayout linlaBtnReOrderLocations = (LinearLayout) findViewById(R.id.layout_btn_reorder);
        @Override
        protected void onPreExecute(){
            if(countReOrder){
                countReOrder = false;
                linlaBtnReOrderLocations.setVisibility(View.GONE);
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }
        /**
         * Mehtod setting up the adapter of the listView.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            //setup our Adapter and set it to the ListView.
            mAdapter = new ProduitsAdapter(Locations.this, -1, ProduitsXmlPullParser.getStackProduitFromFile(Locations.this));
            produitLocationList.setAdapter(mAdapter);
            linlaBtnReOrderLocations.setVisibility(View.VISIBLE);
            linlaHeaderProgress.setVisibility(View.GONE);
            setProgressBarIndeterminateVisibility(false);
            //Log.i("StackProduits", "adapter size = " + mAdapter.getCount()); todo - log adapter size locations
        }
    }
    /***********************************************************************************************
     * END OF INNER CLASS FOR POPULATING LISTVIEW                                                  *
     ***********************************************************************************************/




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
            startActivity(new Intent(Locations.this, MentionsLegales.class));
        } else if (id == R.id.action_a_propos) {
            startActivity(new Intent(Locations.this, APropos.class));
        } else if (id == R.id.action_aide) {
            startActivity(new Intent(Locations.this, Aide.class));
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
            startActivity(new Intent(Locations.this, Ventes.class));
        } else if (id == R.id.nav_accueil) {
            startActivity(new Intent(Locations.this, Home.class));
        } else if (id == R.id.nav_locations) {
            //do nothing = close drawer
        } else if (id == R.id.nav_recherche) {
            startActivity(new Intent(Locations.this, Recherche.class));
        } else if (id == R.id.nav_favoris) {
            startActivity(new Intent(Locations.this, Favoris.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(Locations.this, Contact.class));
        } else if (id == R.id.nav_social) {
            startActivity(new Intent(Locations.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
