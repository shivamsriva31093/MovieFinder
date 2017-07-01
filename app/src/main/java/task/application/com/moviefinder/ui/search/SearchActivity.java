package task.application.com.moviefinder.ui.search;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.navdrawer.NavigationDrawerActivity;
import task.application.com.moviefinder.util.Util;

public class SearchActivity extends NavigationDrawerActivity {

    private static final String SEARCH_HOME_TAG = "search_home";
    private SearchPresenter searchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layoutView = getLayoutInflater().inflate(R.layout.activity_search_1, null, false);
        setContentView(layoutView);
        setUpToolbar();
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.search_home);
        if(null == searchFragment) {
            searchFragment = SearchFragment.newInstance();
            Util.addFragmentToActivity(getSupportFragmentManager(), searchFragment,
                    R.id.fragment_container, SEARCH_HOME_TAG);
        }
        searchPresenter = new SearchPresenter(searchFragment);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    private void setUpToolbar() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if(view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
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
