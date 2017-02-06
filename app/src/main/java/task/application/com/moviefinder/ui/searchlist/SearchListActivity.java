package task.application.com.moviefinder.ui.searchlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListActivity extends AppCompatActivity {

    private static final String TAG = SearchListActivity.class.getName();
    private static final String LIST_FRAG_TAG = "searchlist_frag";
    private RelativeLayout fragment_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Fragment fragment = (SearchListFragment)
                getSupportFragmentManager().findFragmentByTag(LIST_FRAG_TAG);
        if(fragment == null) {
            fragment = SearchListFragment.newInstance();
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.activity_search_list, LIST_FRAG_TAG);
        }

    }
}
