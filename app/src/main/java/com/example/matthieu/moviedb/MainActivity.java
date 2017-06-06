package com.example.matthieu.moviedb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.matthieu.moviedb.R.id.listView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Création des variables
    ListView listRes;
    static final int REQUEST_CODE = 1;
    EditText saisie;
    ArrayList<Film> films=new ArrayList<Film>();
    int nbChoisi=0;
    String  url2;
    JSONArray liste;
    JSONObject test;
    Spinner spinner2;
    String nameput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Spinner 1
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<Integer> choixNombre=new ArrayList<Integer>();
        choixNombre.add(10);
        choixNombre.add(25);
        choixNombre.add(50);
        ArrayAdapter<Integer> nb =
                new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, choixNombre);
        spinner.setAdapter(nb);
        nbChoisi = ((Integer) spinner.getSelectedItem());
        //Spinner 2
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayList<String> choixType=new ArrayList<String>();
        choixType.add("Movie");
        choixType.add("TV");
        choixType.add("Person");
        ArrayAdapter<String> type =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, choixType);
        spinner2.setAdapter(type);

        //Bouton
        Button b1=(Button) findViewById(R.id.button);
        b1.setText("Rechercher");
        b1.setOnClickListener(this);

        this.requete();
    }

    //Récupération des films les plus populaires pour la page d'accueil de l'appli.
    public void requete(){
        RequestQueue queue = Volley.newRequestQueue(this);
        url2 = "https://api.themoviedb.org/3/movie/popular?api_key=d6fab5b1edaec9267c2c49a16f319e59&language=fr&page=1";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            liste = response.getJSONArray("results");
                            int limite;
                            if(nbChoisi>liste.length()){
                                limite = liste.length();
                            }else{
                                limite = nbChoisi;
                            }

                            for(int i=0;i<limite;i++){
                                String url1 ="https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i).getString("poster_path");

                                String titre1 = liste.getJSONObject(i).getString("title");
                                String date1 = "("+liste.getJSONObject(i).getString("release_date")+")";
                                //ajout dans la liste de films
                                films.add(new Film(url1,titre1,date1));
                            }


                        } catch (JSONException e) {
                            Log.v("Ici: ",e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Hello","Erreur reponse");
                    }
                });

        queue.add(jsObjRequest);

        //Gestion des données de la liste de film avec l'Adapter
        listRes = (ListView) findViewById(listView);
        final FilmAdapter adapter=new FilmAdapter(this,R.layout.item, films);
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        listRes.addHeaderView(header);

        listRes.setAdapter(adapter);
        Log.v("echo","Bonjour");
        listRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, FilmActivity.class);


                try {
                    intent.putExtra("affiche", "https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i-1).getString("poster_path"));
                    intent.putExtra("title", liste.getJSONObject(i-1).getString("title"));
                    intent.putExtra("date", liste.getJSONObject(i-1).getString("release_date"));
                    intent.putExtra("description", liste.getJSONObject(i-1).getString("overview"));
                    intent.putExtra("Moyenne", liste.getJSONObject(i-1).getString("vote_average"));
                } catch (JSONException e) {
                    Log.v("Erreur  ","Genre");
                }

                startActivityForResult(intent, REQUEST_CODE);
            }

        });
    }
    @Override
    public void onClick(View view) {
        films.clear();
        //Saisie
        saisie=(EditText)findViewById(R.id.editText);
        String genre=(String)spinner2.getSelectedItem();
        Log.v("Saisie",saisie.getText().toString());
        TextView lire=(TextView) findViewById(R.id.textView7);
        String textRequete=saisie.getText().toString();
        textRequete=textRequete.replaceAll(" ","%20");
        Log.v("lol",textRequete);
        switch (genre){
            case "Movie":
                lire.setText("Recherche '"+saisie.getText().toString()+"' dans Films.");
                this.choixMovie(textRequete);
                break;
            case "TV":
                lire.setText("Recherche '"+saisie.getText().toString()+"' dans TV Show.");
                this.choixTVshow(textRequete);
                break;
            case "Person":
                lire.setText("Recherche '"+saisie.getText().toString()+"' dans People.");
                this.choixPerson(textRequete);
                break;
            default:
                Log.v("Default","");
    /*Action*/;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

        }
    }
    public void choixMovie(String saisieText){
        //requete
        RequestQueue queue = Volley.newRequestQueue(this);
        url2 = "https://api.themoviedb.org/3/search/movie?api_key=c96297c4ea9cb9c4729016fa02ce35e9&language=fr&page=1&query="+saisieText;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            liste = response.getJSONArray("results");
                            int limite;
                            if(nbChoisi>liste.length()){
                                limite = liste.length();
                            }else{
                                limite = nbChoisi;
                            }

                            for(int i=0;i<limite;i++){
                                String url1 ="https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i).getString("poster_path");

                                String titre1 = liste.getJSONObject(i).getString("title");
                                String date1 = "("+liste.getJSONObject(i).getString("release_date")+")";
                                //ajout dans la liste de films
                                films.add(new Film(url1,titre1,date1));
                            }


                        } catch (JSONException e) {
                            Log.v("Ici: ",e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Hello","Erreur reponse");
                    }
                });

        queue.add(jsObjRequest);

        //adapter
        listRes = (ListView) findViewById(listView);
        final FilmAdapter adapter=new FilmAdapter(this,R.layout.item, films);
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        listRes.addHeaderView(header);

        listRes.setAdapter(adapter);
        Log.v("echo","Bonjour");
        listRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, FilmActivity.class);


                try {
                    intent.putExtra("affiche", "https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i-1).getString("poster_path"));
                    intent.putExtra("title", liste.getJSONObject(i-1).getString("title"));
                    intent.putExtra("date", liste.getJSONObject(i-1).getString("release_date"));
                    intent.putExtra("description", liste.getJSONObject(i-1).getString("overview"));
                    intent.putExtra("Moyenne", liste.getJSONObject(i-1).getString("vote_average"));
                } catch (JSONException e) {
                    Log.v("Erreur  ","Genre");
                }

                startActivityForResult(intent, REQUEST_CODE);
            }

        });
    }
    public void choixTVshow(String saisieText){
        //requete
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView mTxtDisplay;
        ;
        final String url = "https://nosql-workshop.herokuapp.com/api/installations/random";
        url2 = "https://api.themoviedb.org/3/search/tv?api_key=c96297c4ea9cb9c4729016fa02ce35e9&language=fr&page=1&query="+saisieText;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            liste = response.getJSONArray("results");
                            int limite;
                            if(nbChoisi>liste.length()){
                                limite = liste.length();
                            }else{
                                limite = nbChoisi;
                            }

                            for(int i=0;i<limite;i++){
                                String url1 ="https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i).getString("poster_path");

                                String titre1 = liste.getJSONObject(i).getString("name");
                                String date1 = "("+liste.getJSONObject(i).getString("first_air_date")+")";
                                //ajout dans la liste de films
                                films.add(new Film(url1,titre1,date1));
                            }


                        } catch (JSONException e) {
                            Log.v("Ici2: ",e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Hello","Erreur reponse");
                    }
                });

        queue.add(jsObjRequest);

        //adapter
        listRes = (ListView) findViewById(listView);
        final FilmAdapter adapter=new FilmAdapter(this,R.layout.item, films);
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        listRes.addHeaderView(header);

        listRes.setAdapter(adapter);
        Log.v("echo","Bonjour");
        listRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, TVShowActivity.class);


                try {

                    intent.putExtra("affiche", "https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i-1).getString("poster_path"));
                    intent.putExtra("title", liste.getJSONObject(i-1).getString("name"));
                    intent.putExtra("date", liste.getJSONObject(i-1).getString("first_air_date"));
                    intent.putExtra("description", liste.getJSONObject(i-1).getString("overview"));
                    intent.putExtra("Moyenne", liste.getJSONObject(i-1).getString("vote_average"));
                    //JSONObject arr = liste.getJSONObject(i-1).getJSONObject("created_by");
                    //intent.putExtra("genre",arr.getString("name"));
                    intent.putExtra("genre",liste.getJSONObject(i-1).getJSONArray("created_by").getString(1));

                    //intent.putExtra("genre", liste.getJSONObject(i-1).getString("name"));
                } catch (JSONException e) {
                    Log.v(e.getMessage(),"ok");
                }

                startActivityForResult(intent, REQUEST_CODE);
            }

        });
    }

    //Choix person nonFonctionnel, données recues mais pas retransmises à l'intent
    public void choixPerson(String saisieText){
        //requete
        RequestQueue queue = Volley.newRequestQueue(this);
        url2 = "https://api.themoviedb.org/3/search/person?api_key=c96297c4ea9cb9c4729016fa02ce35e9&language=fr&page=1&query="+saisieText;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            liste = response.getJSONArray("results");
                            int limite;
                            if(nbChoisi>liste.length()){
                                limite = liste.length();
                            }else{
                                limite = nbChoisi;
                            }

                            for(int i=0;i<limite;i++){
                                String url1 ="https://image.tmdb.org/t/p/w500" +liste.getJSONObject(i).getString("profile_path");

                                String titre1 = liste.getJSONObject(i).getString("name");
                                //ajout dans la liste de films
                                films.add(new Film(url1,titre1,null));
                            }


                        } catch (JSONException e) {
                            Log.v("Ici2: ",e.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Hello","Erreur reponse");
                    }
                });

        queue.add(jsObjRequest);

        //adapter
        listRes = (ListView) findViewById(listView);
        final FilmAdapter adapter=new FilmAdapter(this,R.layout.item, films);
        adapter.notifyDataSetChanged();
        runOnUiThread(new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        listRes.addHeaderView(header);

        listRes.setAdapter(adapter);
        final RequestQueue queue2 = Volley.newRequestQueue(this);
        listRes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PersonActivity.class);


                try {
                    //https://api.themoviedb.org/3/person/51329?api_key=c96297c4ea9cb9c4729016fa02ce35e9&language=fr
                    String urlRes="https://api.themoviedb.org/3/person/"+liste.getJSONObject(i-1).getInt("id")+"?api_key=c96297c4ea9cb9c4729016fa02ce35e9&language=fr";
                    Log.v("Url",urlRes);

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, urlRes, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        Log.v("TESSSSSt",response.getString("name"));
                                        test=response;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("Hello","Erreur reponse");
                                }
                            });
                    queue2.add(jsObjRequest);

                    nameput=test.getString("name");
                    intent.putExtra("affiche", "https://image.tmdb.org/t/p/w500" +test.getString("profile_path"));
                    intent.putExtra("name",nameput);
                    intent.putExtra("birthday","Né le: "+test.getString("birthday"));
                    intent.putExtra("place_of_birth","à "+test.getString("place_of_birth"));
                    intent.putExtra("biography", test.getString("biography"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivityForResult(intent, REQUEST_CODE);
            }

        });
    }
}

