package com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.Produits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.monte_carlo_multimedia.adam_jaamour.fcfimmobilier.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Class containg methods to make recycler view for product detail images functional.
 *
 * @author : Adam Jaamour
 * @version : 1.0
 * @contact : adam@jaamour.com
 * @date_created : 06/07/2016
 * @release_date : 29/07/2016
 * @see : ProduitDetail.java
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    /**fields*/
    private List<String> urlThumbImg;
    private Context context;

    /**
     * Constructor.
     * @param ctx
     * @param urls
     */
    public RecyclerAdapter(Context ctx, List<String> urls){
        this.urlThumbImg = urls;
        this.context = ctx;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_slideshow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position){
        String current = urlThumbImg.get(position);
        Picasso.with(context).load(current).fit().into(holder.myImgView);
    }

    @Override
    public int getItemCount(){
        return urlThumbImg.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView myImgView;

        public MyViewHolder(View view){
            super(view);
            myImgView = (ImageView) view.findViewById(R.id.imageView_slide);
        }
    }
}
