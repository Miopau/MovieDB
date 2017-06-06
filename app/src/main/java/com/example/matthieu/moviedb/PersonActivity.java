package com.example.matthieu.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class PersonActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        ImageView affiche = (ImageView) findViewById(R.id.imageView);
        TextView name = (TextView) findViewById(R.id.textView2);
        TextView date = (TextView) findViewById(R.id.textView3);
        TextView description = (TextView) findViewById(R.id.textView4);
        TextView Desc = (TextView) findViewById(R.id.textView5);
        TextView place = (TextView) findViewById(R.id.textView5);
        Button button =(Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
//        Log.v("Yesss",intent.getStringExtra("name"));
        name.setText(intent.getStringExtra("name"));
        date.setText("("+intent.getStringExtra("birthday")+")");
        Desc.setText("Description:");
        description.setText(intent.getStringExtra("description"));
        place.setText(intent.getStringExtra("place_of_birth"));

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