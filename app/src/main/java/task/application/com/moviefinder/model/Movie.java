package task.application.com.moviefinder.model;

/**
 * Created by shashank on 1/24/2017.
 */

public class Movie extends Result {
    public Movie(){

    }
    public Movie(String title,String plot,String releaseDate,String genre,double imdRating,double rtRating){
        super.setTitle(title);
        super.setGenre(genre);
        super.setReleaseDate(releaseDate);
        super.setImdRating(imdRating);
        super.setPlot(plot);
        super.setRtRating(rtRating);
    }
}
