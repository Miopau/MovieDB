package com.example.matthieu.moviedb; /**
 * Created by Matthieu on 06/06/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matthieu.moviedb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Matthieu on 5/06/17.
 */
public class FilmAdapter extends ArrayAdapter<Film> {
    Context context;
    int layoutResourceId;
    ArrayList<Film> films=null;

    public FilmAdapter(Context context,int layoutResourceId, ArrayList<Film> films) {
        super(context, layoutResourceId, films);
        this.layoutResourceId=layoutResourceId;
        this.context = context;
        this.films = films;



    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne= convertView;
        AffichageFilm affFilm=null;
        if (ligne == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            ligne=inflater.inflate(layoutResourceId,parent,false);

            affFilm = new AffichageFilm();
            affFilm.imgFilm = (ImageView)ligne.findViewById(R.id.imgIcon);
            affFilm.txtTitle = (TextView)ligne.findViewById(R.id.txtTitle);
            affFilm.txtDate = (TextView)ligne.findViewById(R.id.txtDate);

            ligne.setTag(affFilm);
        }else{
            affFilm=(AffichageFilm)ligne.getTag();
        }
        Film leFilm=films.get(position);
        affFilm.txtTitle.setText(leFilm.title);
        affFilm.txtDate.setText(leFilm.date);
        Picasso.with(this.context).load(leFilm.getAffiche()).into(affFilm.imgFilm);
        return ligne;
    }

    public void setFilms(ArrayList<Film> films) {
        this.films = films;
    }

    static class AffichageFilm
    {
        ImageView imgFilm;
        TextView txtTitle;
        TextView txtDate;
    }
}