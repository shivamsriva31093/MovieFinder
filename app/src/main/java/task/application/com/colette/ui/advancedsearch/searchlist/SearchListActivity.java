package task.application.com.colette.ui.advancedsearch.searchlist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;

import task.application.com.colette.R;
import task.application.com.colette.navigation.NavigationModel;
import task.application.com.colette.ui.base.BaseActivity;
import task.application.com.colette.util.Util;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListActivity extends BaseActivity implements
        SearchListFragment.OnReplaceFragmentListener {

    private static final String TAG = SearchListActivity.class.getName();
    private static final String LIST_FRAG_TAG = "searchlist_frag";
    private static final String SEARCH_LIST = "searchList";
    private static final String FILTERING_TYPE = "filtering_type";
    private static final String EMPTY_QUERY = "emptyQuery";
    private static final String SEARCH_QUERY = "query";
    private static final String BUNDLE = "bundle";
    private static final String SEARCH = "Search";

    private static int SEARCH_FRAG_COUNT = 0;

    private SearchListContract.Presenter presenter;
    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private AppCompatImageButton menuButton;
    private ImageButton searchAction;
    private RelativeLayout fragmentContainer;
    private Bundle intentBundle;

    private CharSequence searchQuery;
    private SearchListFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        if (getIntent().hasExtra(BUNDLE)) {
            intentBundle = getIntent().getBundleExtra(BUNDLE);
        }
        if (savedInstanceState == null) {
            fragment = SearchListFragment.newInstance();
            fragment.setArguments(intentBundle);
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.activity_search_list, LIST_FRAG_TAG);
        } else {
            if (savedInstanceState.containsKey(SEARCH_QUERY))
                searchQuery = savedInstanceState.getCharSequence(SEARCH_QUERY);
            fragment = (SearchListFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, LIST_FRAG_TAG);
        }
        presenter = new SearchListPresenter(fragment);
        fragmentContainer = (RelativeLayout) findViewById(R.id.activity_search_list);
        setUpSearchBox();
    }

    private void setUpSearchBox() {
        searchBox = (TextInputLayout) findViewById(R.id.searchbox);
        searchTerm = (EditText) findViewById(R.id.searchterm);
        searchTerm.setText(searchQuery);
        moreOptions = (Spinner) findViewById(R.id.optionsMenu);
        menuButton = (AppCompatImageButton) findViewById(R.id.menu_button);
        searchAction = (ImageButton) findViewById(R.id.search_action);
        searchTerm.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchTerm.getWindowToken(), 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchbox_extra_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreOptions.setAdapter(adapter);
        moreOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTerm.setHint(SEARCH+" " + parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
                Log.d("debug", "Nothing selected");
                presenter.setFilteringType(MediaType.MOVIES);
            }
        });

        searchTerm.setOnTouchListener((v, event) -> {
            searchTerm.setFocusableInTouchMode(true);
            searchTerm.requestFocus();
            return false;
        });

        searchTerm.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                InputMethodManager inputMethodManager1 = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager1.hideSoftInputFromWindow(v.getWindowToken(), 0);
                attemptToSearch();
            }
            return false;
        });

        searchAction.setOnClickListener(v -> {
            searchTerm.setFocusableInTouchMode(true);
            attemptToSearch();
        });

        menuButton.setOnClickListener(v -> openNavDrawer());
    }

    @Override
    protected NavigationModel.NavigationItemEnum getSelfNavDrawerItem() {
        return NavigationModel.NavigationItemEnum.SEARCHLIST;
    }

    @Override
    public void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating) {
        super.onNavDrawerStateChanged(isOpen, isAnimating);
    }

    @Override
    public void onNavDrawerSlide(float offset) {
        super.onNavDrawerSlide(offset);
    }

    private void attemptToSearch() {
        searchTerm.setError(null);
        final String query = searchTerm.getText().toString();
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(query)) {
            searchTerm.setError(EMPTY_QUERY);
            focusView = searchTerm;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (moreOptions.getSelectedItem().toString().equals("Movies"))
                presenter.setFilteringType(MediaType.MOVIES);
            else
                presenter.setFilteringType(MediaType.TV);
            presenter.setQuery(query);
            presenter.searchByKeyword(query);
            Log.d("debug", "selected item is " + moreOptions.getSelectedItem());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(SEARCH_QUERY, searchQuery);
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, LIST_FRAG_TAG, fragment);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.setFocusable(false);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if(count == 0)
            super.onBackPressed();
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void replaceFragment(ArrayList<? extends MediaBasic> movieDbs,
                                String searchQuery, MediaType filterType,
                                int totalResults, int totalPages) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchListFragment fragment =
                SearchListFragment.newInstance(movieDbs, searchQuery, filterType, totalResults, totalPages);
        presenter = new SearchListPresenter(fragment);
        transaction.replace(fragmentContainer.getId(), fragment);
        transaction.addToBackStack(SEARCH_FRAG_COUNT++ + "");
        transaction.commit();
    }
}
