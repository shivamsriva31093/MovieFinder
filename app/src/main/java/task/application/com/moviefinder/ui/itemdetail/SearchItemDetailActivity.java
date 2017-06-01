package task.application.com.moviefinder.ui.itemdetail;

import android.os.Bundle;
import android.view.View;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.navdrawer.NavigationDrawerActivity;
import task.application.com.moviefinder.util.Util;

public class SearchItemDetailActivity extends NavigationDrawerActivity {

    private static final String SEARCH_ITEM = "searchItem";
    private static final String SEARCH_ITEM_DETAIL = "detail_frag";

    private SearchItemDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layoutView = getLayoutInflater().inflate(R.layout.activity_detail_new, null, false);
        setContentView(layoutView);
        Bundle bundle = new Bundle();
        if (getIntent().hasExtra(SEARCH_ITEM)) {
            bundle = getIntent().getBundleExtra(SEARCH_ITEM);
        }


        FragmentPrime fragment = (FragmentPrime)
                getSupportFragmentManager().findFragmentByTag(SEARCH_ITEM_DETAIL);
        if (fragment == null) {
            fragment = FragmentPrime.newInstance(bundle);
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.detail_parent, SEARCH_ITEM_DETAIL);
        }
        this.presenter = new SearchItemDetailPresenter(fragment);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }
}
