package task.application.com.moviefinder.util;

/**
 * Created by sHIVAM on 2/11/2017.
 */

public enum SearchFilter {
    MOVIE("Movie"),
    TV("TV");

    private String queryType;

    SearchFilter(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryType() {
        return this.queryType;
    }
}
