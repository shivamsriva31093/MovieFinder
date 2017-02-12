package task.application.com.moviefinder.model.remote.api.tmdb;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

/**
 * Created by sHIVAM on 2/12/2017.
 */

public class GetSearchDetail {

    private MovieDb item;
    private GetMovieDetailInterface listener;

    public GetSearchDetail(MovieDb item, GetMovieDetailInterface listener) {
        this.item = item;
        this.listener = listener;
    }

    public void startAsyncSearch() {
        new AsyncGetMovieDetails().execute();
    }

    private class AsyncGetMovieDetails extends AsyncTask<String, Void, List<PersonCast>> {

        @Override
        protected List<PersonCast> doInBackground(String... params) {
            TmdbApi api = new TmdbApi("1d49e17fa9eb8f8d72d20a75af1099b1");
            List<PersonCast> result = new ArrayList<>();
            result = searchMovies(api);
            return result;
        }

        @Override
        protected void onPostExecute(List<PersonCast> personCasts) {
            if (!personCasts.isEmpty()) {
                listener.onGetCastDetailsListener(personCasts);
            }
        }

        private List<PersonCast> searchMovies(TmdbApi api) {
            TmdbMovies movies = api.getMovies();
            return movies.getCredits(item.getId()).getCast();
        }
    }

    public interface GetMovieDetailInterface {
        void onGetCastDetailsListener(List<PersonCast> cast);
    }

}
