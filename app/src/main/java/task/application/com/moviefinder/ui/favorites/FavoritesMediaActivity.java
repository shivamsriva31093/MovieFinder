package task.application.com.moviefinder.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.navigation.NavigationModel;
import task.application.com.moviefinder.ui.appsearch.AppSearchActivity;
import task.application.com.moviefinder.ui.base.BaseActivity;
import task.application.com.moviefinder.ui.utility.widgets.BottomNavigationBehaviour;
import task.application.com.moviefinder.util.ActivityUtils;
import task.application.com.moviefinder.util.Util;

public class FavoritesMediaActivity extends BaseActivity implements
        FavoritesMediaFragment.OnFragmentInteractionListener {


    private FavoritesMediaContract.Presenter presenter;
    private FavoritesMediaFragment fragment = null;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_media);
        setUpBottomNavigationView();
        findViewById(R.id.nav_button).setOnClickListener(view -> {
            new Handler().postDelayed(this::openNavDrawer, 150);
        });
        findViewById(R.id.search).setOnClickListener(view -> {
            Intent intent = new Intent(FavoritesMediaActivity.this, AppSearchActivity.class);
            ActivityUtils.startNewActivity(FavoritesMediaActivity.this, intent);
        });
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

}
