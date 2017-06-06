package com.example.matthieu.moviedb;

/**
 * Created by Matthieu on 06/06/2017.
 */

public class Film {
    public String affiche;
    public String title;
    public String date;
    public Film(){
        super();
    }

    public Film(String affiche, String title,String date) {
        super();
        this.affiche = affiche;
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getAffiche() {

        return affiche;
    }
}

