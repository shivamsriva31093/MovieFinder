package task.application.com.moviefinder.ui.itemdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.OmdbMovieDetails;
import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.core.Genre;
import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.credits.MediaCreditCrew;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.mediadetails.Video;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.androidtmdbwrapper.model.tv.TvInfo;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 3/16/2017.
 */

public class FragmentPrime extends Fragment implements SearchItemDetailContract.View, View.OnClickListener {

    private static final String TAG = FragmentPrime.class.getName();
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String YOUTUBE_API_KEY = "AIzaSyC9iXjkY03gWbADszp0x9zX2yRvRMYjaxo";
    private SearchItemDetailContract.Presenter presenter;
    private FragmentInteractionListener listener;
    private BaseMediaData clickedItem;
    private MediaType itemType;
    private BaseMediaData retrievedItem;
    private RelativeLayout fragmentContainer;
    private ImageView backDropImage;
    private AVLoadingIndicatorView progressView;
    private FrameLayout detailView;
    private RelativeLayout basicDetails;
    private RelativeLayout ratingsView;
    private ImageButton trailerButton;
    private FloatingActionButton favorite;
    private ImageButton share;
    private TextView genres;
    private TextView title;
    private TextView lang;
    private TextView runtime;
    private TextView synopsis;
    private String trailerKey;
    private TextView rtRating;
    private TextView imdbRating;
    private ProgressBar ratingsLoader;
    private View snackBarView;

    public FragmentPrime() {
    }

