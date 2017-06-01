package task.application.com.moviefinder.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.search.SearchActivity;

public class FavoritesMedia extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                startActivity(new Intent(FavoritesMedia.this, SearchActivity.class));
                finish();
                return true;
            case R.id.navigation_tv:

                return true;
            case R.id.navigation_movies:

                return true;
        }
        return false;
    };

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_media);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        realm = Realm.getDefaultInstance();
    }

}
