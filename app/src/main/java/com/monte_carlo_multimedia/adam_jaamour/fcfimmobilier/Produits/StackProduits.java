package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Data object that holds all of our information about a product.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 20/06/2016
 * @release_date : 29/07/2016
 * @see : ProduitsXmlPullParser.java
 */
public class StackProduits implements Parcelable {

    /**
     * fields
     */
    private String count;
    private String reference;
    private String dateCreated;
    private String dateLastModified;
    private String typeTransaction; //todo fix
    private String country; //todo fix
    private String city; //todo fix
    private String district;
    private String building;
    private String latitude;    //todo add
    private String longitude;   //todo add
    private String type;
    private String numberOfRooms;
    private String cellar;
    private String parking;
    private String area;
    private String livingArea;
    private String terraceArea;
    private String price;
    private String designation;
    private String about;   //accroche
    private String description;
    private String imgUrl;  //first thumbnail
    private ArrayList<String> imgUrlThumbMul = new ArrayList<String>();
    private ArrayList<String> imgUrlFullMul = new ArrayList<String>();
    private String isAddedAsFav = "0";

    /**
     * Constructor.
     */
    public StackProduits() {
        super();
    }

    /**
     * Constructor to initialize fields.
     *
     * @param parcel
     */
    public StackProduits(Parcel parcel) {
        this.count = parcel.readString();
        this.reference = parcel.readString();
        this.dateCreated = parcel.readString();
        this.dateLastModified = parcel.readString();
        this.typeTransaction = parcel.readString();
        this.country = parcel.readString();
        this.city = parcel.readString();
        this.district = parcel.readString();
        this.building = parcel.readString();
        this.latitude = parcel.readString();
        this.longitude = parcel.readString();
        this.type = parcel.readString();
        this.numberOfRooms = parcel.readString();
        this.cellar = parcel.readString();
        this.parking = parcel.readString();
        this.area = parcel.readString();
        this.livingArea = parcel.readString();
        this.terraceArea = parcel.readString();
        this.price = parcel.readString();
        this.designation = parcel.readString();
        this.about = parcel.readString();
        this.description = parcel.readString();
        this.imgUrl = parcel.readString();
        imgUrlThumbMul = (ArrayList<String>) parcel.readSerializable();
        imgUrlFullMul = (ArrayList<String>) parcel.readSerializable();
        this.isAddedAsFav = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method used to write the values of the fields to the parcel.
     *
     * @param parcel
     * @param flag
     */
    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(count);
        parcel.writeString(reference);
        parcel.writeString(dateCreated);
        parcel.writeString(dateLastModified);
        parcel.writeString(typeTransaction);
        parcel.writeString(country);
        parcel.writeString(city);
        parcel.writeString(district);
        parcel.writeString(building);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(type);
        parcel.writeString(numberOfRooms);
        parcel.writeString(cellar);
        parcel.writeString(parking);
        parcel.writeString(area);
        parcel.writeString(livingArea);
        parcel.writeString(terraceArea);
        parcel.writeString(price);
        parcel.writeString(designation);
        parcel.writeString(about);
        parcel.writeString(description);
        parcel.writeString(imgUrl);
        parcel.writeSerializable(imgUrlThumbMul);
        parcel.writeSerializable(imgUrlFullMul);
        parcel.writeSerializable(isAddedAsFav);
    }

    /**
     * Parcelable for StackProduits class objects.
     */
    public static Parcelable.Creator<StackProduits> CREATOR = new Parcelable.Creator<StackProduits>() {
        @Override
        public StackProduits createFromParcel(Parcel source) {
            return new StackProduits(source);
        }

        @Override
        public StackProduits[] newArray(int size) {
            return new StackProduits[size];
        }
    };

    /*
    @Override
    public String toString() {
        return "StackProduits [imgUrl= " + imgUrlThumbMul + ", imgUrl=" + imgUrl + ", designation=" +
                designation + ", about=" + about + ", price=" + price + ", area=" + area + ", description=" +
                description + ", reference=" + reference + ", type=" + type + ", numberOfRooms=" +
                numberOfRooms + ", livingArea=" + livingArea + ", terraceArea=" + terraceArea +
                ", building=" + building + ", district=" + district + ", parking=" + parking +
                ", cellar=" + cellar + ", typeTransaction=" + typeTransaction + ", city=" + city +
                ", country=" + country + ", date created=" + dateCreated + ", date last modified=" +
                dateLastModified + "]";
    }*/

    /**
     * GETTERS AND SETTERS
     */
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(String livingArea) {
        this.livingArea = livingArea;
    }

    public String getTerraceArea() {
        return terraceArea;
    }

    public void setTerraceArea(String terraceArea) {
        this.terraceArea = terraceArea;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getCellar() {
        return cellar;
    }

    public void setCellar(String cellar) {
        this.cellar = cellar;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(String dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setImgUrlThumbMul(String imgUrl) {
        imgUrlThumbMul.add(imgUrl);
    }

    public ArrayList<String> getImgUrlThumbMul() {
        return imgUrlThumbMul;
    }

    public void setImgUrlFullMul(String imgUrl) {
        imgUrlFullMul.add(imgUrl);
    }

    public ArrayList<String> getImgUrlFullMul() {
        return imgUrlFullMul;
    }

    public String getIsAddedAsFav() {
        return isAddedAsFav;
    }

    public void setIsAddedAsFav(String isAddedAsFav) {
        this.isAddedAsFav = isAddedAsFav;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}