package task.application.com.moviefinder.ui.itemdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.credits.MediaCreditCrew;
import com.androidtmdbwrapper.model.mediadetails.Video;
import com.androidtmdbwrapper.model.movies.MovieInfo;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.utility.ImageSlider;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 3/16/2017.
 */

public class FragmentPrime extends Fragment implements SearchItemDetailContract.View, View.OnClickListener {
    private static final String TAG = FragmentPrime.class.getName();
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String CAST_FRAG = "castFragment";
    private static final String CREW_FRAG = "crewFragment";
    private static final String YOUTUBE_API_KEY = "AIzaSyC9iXjkY03gWbADszp0x9zX2yRvRMYjaxo";
    private SearchItemDetailContract.Presenter presenter;

    private MovieDb clickedItem;

    private RelativeLayout fragmentContainer;
    private ImageView backDropImage;
    private AVLoadingIndicatorView progressView;
    private FrameLayout detailView;
    private RelativeLayout basicDetails;
    private CircleImageView poster;
    private ImageButton trailerButton;
    private ImageButton favorite;
    private ImageButton share;
    private TextView genres;
    private TextView title;
    private TextView lang;
    private TextView runtime;
    private TextView synopsis;
    private String trailerKey;
    private ImageSlider<MediaCreditCast> castFrag;
    private ImageSlider<MediaCreditCrew> crewFrag;

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
            clickedItem = (MovieDb) getArguments().getSerializable(CLICKED_ITEM);
        }
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.detail_parent);
        progressView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.progressView);
        backDropImage = (ImageView) getActivity().findViewById(R.id.app_bar_image);
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
        if (clickedItem != null) {
            presenter.getMovieDetails(clickedItem);
        }
    }

    private void initialiseViewChildren(View rootView) {

        detailView = (FrameLayout) rootView.findViewById(R.id.fragment_content_parent);
        title = (TextView) detailView.findViewById(R.id.title);
        trailerButton = (ImageButton) detailView.findViewById(R.id.imageButton);
        poster = (CircleImageView) detailView.findViewById(R.id.movie_dp);
        basicDetails = (RelativeLayout) detailView.findViewById(R.id.basic_details);
        genres = (TextView) basicDetails.findViewById(R.id.genres);
        lang = (TextView) basicDetails.findViewById(R.id.lang);
        runtime = (TextView) basicDetails.findViewById(R.id.runtime_date);
        synopsis = (TextView) detailView.findViewById(R.id.plot);
        favorite = (ImageButton) detailView.findViewById(R.id.imageButton4);
        share = (ImageButton) detailView.findViewById(R.id.imageButton5);
        castFrag = ImageSlider.newInstance();
        crewFrag = ImageSlider.newInstance();
        Util.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                castFrag, R.id.cast_image_slider, CAST_FRAG);
        Util.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                crewFrag, R.id.crew_image_slider, CREW_FRAG);
        trailerButton.setOnClickListener(this);
        favorite.setOnClickListener(this);
        favorite.setTag(R.drawable.ic_favorite_border_black_24dp);
        share.setOnClickListener(this);
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
    public void showLoadingError() {

    }

    @Override
    public void showUi(MovieInfo item) {
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/original" + item.getBackdropPath()).fit()
                .error(R.drawable.trailer).into(backDropImage);
//        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + item.getPosterPath())
//                .error(android.R.drawable.picture_frame).into(poster);
        showGenresList(item);
        title.setText(item.getOriginalTitle());
        lang.setText(item.getOriginalLanguage().toUpperCase(Locale.ENGLISH));
        runtime.setText(String.valueOf(item.getRuntime()) + "min");
        synopsis.setText(item.getOverview());
        trailerKey = getVideoUrl(item);
        castFrag.updateImageSliderView(item.getCredits().getCast());
        crewFrag.updateImageSliderView(item.getCredits().getCrew());
    }

    private String getVideoUrl(MovieInfo item) {
        ArrayList<String> trailerList = new ArrayList<>();
        for (Video res : item.getVideos().getVideos()) {
            if (res.getSite().equals("YouTube"))
                trailerList.add(res.getKey());
        }
        return trailerList.isEmpty() ? "" : trailerList.get(0);
    }

    private void showGenresList(MovieInfo item) {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        for (int i = 0; i < item.getGenresList().size(); i++) {
            if (i == 3) break;
            sb.append(delim).append(item.getGenresList().get(i).getName());
            delim = ", ";
        }
        genres.setText(sb.toString());
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageButton && v.getId() == R.id.imageButton) {
            if (trailerKey != null && !trailerKey.isEmpty()) {
                startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                        YOUTUBE_API_KEY, trailerKey));
            }
        }
        if (v instanceof ImageButton && v.getId() == R.id.imageButton4) {
            ImageButton button = (ImageButton) v;
            int tag = (int) button.getTag();
            if (tag == R.drawable.ic_favorite_border_black_24dp) {
                button.setImageDrawable(getResources().getDrawable(R.drawable.hearts, null));
                button.setTag(R.drawable.hearts);
            } else {
                button.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, null));
                button.setTag(R.drawable.ic_favorite_border_black_24dp);
            }
        }

        if (v instanceof ImageButton && v.getId() == R.id.imageButton5) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this movie: "
                    + clickedItem.getTitle() + "! "
                    + "www.youtube.com/watch?v=" + trailerKey
                    + " sent via: " + getResources().getString(R.string.app_name));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }
    }
}
