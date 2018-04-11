package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities;

import android.content.Intent;
import android.graphics.PixelFormat;
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
import android.widget.Button;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;

/**
 * Main class used when application is launched. Contains buttons to acces the 6 main activities
 * of the app.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 10/06/2016
 * @release_date : 29/07/2016
 * @see : AppCompatActivity.java and NavigationView.java
 */
public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**fields*/
    private Button butVentes, butLocations, butRecherche, butFavoris, butContact, butSocial;   //Buttons for home screen
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    /**
     * Method used to initialize the app, by inflating the activity's UI and interacting with
     * implemented widgets in the UI. Used to get and set the toolbar, a floating action button, the
     * drawer which is used as the side menu, a navigation view and finally calls methods listening
     * to the buttons on the home screen (main activity launched when the app is launched).
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_main);

        /**toolbar*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**drawer*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**navigationView*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**calling button listeners*/
        onClickButtonVentesListener();
        onClickButtonLocationsListener();
        onClickButtonRechercheListener();
        onClickButtonFavorisListener();
        onClickButtonContactListener();
        onClickButtonSocialListener();
    }





    /***********************************************************************************************
     * BUTTON METHODS                                                                              *
     ***********************************************************************************************/
    /**
     * Method used to create a new activity dedicated to displaying real-estate sales
     * information when the "ventes" button is pressed.
     */
    public void onClickButtonVentesListener(){
        butVentes = (Button) findViewById(R.id.buttonVentes);
        butVentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1 = new Intent("com.example.adam_jaamour.cfimmobilier.MainActivities.Ventes");
                Intent intent1 = new Intent(Home.this, Ventes.class);
                startActivity(intent1);
            }
        });
    }

    /**
     * Method used to create a new activity dedicated to displaying real-estate location
     * information when the "locations" button is pressed.
     */
    public void onClickButtonLocationsListener(){
        butLocations = (Button) findViewById(R.id.buttonLocations);
        butLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent2 = new Intent("com.example.adam_jaamour.cfimmobilier.MainActivities.Locations");
                Intent intent2 = new Intent(Home.this, Locations.class);
                startActivity(intent2);
            }
        });
    }

    /**
     * Method used to create a new activity dedicated to displaying the user's favorite
     * real-estates when the "favorites" button is pressed.
     */
    public void onClickButtonRechercheListener(){
        butRecherche = (Button) findViewById(R.id.buttonRecherche);
        butRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Home.this, Recherche.class);
                startActivity(intent3);
            }
        });
    }

    /**
     * Method used to create a new activity dedicated to displaying the user's favorite
     * real-estates when the "favorites" button is pressed.
     */
    public void onClickButtonFavorisListener(){
        butFavoris = (Button) findViewById(R.id.buttonFavoris);
        butFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent4 = new Intent("com.example.adam_jaamour.cfimmobilier.MainActivities.Favoris");
                Intent intent4 = new Intent(Home.this, Favoris.class);
                startActivity(intent4);
            }
        });
    }

    /**
     * Method used to create a new activity dedicated to displaying contact information
     * when the "contact" button is pressed.
     */
    public void onClickButtonContactListener(){
        butContact = (Button) findViewById(R.id.buttonContact);
        butContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Home.this, Contact.class);
                startActivity(intent5);
            }
        });
    }

    /**
     * Method used to create a new activity dedicated to displaying social network information
     * when the "social" button is pressed.
     */
    public void onClickButtonSocialListener(){
        butSocial = (Button) findViewById(R.id.buttonSocial);
        butSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(Home.this, Social.class);
                startActivity(intent6);
            }
        });
    }
    /***********************************************************************************************
     * END OF BUTTON METHODS                                                                       *
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
        }
        else {
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
     Home/Up button, so long as a parent activity is specified in AndroidManifest.xml.

     * @param item
     *      instance of MenuItem
     * @return super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_mentions_legales) {
            startActivity(new Intent(Home.this, MentionsLegales.class));
        }
        else if(id == R.id.action_a_propos){
            startActivity(new Intent(Home.this, APropos.class));
        }
        else if(id == R.id.action_aide){
            startActivity(new Intent(Home.this, Aide.class));
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
            startActivity(new Intent(Home.this, Ventes.class));
        }
        else if (id == R.id.nav_accueil) {
            //do nothing = close drawer
        }
        else if (id == R.id.nav_locations) {
            startActivity(new Intent(Home.this, Locations.class));
        }
        else if (id == R.id.nav_recherche) {
            startActivity(new Intent(Home.this, Recherche.class));
        }
        else if (id == R.id.nav_favoris) {
            startActivity(new Intent(Home.this, Favoris.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(Home.this, Contact.class));
        }
        else if (id == R.id.nav_social) {
            startActivity(new Intent(Home.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
