package task.application.com.moviefinder.ui.itemdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchItemDetailFragment extends Fragment implements SearchItemDetailContract.View {

    private static final String SEARCH_ITEM = "searchitem";
    private static final String CLICKED_ITEM = "clickedItem";

    private Activity context;
    private SearchItemDetailContract.Presenter presenter;
    private MovieDb clickedItem;

    private RelativeLayout fragmentContainer;
    private FrameLayout detailView;
    private AVLoadingIndicatorView progressView;
    private String imdbId;
    private TextView genreView;
    private TextView plotView;
    private TextView imdbView;
    private TextView rtView;
    private ImageView imageView;
    private TextView dateView;
    private Toolbar toolbar;
    private TextView votesView;
    private TextView directorView;
    private TextView actorsView;
    private TextView yearView;

    public SearchItemDetailFragment() {
        this.context = getActivity();
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
            this.clickedItem = (MovieDb) getArguments().getSerializable(CLICKED_ITEM);
        }
//        if(clickedItem != null) {
//            presenter.getMovieDetails(clickedItem);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_item_detail, container, false);
        initialiseViewChildren();
        imdbView.setVisibility(View.GONE);
        rtView.setVisibility(View.GONE);
        votesView.setVisibility(View.GONE);
        if (clickedItem != null) {
            presenter.setUpUiForItem(clickedItem);
        }
        return rootView;
    }

    private void initialiseViewChildren() {
        fragmentContainer = (RelativeLayout) context.findViewById(R.id.detail_parent);
        detailView = (FrameLayout) context.findViewById(R.id.fragment_content_parent);
        progressView = (AVLoadingIndicatorView) context.findViewById(R.id.progressView);
        genreView = (TextView) context.findViewById(R.id.genre);
        plotView = (TextView) context.findViewById(R.id.plot);
        imdbView = (TextView) context.findViewById(R.id.imdbrating);
        rtView = (TextView) context.findViewById(R.id.rtrating);
        imageView = (ImageView) context.findViewById(R.id.detailimage);
        toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        dateView = (TextView) context.findViewById(R.id.releasedate);
        votesView = (TextView) context.findViewById(R.id.imdbvotes);
        directorView = (TextView) context.findViewById(R.id.directorview);
        actorsView = (TextView) context.findViewById(R.id.actorview);
        yearView = (TextView) context.findViewById(R.id.year);
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
    public void prepareUI(List<PersonCast> cast) {

    }

    @Override
    public void showUi(MovieDb item) {
        StringBuilder genre = new StringBuilder();
        for (Genre g : item.getGenres()) {
            genre.append(g.getName()).append(" ");
        }
        genreView.setText(genre.toString());

        plotView.setText(item.getOverview());
        Picasso.with(context).load(item.getBackdropPath()).into(imageView);
        dateView.setText(item.getReleaseDate());

        for (PersonCrew crew : item.getCrew()) {
            if (crew.getJob().equals("Director") &&
                    crew.getDepartment().equals("Directing")) {
                directorView.setText(crew.getName());
            }
        }
        List<PersonCast> cast = item.getCast();
        StringBuilder builder = new StringBuilder();
        int order = 0;
        for (int i = 0; i < cast.size(); i++) {
            if (order == 4)
                break;
            if (cast.get(i).getOrder() == order) {
                builder.append(cast.get(i).getName())
                        .append(" ");
                order += 1;
            }
        }
        actorsView.setText(builder.toString());
        yearView.setText(item.getReleaseDate());
    }
}
