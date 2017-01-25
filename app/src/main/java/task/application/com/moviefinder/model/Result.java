package task.application.com.moviefinder.model;

/**
 * Created by shashank on 1/24/2017.
 */

public class Result {
    private String title;
    private double imdRating;
    private String plot;
    private double rtRating;
    private String releaseDate;
    private String genre;
    private String type;
    private String year;
    private String imdbId;
    private String imageUrl;
    public Result(){

    }
    public Result(String title,String plot,String releaseDate,String genre,double imdRating,double rtRating){
        this.title = title;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.imdRating = imdRating;
        this.rtRating = rtRating;
    }

    public void setTitle(String title){
        this.title = title;

    }
    public String getTitle(){

        return title;
    }
    public void setImageUrl(String url){
        this.imageUrl = url;

    }
    public String getImageUrl(){

        return imageUrl;
    }
    public void setImdbId(String id){
        this.imdbId = id;

    }
    public String getImdbId(){

        return imdbId;
    }
    public void setYear(String year){
        this.year = year;

    }
    public String getYear(){

        return year;
    }
    public void setType(String type){
        this.type = type;

    }
    public String getType(){

        return type;
    }
    public void setImdRating(double imdRating){
        this.imdRating = imdRating;

    }
    public double getImdRating(){

        return imdRating;
    }
    public void setRtRating(double rtRating){
        this.rtRating = rtRating;
    }
    public double getRtRating(){

        return rtRating;
    }
    public void setPlot(String plot){
        this.plot = plot;

    }
    public String getPlot(){

        return plot;
    }
    public void setGenre(String genre){
        this.genre = genre;

    }
    public String getGenre(){

        return genre;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;

    }
    public String getReleaseDate(){

        return releaseDate;
    }
}
