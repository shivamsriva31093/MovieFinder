package task.application.com.moviefinder.ui.favorites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.search.SearchActivity;
import task.application.com.moviefinder.util.Util;

public class FavoritesMediaActivity extends AppCompatActivity implements FavoritesMediaFragment.OnFragmentInteractionListener {

    private FavoritesMediaContract.Presenter presenter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Bundle bundle = new Bundle();
        FavoritesMediaFragment fragment = null;
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SearchActivity.class));
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
