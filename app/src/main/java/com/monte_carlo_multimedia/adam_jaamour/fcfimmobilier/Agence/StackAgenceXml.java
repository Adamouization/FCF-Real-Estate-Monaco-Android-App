package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Data object that holds all of our information about an agency's contact information.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 06/07/2016
 * @release_date : 29/07/2016
 * @see : Contact.java , XMLResponse.java
 */
public class StackAgenceXml {

    /**
     * fields
     */
    Thread threadFetchXmlAgency;
    private String urlAgenceStringXml = null;
    private String nom = "nom";
    private String adresse = "adresse";
    private String codepostal = "codepostal";
    private String ville = "ville";
    private String pays = "pays";
    private String lat = "lat";
    private String lng = "lng";
    private String tel = "tel";
    private String fax = "fax";
    private String email = "email";
    private String web = "url";
    private String logo = "logo";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = false;

    /**
     * Constructor.
     *
     * @param url
     */
    public StackAgenceXml(String url) {
        this.urlAgenceStringXml = url;
    }

    /**
     * parse retrieved xml data for agency info
     * @param agenceParser
     */
    public void parseXmlAgenceAndStore(XmlPullParser agenceParser) {
        int event;
        String text = null;

        try {
            event = agenceParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = agenceParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = agenceParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("nom")) {
                            nom = text;
                        } else if (name.equals("adresse")) {
                            //adresse = agenceParser.getAttributeValue(null, "value");
                            adresse = text;
                        } else if (name.equals("codepostal")) {
                            codepostal = text;
                        } else if (name.equals("ville")) {
                            ville = text;
                        } else if (name.equals("pays")) {
                            pays = text;
                        } else if (name.equals("lat")) {
                            lat = text;
                        } else if (name.equals("lng")) {
                            lng = text;
                        } else if (name.equals("tel")) {
                            tel = text;
                        } else if (name.equals("fax")) {
                            fax = text;
                        } else if (name.equals("email")) {
                            email = text;
                        } else if (name.equals("url")) {
                            web = text;
                        } else if (name.equals("logo")) {
                            logo = text;
                        } else {
                        }
                        break;
                }
                event = agenceParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.i("ex of agency info = ", nom + adresse + codepostal + ville + pays + lat + lng + tel +
                //fax + email + web + logo);
    }

    /**
     * fetch data from xml url before parsing
     */
    public void fetchTypesXml(final XMLResponse listener) {
        threadFetchXmlAgency = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAgenceStringXml);
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

                    parseXmlAgenceAndStore(myparser);
                    stream.close();
                    //Log.i("test", adresse + " " + codepostal + " " + pays + " " + tel + " " + fax + " " + web);
                    listener.sendXMLDataAgency(adresse, codepostal, pays, tel, fax, web);
                    listener.sendXMLDataLatLng(lat, lng);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        threadFetchXmlAgency.start();
    }

    /**
     * GETTERS AND SETTERS*
     */
    public String getLogo() {
        return logo;
    }

    public Thread getThread(){
        return threadFetchXmlAgency;
    }
}
