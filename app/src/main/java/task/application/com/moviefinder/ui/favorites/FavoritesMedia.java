package task.application.com.moviefinder.ui.favorites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.search.SearchActivity;
import task.application.com.moviefinder.util.Util;

public class FavoritesMedia extends AppCompatActivity implements FavoritesMediaFragment.OnFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(FavoritesMedia.this, SearchActivity.class));
                finish();
                return true;
            case R.id.navigation_tv:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("favorite_media_tv");
                if (fragment == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("FILTER", "Tv");
                    fragment = FavoritesMediaFragment.newInstance(bundle);
                    Util.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content, "favorite_media_tv");
                }
                return true;
            case R.id.navigation_movies:
                Fragment mvFragment = getSupportFragmentManager().findFragmentByTag("favorite_media_movies");
                if (mvFragment == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("FILTER", "Movies");
                    mvFragment = FavoritesMediaFragment.newInstance(bundle);
                    Util.addFragmentToActivity(getSupportFragmentManager(), mvFragment, R.id.content, "favorite_media_movies");
                }
                return true;
        }
        return false;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_media);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
