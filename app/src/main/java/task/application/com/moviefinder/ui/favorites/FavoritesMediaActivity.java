package task.application.com.moviefinder.ui.favorites;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.navigation.NavigationModel;
import task.application.com.moviefinder.ui.base.BaseActivity;
import task.application.com.moviefinder.ui.utility.BottomNavigationBehaviour;
import task.application.com.moviefinder.util.Util;

public class FavoritesMediaActivity extends BaseActivity implements
        FavoritesMediaFragment.OnFragmentInteractionListener {

    private static final String SEARCH_QUERY = "query";
    private static final String EMPTY_QUERY = "emptyQuery";

    private CharSequence searchQuery;
    private FavoritesMediaContract.Presenter presenter;
    FavoritesMediaFragment fragment = null;
    private EditText searchInput;
    private ImageView clearBtn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {

            case R.id.navigation_tv:
                bundle.putString("FILTER", "Tv");
                fragment = FavoritesMediaFragment.newInstance(bundle);
                break;

            case R.id.navigation_movies:
                bundle = new Bundle();
                bundle.putString("FILTER", "Movies");
                fragment = FavoritesMediaFragment.newInstance(bundle);
                break;
        }
        Util.replaceFragmentFromActivity(getSupportFragmentManager(), fragment, R.id.content);
        presenter = new FavoritesPresenter(fragment);
        return true;
    };
    private TextInputLayout searchInputTextLayout;
    private ImageView navDrawerButton;
    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_media);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCH_QUERY))
                searchQuery = savedInstanceState.getCharSequence(SEARCH_QUERY);
        }
        setUpBottomNavigationView();
        setUpSearchBox();
    }

    @Override
    protected NavigationModel.NavigationItemEnum getSelfNavDrawerItem() {
        return NavigationModel.NavigationItemEnum.FAVORITES;
    }

    @Override
    public void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        super.onNavDrawerStateChanged(isOpen, isAnimating);
    }

    @Override
    public void onNavDrawerSlide(float offset) {
        super.onNavDrawerSlide(offset);
    }


    private void setUpBottomNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationBehaviour<BottomNavigationView> behaviour = new BottomNavigationBehaviour<>(this);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        params.setBehavior(behaviour);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_movies);
    }

    private void setUpSearchBox() {
        FrameLayout searchBar = (FrameLayout) findViewById(R.id.search_bar);
        searchInput = (EditText) searchBar.findViewById(R.id.searchInput);
        searchInputTextLayout = (TextInputLayout) searchBar.findViewById(R.id.searchInputTextLayout);
        navDrawerButton = (ImageView) searchBar.findViewById(R.id.search_bar_left_action);
        backButton = (ImageView) searchBar.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOnBackButtonPress();
            }
        });
        clearBtn = (ImageView) searchBar.findViewById(R.id.clear_btn);
        searchInput.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        searchInput.setOnTouchListener((v, event) -> {
            searchInput.setFocusableInTouchMode(true);
            searchInput.requestFocus();
            animateOnSearchInput();
            return false;
        });

        searchInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager1 = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                attemptToSearch();
            }
            return false;
        });
        clearBtn.setVisibility(View.GONE);
        clearBtn.setOnClickListener(view -> {
            searchInput.setText("");
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearBtn.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void animateOnBackButtonPress() {

    }

    private void animateOnSearchInput() {

    }

    private void attemptToSearch() {
        searchInput.setError(null);
        final String query = searchInput.getText().toString();
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(query)) {
            searchInput.setError(EMPTY_QUERY);
            focusView = searchInput;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            presenter.searchByKeyword(query);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(SEARCH_QUERY, searchQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragment != null) {
            presenter = new FavoritesPresenter(fragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

}
