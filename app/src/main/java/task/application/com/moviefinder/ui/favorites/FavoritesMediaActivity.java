package task.application.com.moviefinder.ui.favorites;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.TextUtils;
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
    private static final long MENU_ICON_ANIM_DURATION = 250;

    private enum LeftActionState {
        SHOW_NAV_DRAWER,
        SHOW_BACK_BUTTON
    }

    private LeftActionState leftActionState = LeftActionState.SHOW_NAV_DRAWER;
    private DrawerArrowDrawable menuDrawable;
    private Drawable backArrow;

    private CharSequence searchQuery;
    private FavoritesMediaContract.Presenter presenter;
    FavoritesMediaFragment fragment = null;
    private EditText searchInput;

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
    private ImageView searchBarLeftAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_media);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCH_QUERY))
                searchQuery = savedInstanceState.getCharSequence(SEARCH_QUERY);
        }
        menuDrawable = new DrawerArrowDrawable(this);
        backArrow = getDrawable(R.drawable.ic_arrow_back_black_24dp);
        leftActionState = LeftActionState.SHOW_NAV_DRAWER;
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

    @Override
    public void lockOrUnlockNavDrawer(boolean status) {
        getDrawer().setDrawerLockMode(status ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED :
                DrawerLayout.LOCK_MODE_UNLOCKED);
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
        searchBarLeftAction = (ImageView) searchBar.findViewById(R.id.search_bar_left_action);
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

        searchInput.setOnTouchListener((v, event) -> {
            searchInput.setFocusableInTouchMode(true);
            searchInput.requestFocus();
            transitionInLeftSection(true);
            return false;
        });

        searchInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                setInputMethodVisibile(false);
                attemptToSearch();
            }
            return false;
        });

        setUpLeftActionContainer();

    }

    private void setUpLeftActionContainer() {
        searchBarLeftAction.setImageDrawable(menuDrawable);
        searchBarLeftAction.setOnClickListener(view -> {
            if (searchInput.isFocused()) {
                clearSearch();
            } else {
                switch (leftActionState) {
                    case SHOW_NAV_DRAWER:

                        toggleNavDrawer();
                        break;
                }
            }
        });
    }

    private void clearSearch() {
        transitionOutLeftSection(true);
        searchInput.setText("");
        setInputMethodVisibile(false);
    }

    private void transitionOutLeftSection(boolean withAnim) {
        switch (leftActionState) {
            case SHOW_NAV_DRAWER:
                closeMenuDrawable(menuDrawable, withAnim);
        }
    }

    private void transitionInLeftSection(boolean withAnim) {
        switch (leftActionState) {
            case SHOW_NAV_DRAWER:
                openMenuDrawable(menuDrawable, withAnim);
        }
    }

    private void closeMenuDrawable(DrawerArrowDrawable menuDrawable, boolean withAnim) {
        if (withAnim) {
            ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
            animator.addUpdateListener(valueAnimator -> {
                float val = (Float) valueAnimator.getAnimatedValue();
                menuDrawable.setProgress(val);
            });
            animator.setDuration(MENU_ICON_ANIM_DURATION);
            animator.start();
        } else {
            menuDrawable.setProgress(0.0f);
        }
    }

    private void openMenuDrawable(DrawerArrowDrawable menuDrawable, boolean withAnim) {
        if (withAnim) {
            ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
            animator.addUpdateListener(valueAnimator -> {
                float val = (Float) valueAnimator.getAnimatedValue();
                menuDrawable.setProgress(val);
            });
            animator.setDuration(MENU_ICON_ANIM_DURATION);
            animator.start();
        } else {
            menuDrawable.setProgress(1.0f);
        }
    }

    private void toggleNavDrawer() {
        openNavDrawer();
    }

    private void animateOnBackButtonPress() {

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

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.setFocusable(false);
                    setInputMethodVisibile(false);
                    transitionInLeftSection(true);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
