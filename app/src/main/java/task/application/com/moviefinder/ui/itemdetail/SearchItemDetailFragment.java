package task.application.com.moviefinder.ui.itemdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchItemDetailFragment extends Fragment implements SearchItemDetailContract.View {

    private static final String SEARCH_ITEM = "searchitem";
    private static final String CLICKED_ITEM = "clickedItem";

    private SearchItemDetailContract.Presenter presenter;
    private MovieDb clickedItem;

    private RelativeLayout fragmentContainer;
    private FrameLayout detailView;
    private AVLoadingIndicatorView progressView;
    private RelativeLayout releaseView;
    private RelativeLayout plotView;
    private RelativeLayout genreView;
    private RelativeLayout castView;
    private ImageView backDropImage;


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
            this.clickedItem = (MovieDb) getArguments().getSerializable(CLICKED_ITEM);
            Log.d("test", getArguments().size() + "");
            Log.d("test", (clickedItem == null) + "");
        }
//        if(clickedItem != null) {
//            presenter.getMovieDetails(clickedItem);
//        }
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.detail_parent);
        progressView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.progressView);
        backDropImage = (ImageView) getActivity().findViewById(R.id.detailimage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_item_detail, container, false);
        initialiseViewChildren(rootView);
        presenter.setUpUiForItem(clickedItem);
        return rootView;
    }

    private void initialiseViewChildren(View rootView) {

        detailView = (FrameLayout) rootView.findViewById(R.id.fragment_content_parent);
        releaseView = (RelativeLayout) detailView.findViewById(R.id.releaseview);
        plotView = (RelativeLayout) detailView.findViewById(R.id.plotview);
        genreView = (RelativeLayout) detailView.findViewById(R.id.genreview);
        castView = (RelativeLayout) detailView.findViewById(R.id.castview);
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
        if (item.getPosterPath() != null) {
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + item.getBackdropPath())
                    .error(R.drawable.arrival).into(backDropImage);
        }
    }
}
