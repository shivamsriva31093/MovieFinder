package task.application.com.colette.ui.itemdetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.R;
import task.application.com.colette.ui.base.PresenterCache;
import task.application.com.colette.ui.base.PresenterFactory;
import task.application.com.colette.ui.utility.widgets.CollapsibleTextView;
import task.application.com.colette.ui.utility.widgets.GeneralTextView;
import task.application.com.colette.util.ActivityUtils;
import task.application.com.colette.util.Util;

/**
 * Created by sHIVAM on 3/16/2017.
 */

public class FragmentPrime extends Fragment implements SearchItemDetailContract.View, View.OnClickListener {

    private static final String TAG = FragmentPrime.class.getName();
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String YOUTUBE_API_KEY = "AIzaSyC9iXjkY03gWbADszp0x9zX2yRvRMYjaxo";

    private boolean isDestroyedBySystem;
    private PresenterCache<SearchItemDetailPresenter> presenterCache
            = PresenterCache.getInstance();

    private SearchItemDetailContract.Presenter presenter;
    private FragmentInteractionListener listener;
    private BaseMediaData clickedItem;
    private MediaType itemType;
    private BaseMediaData retrievedItem;
    private FrameLayout fragmentContainer;
    private ImageView backDropImage;
    private AVLoadingIndicatorView progressView;
    private FrameLayout detailView;
    private RelativeLayout basicDetails;
    private RelativeLayout ratingsView;
    private ImageButton trailerButton;
    private CircleImageView circleImageView;
    private GeneralTextView genres;
    private GeneralTextView genresTitle;
    private GeneralTextView langTitle;
    private GeneralTextView runtimeTitle;
    private NumberProgressBar syncProgress;
    private NumberProgressBar syncProgress1;
    private GeneralTextView title;
    private GeneralTextView lang;
    private GeneralTextView runtime;
    private CollapsibleTextView synopsis;
    private String trailerKey;
    private GeneralTextView rtRating;
    private GeneralTextView imdbRating;
    private ImageView imdbImage;
    private ImageView rtImage;
    private View snackBarView;
    private RelativeLayout emptyDataHandlerView;
    private ConstraintLayout contentHolder;
    private GeneralTextView castTitle;
    private GeneralTextView crewTitle;
    private GeneralTextView synopsisTitle;
    private SwipeRefreshLayout layout;
    private GeneralTextView releaseTitle;
    private GeneralTextView releaseDate;
    private AppCompatButton trailerAppCompatButton;
    private AppCompatImageButton share;
    private LikeButton favorite;

    private SwipeRefreshLayout swipeRefreshLayout;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_item_detail, container, false);
        syncProgress = (NumberProgressBar) rootView.findViewById(R.id.progress_sync);
        float scale = getContext().getResources().getDisplayMetrics().density;
        syncProgress.setProgress(0);
        syncProgress.setProgressTextSize(scale * 12);
        syncProgress.setReachedBarHeight(scale * 4);
        syncProgress.setProgressTextVisibility(com.daimajia.numberprogressbar.NumberProgressBar.ProgressTextVisibility.Invisible);
        syncProgress.setUnreachedBarHeight(scale * 4);
        syncProgress.setUnreachedBarColor(Color.parseColor("#dfe0e2"));
        syncProgress.setReachedBarColor(Color.parseColor("#d23041"));
        syncProgress.setVisibility(View.GONE);

        syncProgress1 = (NumberProgressBar) rootView.findViewById(R.id.progress_sync1);
        syncProgress1.setProgress(0);
        syncProgress1.setProgressTextSize(scale * 12);
        syncProgress1.setReachedBarHeight(scale * 4);
        syncProgress1.setProgressTextVisibility(com.daimajia.numberprogressbar.NumberProgressBar.ProgressTextVisibility.Invisible);
        syncProgress1.setUnreachedBarHeight(scale * 4);
        syncProgress1.setUnreachedBarColor(Color.parseColor("#dfe0e2"));
        syncProgress1.setReachedBarColor(Color.parseColor("#d23041"));
        syncProgress1.setVisibility(View.GONE);
        initialiseViewChildren(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            itemType = (MediaType) savedInstanceState.getSerializable("filtering_type");
            clickedItem = savedInstanceState.getParcelable(CLICKED_ITEM);
            presenter = presenterCache.getPresenter(TAG, factory);
        }
        fragmentContainer = (FrameLayout) getActivity().findViewById(R.id.container);
        progressView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.progressView);
        backDropImage = (ImageView) getActivity().findViewById(R.id.app_bar_image);
