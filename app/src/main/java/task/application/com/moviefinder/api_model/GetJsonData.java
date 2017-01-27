package task.application.com.moviefinder.api_model;

import org.json.JSONObject;

/**
 * Created by shashank on 1/24/2017.
 */

public class GetJsonData {

    private static final String PLOTQUERY = "Plot";
    private static final String RELEASEQUERY = "Released";
    private static final String YEARQUERY = "Year";
    private static final String IMDBRATINGQUERY = "imdbRating";
    private static final String IMDBVOTESQUERY = "imdbVotes";
    private static final String RTQUERY = "tomatoMeter";
    private static final String DIRECTORQUERY = "Director";
    private static final String ACTORQUERY = "Actors";
    private static final String IMAGEQUERY = "Poster";
    private static final String TITLEQUERY = "Title";
    private static final String GENREQUERY = "Genre";



    public JSONObject jsonObject;


    public GetJsonData(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public String getPlot(){
        String plot = jsonObject.optString(PLOTQUERY);
        return plot;
    }
    public String getReleaseDate(){
        String releaseDate = jsonObject.optString(RELEASEQUERY);
        return releaseDate;
    }
    public String getYear(){
        String year = jsonObject.optString(YEARQUERY);
        return year;
    }
    public String getImdbVotes(){
        String imdbVotes = jsonObject.optString(IMDBVOTESQUERY);
        return imdbVotes;
    }
    public String getDirector(){
        String director = jsonObject.optString(DIRECTORQUERY);
        return director;
    }
    public String getActors(){
        String actors = jsonObject.optString(ACTORQUERY);
        return actors;
    }
    public String getGenre(){
        String genre = jsonObject.optString(GENREQUERY);
        return genre;
    }
    public String getImageUrl(){
        String imageUrl = jsonObject.optString(IMAGEQUERY);
        return imageUrl;
    }

    public String getImdbRating(){
        String imdbrating = jsonObject.optString(IMDBRATINGQUERY);
        return imdbrating;
    }
    public String getTitle(){
        String title = jsonObject.optString(TITLEQUERY);
        return title;
    }
    public String getTomatoRating(){
        String tomatoRating = jsonObject.optString(RTQUERY);
        return tomatoRating;
    }
}
