package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Custom Adapter class that is responsible for holding the list of sites after they
 * get parsed out of XML and building row views to display them on the screen.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 20/06/2016
 * @release_date : 29/07/2016
 * @see :
 */
public class ProduitsAdapter extends ArrayAdapter<StackProduits> {

    /**
     * fields
     */
    ImageLoader imageLoader;
    DisplayImageOptions options;
    List<StackProduits> productList;
    SparseBooleanArray mSelectedItemsIds;
    Context context;

    /**
     * Constructor.
     *
     * @param ctx
     * @param textViewResourceId
     * @param sites
     */
    public ProduitsAdapter(Context ctx, int textViewResourceId, List<StackProduits> sites) {
        super(ctx, textViewResourceId, sites);

                /**Setup the ImageLoader, we'll use this to display our images*/
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        mSelectedItemsIds = new SparseBooleanArray();

        /**Setup options for ImageLoader so it will handle caching for us.*/
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        this.context = ctx;
    }

    /**
     * This method is responsible for creating row views out of a StackProduits object that can be put
     * into our ListView.
     * <p/>
     * (non-Javadoc)
     *
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        RelativeLayout row = (RelativeLayout) convertView;
        //Log.i("StackSites", "getView pos = " + pos);
        if (null == row) {   //No recycled View, we have to inflate one.
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (RelativeLayout) inflater.inflate(R.layout.item_row, null);
        }

        //Get our View References from item_row.xml
        final ImageView iconImg = (ImageView) row.findViewById(R.id.iconImg);
        TextView txtDesignation = (TextView) row.findViewById(R.id.nameTxt);
        TextView txtAbout = (TextView) row.findViewById(R.id.aboutTxt);
        TextView txtPrice = (TextView) row.findViewById(R.id.priceTxt);
        TextView txtTotalArea = (TextView) row.findViewById(R.id.areaTxt);
        final ProgressBar indicator = (ProgressBar) row.findViewById(R.id.progress);

        //Initially we want the progress indicator visible, and the image invisible
        indicator.setVisibility(View.VISIBLE); //show progress indicator
        iconImg.setVisibility(View.INVISIBLE); //make image invisible

        //Setup a listener we can use to switch from the loading indicator to the Image once it's ready
        //changed ImageLoadingListener with SimpleImageLoadingListener
        SimpleImageLoadingListener listener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                indicator.setVisibility(View.INVISIBLE);
                iconImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub
            }
        };

        //Load the image and use our options so caching is handled.
        imageLoader.displayImage(getItem(pos).getImgUrl(), iconImg, options, listener);

        //Set the relevant text in our TextViews (ListView)
        txtDesignation.setText(getItem(pos).getDesignation());
        txtAbout.setText(getItem(pos).getAbout());
        txtPrice.setText(getItem(pos).getPrice());
        if(getItem(pos).getArea().equalsIgnoreCase("-")){
            txtTotalArea.setText(context.getString(R.string.produit_area_not_available));
        } else {
            txtTotalArea.setText(getItem(pos).getArea());
        }
        //return view that represents the full row
        return row;
    }


    /***********************************************************************************************
     * START OF MULTIPLE ITEM DELETION METHODS                                                     *
     ***********************************************************************************************/
    @Override
    public void remove(StackProduits object) {
        super.remove(object);
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    /***********************************************************************************************
     * END OF MULTIPLE ITEM DELETION METHODS                                                       *
     ***********************************************************************************************/
}
