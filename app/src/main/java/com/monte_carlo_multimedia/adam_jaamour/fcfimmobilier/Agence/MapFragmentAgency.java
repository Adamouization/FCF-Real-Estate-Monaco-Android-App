package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence;

import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

//http://code.tutsplus.com/tutorials/getting-started-with-google-maps-for-android-basics--cms-24635

/**
 * ConnectionCallbacks and OnConnectionFailedListener are designed to monitor the state of the
 * GoogleApiClient, which is used in this application for getting the user's current location.
 * OnInfoWindowClickListener is triggered when the user clicks on the info window that pops up over
 * a marker on the map. OnMapLongClickListener and OnMapClickListener are triggered when the user
 * either taps or holds down on a portion of the map. OnMarkerClickListener is called when the user
 * clicks on a marker on the map, which typically also displays the info window for that marker.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 12/07/2016
 * @release_date : 29/07/2016
 * @see : Contact.java
 */
public class MapFragmentAgency extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    /**
     * fields
     */
    //mGoogleApiClient and mCurrentLocation are used for getting the user's location for initializing the map camera
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    //MAP_TYPES and curMapTypeIndex are used in the sample code for switching between different map display types.
    //Each of the map types serves a different purpose, so one or more may be suitable for your own applications.
    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE, //displays a satellite view of the area without street names or labels.
            GoogleMap.MAP_TYPE_NORMAL,  //shows a generic map with street names and labels.
            GoogleMap.MAP_TYPE_HYBRID,  //combines satellite and normal mode, displaying satellite images of an area with all labels.
            GoogleMap.MAP_TYPE_TERRAIN, //is similar to a normal map, but textures are added to display changes in elevation in the environment.
            GoogleMap.MAP_TYPE_NONE};  //is similar to a normal map, but doesn't display any labels or coloration for the type of environment in an area.
    private int curMapTypeIndex = 0;
    double latitude = 43.74358400;
    double longitude = 7.42828700;

    /**
     * create GoogleApiClient and initiate LocationServices in order to get user's current location.
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initListeners();
    }

    /**
     * Binds the interfaces declared at the top of the class with the GoogleMap object associated
     * with SupportMapFragment.
     */
    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        initCamera(latitude, longitude);
    }

    /**
     * Initialize the camera and some basic map properties. Create a CameraPosition object through
     * the CameraPosition.Builder, with a target set for the latitude and longitude of  user and a
     * set zoom level. Tilt and bearing are used here at their default values to illustrate that
     * they are available options. Once we have a CameraPosition object, we can animate the map
     * camera to that position using the CameraUpdateFactory.
     *
     * @param lat
     * @param lng
     */
    //private void initCamera(Location location){
    private void initCamera(double lat, double lng) {
        CameraPosition position = CameraPosition.builder()
                //.target(new LatLng(location.getLatitude(), location.getLongitude()))
                .target(new LatLng(lat, lng))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();
        getMap().animateCamera(CameraUpdateFactory.newCameraPosition(position), null);

        getMap().addMarker(new MarkerOptions().anchor(0.0f, 1.0f).position(new LatLng(lat, lng))//put marker on map for location
                .title(getString(R.string.app_name)).icon(BitmapDescriptorFactory.defaultMarker())).showInfoWindow();

        //set map properties
        getMap().setMapType(MAP_TYPES[curMapTypeIndex]);    //set map type
        getMap().setTrafficEnabled(false);   //enable live traffic
        getMap().getUiSettings().setZoomControlsEnabled(true); //adds + and - buttons to change zoom level
        getMap().getUiSettings().setAllGesturesEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng);
        //options.title(getString(R.string.app_name));
        options.title(getAddressFromLatLng(latLng));
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
        getMap().addMarker(options);
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}
