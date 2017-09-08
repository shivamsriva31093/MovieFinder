package task.application.com.moviefinder.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.io.Serializable;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.navigation.NavigationModel;
import task.application.com.moviefinder.ui.appsearch.AppSearchActivity;
import task.application.com.moviefinder.ui.base.BaseActivity;
import task.application.com.moviefinder.util.ActivityUtils;
import task.application.com.moviefinder.util.CustomSpannableStringBuilder;
import task.application.com.moviefinder.util.CustomTabLayout;

public class DiscoverActivity extends BaseActivity {

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private CustomTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setElevation(0);
        initViews();
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
        float scale = getResources().getDisplayMetrics().density;
        tabLayout.setSelectedTabIndicatorHeight((int) (scale * 1.5f));
        tabLayout.setupWithViewPager(mViewPager);

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
                    break;
                case 1:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.UPCOMING);
                    break;
                case 2:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.POPULAR);
                    break;
                case 3:
                    bundle.putSerializable(RecentMoviesFragment.QUERY_TYPE, QueryType.TOP_RATED);
                    break;
            }
            fragment = RecentMoviesFragment.newInstance(bundle);
            presenter = new DiscoverPresenter(fragment, position + "");
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
