package com.aho.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aho.news.models.Article;
import com.aho.news.models.Source;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView author,title,desc,source,time,published_at;
    String urlImg;
    ProgressBar progressBar;
    String url;
    FirebaseAuth fAuth;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseInstance;
    String article_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        setContentView(R.layout.activity_news_detail);
        author = (TextView) findViewById(R.id.author);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        source = (TextView) findViewById(R.id.source);
        time = (TextView) findViewById(R.id.time);
        published_at = (TextView) findViewById(R.id.publishedAt);
        image = (ImageView) findViewById(R.id.img);

        progressBar = (ProgressBar) findViewById(R.id.prograss_load_photo);

        /* Recieve All extras from the intent */
        Bundle extras = getIntent().getExtras();

        RequestOptions requestOptions = new RequestOptions();
        urlImg = extras.getString("image");
        Glide.with(this)
                .load(extras.getString("image"))
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image);

        title.setText(extras.getString("title"));
        author.setText(extras.getString("author"));
        desc.setText(extras.getString("desc"));
        source.setText(extras.getString("source"));
        published_at.setText(extras.getString("published_ad"));
        author.setText(extras.getString("auther"));
        time.setText(extras.getString("time"));
        url = extras.getString("url");


        firebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseInstance.getReference("DataArticles");
        article_id = databaseReference.push().getKey();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    public void addArticle(){

        /*
        * Save to Firebase
        * */

        Article article = new Article(new Source(),
                author.getText().toString().trim(),
                title.getText().toString().trim(),
                desc.getText().toString().trim(),
                url.toString().trim(),
                urlImg.trim(),
                published_at.getText().toString().trim());

        databaseReference.child("Articles").child(article_id).setValue(article); //referance = DataArticles
    }
    public void onInsertArticle(View v){

        /*
        * method binded to the save button to execute addArticle()
        * */
        addArticle();
        Toast.makeText(this, "Done &isin;\t", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*
        * add signOut logic to menu item
        * */
        if (item.getItemId() == R.id.action_sign_out){
            Toast.makeText(this, "signout",Toast.LENGTH_LONG).show();
            fAuth.signOut();
            startActivity(new Intent(NewsDetailActivity.this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onReadMore(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

}
