package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Helper class for downloading a file. In this case, used to download the XML file. Allows
 * for offline mode.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 20/06/2016
 * @release_date : 29/07/2016
 */
public class Downloader {

    /**fields*/
    private static String myTag = "StackProduits";  //Tag for Log statements
    static final int POST_PROGRESS = 1; //Handler msg that represents we are posting a progress update.

    /************************************************
     * Download a file from the Internet and store it locally
     *
     * @param URL - the url of the file to download
     * @param fos - a FileOutputStream to save the downloaded file to.
     ************************************************/
    public static void DownloadFromUrl(String URL, FileOutputStream fos) {  //this is the downloader method
        try {
            URL url = new URL(URL); //URL of the file

            /**keep the start time so we can display how long it took to the Log.*/
            long startTime = System.currentTimeMillis();
            //Log.d(myTag, "Download beginning");

            /** Open a connection to that URL.*/
            URLConnection ucon = url.openConnection();

            int lenghtOfFile = ucon.getContentLength(); // useful to show a typical 0-100% progress bar

            //Log.i(myTag, "Opened Connection"); //todo - log opened connection

            /**Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            //Log.i(myTag, "Got InputStream and BufferedInputStream"); //todo - log get input stream and bufferedinputstream

            /** Define OutputStreams to write to our file.*/
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //Log.i(myTag, "Got FileOutputStream and BufferedOutputStream"); //todo - log get fileoutputstream and bufferedinputstream

            /** Start reading the and writing our file.*/
            byte data[] = new byte[1024];
            //long total = 0;
            int count;
            while ((count = bis.read(data)) != -1) {    //loop and read the current chunk
                //keep track of size for progress.
                //total += count;
                //write this chunk
                bos.write(data, 0, count);
            }

            /**Have to call flush or the  file can get corrupted.*/
            bos.flush();
            bos.close();
            //Log.d(myTag, "download ready in " + ((System.currentTimeMillis() - startTime)) + " milisec"); //todo - log ready in <time>
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

