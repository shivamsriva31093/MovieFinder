package task.application.com.moviefinder.model;

/**
 * Created by shashank on 1/25/2017.
 */

public class URLify {
    private static final String TMDBURL = "http://www.omdbapi.com/?plot=full&r=json";
    String parameter;
    String type ;
    public URLify(String parameter, String type){
        this.parameter = parameter;
        this.type = type;
    }

    public String addParameter(){
        String url = TMDBURL+"&"+type+"="+parameter;
        return url;
    }

}
