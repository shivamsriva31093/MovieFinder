package task.application.com.moviefinder.api_model;

import android.util.Log;

/**
 * Created by shashank on 1/25/2017.
 */

public class URLify {
    private static final String IMDBURL = "http://www.omdbapi.com/?plot=full&r=json&tomatoes=true";
    private static final String MOVIE = "Movies";
    private static final String TV = "TV";

    String parameter;
    String searchType;

    public URLify(String parameter, String searchtype) {
        this.parameter = parameter;
        this.searchType = searchtype;
    }

    public String addParameter(String type) {

        String url = IMDBURL + "&" + searchType + "=" + parameter;
        if(type.equals(MOVIE)){
            url = url+"&type=movie";
        }
        else if(type.equals(TV)) {
            url = url+"&type=series";
        }
        Log.d("url",url);
        return url;
    }

}
