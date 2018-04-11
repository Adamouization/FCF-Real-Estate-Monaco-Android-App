package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Favoris;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Home;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Locations;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Ventes;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;

/**
 * Class containing methods to fill up the fields for contacting the agency.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 29/07/2016
 * @see : StackAgenceXml.java, XMLResponse.java
 */
public class Contact extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener/*,XMLResponse*/ {

    /**
     * fields
     */
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    //private StackAgenceXml stackAgence;
    //private TextView txtAdresse, txtTel, txtFax, txtWeb;
    //private String adresse, telephone, fax, website, latitude, longitude;
    //to change agency go to string values and change the id at the end of xml feed url
    //private String urlAgencesXml = "http://api.immotoolbox.com/xml/2.0/AGENCE/agences/?id=216";

    /**
     * Method used to initialize the current activity, by inflating the activity's UI and interacting with
     * implemented widgets in the UI. Used to get and set the toolbar, (a floating action button), the
     * drawer which is used as the side menu, a navigation view.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //todo no need to fetch, agency data hardcoded
        /**fetch xml data*/
        /*stackAgence = new StackAgenceXml(urlAgencesXml);
        stackAgence.fetchTypesXml(this);*/

        /**get references*/
        /*txtAdresse = (TextView) findViewById(R.id.txt_contact_adresse);
        txtTel = (TextView) findViewById(R.id.txt_contact_tel);
        txtFax = (TextView) findViewById(R.id.txt_contact_fax);
        txtWeb = (TextView) findViewById(R.id.txt_contact_web);*/

        /**toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**floatingActionButton*/
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_contact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.content_produit_detail_email_agency)});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.content_produit_detail_email_subject_information));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.content_produit_detail_email_body_information));
                startActivity(Intent.createChooser(intent, "send email..."));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        /**drawer*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**Action bar for email*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**navigationView*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**Waiting for thread from StackAgenceXml.java to finish running*/
        /*Thread t;
        t = stackAgence.getThread();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /**set text views*/
        //setTextView();
    }

    /**
     * Method used to retrieve Strings from StackAgenceXml.java
     *
     * @param adrs
     * @param cdpostal
     * @param ps
     * @param tel
     * @param fx
     * @param web
     */
    /*@Override
    public void sendXMLDataAgency(String adrs, String cdpostal, String ps, String tel, String fx, String web) {
        //Log.i("test contact", adrs + " " + cdpostal + " " + tel + " " + fx + " " + web);
        /*adresse = adrs + " - " + cdpostal + " " + ps;
        telephone = tel;
        fax = fx;
        website = web;*/
        //Log.i("addresse", adresse);
        //Log.i("telephone", tel);
        //Log.i("fax", fax);
        //Log.i("website", web);
    //}

    /**
     * Method used to retrieve the lat and lng Strings from StackAgenceXml.java
     *
     * @param lat
     * @param lng
     */
    /*@Override
    public void sendXMLDataLatLng(String lat, String lng) {
        //Log.i("test location", lat + " , " + lng);
        latitude = lat;
        longitude = lng;
    }*/

    /**
     * Method used to set retrieved text to TextViews.
     */
    /*public void setTextView() {
        txtAdresse.setText(adresse);
        txtTel.setText(telephone);
        txtFax.setText(fax);
        txtWeb.setText(website);
        //Log.i("test location final ", latitude + " , " + longitude);
    }*/



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
            startActivity(new Intent(Contact.this, MentionsLegales.class));
        } else if (id == R.id.action_a_propos) {
            startActivity(new Intent(Contact.this, APropos.class));
        } else if (id == R.id.action_aide) {
            startActivity(new Intent(Contact.this, Aide.class));
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
            startActivity(new Intent(Contact.this, Ventes.class));
        } else if (id == R.id.nav_accueil) {
            startActivity(new Intent(Contact.this, Home.class));
        } else if (id == R.id.nav_locations) {
            startActivity(new Intent(Contact.this, Locations.class));
        } else if (id == R.id.nav_recherche) {
            startActivity(new Intent(Contact.this, Recherche.class));
        } else if (id == R.id.nav_favoris) {
            startActivity(new Intent(Contact.this, Favoris.class));
        } else if (id == R.id.nav_contact) {
            //do nothing = close drawer
        } else if (id == R.id.nav_social) {
            startActivity(new Intent(Contact.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
