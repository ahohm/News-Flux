package com.aho.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aho.news.models.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private List<Article> articles;
    private Context context;


    public Adapter(List<Article> articles, Context context) {
        /*
        * instancier Adapter par context "main activity", articles "resultat de la requete effectué au niveau LoadJson methode"
        * */
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        * on lui accorde l xml de item
        * return viewholder compesé de notre item
        * */
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        /*
        * relier les Views de View holder par les données de position x de resultas de requete
        *
        * Glide responsable de telecharger l'image de l'article la passer au ImageView
        *
        * setOnclickLisner ajouter action au click sur le viewholder pour passer tt les donner a lactivity suivante
        * */
        final MyViewHolder holder = holders;
        final Article model = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(model.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource().getName());
        holder.time.setText(" \u0660 " + Utils.DateToTimeFormat(model.getPublishedAt()));
        holder.published_ad.setText(Utils.DateFormat(model.getPublishedAt()));
        holder.author.setText(model.getAuthor());
        holder.url.setText(model.getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra("title",model.getTitle());
                intent.putExtra("desc",model.getDescription());
                intent.putExtra("source",model.getSource().getName());
                intent.putExtra("time"," \u0660 " + Utils.DateToTimeFormat(model.getPublishedAt()));
                intent.putExtra("published_ad", Utils.DateFormat(model.getPublishedAt()));
                intent.putExtra("author",model.getAuthor());
                intent.putExtra("url",model.getUrl());
                intent.putExtra("image",model.getUrlToImage());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView desc;
        TextView author;
        TextView published_ad;
        TextView source;
        TextView time;
        TextView url;
        ImageView imageView;
        ProgressBar progressBar;


        public MyViewHolder(View itemView) {

            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.time);
            url = itemView.findViewById(R.id.url);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.prograss_load_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });

        }
    }
}
