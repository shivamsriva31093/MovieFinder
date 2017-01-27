package task.application.com.moviefinder.api_model;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import task.application.com.moviefinder.R;

/**
 * Created by shashank on 1/26/2017.
 */

public class DetailData {



    private Result result ;
    private static final String NA = "N/A";


    public Result detailResult(JSONObject jsonObject){
        result = new Result();
        GetJsonData getJsonData = new GetJsonData(jsonObject);
        result.setTitle(getJsonData.getTitle());
        result.setPlot(getJsonData.getPlot());
        result.setImdRating(getJsonData.getImdbRating());
        result.setRtRating(getJsonData.getTomatoRating());
        result.setGenre(getJsonData.getGenre());
        result.setImageUrl(getJsonData.getImageUrl());
        result.setReleaseDate(getJsonData.getReleaseDate());
        result.setImdVotes(getJsonData.getImdbVotes());
        result.setActors(getJsonData.getActors());
        result.setDirector(getJsonData.getDirector());
        result.setYear(getJsonData.getYear());

        return result;

    }
    public void populateViews(Context context, Toolbar toolbar,TextView actorView,TextView directorView,
                              TextView yearview, TextView genreView, TextView plotView, TextView imdbView, TextView rtView,
                              TextView dateview,TextView votesview,ImageView imageView, Result result){

        String imageurl = result.getImageUrl();
        if(imageurl.equals(NA)){
            imageView.setPadding(200,200,200,300);
            imageView.setImageResource(R.drawable.clapperboard);
        }
        else {
            imageView.setPadding(0,0,0,0);
            Picasso.with(context).load(result.getImageUrl()).into(imageView);
        }
        actorView.setText(result.getActors());
        directorView.setText(result.getDirector());
        yearview.setText(result.getYear());
        genreView.setText(result.getGenre());
        plotView.setText(result.getPlot());
        imdbView.setText(result.getImdRating());
        rtView.setText(result.getRtRating()+" % ");
        toolbar.setTitle(result.getTitle());
        dateview.setText(result.getReleaseDate());
        votesview.setText(result.getImdbVotes()+" votes");
    }


}
