package task.application.com.moviefinder.ui.searchlist;

import android.util.Log;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.util.TmdbApi;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListPresenter implements SearchListContract.Presenter {

    private SearchListContract.View view;
    private MediaType filterType;

    public SearchListPresenter(SearchListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void searchByKeyword(String keyword) {
        view.showLoadingIndicator(true);
        TmdbApi tmdb = TmdbApi.getApiClient(ApplicationClass.API_KEY);
        if (getFilteringType().equals(MediaType.MOVIES)) {
//            TmdbWrapper wrapper = new TmdbWrapper(this);
//            wrapper.execute(keyword);
            tmdb.searchService().searchMovies(keyword)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        view.showLoadingIndicator(false);
                        view.showSearchList(new ArrayList<>(searchRes.getResults()));
                    });

        } else {
            Log.d("debug", "Query for TV");
            tmdb.searchService().searchTv(keyword)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(searchRes -> {
                        view.showLoadingIndicator(false);
                        view.showSearchList(new ArrayList<>(searchRes.getResults()));
                    });
        }
    }

    @Override
    public void clearRecyclerView() {

    }

    @Override
    public <T extends MediaBasic> Void onSearchItemClick(T item) {
        view.showItemDetailsUi(item);
        return null;
    }

    @Override
    public void setFilteringType(MediaType filteringType) {
        this.filterType = filteringType;
        Log.d("debug", "filter is et to: " + filteringType.toString());
    }

    @Override
    public MediaType getFilteringType() {
        return filterType;
    }

    @Override
    public void start() {

    }

}
