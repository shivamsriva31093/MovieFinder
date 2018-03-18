package task.application.com.colette.ui.discover;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.WeakHashMap;

import task.application.com.colette.R;
import task.application.com.colette.navigation.NavigationModel;
import task.application.com.colette.ui.appsearch.AppSearchActivity;
import task.application.com.colette.ui.base.BaseActivity;
import task.application.com.colette.ui.utility.SplashDataHelper;
import task.application.com.colette.util.ActivityUtils;
import task.application.com.colette.util.CustomSpannableStringBuilder;
import task.application.com.colette.util.CustomTabLayout;

import static task.application.com.colette.util.LogUtils.makeLogTag;


public class DiscoverActivity extends BaseActivity implements
        RecentMoviesFragment.OnFragmentInteractionListener {

    private static final String TAG = makeLogTag(DiscoverActivity.class);
    private int fragNetworkErrorCount = 0;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DiscoverPresenter presenter;
    private WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data
            = new WeakHashMap<>();
    private WeakHashMap<DiscoverActivity.QueryType, int[]> pages
            = new WeakHashMap<>();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private CustomTabLayout tabLayout;
    private DiscoverPresenter[] presenterList = new DiscoverPresenter[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discover);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setElevation(0);
        if (SplashDataHelper.getData() != null && !SplashDataHelper.getData().isEmpty()) {
            data = SplashDataHelper.getData();
            pages = SplashDataHelper.getDataPages();
        }
        initViews();
    }

    @Override
    public void onDataLoad(boolean status) {
    }


    @Override
    public void onNoNetworkError(boolean isNetworkAvailable) {
        if (!isNetworkAvailable) {
            fragNetworkErrorCount += 1;
            //Toast.makeText(this, "Retrying...", Toast.LENGTH_SHORT).show();

        }
        if (fragNetworkErrorCount == 4) {
            fragNetworkErrorCount = 0;
            ActivityUtils.showBottomSheetMessage(
                    "Check connection or tap above to retry",
                    this,
                    R.drawable.ic_error_outline_white_24px,
                    -1,
                    (dialogInterface) -> {
                        for (DiscoverContract.Presenter presenter : presenterList)
                            presenter.makeQuery("en", 1, null);
                    });
        }
    }

    private void initViews() {

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        findViewById(R.id.nav_button).setOnClickListener(view -> {
            new Handler().postDelayed(this::openNavDrawer, 150);
        });
        findViewById(R.id.search).setOnClickListener(view -> {
            Intent intent = new Intent(DiscoverActivity.this, AppSearchActivity.class);
            ActivityUtils.startNewActivity(DiscoverActivity.this, intent);
        });
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setFillViewport(true);
//        float scale = getResources().getDisplayMetrics().density;
//        tabLayout.setSelectedTabIndicatorHeight((int) (scale * 1.5f));
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setCustomTabColors(new ColorStateList(
        new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        },
                new int[]{
                        ContextCompat.getColor(this, R.color.white),
                        ContextCompat.getColor(this, R.color.white),
                        ContextCompat.getColor(this, R.color.colorPrimaryMid),
                        ContextCompat.getColor(this, R.color.body_text_1)
                }
        ), R.color.black);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected NavigationModel.NavigationItemEnum getSelfNavDrawerItem() {
        return NavigationModel.NavigationItemEnum.DISCOVER_MOVIES;
    }

    @Override
    public void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        super.onNavDrawerStateChanged(isOpen, isAnimating);
    }

    @Override
    public void onNavDrawerSlide(float offset) {
        super.onNavDrawerSlide(offset);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle bundle = new Bundle();
            RecentMoviesFragment fragment;
            switch (position) {
                case 0:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.NOW_PLAYING);
                    bundle.putParcelableArrayList("data", data.get(QueryType.NOW_PLAYING));
                    bundle.putInt("totalPages", pages.get(QueryType.NOW_PLAYING) == null ? 0 : pages.get(QueryType.NOW_PLAYING)[0]);
                    bundle.putInt("totalResults", pages.get(QueryType.NOW_PLAYING) == null ? 0 : pages.get(QueryType.NOW_PLAYING)[1]);
                    break;
                case 1:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.UPCOMING);
                    bundle.putParcelableArrayList("data", data.get(QueryType.UPCOMING));
                    bundle.putInt("totalPages", pages.get(QueryType.UPCOMING) == null ? 0 : pages.get(QueryType.UPCOMING)[0]);
                    bundle.putInt("totalResults", pages.get(QueryType.UPCOMING) == null ? 0 : pages.get(QueryType.UPCOMING)[1]);
                    break;
                case 2:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.POPULAR);
                    bundle.putParcelableArrayList("data", data.get(QueryType.POPULAR));
                    bundle.putInt("totalPages", pages.get(QueryType.POPULAR) == null ? 0 : pages.get(QueryType.POPULAR)[0]);
                    bundle.putInt("totalResults", pages.get(QueryType.POPULAR) == null ? 0 : pages.get(QueryType.POPULAR)[1]);
                    break;
                case 3:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.TOP_RATED);
                    bundle.putParcelableArrayList("data", data.get(QueryType.TOP_RATED));
                    bundle.putInt("totalPages", pages.get(QueryType.TOP_RATED) == null ? 0 : pages.get(QueryType.TOP_RATED)[1]);
                    bundle.putInt("totalResults", pages.get(QueryType.TOP_RATED) == null ? 0 : pages.get(QueryType.TOP_RATED)[1]);
                    break;
            }
            fragment = RecentMoviesFragment.newInstance(bundle);
            presenterList[position] = new DiscoverPresenter(fragment, position + "");
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CustomSpannableStringBuilder spannableStringBuilder = new CustomSpannableStringBuilder();
            switch (position) {
                case 0:
                    return "Now Playing";
                case 1:
                    return "Upcoming";
                case 2:
                    return "Popular";
                default:
                    return "Top Rated";
            }

        }
    }

    public enum QueryType implements Serializable {
        NOW_PLAYING,
        UPCOMING,
        POPULAR,
        TOP_RATED;

        QueryType() {
        }

        public String getQuery() {
            switch (this) {
                case NOW_PLAYING:
                    return "Now Playing";
                case POPULAR:
                    return "Popular";
                case TOP_RATED:
                    return "Top Rated";
                case UPCOMING:
                    return "Upcoming";
                default:
                    return "";
            }
        }
    }
}
