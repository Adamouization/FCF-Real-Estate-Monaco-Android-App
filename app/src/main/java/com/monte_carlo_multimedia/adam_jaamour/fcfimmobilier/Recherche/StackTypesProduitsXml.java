package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Data object that holds all of our information about a type of product.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 04/07/2016
 * @release_date : 29/07/2016
 * @see : Recherche.kjava
 */
public class StackTypesProduitsXml{

    /**
     * fields
     */
    Thread threadfetchTypeProduits;
    private String urlTypesString = null;
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<String> types = new ArrayList<String>();
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;

    /**
     * Constructor.
     * @param url
     */
    public StackTypesProduitsXml(String url) {
        this.urlTypesString = url;
    }

    /**
     * Parser to retrieve all fields from xml feed.
     * @param typeParser
     */
    public void parseXmlTypeProduitsAndStore(XmlPullParser typeParser) {
        int event;
        String text = null;

        try {
            event = typeParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = typeParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = typeParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("type")) {
                            type.add(text);
                        } else if (name.equals("types")) {
                            types.add(text);
                        } else {
                        }
                        break;
                }
                event = typeParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.i("arraylist of type of products = ", type.toString()); //todo log: arraylist of product types retrieved
    }

    /**
     * Method used to retrieve the data from the xml feed before parsing it.
     */
    public void fetchTypesXml() {
        threadfetchTypeProduits = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlTypesString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /*ms*/);
                    conn.setConnectTimeout(15000 /*ms*/);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXmlTypeProduitsAndStore(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threadfetchTypeProduits.start();
    }

    /**
     * GETTERS AND SETTERS
     */
    public ArrayList<String> getType(){
        return type;
    }

    public ArrayList<String> getTypes(){
        return types;
    }

    public Thread getThread(){
        return threadfetchTypeProduits;
    }
}