//        shareView = (AppCompatImageButton) getActivity().findViewById(R.id.shareButton);
        trailerButton = (ImageButton) getActivity().findViewById(R.id.imageButton1);
        circleImageView = (CircleImageView)getActivity().findViewById(R.id.trailerBackground);
        trailerAppCompatButton = (AppCompatButton)getActivity().findViewById(R.id.imageButton5);
//        favorite = (FloatingActionButton) getActivity().findViewById(R.id.favorite);
//        favorite.setOnClickListener(this);
//        shareView.setOnClickListener(this);
        layout =(SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_refresh);

        snackBarView = getActivity().findViewById(R.id.activity_detail_coord_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(presenter != null) {
                presenter.getMovieDetails(clickedItem);
            }
        });


        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryMid),
                ContextCompat.getColor(getActivity(), R.color.status_bar_light_grey)
        );
        presenter.setFilteringType(itemType);
        if (clickedItem != null) {
            presenter.getMovieDetails(clickedItem);
        }
    }

    private void initialiseViewChildren(View rootView) {

        detailView = (FrameLayout) rootView.findViewById(R.id.fragment_content_parent);
        title = (GeneralTextView) detailView.findViewById(R.id.title);

        emptyDataHandlerView = (RelativeLayout) detailView.findViewById(R.id.empty_data_handler);
        contentHolder = (ConstraintLayout) rootView.findViewById(R.id.item_detail);
        basicDetails = (RelativeLayout) detailView.findViewById(R.id.basic_details);
        genres = (GeneralTextView) basicDetails.findViewById(R.id.genres);
        lang = (GeneralTextView) basicDetails.findViewById(R.id.lang);
        runtime = (GeneralTextView) basicDetails.findViewById(R.id.runtime_date);
        genresTitle = (GeneralTextView) basicDetails.findViewById(R.id.genres_title);
        langTitle = (GeneralTextView) basicDetails.findViewById(R.id.lang_title);
        runtimeTitle = (GeneralTextView) basicDetails.findViewById(R.id.runtime_title);
        ratingsView = (RelativeLayout) detailView.findViewById(R.id.ratingsView);
        rtRating = (GeneralTextView) ratingsView.findViewById(R.id.rt_rating);
        imdbImage = (ImageView) ratingsView.findViewById(R.id.imageView10);
        rtImage = (ImageView) ratingsView.findViewById(R.id.imageView12);
        imdbRating = (GeneralTextView) ratingsView.findViewById(R.id.imdb_rating);
        synopsis = (CollapsibleTextView) detailView.findViewById(R.id.plot);
        share = (AppCompatImageButton) detailView.findViewById(R.id.share_button);
        favorite = (LikeButton) detailView.findViewById(R.id.favorite_button);

        favorite.setEnabled(true);
        favorite.setTag(R.drawable.ic_favorite_border_black_24dp);
        addCastCrewImageSliders();
        share.setOnClickListener(this);
        favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                MediaBasic item = (MediaBasic) retrievedItem;
                item.setImdbRating(imdbRating.getText().toString());
                presenter.addMediaToFavorites(item);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                MediaBasic item = (MediaBasic) retrievedItem;
                item.setImdbRating(imdbRating.getText().toString());
                presenter.removeMediaFromFavorites(item);
            }
        });
        castTitle = (GeneralTextView) contentHolder.findViewById(R.id.cast);
        crewTitle = (GeneralTextView) contentHolder.findViewById(R.id.crew);
        synopsisTitle = (GeneralTextView) contentHolder.findViewById(R.id.synopsis);
        releaseDate = (GeneralTextView) basicDetails.findViewById(R.id.release_date);
        releaseTitle = (GeneralTextView) basicDetails.findViewById(R.id.release_date_tv);
        castTitle.setVisibility(View.GONE);
        imdbImage.setVisibility(View.GONE);
        rtImage.setVisibility(View.GONE);
        ratingsView.setVisibility(View.GONE);
        crewTitle.setVisibility(View.GONE);
        synopsisTitle.setVisibility(View.GONE);
        genresTitle.setVisibility(View.GONE);
        langTitle.setVisibility(View.GONE);
        runtimeTitle.setVisibility(View.GONE);
        releaseTitle.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyedBySystem = false;
        trailerButton.setOnClickListener(this);
        trailerAppCompatButton.setOnClickListener(this);

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
            if(swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            progressView.hide();
            progressView.setVisibility(View.GONE);
            detailView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showRatingsViewLoadingIndicator(boolean show) {
    }

    @Override
    public void showTestToast(String msg) {
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        ActivityUtils.showBottomSheetMessage(msg, getActivity(), R.drawable.heart_white, 1125, null);
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
//        Log.d("test_d", data.getImdbRating() + " " + presenter.getFilteringType().toString());
        if (presenter.getFilteringType().equals(MediaType.MOVIES))
            setUpMovieDetails((MovieInfo) data);
        else if (presenter.getFilteringType().equals(MediaType.TV))
            setUpTvDetails((TvInfo) data);
    }

    private void setUpTvDetails(TvInfo data) {
        retrievedItem = data;

        if ((data.getCredits().getCast().isEmpty()) || data.getCredits().getCrew().isEmpty()
                && (data.getOverview().isEmpty() && data.getOverview().length() <= 10)
                && data.getName().isEmpty()) {
            handleEmptyData();
            return;
        }

        castTitle.setVisibility(View.VISIBLE);
        crewTitle.setVisibility(View.VISIBLE);
        synopsisTitle.setVisibility(View.VISIBLE);
        genresTitle.setVisibility(View.VISIBLE);
        langTitle.setVisibility(View.VISIBLE);
        runtimeTitle.setVisibility(View.VISIBLE);
        releaseTitle.setVisibility(View.VISIBLE);

        if (data.getCredits().getCast().isEmpty()) {
            castTitle.setVisibility(View.GONE);
        }
        if (data.getCredits().getCrew().isEmpty()) {
            crewTitle.setVisibility(View.GONE);
        }
        if (data.getOverview().isEmpty()) {
            synopsisTitle.setVisibility(View.GONE);
        }


        Picasso picasso = Picasso.with(getActivity());
        picasso.load("https://image.tmdb.org/t/p/original" + data.getBackdropPath()).fit()
                .error(R.drawable.imgfound).into(backDropImage);
        showGenresList(data.getGenres());

        title.setText(data.getName());
        lang.setText(data.getOriginalLanguage().toUpperCase(Locale.ENGLISH));
        //setRatings(data);
        runtime.setText(String.valueOf(data.getEpisodeRunTime()) + "min");
        releaseDate.setText(data.getFirstAirDate());
        synopsis.setText(data.getOverview());
        String trailer = getVideoUrl(data);
        if (trailer.isEmpty()) {
            trailerButton.setVisibility(View.GONE);
            trailerAppCompatButton.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            circleImageView.setVisibility(View.GONE);
        }
        else {
            trailerKey = trailer;
        }
        listener.updateImageSliders(data.getCredits().getCast(), data.getCredits().getCrew());
    }

    private void handleEmptyData() {
        contentHolder.setVisibility(View.GONE);
        Log.d("emptyData", "true");
        emptyDataHandlerView.setVisibility(View.VISIBLE);
        emptyDataHandlerView.setOnClickListener(view -> {
            presenter.getMovieDetails(clickedItem);
        });
    }

    private void setUpMovieDetails(MovieInfo data) {
        retrievedItem = (MediaBasic) data;
        Log.d("emptyData", data.getCredits() + "");
        if ((data.getCredits().getCast().isEmpty()) || data.getCredits().getCrew().isEmpty()
                && (data.getOverview().isEmpty() && data.getOverview().length() <= 10)
                && (data.getTitle().isEmpty() || data.getOriginalTitle().isEmpty())) {
            handleEmptyData();
            return;
        }

        castTitle.setVisibility(View.VISIBLE);
        crewTitle.setVisibility(View.VISIBLE);
        synopsisTitle.setVisibility(View.VISIBLE);
        genresTitle.setVisibility(View.VISIBLE);
        langTitle.setVisibility(View.VISIBLE);
        runtimeTitle.setVisibility(View.VISIBLE);
        releaseTitle.setVisibility(View.VISIBLE);
        if (data.getCredits().getCast().isEmpty()) {
            castTitle.setVisibility(View.GONE);
        }
        if (data.getCredits().getCrew().isEmpty()) {
            crewTitle.setVisibility(View.GONE);
        }
        if (data.getOverview().isEmpty()) {
            synopsis.setVisibility(View.GONE);
        }
//        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/original" + data.getBackdropPath())
//                .error(R.drawable.imgfound)
//                .into(backDropImage,
//                        PicassoPalette.with("https://image.tmdb.org/t/p/original" + data.getBackdropPath(), backDropImage)
////                                .use(PicassoPalette.Profile.MUTED_DARK)
////                                .intoBackground(textView)
////                                .intoTextColor(textView)
//                                .use(PicassoPalette.Profile.VIBRANT)
//                                .intoBackground(contentHolder, PicassoPalette.Swatch.RGB)
//
//                );
        //swipeRefreshLayout.setBackgroundColor(Color.BLACK);


        Picasso picasso = Picasso.with(getActivity());
        picasso.load("https://image.tmdb.org/t/p/original" + data.getBackdropPath()).fit()
                .error(R.drawable.imgfound).into(backDropImage);
        showGenresList(data.getGenresList());

        title.setText(data.getTitle());
        lang.setText(data.getOriginalLanguage().toUpperCase(Locale.ENGLISH));
        //setRatings(data);
        runtime.setText(String.valueOf(data.getRuntime()) + "min");
        releaseDate.setText(data.getReleaseDate());
        synopsis.setText(data.getOverview());
        String trailer = getVideoUrl(data);
        if (trailer.isEmpty()) {
            trailerButton.setVisibility(View.GONE);
            trailerAppCompatButton.setVisibility(View.GONE);
            circleImageView.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
        }
        else {
            trailerKey = trailer;
        }
        listener.updateImageSliders(data.getCredits().getCast(), data.getCredits().getCrew());
    }

    @Override
    public void showRatingsUi(OmdbMovieDetails data) {
        if (data == null) {
//            imdbRating.setText("-");
//            rtRating.setText("-");
            ratingsView.setVisibility(View.GONE);
        } else {
            if (data.getRatings().get(0).getValue().equals("N/A"))
                imdbRating.setText("N/A");
            else {
                ratingsView.setVisibility(View.VISIBLE);
                String imdbString = data.getRatings().get(0).getValue().substring(0, 3);
                imdbRating.setText(imdbString+" "+"stars");
                Log.d("imdb", imdbString);
                imdbImage.setVisibility(View.VISIBLE);
                syncProgress.setVisibility(View.VISIBLE);
                float temp = 0;
                try {
                    temp = Float.parseFloat(imdbString);
                    syncProgress.setProgress((int) (temp * 10));
                } catch (NumberFormatException ex) {
                    imdbImage.setVisibility(View.GONE);
                    syncProgress.setVisibility(View.GONE);
                    imdbRating.setVisibility(View.GONE);
                }

            }
            if (data.getRatings().size() > 1) {
                if (data.getRatings().get(1).getSource().equals("Rotten Tomatoes") &&
                        data.getRatings().get(1).getValue().equals("N/A"))
                    rtRating.setText("N/A");
                else {
                    ratingsView.setVisibility(View.VISIBLE);
                    int length = data.getRatings().get(1).getValue().length();
                    String rtString = data.getRatings().get(1).getValue().substring(0, length - 1);
                    Log.d("rt", rtString);
                    rtImage.setVisibility(View.VISIBLE);
                    syncProgress1.setVisibility(View.VISIBLE);
                    float temp = 0;
                    try {
                        temp = Float.parseFloat(rtString);
                        syncProgress1.setProgress((int) (temp));
                        rtRating.setText(data.getRatings().get(1).getValue() + " " + "positive");
                    } catch (NumberFormatException ex) {
                        rtImage.setVisibility(View.GONE);
                        syncProgress1.setVisibility(View.GONE);
                        rtRating.setVisibility(View.GONE);
                    }


                }
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
//            favorite.setImageDrawable(ApplicationClass.getInstance()
//                    .getResources().getDrawable(R.drawable.favorite));
            favorite.setEnabled(true);
            favorite.setLiked(true);
            favorite.setTag(R.drawable.favorite);
        }
    }

    @Override
    public void onClick(View v) {
        Context context = ApplicationClass.getInstance();
        if (v instanceof ImageButton && v.getId() == R.id.imageButton1 || (v instanceof AppCompatButton && v.getId() == R.id.imageButton5)) {
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
        if (v instanceof LikeButton && v.getId() == R.id.favorite_button) {
            LikeButton button = (LikeButton) v;
            favorite.setEnabled(true);
            favorite.callOnClick();
//            int tag = (int) button.getTag();
//            toggleFavorite(button, tag, context);
        }
        if (v instanceof AppCompatImageButton && v.getId() == R.id.share_button) {
            AppCompatImageButton button = (AppCompatImageButton) v;
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

    private void toggleFavorite(AppCompatImageButton button, int tag, Context context) {
        MediaBasic item = (MediaBasic) retrievedItem;
        item.setImdbRating(imdbRating.getText().toString());
        if (tag == R.drawable.ic_favorite_border_black_24dp) {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite, null));
            button.setTag(R.drawable.favorite);
            presenter.addMediaToFavorites(item);
        } else {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, null));
            button.setTag(R.drawable.ic_favorite_border_black_24dp);
            item.setImdbRating(imdbRating.getText().toString());
            presenter.removeMediaFromFavorites(item);
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
        if (!isDestroyedBySystem) {
            presenterCache.removePresenter(TAG);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        outState.putSerializable("filtering_type", itemType);
        outState.putParcelable(CLICKED_ITEM, clickedItem);
        super.onSaveInstanceState(outState);
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

    private PresenterFactory<SearchItemDetailPresenter> factory = () -> new SearchItemDetailPresenter(FragmentPrime.this);
}
