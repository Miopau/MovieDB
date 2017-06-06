package com.example.matthieu.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class FilmActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        Intent intent = getIntent();
        ImageView affiche = (ImageView) findViewById(R.id.imageView);
        TextView title = (TextView) findViewById(R.id.textView2);
        TextView date = (TextView) findViewById(R.id.textView3);
        TextView description = (TextView) findViewById(R.id.textView4);
        TextView Desc = (TextView) findViewById(R.id.textView5);
        RatingBar rate=(RatingBar) findViewById(R.id.ratingBar);
        Button button =(Button) findViewById(R.id.button2);
        button.setText("Retour");
        button.setOnClickListener(this);

        title.setText(intent.getStringExtra("title"));
        date.setText("("+intent.getStringExtra("date")+")");
        Desc.setText("Description:");
        description.setText(intent.getStringExtra("description"));

        rate.setRating(Float.parseFloat(intent.getStringExtra("Moyenne")));
        rate.setIsIndicator(true);
        Picasso.with(getBaseContext()).load(intent.getStringExtra("affiche")).into(affiche);

    }
    public void finish(){
        // Activity finished ok, return the data
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }
}
