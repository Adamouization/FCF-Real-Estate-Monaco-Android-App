package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Contact;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Favoris;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.APropos;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.Aide;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Informations.MentionsLegales;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Locations;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Home;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Recherche.Recherche;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Agence.Social;
import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.MainActivities.Ventes;

import junit.framework.Assert;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Class containing the methods used to create a functional activity to show a gallery of pictures
 * for the appropriate product.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 24/06/2016
 * @release_date : 29/07/2016
 */
public class GalleryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * fields
     */
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    public static final String TAG = "GalleryActivity";
    public static final String EXTRA_NAME = "images";
    private ArrayList<String> _images;
    private GalleryPagerAdapter _adapter;

    @InjectView(R.id.pager)
    ViewPager _pager;
    @InjectView(R.id.thumbnails)
    LinearLayout _thumbnails;
    @InjectView(R.id.btn_close)
    ImageButton _closeButton;

    /**
     * Method called when activity created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.inject(this);

        _images = (ArrayList<String>) getIntent().getSerializableExtra(EXTRA_NAME);
        Assert.assertNotNull(_images);

        _adapter = new GalleryPagerAdapter(this);
        _pager.setAdapter(_adapter);
        _pager.setOffscreenPageLimit(6); // how many images to load into memory

        /**button listener to close gallery*/
        _closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "Close clicked");  todo - log close click
                finish();
            }
        });

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
            startActivity(new Intent(GalleryActivity.this, MentionsLegales.class));
        }
        else if(id == R.id.action_a_propos){
            startActivity(new Intent(GalleryActivity.this, APropos.class));
        }
        else if(id == R.id.action_aide){
            startActivity(new Intent(GalleryActivity.this, Aide.class));
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
            startActivity(new Intent(GalleryActivity.this, Ventes.class));
        }
        else if (id == R.id.nav_accueil) {
            startActivity(new Intent(GalleryActivity.this, Home.class));
        }
        else if (id == R.id.nav_locations) {
            startActivity(new Intent(GalleryActivity.this, Locations.class));
        }
        else if (id == R.id.nav_recherche) {
            startActivity(new Intent(GalleryActivity.this, Recherche.class));
        }
        else if (id == R.id.nav_favoris) {
            startActivity(new Intent(GalleryActivity.this, Favoris.class));
        }
        else if (id == R.id.nav_contact) {
            startActivity(new Intent(GalleryActivity.this, Contact.class));
        }
        else if (id == R.id.nav_social) {
            startActivity(new Intent(GalleryActivity.this, Social.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /***********************************************************************************************
     * END OF NAVIGATION MENU METHODS                                                              *
     ***********************************************************************************************/


    /**
     * Inner class containing methods to handle gallery of images.
     */
    class GalleryPagerAdapter extends PagerAdapter {

        /**fields*/
        Context _context;
        LayoutInflater _inflater;

        /**
         *
         * @param context
         */
        public GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        /**
         *
         * @return
         */
        @Override
        public int getCount() {
            return _images.size();
        }

        /**
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        /**
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.pager_gallery_item, container, false);
            container.addView(itemView);

            // Get the border size to show around each image
            int borderSize = _thumbnails.getPaddingTop();

            // Get the size of the actual thumbnail image
            int thumbnailSize = ((FrameLayout.LayoutParams)
                    _pager.getLayoutParams()).bottomMargin - (borderSize*2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);

            // You could also set like so to remove borders
            //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
            //        ViewGroup.LayoutParams.WRAP_CONTENT,
            //        ViewGroup.LayoutParams.WRAP_CONTENT);

            final ImageView thumbView = new ImageView(_context);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, "Thumbnail clicked");   todo - log thumbnail clicked

                    // Set the pager position when thumbnail clicked
                    _pager.setCurrentItem(position);
                }
            });
            _thumbnails.addView(thumbView);

            final SubsamplingScaleImageView imageView =
                    (SubsamplingScaleImageView) itemView.findViewById(R.id.image);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(_images.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImage(ImageSource.bitmap(bitmap));
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            return itemView;
        }

        /**
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
