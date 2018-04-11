package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence;

/**
 * Interface with methods to set String values for agency contact information.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 07/07/2016
 * @release_date : 29/07/2016
 * @see : Contact.java , StackAgenceXml
 */
public interface XMLResponse {

    void sendXMLDataAgency(String adresse, String codepostal, String pays, String telephone, String fax, String website);

    void sendXMLDataLatLng(String lat, String lng);
}
