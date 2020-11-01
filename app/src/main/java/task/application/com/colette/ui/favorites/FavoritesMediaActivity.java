package task.application.com.colette.ui.favorites;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;

import task.application.com.colette.R;
import task.application.com.colette.navigation.NavigationModel;
import task.application.com.colette.ui.appsearch.AppSearchActivity;
import task.application.com.colette.ui.base.BaseActivity;
import task.application.com.colette.ui.utility.widgets.BottomNavigationBehaviour;
import task.application.com.colette.util.ActivityUtils;
import task.application.com.colette.util.Util;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            this.getWindow().setAttributes(attributes);
        }
        setContentView(R.layout.activity_favorites_media);
        setUpBottomNavigationView();
        findViewById(R.id.nav_button).setOnClickListener(view -> {
            new Handler().postDelayed(this::openNavDrawer, 150);
        });
        findViewById(R.id.search).setOnClickListener(view -> {
            Intent intent = new Intent(FavoritesMediaActivity.this, AppSearchActivity.class);
            ActivityUtils.startNewActivity(FavoritesMediaActivity.this, intent);
        });
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setElevation(0);
        appBarLayout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