    public static FragmentPrime newInstance(@NonNull Bundle args) {
        FragmentPrime fragment = new FragmentPrime();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
            itemType = (MediaType) getArguments().getSerializable("filtering_type");
            clickedItem = (BaseMediaData) getArguments().getParcelable(CLICKED_ITEM);
            Log.d("test", "itemType is " + itemType.toString());
        }
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.detail_parent);
        progressView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.progressView);
        backDropImage = (ImageView) getActivity().findViewById(R.id.app_bar_image);
        trailerButton = (ImageButton) getActivity().findViewById(R.id.imageButton);
        presenter.setFilteringType(itemType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_item_detail, container, false);
        initialiseViewChildren(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favorite = (FloatingActionButton) getActivity().findViewById(R.id.favorite);
        favorite.setOnClickListener(this);
        favorite.setTag(R.drawable.ic_favorite_border_black_24dp);
        snackBarView = getActivity().findViewById(R.id.activity_detail_coord_layout);
        if (clickedItem != null) {
            presenter.getMovieDetails(clickedItem);
        }
    }

    private void initialiseViewChildren(View rootView) {

        detailView = (FrameLayout) rootView.findViewById(R.id.fragment_content_parent);
        title = (TextView) detailView.findViewById(R.id.title);

        favorite = (FloatingActionButton) rootView.findViewById(R.id.favorite);
        basicDetails = (RelativeLayout) detailView.findViewById(R.id.basic_details);
        genres = (TextView) basicDetails.findViewById(R.id.genres);
        lang = (TextView) basicDetails.findViewById(R.id.lang);
        runtime = (TextView) basicDetails.findViewById(R.id.runtime_date);
        ratingsView = (RelativeLayout) detailView.findViewById(R.id.ratingsView);
        ratingsLoader = (ProgressBar) ratingsView.findViewById(R.id.ratings_load);
        ratingsLoader.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        rtRating = (TextView) ratingsView.findViewById(R.id.rt_rating);
        imdbRating = (TextView) ratingsView.findViewById(R.id.imdb_rating);
        synopsis = (TextView) detailView.findViewById(R.id.plot);
        share = (ImageButton) detailView.findViewById(R.id.imageButton5);
        addCastCrewImageSliders();
        trailerButton.setOnClickListener(this);

        share.setOnClickListener(this);
    }

    private void addCastCrewImageSliders() {
        listener.addCreditImageSlider();
    }

    @Override
    public void setPresenter(SearchItemDetailContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter, TAG + " presenter can't be null!");
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
    public void showRatingsViewLoadingIndicator(boolean show) {

        if (show) {
            ratingsLoader.setVisibility(View.VISIBLE);
        } else {
            ratingsLoader.setVisibility(View.GONE);
        }

    }

    @Override
    public void showTestToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingError() {
        share.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(snackBarView,
                "Error in Connectivity.", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Reload", new ErrorHandlerClickListener());
    }

    @Override
    public void showUi(MediaBasic data) {
        presenter.checkMediaInDB(data);
        if (presenter.getFilteringType().equals(MediaType.MOVIES))
            setUpMovieDetails((MovieInfo) data);
        else if (presenter.getFilteringType().equals(MediaType.TV))
            setUpTvDetails((TvInfo) data);
    }

    private void setUpTvDetails(TvInfo data) {
        retrievedItem = data;
        Picasso picasso = Picasso.with(getActivity());
        picasso.load("https://image.tmdb.org/t/p/original" + data.getBackdropPath()).fit()
                .error(R.drawable.imgfound).into(backDropImage);
        showGenresList(data.getGenres());

        title.setText(data.getOriginalName());
        lang.setText(data.getOriginalLanguage().toUpperCase(Locale.ENGLISH));
        //setRatings(data);
        runtime.setText(String.valueOf(data.getEpisodeRunTime()) + "min");
        synopsis.setText(data.getOverview());
        String trailer = getVideoUrl(data);
        if (trailer.isEmpty())
            trailerButton.setVisibility(View.GONE);
        else
            trailerKey = trailer;
        listener.updateImageSliders(data.getCredits().getCast(), data.getCredits().getCrew());
    }

    private void setUpMovieDetails(MovieInfo data) {
        retrievedItem = (MediaBasic) data;
        Picasso picasso = Picasso.with(getActivity());
        picasso.load("https://image.tmdb.org/t/p/original" + data.getBackdropPath()).fit()
                .error(R.drawable.imgfound).into(backDropImage);
        showGenresList(data.getGenresList());
        title.setText(data.getOriginalTitle());
        lang.setText(data.getOriginalLanguage().toUpperCase(Locale.ENGLISH));
        //setRatings(data);
        runtime.setText(String.valueOf(data.getRuntime()) + "min");
        synopsis.setText(data.getOverview());
        String trailer = getVideoUrl(data);
        if (trailer.isEmpty())
            trailerButton.setVisibility(View.GONE);
        else
            trailerKey = trailer;
        listener.updateImageSliders(data.getCredits().getCast(), data.getCredits().getCrew());
    }

    @Override
    public void showRatingsUi(OmdbMovieDetails data) {
        if (data == null ||
                data.getRatings().isEmpty()) {
            imdbRating.setText("-");
            rtRating.setText("-");
        } else {
            if (data.getRatings().get(0).getValue().equals("N/A"))
                imdbRating.setText("-");
            else
                imdbRating.setText(data.getRatings().get(0).getValue());
            if (data.getRatings().size() > 1) {
                if (data.getRatings().get(1).getSource().equals("Rotten Tomatoes") &&
                        data.getRatings().get(1).getValue().equals("N/A"))
                    rtRating.setText("-");
                else
                    rtRating.setText(data.getRatings().get(1).getValue());
            } else {
                rtRating.setText("-");
            }
        }
    }

    private String getVideoUrl(MediaBasic item) {
        List<Video> list = new ArrayList<>();
        if (itemType.equals(MediaType.TV))
            list = ((TvInfo) item).getVideos().getVideos();
        else
            list = ((MovieInfo) item).getVideos().getVideos();
        ArrayList<String> trailerList = new ArrayList<>();
        for (Video res : list) {
            if (res.getSite().equals("YouTube"))
                trailerList.add(res.getKey());
        }
        return trailerList.isEmpty() ? "" : trailerList.get(0);
    }

    private void showGenresList(List<Genre> item) {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (int i = 0; i < item.size(); i++) {
            if (i == 3) break;
            sb.append(delim).append(item.get(i).getName());
            delim = ", ";
        }
        genres.setText(sb.toString());
    }

    @Override
    public void setFavorite(boolean status) {
        if (status) {
            favorite.setImageDrawable(ApplicationClass.getInstance()
                    .getResources().getDrawable(R.drawable.favorite));
            favorite.setTag(R.drawable.favorite);
        }
    }

    @Override
    public void onClick(View v) {
        Context context = ApplicationClass.getInstance();
        if (v instanceof ImageButton && v.getId() == R.id.imageButton) {
            if (trailerKey != null && !trailerKey.isEmpty()) {
                if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(context).equals(YouTubeInitializationResult.SUCCESS))
                    getActivity().startActivity(
                            YouTubeStandalonePlayer.createVideoIntent(
                                    getActivity(),
                                    YOUTUBE_API_KEY,
                                    trailerKey
                            )
                    );
                else
                    showTestToast("No youtube service found");
            } else {
                Toast.makeText(context, "No trailer available.", Toast.LENGTH_SHORT).show();
            }
        }
        if (v instanceof FloatingActionButton && v.getId() == R.id.favorite) {
            FloatingActionButton button = (FloatingActionButton) v;
            int tag = (int) button.getTag();
            toggleFavorite(button, tag, context);
        }

        if (v instanceof ImageButton && v.getId() == R.id.imageButton5) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this trailer: "
                    + ((presenter.getFilteringType().equals(MediaType.MOVIES)) ? ((MovieInfo) retrievedItem).getTitle() : ((TvInfo) retrievedItem).getOriginalName()) + "! "
                    + "www.youtube.com/watch?v=" + trailerKey
                    + " sent via: " + getResources().getString(R.string.app_name));
            sendIntent.setType("text/plain");
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }
    }

    private void toggleFavorite(FloatingActionButton button, int tag, Context context) {
        if (tag == R.drawable.ic_favorite_border_black_24dp) {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite, null));
            button.setTag(R.drawable.favorite);
            presenter.addMediaToFavorites((MediaBasic) retrievedItem);
        } else {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, null));
            button.setTag(R.drawable.ic_favorite_border_black_24dp);
            presenter.removeMediaFromFavorites((MediaBasic) retrievedItem);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    private class ErrorHandlerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (clickedItem != null) {
                presenter.getMovieDetails(clickedItem);
            }
        }
    }

    public interface FragmentInteractionListener {
        void addCreditImageSlider();

        void updateImageSliders(List<MediaCreditCast> cast, List<MediaCreditCrew> crew);
    }
}
