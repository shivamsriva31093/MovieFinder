package task.application.com.moviefinder.ui.discover;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.navigation.NavigationModel;
import task.application.com.moviefinder.ui.base.BaseActivity;
import task.application.com.moviefinder.util.CustomSpannableStringBuilder;

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private TabLayout tabLayout;

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
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setFillViewport(true);
        changeTabsFont();
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void changeTabsFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + "FrancoisOne-Regular.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return RecentMoviesFragment.newInstance(new Bundle());
                case 1:
                case 2:
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
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
                    spannableStringBuilder.append("Now", new AbsoluteSizeSpan(14), new RelativeSizeSpan(2f),
                            new ForegroundColorSpan(getResources().getColor(R.color.black)))
                            .append(" ")
                            .append("Playing", new RelativeSizeSpan(2f), new ForegroundColorSpan(getResources().getColor(R.color.black)));
                    return spannableStringBuilder.build();
                case 1:
                    spannableStringBuilder.append("Recent", new RelativeSizeSpan(2f),
                            new ForegroundColorSpan(getResources().getColor(R.color.black)))
                            .append(" ")
                            .append("Movies", new RelativeSizeSpan(2f), new ForegroundColorSpan(getResources().getColor(R.color.black)));
                    return spannableStringBuilder.build();
                case 2:
                    spannableStringBuilder.append("Popular", new RelativeSizeSpan(2f),
                            new ForegroundColorSpan(getResources().getColor(R.color.black)));
                    return spannableStringBuilder.build();
                default:
                    spannableStringBuilder.append("Top Rated", new RelativeSizeSpan(2f),
                            new ForegroundColorSpan(getResources().getColor(R.color.black)));
                    return spannableStringBuilder.build();
            }

        }
    }
}
