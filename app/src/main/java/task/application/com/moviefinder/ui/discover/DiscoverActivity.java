package task.application.com.moviefinder.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        tabLayout.setSelectedTabIndicatorHeight(4);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
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
            Log.d("test", position + "  is");
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
//                    spannableStringBuilder.append("Now", new AbsoluteSizeSpan(14), new RelativeSizeSpan(2f),
//                            new ForegroundColorSpan(getResources().getColor(R.color.black)))
//                            .append(" ")
//                            .append("Playing", new RelativeSizeSpan(2f), new ForegroundColorSpan(getResources().getColor(R.color.black)));
//                    return spannableStringBuilder.build();
                    return "Now Playing";
                case 1:
//                    spannableStringBuilder.append("Upcoming", new RelativeSizeSpan(2f),
//                            new ForegroundColorSpan(getResources().getColor(R.color.black)));
//                    return spannableStringBuilder.build();
                    return "Upcoming";
                case 2:
//                    spannableStringBuilder.append("Popular", new RelativeSizeSpan(2f),
//                            new ForegroundColorSpan(getResources().getColor(R.color.black)));
//                    return spannableStringBuilder.build();
                    return "Popular";
                default:
//                    spannableStringBuilder.append("Top Rated", new RelativeSizeSpan(2f),
//                            new ForegroundColorSpan(getResources().getColor(R.color.black)));
//                    return spannableStringBuilder.build();
                    return "Top Rated";
            }

        }
    }

    public enum QueryType implements Serializable {
        NOW_PLAYING,
        UPCOMING,
        POPULAR,
        TOP_RATED
    }
}
