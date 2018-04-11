package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class containing the methods to display the details of each product, including title, pictures,
 * gallery of pictures, favourite button and all the detailed fields.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 21/06/2016
 * @release_date : 29/07/2016
 * @see : StackProduits.java
 */
public class ProduitDetail extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {

    /**
     * fields
     */
    //instance of StackProduits
    private StackProduits produit;
    //navigation
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //favourite button implementation
    private Button butStar;
    //images (main, thumbnails and gallery)
    private ArrayList<String> imagesUrlListThumb, imagesUrlListFull = new ArrayList<String>();
    private RecyclerAdapter recyclerAdapter;
    private String urlRecyclerThumb = "";
    //view elements
    private TextView txtTitle, txtPrice, txtTotalAreaTop, txtDescription, txtReference, txtType,
            txtNumberOfRooms, txtTotalArea, txtLivingArea, txtTerraceArea, txtBuilding, txtDistrict,
            txtCity, txtCountry, txtParking, txtCellar;
    private LinearLayout linlaReference, linlaType, linlaNbRooms, linlaTotalArea, linlaLivingArea,
            linlaTerraceArea, linlaBuilding, linlaDistrict, linlaCity, linlaCountry, linlaParking,
            linlaCellar;
    private RecyclerView recyclerView;
    private ImageView imgCurRecyclerView;
    private Button btnMailCurProduct, btnMailAgency;
    private boolean colorBackground = true;
    //google maps
    private GoogleMap map;
    private MapFragment mapFragment;
    private LatLng latLng;
    private double convertLat, converLng;
    private float zoom;
    private String mapTitle;

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
        setContentView(R.layout.activity_produit_detail);

        //get ids of the two star icons
        final int imgStarFull = R.drawable.ic_star;
        final int imgStarBorder = R.drawable.ic_star_border;

