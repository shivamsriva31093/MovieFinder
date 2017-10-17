package task.application.com.moviefinder.ui.appsearch;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidtmdbwrapper.enums.MediaType;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.navigation.NavigationModel;
import task.application.com.moviefinder.ui.base.BaseActivity;
import task.application.com.moviefinder.ui.utility.widgets.ClearableEditText;
import task.application.com.moviefinder.util.CustomTabLayout;

public class AppSearchActivity extends BaseActivity
        implements AppSearchFragment.OnFragmentInteractionListener {

    AppSearchFragment fragment;
    private MyPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CustomTabLayout tabLayout;

    private TextInputLayout searchInputTextLayout;
    private ClearableEditText searchInput;
    private ProgressBar progressBar;
    private ImageView backButton;
    private String searchQuery = "";

    private int count = 0;

    private static SparseArray<AppSearchContract.Presenter> presenters =
            new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_search);
        initViews();
        setUpSearchBox();
    }

    private void attemptToSearch(String query) {
        if (TextUtils.isEmpty(query))
            return;
        searchQuery = query;
        progressBar.setVisibility(View.VISIBLE);
        presenters.get(0).setQuery(searchQuery);
        presenters.get(0).searchQuery(query);
        presenters.get(1).setQuery(searchQuery);
        presenters.get(1).searchQuery(query);
        presenters.get(2).setQuery(searchQuery);
        presenters.get(2).searchQuery(query);
        setInputMethodVisibile(false);
    }

    private void setUpSearchBox() {
        searchInputTextLayout = (TextInputLayout) findViewById(R.id.searchInputTextLayout);
        searchInput = (ClearableEditText) findViewById(R.id.searchInput);
        backButton = (ImageView) findViewById(R.id.backButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchInput.setOnTouchListener((v, event) -> {
            searchInput.setFocusableInTouchMode(true);
            searchInput.requestFocus();
            return false;
        });

        searchInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager1 = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                attemptToSearch(searchInput.getText().toString());
            }
            return false;
        });
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void changeProBarVisibility(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            count += 1;
        }
        if (count == 3) {
            progressBar.setVisibility(View.GONE);
            count = 0;
        }
    }

    private void initViews() {
        mSectionsPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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
    protected void onResume() {
        super.onResume();
        searchInput.setFocusableInTouchMode(true);
        searchInput.requestFocus();
        searchInput.setOnFocusChangeListener((view, hasFocus) -> {
            setInputMethodVisibile(hasFocus);
        });
    }

    private Runnable showInputMethodRunnable = () -> {
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.showSoftInput(searchInput, 0);
    };

    private void setInputMethodVisibile(boolean hasFocus) {
        if (hasFocus)
            new Handler().post(showInputMethodRunnable);
        else {
            new Handler().removeCallbacks(showInputMethodRunnable);
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }

    @Override
    protected NavigationModel.NavigationItemEnum getSelfNavDrawerItem() {
        return NavigationModel.NavigationItemEnum.APPSEARCH;
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
        getMenuInflater().inflate(R.menu.menu_app_search, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.setFocusable(false);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static class MyPagerAdapter extends CustomFragmentStatePagerAdapter<AppSearchFragment> {

        private static int NUM_ITEMS = 3;
        private static MediaType[] queryType = {MediaType.MOVIES, MediaType.TV, MediaType.PEOPLE};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AppSearchFragment fragment = (AppSearchFragment)
                    super.instantiateItem(container, position);
            presenters.put(position, new AppSearchPresenter(fragment, fragment.getTag()));
            presenters.get(position).setQueryType(queryType[position]);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            presenters.remove(position);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putSerializable("queryType", MediaType.MOVIES);
                    break;
                case 1:
                    bundle.putSerializable("queryType", MediaType.TV);
                    break;
                case 2:
                    bundle.putSerializable("queryType", MediaType.PEOPLE);
                    break;
                default:
                    return null;
            }
            return AppSearchFragment.newInstance(bundle);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Movies";
                case 1:
                    return "Tv Shows";
                case 2:
                    return "People";
            }
            return null;
        }
    }
}
