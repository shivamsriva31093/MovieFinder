package task.application.com.moviefinder.ui.itemdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

public class SearchItemDetailActivity extends AppCompatActivity {

    private static final String SEARCH_ITEM = "searchItem";
    private static final String SEARCH_ITEM_DETAIL = "detail_frag";

    private SearchItemDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = new Bundle();
        if (getIntent().hasExtra(SEARCH_ITEM)) {
            bundle = getIntent().getBundleExtra(SEARCH_ITEM);
        }
        SearchItemDetailFragment fragment = (SearchItemDetailFragment)
                getSupportFragmentManager().findFragmentByTag(SEARCH_ITEM_DETAIL);
        if (fragment == null) {
            fragment = SearchItemDetailFragment.newInstance(this, bundle);
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.detail_parent, SEARCH_ITEM_DETAIL);
        }

        this.presenter = new SearchItemDetailPresenter(fragment);
    }

}
