package task.application.com.moviefinder.model.remote.api.tmdb;

import android.os.AsyncTask;

import java.util.ArrayList;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by sHIVAM on 2/4/2017.
 */

public class TmdbWrapper extends AsyncTask<String, Void, ArrayList<MovieDb>> {

    private TmdbWrapperCallbackInterface mCallback;

    public TmdbWrapper(TmdbWrapperCallbackInterface callback) {
        this.mCallback = callback;
    }

    @Override
    protected ArrayList<MovieDb> doInBackground(String... params) {
        TmdbApi api = new TmdbApi("1d49e17fa9eb8f8d72d20a75af1099b1");
        TmdbSearch search = api.getSearch();
        return (ArrayList<MovieDb>) search.searchMovie(params[0], 0, "", true, 0).getResults();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDb> movieDbs) {
        if (mCallback != null) {
            mCallback.onSearchResult(movieDbs);
        }
    }

    public interface TmdbWrapperCallbackInterface {
        void onSearchResult(ArrayList<MovieDb> movieDbs);
    }
}