        /**get view references from content_produit_detail.xml*/
        //imageview
        imgCurRecyclerView = (ImageView) findViewById(R.id.content_product_detail_recycer_view_cur_image);
        //textview
        txtTitle = (TextView) findViewById(R.id.txt_detail_title);
        txtPrice = (TextView) findViewById(R.id.txt_detail_price);
        txtTotalAreaTop = (TextView) findViewById(R.id.txt_detail_total_area_top);
        txtDescription = (TextView) findViewById(R.id.txt_detail_description);
        txtReference = (TextView) findViewById(R.id.txt_detail_ref);
        txtType = (TextView) findViewById(R.id.txt_detail_type);
        txtNumberOfRooms = (TextView) findViewById(R.id.txt_detail_nb_rooms);
        txtTotalArea = (TextView) findViewById(R.id.txt_detail_total_area);
        txtLivingArea = (TextView) findViewById(R.id.txt_detail_living_area);
        txtTerraceArea = (TextView) findViewById(R.id.txt_detail_terrace_area);
        txtBuilding = (TextView) findViewById(R.id.txt_detail_building);
        txtDistrict = (TextView) findViewById(R.id.txt_detail_district);
        txtCity = (TextView) findViewById(R.id.txt_detail_city);
        txtCountry = (TextView) findViewById(R.id.txt_detail_country);
        txtParking = (TextView) findViewById(R.id.txt_detail_parking);
        txtCellar = (TextView) findViewById(R.id.txt_detail_cellar);
        //button
        butStar = (Button) findViewById(R.id.button_detail_heart);
        btnMailCurProduct = (Button) findViewById(R.id.content_product_detail_btn_mailCurProd);
        btnMailAgency = (Button) findViewById(R.id.content_product_detail_btn_mailAgency);
        //Linear layouts
        linlaReference = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_reference);
        linlaType = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_type);
        linlaNbRooms = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_nb_rooms);
        linlaTotalArea = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_total_area_detail);
        linlaLivingArea = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_area_living);
        linlaTerraceArea = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_area_terrace);
        linlaBuilding = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_building);
        linlaDistrict = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_district);
        linlaCity = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_city);
        linlaCountry = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_country);
        linlaParking = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_parking);
        linlaCellar = (LinearLayout) findViewById(R.id.content_produit_detail_linear_layout_cellar);

        /**get produit class object from both Ventes.java, Location.java, Recherche.java and Favoris.java*/
        produit = new StackProduits();
        Intent intent = getIntent();
        produit = intent.getParcelableExtra("produit");

        /**change image and text fields in content_produit_detail.xml*/
        txtTitle.setText(produit.getDesignation());
        txtPrice.setText(produit.getPrice());
        //if area isn't availble, set string value to 'on demand'
        if(produit.getArea().equalsIgnoreCase("-")){
            txtTotalAreaTop.setText(getString(R.string.produit_area_not_available));
        } else {
            txtTotalAreaTop.setText(produit.getArea());
        }
        txtDescription.setText(Html.fromHtml(produit.getDescription()));

        //reference
        txtReference.setText(produit.getReference());

        //type
        txtType.setText(produit.getType());

        //nb of rooms
        txtNumberOfRooms.setText(produit.getNumberOfRooms());


        //total area, if empty value remove fields
        if(produit.getArea().equalsIgnoreCase("-")){
            linlaTotalArea.setVisibility(View.GONE);
        } else {
            txtTotalArea.setText(produit.getArea());
        }

        //living area, if empty remove fields
        if(produit.getLivingArea().equalsIgnoreCase("-")){
            linlaLivingArea.setVisibility(View.GONE);
        } else {
            txtLivingArea.setText(produit.getLivingArea());
        }

        //terrace area, if empty remove fields
        if(produit.getTerraceArea().equalsIgnoreCase("-")){
            linlaTerraceArea.setVisibility(View.GONE);
        } else {
            txtTerraceArea.setText(produit.getTerraceArea());
        }

        //building
        txtBuilding.setText(produit.getBuilding());

        //district
        txtDistrict.setText(produit.getDistrict());

        //city
        txtCity.setText(produit.getCity());

        //country
        txtCountry.setText(produit.getCountry());

        //if country = monaco, remove city field
        if(produit.getCity().equalsIgnoreCase(produit.getCountry())){
            linlaCity.setVisibility(View.GONE);
        }

        //num of parking spaces, if empty remove fields
        if(produit.getParking().equalsIgnoreCase("-")){
            linlaParking.setVisibility(View.GONE);
        } else {
            txtParking.setText(produit.getParking());
        }

        //num of cellars, if empty remove fields
        if(produit.getCellar().equalsIgnoreCase("-")){
            linlaCellar.setVisibility(View.GONE);
        } else {
            txtCellar.setText(produit.getCellar());
        }

        /**check if linear layout is visible to set background color*/
        if(linlaReference.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaReference.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaType.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaType.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaNbRooms.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaNbRooms.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaTotalArea.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaTotalArea.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaLivingArea.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaLivingArea.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaTerraceArea.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaTerraceArea.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaBuilding.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaBuilding.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaDistrict.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaDistrict.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaCity.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaCity.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaCountry.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaCountry.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaParking.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaParking.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }
        if(linlaCellar.getVisibility() == View.VISIBLE){
            if (colorBackgroundLinearLayout()){
                linlaCellar.setBackgroundColor(Color.parseColor("#23a1a1a1"));
            }
        }

        /**main image view*/
        try{
            Picasso.with(getApplicationContext()).load(produit.getImgUrlThumbMul().get(0)).fit().into(imgCurRecyclerView);
        }
        catch (Exception e){
            e.printStackTrace();
            //Log.e("ERROR", "No image available, setting default image");
            imgCurRecyclerView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgCurRecyclerView.setImageResource(R.drawable.no_image_available);
        }

        /**handle gallery launch on image click*/
        imagesUrlListFull = produit.getImgUrlFullMul();
        imgCurRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> images = new ArrayList<String>();
                images = imagesUrlListFull;
                Intent intentGallery = new Intent(ProduitDetail.this, GalleryActivity.class);
                intentGallery.putStringArrayListExtra(GalleryActivity.EXTRA_NAME, images);
                startActivity(intentGallery);
            }
        });

        /**recycler view*/
        imagesUrlListThumb = produit.getImgUrlThumbMul();
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), imagesUrlListThumb);
        recyclerView = (RecyclerView) findViewById(R.id.content_product_detail_recycer_view);
        RecyclerView.LayoutManager recyclerLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        urlRecyclerThumb = imagesUrlListThumb.get(0);
        RecyclerItemClickSupport.addTo(recyclerView).setOnItemClickListener(new RecyclerItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView rv, int pos, View view) {
                urlRecyclerThumb = imagesUrlListThumb.get(pos);
                Picasso.with(getApplicationContext()).load(urlRecyclerThumb).fit().into(imgCurRecyclerView);
            }
        });

        /**handle favorites icon turned on or off when entering the product detail*/
        final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> jsonList = mPrefs.getStringSet("listJson2", new HashSet<String>());
        Set<String> jsonList2 = new HashSet<>();
        jsonList2.addAll(jsonList);
        produit.setIsAddedAsFav("1");
        Gson gson = new Gson();
        String myJson = gson.toJson(produit);
        if (jsonList2.contains(myJson)) {
            butStar.setCompoundDrawablesWithIntrinsicBounds(imgStarFull, 0, 0, 0);
        } else {
            produit.setIsAddedAsFav("0");
        }

        /**
         * Listener for the star button used to make the current product favorite by adding it to a
         * list of all the products currently set as favorite
         */
        butStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Change the icon and make a toast when the heart button is pressed*/
                if (produit.getIsAddedAsFav().equalsIgnoreCase("0")) {
                    produit.setIsAddedAsFav("1");
                    butStar.setCompoundDrawablesWithIntrinsicBounds(imgStarFull, 0, 0, 0);

                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();

                    //convert product to string via gson
                    String myJson = gson.toJson(produit);
                    //prefsEditor.putString("MyObject", myJson);

                    //get the Set<String> reference back to keep the entire list of favorites
                    Set<String> jsonList = mPrefs.getStringSet("listJson2", new HashSet<String>());

                    //make a copy of Set<String> pref to make it usable
                    Set<String> jsonList2 = new HashSet<>();

                    //add the list and the new product to the copy of Set<String>
                    jsonList2.addAll(jsonList);
                    jsonList2.add(myJson);

                    //add list to editor
                    prefsEditor.putStringSet("listJson2", jsonList2);
                    prefsEditor.apply();
                    Toast.makeText(ProduitDetail.this, getString(R.string.toast_favorite_added), Toast.LENGTH_SHORT).show();
                } else {
                    produit.setIsAddedAsFav("1");
                    butStar.setCompoundDrawablesWithIntrinsicBounds(imgStarBorder, 0, 0, 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();

                    Set<String> jsonList = mPrefs.getStringSet("listJson2", new HashSet<String>());

                    Set<String> jsonList2 = new HashSet<>();
                    jsonList2.addAll(jsonList);

                    Gson gson = new Gson();
                    String myJson = gson.toJson(produit);

                    jsonList2.remove(myJson);

                    prefsEditor.putStringSet("listJson2", jsonList2);
                    prefsEditor.apply();
                    produit.setIsAddedAsFav("0");
                    Toast.makeText(ProduitDetail.this, getString(R.string.toast_favorite_removed), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**Google map API*/
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.content_product_detail_gmaps);
        mapFragment.getMapAsync(this);

        /** button listeners */
        onClickBtnMailCurProductListener();
        onClickBtnMailAgencyListener();

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


    public boolean colorBackgroundLinearLayout(){
        colorBackground = !colorBackground;
        return colorBackground;
    }




    /***********************************************************************************************
     * GOOGLE MAPS API                                                                             *
     ***********************************************************************************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
    }

    public void setUpMap(){
        //get position of current product and transform to double
        try {
            convertLat = Double.parseDouble(produit.getLatitude());
            converLng = Double.parseDouble(produit.getLongitude());
            zoom = 16;
            mapTitle = produit.getBuilding();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            //default values if no coordinates retrieved
            convertLat = 43.7384;
            converLng = 7.4246;
            zoom = 11;
            mapTitle = "Monaco";
        }
        //Log.i("product coordinates", String.valueOf(convertLat) + " , " + String.valueOf(converLng)); //todo log: coordinates

        //give coordinates and set marker
        latLng = new LatLng(convertLat, converLng);
        map.addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(new LatLng(convertLat, converLng))
        .title(mapTitle).icon(BitmapDescriptorFactory.defaultMarker())).showInfoWindow();

        //set camera on the marker + zoom enough to see clearly
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
    }
    /***********************************************************************************************
     * END OF GOOGLE MAPS API                                                                      *
     ***********************************************************************************************/


    /***********************************************************************************************
     * BUTTON LISTENERS                                                                            *
     ***********************************************************************************************/
    public void onClickBtnMailCurProductListener() {
        btnMailCurProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendCurProd = new Intent(Intent.ACTION_SENDTO);
                String uriTextCurProd = "mailto:" + Uri.encode(getString(R.string.content_produit_detail_email_agency)) +
                        "?subject=" + Uri.encode(getString(R.string.content_produit_detail_email_subject_product)) +
                        "&body=" + Uri.encode(Html.fromHtml("<strong>" + getString(R.string.content_produit_detail_email_body_product) + "</strong>" +
                        "<br><br>" + produit.getDesignation() + "<br>" +
                        getString(R.string.content_produit_detail_email_body_reference) + produit.getReference() + "<br>" +
                        getString(R.string.content_produit_detail_email_body_price) + produit.getPrice() + "<br>" +
                        getString(R.string.content_produit_detail_email_body_area) + produit.getArea() + "<br><br>" +
                        getString(R.string.content_produit_detail_email_signature)).toString());
                Uri uriCurProd = Uri.parse(uriTextCurProd);
                intentSendCurProd.setData(uriCurProd);
                startActivity((Intent.createChooser(intentSendCurProd, "Send mail...")));
            }
        });
    }

    public void onClickBtnMailAgencyListener() {
        btnMailAgency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendToAgency = new Intent(Intent.ACTION_SENDTO);
                String uriTextAgency = "mailto:" + Uri.encode(getString(R.string.content_produit_detail_email_agency)) +
                        "?subject=" + Uri.encode(getString(R.string.content_produit_detail_email_subject_information)) +
                        "&body=" + Uri.encode(Html.fromHtml(getString(R.string.content_produit_detail_email_body_information) +
                        "<br><br>" +
                        getString(R.string.content_produit_detail_email_signature)).toString());
                Uri uriAgency = Uri.parse(uriTextAgency);
                intentSendToAgency.setData(uriAgency);
                startActivity(Intent.createChooser(intentSendToAgency, "Send mail..."));
            }
        });
    }
    /***********************************************************************************************
     * END OF BUTTON LISTENERS                                                                     *
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
            startActivity(new Intent(ProduitDetail.this, MentionsLegales.class));
        } else if (id == R.id.action_a_propos) {
            startActivity(new Intent(ProduitDetail.this, APropos.class));
        } else if (id == R.id.action_aide) {
            startActivity(new Intent(ProduitDetail.this, Aide.class));
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
            startActivity(new Intent(ProduitDetail.this, Ventes.class));
        } else if (id == R.id.nav_accueil) {
            startActivity(new Intent(ProduitDetail.this, Home.class));
        } else if (id == R.id.nav_locations) {
            startActivity(new Intent(ProduitDetail.this, Locations.class));
        } else if (id == R.id.nav_recherche) {
            startActivity(new Intent(ProduitDetail.this, Recherche.class));
        } else if (id == R.id.nav_favoris) {
            startActivity(new Intent(ProduitDetail.this, Favoris.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(ProduitDetail.this, Contact.class));
        } else if (id == R.id.nav_social) {
            startActivity(new Intent(ProduitDetail.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
