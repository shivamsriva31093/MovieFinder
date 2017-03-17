package task.application.com.moviefinder.ui.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtmdbwrapper.model.core.Genre;
import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchItemDetailFragment extends Fragment implements SearchItemDetailContract.View {

    private static final String SEARCH_ITEM = "searchitem";
    private static final String CLICKED_ITEM = "clickedItem";

    private SearchItemDetailContract.Presenter presenter;

    private RelativeLayout fragmentContainer;
    private FrameLayout detailView;
    private AVLoadingIndicatorView progressView;
    private RelativeLayout releaseView;
    private RelativeLayout plotView;
    private RelativeLayout genreView;
    private RelativeLayout castView;
    private ImageView backDropImage;
    private MovieDb clickedItem;
    private TextView releaseTitle;
    private TextView releaseDate;
    private TextView plot;
    private TextView genre;
    private TextView director;
    private TextView actors;


    public SearchItemDetailFragment() {
    }

    public static SearchItemDetailFragment newInstance(Context context, @NonNull Bundle args) {
        SearchItemDetailFragment fragment = new SearchItemDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
            clickedItem = (MovieDb) getArguments().getSerializable(CLICKED_ITEM);
        }

        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.detail_parent);
        progressView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.progressView);
        backDropImage = (ImageView) getActivity().findViewById(R.id.detailimage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_item_detail, container, false);
        initialiseViewChildren(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (clickedItem != null) {
            presenter.getMovieDetails(clickedItem);
        }
    }

    private void initialiseViewChildren(View rootView) {

        detailView = (FrameLayout) rootView.findViewById(R.id.fragment_content_parent);
        releaseView = (RelativeLayout) detailView.findViewById(R.id.releaseview);
        plotView = (RelativeLayout) detailView.findViewById(R.id.plotview);
        genreView = (RelativeLayout) detailView.findViewById(R.id.genreview);
        castView = (RelativeLayout) detailView.findViewById(R.id.castview);
        releaseTitle = (TextView) releaseView.findViewById(R.id.releasetitle);
        releaseDate = (TextView) releaseView.findViewById(R.id.releasedate);
        plot = (TextView) plotView.findViewById(R.id.plot);
        genre = (TextView) genreView.findViewById(R.id.genre);
        director = (TextView) castView.findViewById(R.id.directorview);
        actors = (TextView) castView.findViewById(R.id.actorview);
    }


    @Override
    public void setPresenter(SearchItemDetailContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter, "presenter is null");
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        if (show) {
            detailView.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            progressView.show();
        } else {
            progressView.hide();
            progressView.setVisibility(View.GONE);
            detailView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showUi(MovieInfo item) {
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + item.getBackdropPath())
                .error(R.drawable.arrival).into(backDropImage);
        releaseTitle.setText(item.getTitle());
        releaseDate.setText(item.getReleaseDate());
        plot.setText(item.getOverview());
        setActors(item);
        setGenres(item);
    }

    private void setGenres(MovieInfo item) {
        List<Genre> genresList = item.getGenresList();
        if (!genresList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Genre genre : genresList) {
                if (count == 5) break;
                sb.append(genre.getName());
                count++;
                if (count < 5)
                    sb.append(", ");
            }
            genre.setText(sb.toString());
        }
    }

    private void setActors(MovieInfo item) {
        List<MediaCreditCast> cast = item.getCredits().getCast();
        Collections.sort(cast, new Comparator<MediaCreditCast>() {
            @Override
            public int compare(MediaCreditCast lhs, MediaCreditCast rhs) {
                return Integer.parseInt(lhs.getOrder()) - Integer.parseInt(rhs.getOrder());
            }
        });
        if (!cast.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (MediaCreditCast castDetails : cast) {
                if (count == 5) break;
                sb.append(castDetails.getName());
                count++;
                if (count < 5)
                    sb.append(", ");
            }
            actors.setText(sb.toString());
        }
    }


}
