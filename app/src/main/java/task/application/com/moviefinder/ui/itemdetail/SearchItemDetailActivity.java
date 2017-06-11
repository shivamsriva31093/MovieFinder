package task.application.com.moviefinder.ui.itemdetail;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.credits.MediaCreditCrew;

import java.util.List;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.navdrawer.NavigationDrawerActivity;
import task.application.com.moviefinder.ui.utility.ImageSlider;
import task.application.com.moviefinder.util.Util;

public class SearchItemDetailActivity extends NavigationDrawerActivity implements FragmentPrime.FragmentInteractionListener {

    private static final String SEARCH_ITEM = "searchItem";
    private static final String SEARCH_ITEM_DETAIL = "detail_frag";

    private SearchItemDetailPresenter presenter;
    private AppCompatImageButton menuButton;
    private FragmentPrime fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layoutView = getLayoutInflater().inflate(R.layout.activity_detail_new, null, false);
        setContentView(layoutView);
        Bundle bundle = new Bundle();
        if (getIntent().hasExtra(SEARCH_ITEM)) {
            bundle = getIntent().getBundleExtra(SEARCH_ITEM);
        }
        menuButton = (AppCompatImageButton) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fragment = (FragmentPrime)
                getSupportFragmentManager().findFragmentByTag(SEARCH_ITEM_DETAIL);
        if (fragment == null) {
            fragment = FragmentPrime.newInstance(bundle);
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.detail_parent, SEARCH_ITEM_DETAIL);
        }
        this.presenter = new SearchItemDetailPresenter(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter = new SearchItemDetailPresenter(fragment);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @SuppressWarnings("Unchecked")
    @Override
    public void addCreditImageSlider() {
        ImageSlider<MediaCreditCast> castFrag = (ImageSlider<MediaCreditCast>)
                getSupportFragmentManager().findFragmentByTag(ImageSlider.CAST_FRAG);
        ImageSlider<MediaCreditCrew> crewFrag = (ImageSlider<MediaCreditCrew>)
                getSupportFragmentManager().findFragmentByTag(ImageSlider.CREW_FRAG);

        if (castFrag == null) {
            castFrag = ImageSlider.<MediaCreditCast>newInstance();
            Util.addFragmentToActivity(getSupportFragmentManager(), castFrag,
                    R.id.cast_image_slider, ImageSlider.CAST_FRAG);
        }
        if (crewFrag == null) {
            crewFrag = ImageSlider.<MediaCreditCrew>newInstance();
            Util.addFragmentToActivity(getSupportFragmentManager(), crewFrag,
                    R.id.crew_image_slider, ImageSlider.CREW_FRAG);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void updateImageSliders(List<MediaCreditCast> cast, List<MediaCreditCrew> crew) {
        ImageSlider<MediaCreditCast> castFrag = (ImageSlider<MediaCreditCast>)
                getSupportFragmentManager().findFragmentByTag(ImageSlider.CAST_FRAG);
        ImageSlider<MediaCreditCrew> crewFrag = (ImageSlider<MediaCreditCrew>)
                getSupportFragmentManager().findFragmentByTag(ImageSlider.CREW_FRAG);
        if (castFrag != null) {
            castFrag.updateImageSliderView(cast);
        }
        if (crewFrag != null) {
            crewFrag.updateImageSliderView(crew);
        }
    }
}
