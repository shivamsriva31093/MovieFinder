package task.application.com.moviefinder.ui.searchlist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListActivity extends AppCompatActivity implements SearchListFragment.OnReplaceFragmentListener {

    private static final String TAG = SearchListActivity.class.getName();
    private static final String LIST_FRAG_TAG = "searchlist_frag";
    private static final String SEARCH_LIST = "searchList";
    private static final String EMPTY_QUERY = "emptyQuery";
    private static final String SEARCH_QUERY = "query";
    private static final String BUNDLE = "bundle";
    private static final String SEARCH = "Search";

    private static int SEARCH_FRAG_COUNT = 0;

    private SearchListContract.Presenter presenter;
    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private ImageButton searchAction;
    private RelativeLayout fragmentContainer;
    private Bundle intentBundle;

    private CharSequence searchQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(SEARCH_QUERY))
                searchQuery = savedInstanceState.getCharSequence(SEARCH_QUERY);
        } else {
            if(getIntent().hasExtra(BUNDLE)) {
                intentBundle = getIntent().getBundleExtra(BUNDLE);
            }
        }

        SearchListFragment fragment = (SearchListFragment)
                getSupportFragmentManager().findFragmentByTag(LIST_FRAG_TAG);
        if(fragment == null) {
            fragment = SearchListFragment.newInstance();
            fragment.setArguments(intentBundle);
            Util.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.activity_search_list, LIST_FRAG_TAG);
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
            }
        });

        searchTerm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchTerm.setFocusableInTouchMode(true);
                searchTerm.requestFocus();
                return false;
            }
        });

        searchTerm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    attemptToSearch();
                }
                return false;
            }
        });

        searchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToSearch();
            }
        });


    }

    private void attemptToSearch() {
        searchTerm.setError(null);
        final String query = searchTerm.getText().toString();
        final String queryType = moreOptions.getSelectedItem().toString();
        Log.d("querytype", queryType);
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
            presenter.searchByKeyword(query, queryType);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(SEARCH_QUERY, searchQuery);
        super.onSaveInstanceState(outState);
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
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if(count == 0)
            super.onBackPressed();
        else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void replaceFragment(ArrayList<MovieDb> movieDbs) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SearchListFragment fragment = SearchListFragment.newInstance(movieDbs);
        presenter = new SearchListPresenter(fragment);
        transaction.replace(fragmentContainer.getId(), fragment);
        transaction.addToBackStack(SEARCH_FRAG_COUNT++ + "");
        transaction.commit();
    }
}