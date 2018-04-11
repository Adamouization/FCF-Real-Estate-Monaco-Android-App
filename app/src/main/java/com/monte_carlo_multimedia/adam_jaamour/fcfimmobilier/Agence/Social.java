package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence;

import android.content.Intent;
import android.net.Uri;
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

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Favoris;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Locations;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Home;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Ventes;

/**
 * Class containing the methods used to display and add functionality to elements from content_social.xml
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 10/06/2016
 * @release_date : 29/07/2016
 */
public class Social extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**fields*/
    private Button butFacebook, butGooglePlus;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    /**
     * Method used to initialize the current activity, by inflating the activity's UI and interacting with
     * implemented widgets in the UI. Used to get and set the toolbar, (a floating action button), the
     * drawer which is used as the side menu, a navigation view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

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

        /**calling button listeners*/
        onClickButtonFacebookListener();
        onClickButtonGooglePlusListener();
    }





    /***********************************************************************************************
     * BUTTON METHODS                                                                              *
     ***********************************************************************************************/
    /**
     * Method used to open a web url when the button is pressed. Opens Facebook page.
     */
    public void onClickButtonFacebookListener(){
        butFacebook = (Button) findViewById(R.id.buttonFacebook);
        butFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFB = new Intent();
                intentFB.setAction(Intent.ACTION_VIEW);
                intentFB.addCategory(Intent.CATEGORY_BROWSABLE);
                intentFB.setData(Uri.parse("https://www.facebook.com/FCF-Real-Estate-728044310665935/"));
                startActivity(intentFB);
            }
        });
    }
    /**
     * Method used to open a web url when the button is pressed. Opens Google + page.
     */
    public void onClickButtonGooglePlusListener(){
        butGooglePlus = (Button) findViewById(R.id.buttonGooglePlus);
        butGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGP = new Intent();
                intentGP.setAction(Intent.ACTION_VIEW);
                intentGP.addCategory(Intent.CATEGORY_BROWSABLE);
                intentGP.setData(Uri.parse("https://plus.google.com/+Fcfrealestate/posts"));
                startActivity(intentGP);
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
            startActivity(new Intent(Social.this, MentionsLegales.class));
        }
        else if(id == R.id.action_a_propos){
            startActivity(new Intent(Social.this, APropos.class));
        }
        else if(id == R.id.action_aide){
            startActivity(new Intent(Social.this, Aide.class));
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
            startActivity(new Intent(Social.this, Ventes.class));
        }
        else if (id == R.id.nav_accueil) {
            startActivity(new Intent(Social.this, Home.class));
        }
        else if (id == R.id.nav_locations) {
            startActivity(new Intent(Social.this, Locations.class));
        }
        else if (id == R.id.nav_recherche) {
            startActivity(new Intent(Social.this, Recherche.class));
        }
        else if (id == R.id.nav_favoris) {
            startActivity(new Intent(Social.this, Favoris.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(Social.this, Contact.class));
        }
        else if (id == R.id.nav_social) {
            //do nothing = close drawer
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
