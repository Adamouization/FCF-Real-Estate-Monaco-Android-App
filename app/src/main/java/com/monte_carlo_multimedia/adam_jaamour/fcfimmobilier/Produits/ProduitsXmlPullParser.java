package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class with 1 static method to parse XML file and return a list of StackProduits objects.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 20/06/2016
 * @release_date : 29/07/2016
 * @see : StackProduits.java
 */
public class ProduitsXmlPullParser {

    /**
     * fields
     */
    //KEYS used to analyse the tags from the XML feed and retrieve their plain text
    static final String KEY_COUNT = "count";
    static final String KEY_PRODUIT = "produit";
    static final String KEY_REFERENCE = "reference";
    static final String KEY_DATE_CREATED = "datecreation";
    static final String KEY_DATE_LAST_MODIFIED = "datemodification";
    static final String KEY_TYPE_TRANSACTION = "typetransaction";
    static final String KEY_COUNTRY = "pays";
    static final String KEY_CITY = "ville";
    static final String KEY_DISTRICT = "quartier";
    static final String KEY_BUILDING = "immeuble";
    static final String KEY_LAT = "latitude";
    static final String KEY_LON = "longitude";
    static final String KEY_TYPE = "typeproduit";
    static final String KEY_NB_ROOMS = "numpieces";
    static final String KEY_CELLAR = "numcaves";    //todo find cellar tag in xml feed
    static final String KEY_PARKING = "numparkings";    //todo find parking tag in xml feed
    static final String KEY_TOTAL_AREA = "superficietotale";
    static final String KEY_LIVING_AREA = "superficiehabitable";
    static final String KEY_TERRACE_AREA = "superficieterrasse";
    static final String KEY_PRICE = "prix";
    static final String KEY_DESIGNATION = "designation";
    static final String KEY_ABOUT = "accroche";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_IMAGE_URL = "image";
    static final String KEY_IMAGE_URL_THUMB = "thumb";
    static final String KEY_IMAGE_URL_FULL = "full";
    //boolean to retrieve first thumb image for listview
    private static boolean firstImageUrlRetrieved = true;
    private static boolean firstLatRetrieved = true;
    private static boolean firstLngRetrieved = true;

    /**
     * Method used to get all the text needed to define a StackProduits class object.
     *
     * @param ctx
     * @return stackProduits
     * populated list
     */
    public static List<StackProduits> getStackProduitFromFile(Context ctx) {

        /**
         * fields
         */
        /*List of StackProduits that we will return*/
        List<StackProduits> stackProduits;
        stackProduits = new ArrayList<StackProduits>();

        /*temporary holder for current StackProduits while parsing*/
        StackProduits curStackProduit = null;

        /*temporary holder for current text value while parsing*/
        String curText = "";

        /**parsing*/
        try {
            // Get ParserFactory and PullParser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();    //used to parse XML file

            // Open up InputStream and Reader of our file.
            FileInputStream fis = ctx.openFileInput("StackSites.xml");  //goes to downloaded file (in internal storage, or external storage if device has external storage (SD card)
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // point the parser to our file and pass buffered reader
            xpp.setInput(reader);

            // get initial eventType
            int eventType = xpp.getEventType(); //represent what kind of event happens, then loop through each event

            // Loop through pull events until we reach END_DOCUMENT (event)
            while (eventType != XmlPullParser.END_DOCUMENT) {   //loop until we get to end of document
                // Get the current tag and store tag name in String tagname
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType) {    // = what type of event did we get?

                    case XmlPullParser.START_TAG:   //start of tag
                        if (tagname.equalsIgnoreCase(KEY_PRODUIT)) {
                            /*If we are starting a new <produit> block we need
                            a new StackProduits object to represent it*/
                            firstImageUrlRetrieved = true;
                            firstLatRetrieved = true;
                            firstLngRetrieved = true;
                            curStackProduit = new StackProduits(); //starting a new "produit"
                        }
                        break;

                    case XmlPullParser.TEXT:    //text between start_tag and end_tag
                        //grab the current text so we can use it in END_TAG event
                        curText = xpp.getText();    //store string from text inside string variable
                        break;

                    case XmlPullParser.END_TAG: //end of tag, done with this current tag
                        assert curStackProduit != null;
                        if (tagname.equalsIgnoreCase(KEY_PRODUIT)) {
                            // if <produit/> then we are done with current product add it to the list.
                            stackProduits.add(curStackProduit);   //if at end of block, done with one full "produit" so add object to list
                        } else if (tagname.equalsIgnoreCase(KEY_DESIGNATION)) {
                            curStackProduit.setDesignation(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_BUILDING)) {
                            curStackProduit.setBuilding(curText);  //set value from curText
                        } else if (tagname.equalsIgnoreCase(KEY_ABOUT)) {
                            curStackProduit.setAbout(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL_FULL)) {
                            curStackProduit.setImgUrlFullMul(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_IMAGE_URL_THUMB)) {
                            if (firstImageUrlRetrieved) {
                                curStackProduit.setImgUrl(curText); // if <image/> use setImgUrl() on curSite
                                firstImageUrlRetrieved = false; //take only first image from feed for ListView
                            }
                            curStackProduit.setImgUrlThumbMul(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_PRICE)) {
                            curStackProduit.setPrice(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_TOTAL_AREA)) {
                            curStackProduit.setArea(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DESCRIPTION)) {
                            curStackProduit.setDescription(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_REFERENCE)) {
                            curStackProduit.setReference(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_TYPE)) {
                            curStackProduit.setType(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_NB_ROOMS)) {
                            curStackProduit.setNumberOfRooms(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LIVING_AREA)) {
                            curStackProduit.setLivingArea(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_TERRACE_AREA)) {
                            curStackProduit.setTerraceArea(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DISTRICT)) {
                            curStackProduit.setDistrict(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_PARKING)) {
                            curStackProduit.setParking(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_CELLAR)) {
                            curStackProduit.setCellar(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DATE_CREATED)) {
                            curStackProduit.setDateCreated(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_DATE_LAST_MODIFIED)) {
                            curStackProduit.setDateLastModified(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_TYPE_TRANSACTION)) {
                            curStackProduit.setTypeTransaction(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_COUNTRY)) {
                            curStackProduit.setCountry(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_CITY)) {
                            curStackProduit.setCity(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LAT)) {
                            if(firstLatRetrieved){
                                curStackProduit.setLatitude(curText);
                                firstLatRetrieved = false;
                            }
                        } else if (tagname.equalsIgnoreCase(KEY_LON)) {
                            if(firstLngRetrieved){
                                curStackProduit.setLongitude(curText);
                                firstLngRetrieved = false;
                            }
                        } else if (tagname.equalsIgnoreCase(KEY_CELLAR)) {
                            curStackProduit.setCellar(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_PARKING)) {
                            curStackProduit.setParking(curText);
                        }
                        break;

                    default:
                        break;
                }
                //move on to next iteration (the next event type in order to go through entire document)
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();    //print exception
        }
        //Log.i("values xmlpull parser=", curStackProduit.getTypeTransaction() + curStackProduit.getCity() + curStackProduit.getLatitude());
        return stackProduits;  // return the populated list.
    }
}