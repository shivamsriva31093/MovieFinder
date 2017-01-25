package task.application.com.moviefinder.model;

import org.json.JSONObject;

/**
 * Created by shashank on 1/24/2017.
 */

public class GetJsonData {

    public JSONObject jsonObject;
    public GetJsonData(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }
    public String getPlot(){
        String plot = jsonObject.optString("Plot");
        return plot;
    }
    public String getReleaseDate(){
        String releaseDate = jsonObject.optString("Released");
        return releaseDate;
    }
    public String getGenre(){
        String genre = jsonObject.optString("Genre");
        return genre;
    }
    public String getImdbRating(){
        String imdbrating = jsonObject.optString("imdbRating");
        return imdbrating;
    }
    public String getTitle(){
        String title = jsonObject.optString("Title");
        return title;
    }
    public String getTomatoRating(){
        String tomatoRating = jsonObject.optString("tomatoRating");
        return tomatoRating;
    }
}
