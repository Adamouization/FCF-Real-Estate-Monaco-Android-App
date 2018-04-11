package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Favoris;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Locations;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Home;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Ventes;

import java.util.ArrayList;

/**
 * Class containing methods to make the search engine functional. Works by adding Strings to a main
 * String used as an url from which our downloaded retrieves the xml data. Appends small Strings to
 * the main one if the corresponding checkboxes are checked. All fields with checked checkboxes are
 * appended at the end of the url when the search button is pressed.
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 17/06/2016
 * @release_date : 29/07/2016
 * @see RechercheListViewResult
 */
public class Recherche extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * fields
     */
    //navigation
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //view
    private Switch switchVentes, switchLocations;
    private boolean isCorrectSwicth = false;
    private Spinner spinnerCountry, spinnerNbRooms, spinnerProductType;
    private EditText editTextPriceMin, editTextPriceMax, editTextAreaMin, editTextAreaMax;
    private CheckBox checkBoxCountry, checkBoxNbRooms, checkBoxProductType, checkBoxPrice, checkBoxArea;
    private Button buttonReset, buttonSearch;
    //url manipulation
    private String urlBase, urlSearchDetailAppend;  //main url
    private String endOfUrlLocations, endOfUrlVentes;   //string to append at end of url for type of transaction
    private String selectedTag = "";
    private String valueSpinnerCountry, finalValueSpinnerCountry, valueSpinnerNbRooms, finalValueSpinnerNbRooms,
            valueSpinnerProductType, finalValueSpinnerProductType = "";
    private String valueEditTextPriceMin, valueEditTextPriceMax, valueEditTextAreaMin, valueEditTextAreaMax = "";
    private String _1preFinalUrl, _2finalCountryUrl, _3finalNbRoomsUrl, _4finalTypeProduct,
            _5finalPriceMinUrl, _6finalPriceMaxUrl, _7finalAreaMinUrl, _8finalAreaMaxUrl, _9finalFinalUrl = "";
    //pass string to RechercheListViewResult.java 's activity
    SharedPreferences mySharedPreferences;
    public final static String MY_PREFS = "MY_PREFS";
    int prefMode = Activity.MODE_PRIVATE;
    //spinner items
    private ArrayList<String> spinnerCountryItems, spinnerNbRoomsItems, spinnerProductTypeItems, spinnerProductTypeItemsTemp;
    private String typeProduitUrl;
    private StackTypesProduitsXml typesObj;

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
        setContentView(R.layout.activity_recherche);

        //get references
        switchVentes = (Switch) findViewById(R.id.switch_ventes);
        switchLocations = (Switch) findViewById(R.id.switch_locations);
        checkBoxCountry = (CheckBox) findViewById(R.id.checkBox_country);
        checkBoxNbRooms = (CheckBox) findViewById(R.id.checkBox_nb_rooms);
        checkBoxProductType = (CheckBox) findViewById(R.id.checkBox_product_type);
        checkBoxPrice = (CheckBox) findViewById(R.id.checkBox_price);
        checkBoxArea = (CheckBox) findViewById(R.id.checkBox_area);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_search_country);
        spinnerNbRooms = (Spinner) findViewById(R.id.spinner_search_number_of_rooms);
        spinnerProductType = (Spinner) findViewById(R.id.spinner_search_product_type);
        editTextPriceMin = (EditText) findViewById(R.id.edit_text_min_price);
        editTextPriceMax = (EditText) findViewById(R.id.edit_text_max_price);
        editTextAreaMin = (EditText) findViewById(R.id.edit_text_min_area);
        editTextAreaMax = (EditText) findViewById(R.id.edit_text_max_area);
        buttonReset = (Button) findViewById(R.id.button_recherche_reset);
        buttonSearch = (Button) findViewById(R.id.button_recherche_launch_research);

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

        /**sharedpreferences*/
        mySharedPreferences = getSharedPreferences(MY_PREFS, prefMode);
        endOfUrlLocations = getString(R.string.url_manipulation_search_location);
        endOfUrlVentes = getString(R.string.url_manipulation_search_vente);

        /**url*/
        urlBase = getString(R.string.url_manipulation_default);
        urlSearchDetailAppend = "&" + getString(R.string.content_search_detail_append);

        /**checkboxes listeners*/
        onClickCheckBoxCountryListener();
        onClickCheckBoxNbRoomsListener();
        onClickCheckBoxProductTypeListener();
        onClickCheckBoxPriceListener();
        onClickCheckBoxAreaListener();

        /**spinners*/
        populateSpinners();
        setupSpinners();

        /**edittext*/
        setupEditTexts();

        /**call switch listeners*/
        onClickSwitchVenteListener();
        onClickSwitchLocationsListener();

        /**call button listeners*/
        onClickResetListener();
        onClickSearchListener();
    }


    /***********************************************************************************************
     * SWITCH METHODS                                                                              *
     ***********************************************************************************************/
    /**
     * Method used to do something when switch is turned on or off.
     */
    public void onClickSwitchVenteListener() {
        switchVentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchVentes.isChecked()) {
                    switchLocations.setChecked(false);
                } else {
                    switchLocations.setChecked(true);
                }
            }
        });
    }

    /**
     * Method used to do something when switch is turned on or off.
     */
    public void onClickSwitchLocationsListener() {
        switchLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchLocations.isChecked()) {
                    switchVentes.setChecked(false);
                } else {
                    switchVentes.setChecked(true);
                }
            }
        });
    }
    /***********************************************************************************************
     * END OF SWITCH METHODS                                                                       *
     ***********************************************************************************************/


    /***********************************************************************************************
     * CHECKBOX METHODS                                                                            *
     ***********************************************************************************************/
    /**
     * Method used to do something when checkbox is checked.
     */
    public void onClickCheckBoxCountryListener() {
        checkBoxCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCountry.isChecked()) {
                    spinnerCountry.setEnabled(true);
                } else if (!checkBoxCountry.isChecked()) {
                    spinnerCountry.setEnabled(false);
                }
            }
        });
    }
    /**
     * Method used to do something when checkbox is checked.
     */
    public void onClickCheckBoxNbRoomsListener() {
        checkBoxNbRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxNbRooms.isChecked()) {
                    spinnerNbRooms.setEnabled(true);
                } else if (!checkBoxNbRooms.isChecked()) {
                    spinnerNbRooms.setEnabled(false);
                }
            }
        });
    }
    /**
     * Method used to do something when checkbox is checked.
     */
    public void onClickCheckBoxProductTypeListener() {
        checkBoxProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxProductType.isChecked()) {
                    spinnerProductType.setEnabled(true);
                } else if (!checkBoxProductType.isChecked()) {
                    spinnerProductType.setEnabled(false);
                }
            }
        });
    }
    /**
     * Method used to do something when checkbox is checked.
     */
    public void onClickCheckBoxPriceListener() {
        checkBoxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxPrice.isChecked()) {
                    editTextPriceMin.setEnabled(true);
                    editTextPriceMax.setEnabled(true);
                } else if (!checkBoxPrice.isChecked()) {
                    editTextPriceMin.setEnabled(false);
                    editTextPriceMax.setEnabled(false);
                }
            }
        });
    }
    /**
     * Method used to do something when checkbox is checked.
     */
    public void onClickCheckBoxAreaListener() {
        checkBoxArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxArea.isChecked()) {
                    editTextAreaMin.setEnabled(true);
                    editTextAreaMax.setEnabled(true);
                } else if (!checkBoxArea.isChecked()) {
                    editTextAreaMin.setEnabled(false);
                    editTextAreaMax.setEnabled(false);
                }
            }
        });
    }
    /***********************************************************************************************
     * END OF CHECKBOX METHODS                                                                     *
     ***********************************************************************************************/


    /***********************************************************************************************
     * BUTTON METHODS                                                                              *
     ***********************************************************************************************/
    /**
     * Method used to reset all fields from research activity when clicked.
     */
    public void onClickResetListener() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchVentes.setChecked(false);
                switchLocations.setChecked(false);
                checkBoxCountry.setChecked(false);
                checkBoxNbRooms.setChecked(false);
                checkBoxProductType.setChecked(false);
                checkBoxArea.setChecked(false);
                checkBoxPrice.setChecked(false);
                spinnerCountry.setEnabled(false);
                spinnerNbRooms.setEnabled(false);
                spinnerProductType.setEnabled(false);
                editTextPriceMin.setText("");
                editTextPriceMax.setText("");
                editTextAreaMin.setText("");
                editTextAreaMax.setText("");
                editTextPriceMin.setEnabled(false);
                editTextPriceMax.setEnabled(false);
                editTextAreaMin.setEnabled(false);
                editTextAreaMax.setEnabled(false);
                Toast.makeText(Recherche.this, getString(R.string.content_search_reset_search), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Method used to launch a new activity with a ListView containing all elements that are included
     * in the search.
     */
    public void onClickSearchListener() {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if either sales or rentals are toggled on
                if (switchVentes.isChecked() && !switchLocations.isChecked()) {
                    _1preFinalUrl = appendEndOfUrlSwitch(urlBase, endOfUrlVentes);
                    isCorrectSwicth = true;
                } else if (!switchVentes.isChecked() && switchLocations.isChecked()) {
                    _1preFinalUrl = appendEndOfUrlSwitch(urlBase, endOfUrlLocations);
                    isCorrectSwicth = true;
                }

                //get country value from dropdown menu
                if (checkBoxCountry.isChecked()) {
                    selectedTag = "pays";
                    valueSpinnerCountry = spinnerCountry.getSelectedItem().toString();
                    finalValueSpinnerCountry = getSpinnerValue(selectedTag, valueSpinnerCountry);
                    _2finalCountryUrl = appendEndOfUrlSpinner(_1preFinalUrl, selectedTag, finalValueSpinnerCountry);
                } else {
                    _2finalCountryUrl = _1preFinalUrl;
                }

                //get number of rooms from dropdown menu
                if (checkBoxNbRooms.isChecked()) {
                    _3finalNbRoomsUrl = _2finalCountryUrl;
                    selectedTag = "numPieces";
                    valueSpinnerNbRooms = spinnerNbRooms.getSelectedItem().toString();
                    finalValueSpinnerNbRooms = getSpinnerValue(selectedTag, valueSpinnerNbRooms);
                    _3finalNbRoomsUrl = appendEndOfUrlSpinner(_2finalCountryUrl, selectedTag, finalValueSpinnerNbRooms);
                } else {
                    _3finalNbRoomsUrl = _2finalCountryUrl;
                }

                //get product type from dropdown menu
                if (checkBoxProductType.isChecked()) {
                    _4finalTypeProduct = _3finalNbRoomsUrl;
                    selectedTag = "typeProduit";
                    valueSpinnerProductType = spinnerProductType.getSelectedItem().toString();
                    finalValueSpinnerProductType = getSpinnerValue(selectedTag, valueSpinnerProductType);
                    _4finalTypeProduct = appendEndOfUrlSpinner(_3finalNbRoomsUrl, selectedTag, finalValueSpinnerProductType);
                } else {
                    _4finalTypeProduct = _3finalNbRoomsUrl;
                }

                //get price range from dropdown menu
                if (checkBoxPrice.isChecked()) {
                    _5finalPriceMinUrl = _4finalTypeProduct;
                    selectedTag = "prix_min";
                    valueEditTextPriceMin = editTextPriceMin.getText().toString();
                    _5finalPriceMinUrl = appendEndOfUrlSpinner(_4finalTypeProduct, selectedTag, valueEditTextPriceMin);

                    _6finalPriceMaxUrl = _5finalPriceMinUrl;
                    selectedTag = "prix_max";
                    valueEditTextAreaMax = editTextPriceMax.getText().toString();
                    _6finalPriceMaxUrl = appendEndOfUrlSpinner(_5finalPriceMinUrl, selectedTag, valueEditTextAreaMax);
                } else {
                    _6finalPriceMaxUrl = _4finalTypeProduct;
                }

                //get area from dropdown menu
                if (checkBoxArea.isChecked()) {
                    _7finalAreaMinUrl = _6finalPriceMaxUrl;
                    selectedTag = "superficie_min";
                    valueEditTextAreaMin = editTextAreaMin.getText().toString();
                    _7finalAreaMinUrl = appendEndOfUrlSpinner(_6finalPriceMaxUrl, selectedTag, valueEditTextAreaMin);

                    _8finalAreaMaxUrl = _7finalAreaMinUrl;
                    selectedTag = "superficie_max";
                    valueEditTextAreaMax = editTextAreaMax.getText().toString();
                    _8finalAreaMaxUrl = appendEndOfUrlSpinner(_7finalAreaMinUrl, selectedTag, valueEditTextAreaMax);
                } else {
                    _8finalAreaMaxUrl = _6finalPriceMaxUrl;
                }

                //launch new activity with newly built url
                if (isCorrectSwicth) {
                    isCorrectSwicth = false;
                    _9finalFinalUrl = _8finalAreaMaxUrl;
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("urlSearchKey", _9finalFinalUrl);
                    editor.commit();
                    Intent intentSearchResult = new Intent(Recherche.this, RechercheListViewResult.class);
                    startActivity(intentSearchResult);
                } else {
                    Toast.makeText(Recherche.this, getString(R.string.content_search_invalid_search), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /***********************************************************************************************
     * END OF BUTTON METHODS                                                                       *
     ***********************************************************************************************/




    /***********************************************************************************************
     * START OF URL BUILDING MENU METHODS                                                          *
     ***********************************************************************************************/
    /**
     * Method used to append a String to the end of another.
     *
     * @param prePreFinalString main String to which a second String is going to be appended
     * @param toAppend          String that is going to be appended to the end of the main one
     * @return full String (original + append)
     */
    public String appendEndOfUrlSwitch(String prePreFinalString, String toAppend) {
        StringBuilder stringBuilderSwitch = new StringBuilder(prePreFinalString);
        stringBuilderSwitch.append(toAppend);
        return stringBuilderSwitch.toString();
    }
    /**
     * Method used to append a String to the end of another.
     *
     * @param preFinalString main String to which a second String is going to be appended
     * @param selectedTag    tag
     * @param valueToAppend  String that is going to be appended to the end of the main one
     * @return full String (original + append)
     */
    public String appendEndOfUrlSpinner(String preFinalString, String selectedTag, String valueToAppend) {
        StringBuilder stringBuilderSpinner = new StringBuilder(preFinalString);
        stringBuilderSpinner.append(urlSearchDetailAppend + "[" + selectedTag + "]=" + valueToAppend);
        return stringBuilderSpinner.toString();
    }
    /***********************************************************************************************
     * END OF URL BUILDING MENU METHODS                                                            *
     ***********************************************************************************************/




    /***********************************************************************************************
     * START OF SPINNER MENU METHODS                                                               *
     ***********************************************************************************************/
    /**
     * Method used to link spinner fields value with its id in the xml feed.
     * @param selectedTag
     * @param spinnerValue
     * @return
     */
    public String getSpinnerValue(String selectedTag, String spinnerValue) {
        //http://api.immotoolbox.com/xml/2.0/MCRE/typesproduits/?_locale=fr
        String newValue = "";
        spinnerValue = spinnerValue.trim();
        //Log.i("spinner value= ", spinnerValue);
        if (selectedTag.equalsIgnoreCase("pays")) {
            if (spinnerValue.equalsIgnoreCase("Monaco")) {
                newValue = "1";
            } else if (spinnerValue.equalsIgnoreCase("France")) {
                newValue = "2";
            } else if (spinnerValue.equalsIgnoreCase("Italie")) {
                newValue = "3";
            } else if (spinnerValue.equalsIgnoreCase("États-Unis")) {
                newValue = "4";
            }
        } else if (selectedTag.equalsIgnoreCase("numPieces")) {
            if (spinnerValue.equalsIgnoreCase("Studio")) {
                newValue = "1";
            } else if (spinnerValue.equalsIgnoreCase("2 rooms")) {
                newValue = "2";
            } else if (spinnerValue.equalsIgnoreCase("3 rooms")) {
                newValue = "3";
            } else if (spinnerValue.equalsIgnoreCase("4 rooms")) {
                newValue = "4";
            } else if (spinnerValue.equalsIgnoreCase("5 rooms")) {
                newValue = "5";
            } else if (spinnerValue.equalsIgnoreCase("5+ rooms")) {
                newValue = "10";
            }
        } else if (selectedTag.equalsIgnoreCase("typeProduit")) {
            if (spinnerValue.equalsIgnoreCase("Appartement") || spinnerValue.equalsIgnoreCase("Apartment") || spinnerValue.equalsIgnoreCase("Appartamento")) {
                newValue = "1";
            } else if (spinnerValue.equalsIgnoreCase("Penthouse")) {
                newValue = "12";
            } else if (spinnerValue.equalsIgnoreCase("Boutique") || spinnerValue.equalsIgnoreCase("Shop") || spinnerValue.equalsIgnoreCase("Negozio")) {
                newValue = "3";
            } else if (spinnerValue.equalsIgnoreCase("Bureau") || spinnerValue.equalsIgnoreCase("Office") || spinnerValue.equalsIgnoreCase("Ufficio")) {
                newValue = "5";
            } else if (spinnerValue.equalsIgnoreCase("Cave") || spinnerValue.equalsIgnoreCase("Cellar") || spinnerValue.equalsIgnoreCase("Cantina")) {
                newValue = "6";
            } else if (spinnerValue.equalsIgnoreCase("Immeuble") || spinnerValue.equalsIgnoreCase("Building") || spinnerValue.equalsIgnoreCase("Edificio")) {
                newValue = "8";
            } else if (spinnerValue.equalsIgnoreCase("Local") || spinnerValue.equalsIgnoreCase("Premises") || spinnerValue.equalsIgnoreCase("Locale")) {
                newValue = "9";
            } else if (spinnerValue.equalsIgnoreCase("Réserve")) {
                newValue = "32";
            } else if (spinnerValue.equalsIgnoreCase("Loft")) {
                newValue = "10";
            } else if (spinnerValue.equalsIgnoreCase("Parking / Garage / Box") || spinnerValue.equalsIgnoreCase("Parcheggio / Garage / Box")) {
                newValue = "11";
            } else if (spinnerValue.equalsIgnoreCase("Roof")) {
                newValue = "26";
            } else if (spinnerValue.equalsIgnoreCase("Duplex")) {
                newValue = "27";
            } else if (spinnerValue.equalsIgnoreCase("Duplex-Roof")) {
                newValue = "30";
            } else if (spinnerValue.equalsIgnoreCase("Terrain") || spinnerValue.equalsIgnoreCase("Land") || spinnerValue.equalsIgnoreCase("Terreno")) {
                newValue = "13";
            } else if (spinnerValue.equalsIgnoreCase("Villa") || spinnerValue.equalsIgnoreCase("Ville")) {
                newValue = "14";
            } else if (spinnerValue.equalsIgnoreCase("Chambre de bonne") || spinnerValue.equalsIgnoreCase("Maid room") || spinnerValue.equalsIgnoreCase("Camera di servizio")) {
                newValue = "16";
            } else if (spinnerValue.equalsIgnoreCase("Fonds de commerce") || spinnerValue.equalsIgnoreCase("Commercial enterprise") || spinnerValue.equalsIgnoreCase("Commercio")) {
                newValue = "18";
            } else if (spinnerValue.equalsIgnoreCase("Murs local commercial")) {
                newValue = "24";
            } else if (spinnerValue.equalsIgnoreCase("Cession de droit au bail")) {
                newValue = "22";
            } else if (spinnerValue.equalsIgnoreCase("Autre produit")) {
                newValue = "31";
            }
        }
        return newValue;
    }
    /**
     * Method used to populate the dropdown menus of the spinners.
     */
    public void populateSpinners() {
        spinnerCountryItems = new ArrayList<String>();
        spinnerCountryItems.add("Monaco");
        spinnerCountryItems.add("France");
        spinnerCountryItems.add("Italie");
        spinnerCountryItems.add("États-Unis");

        spinnerNbRoomsItems = new ArrayList<String>();
        spinnerNbRoomsItems.add("Studio");
        spinnerNbRoomsItems.add("2 rooms");
        spinnerNbRoomsItems.add("3 rooms");
        spinnerNbRoomsItems.add("4 rooms");
        spinnerNbRoomsItems.add("5 rooms");
        spinnerNbRoomsItems.add("5+ rooms");
        spinnerNbRoomsItems.add("Open plan office");
        spinnerNbRoomsItems.add("Other properties");

        typeProduitUrl = getString(R.string.content_search_typeproduits_url);
        typesObj = new StackTypesProduitsXml(typeProduitUrl);
        typesObj.fetchTypesXml();
        spinnerProductTypeItems = typesObj.getType();
        spinnerProductTypeItems = new ArrayList<>();

        //wait for thread in StackTypeProduitsXml.java to finish
        Thread t;
        t = typesObj.getThread();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        spinnerProductTypeItemsTemp = typesObj.getType();
        //Log.i("retrieved product type:", typesObj.getType().toString());

        for (int i = 0; i < spinnerProductTypeItemsTemp.size(); i++) {
            spinnerProductTypeItems.add(spinnerProductTypeItemsTemp.get(i));
        }

        ArrayAdapter<String> spinnerCountryItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerCountryItems);
        ArrayAdapter<String> spinnerNbRoomsItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerNbRoomsItems);
        ArrayAdapter<String> spinnerProductTypeItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerProductTypeItems);

        spinnerCountry.setAdapter(spinnerCountryItemsAdapter);
        spinnerNbRooms.setAdapter(spinnerNbRoomsItemsAdapter);
        spinnerProductType.setAdapter(spinnerProductTypeItemsAdapter);
    }
    /**
     * Method used to set up all the spinners as disabled when the activity is launched. They are only
     * activated once their corresponding checkbox is checked.
     */
    public void setupSpinners() {
        spinnerCountry.setEnabled(false);
        spinnerNbRooms.setEnabled(false);
        spinnerProductType.setEnabled(false);
    }
    public void setupEditTexts() {
        editTextPriceMin.setEnabled(false);
        editTextPriceMax.setEnabled(false);
        editTextAreaMin.setEnabled(false);
        editTextAreaMax.setEnabled(false);
    }
    /***********************************************************************************************
     * END OF SPINNER MENU METHODS                                                                 *
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
            startActivity(new Intent(Recherche.this, MentionsLegales.class));
        } else if (id == R.id.action_a_propos) {
            startActivity(new Intent(Recherche.this, APropos.class));
        } else if (id == R.id.action_aide) {
            startActivity(new Intent(Recherche.this, Aide.class));
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
            startActivity(new Intent(Recherche.this, Ventes.class));
        } else if (id == R.id.nav_accueil) {
            startActivity(new Intent(Recherche.this, Home.class));
        } else if (id == R.id.nav_locations) {
            startActivity(new Intent(Recherche.this, Locations.class));
        } else if (id == R.id.nav_favoris) {
            startActivity(new Intent(Recherche.this, Favoris.class));
        } else if (id == R.id.nav_recherche) {
            //do nothing = close drawer
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(Recherche.this, Contact.class));
        } else if (id == R.id.nav_social) {
            startActivity(new Intent(Recherche.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/
}
