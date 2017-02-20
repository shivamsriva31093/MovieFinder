package task.application.com.moviefinder.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.searchlist.SearchListActivity;
import task.application.com.moviefinder.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment implements SearchContract.View{

    private SearchContract.Presenter presenter;
    private View noResultsView;

    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private ImageButton searchAction;

    private LinearLayout logoContainer;
    private ImageView logo;
    private RotateLoading progressView;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        logo = (ImageView) rootView.findViewById(R.id.logo);
        logoContainer = (LinearLayout) rootView.findViewById(R.id.logo_container);
        progressView = (RotateLoading) rootView.findViewById(R.id.progressView);
        setUpSearchBox(rootView);
        return rootView;
    }

    private void setUpSearchBox(View rootView) {
        searchBox = (TextInputLayout) rootView.findViewById(R.id.searchbox);
        searchTerm = (EditText) rootView.findViewById(R.id.searchterm);
        moreOptions = (Spinner) rootView.findViewById(R.id.optionsMenu);
        searchAction = (ImageButton) rootView.findViewById(R.id.search_action);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.searchbox_extra_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreOptions.setAdapter(adapter);
        moreOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTerm.setHint("Search "+parent.getSelectedItem().toString());
                presenter.setFilteringType(parent.getSelectedItem().toString());
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
                if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
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
        final String queryType = presenter.getFilteringType();
        View focusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(query)) {
            searchTerm.setError("Empty Query");
            focusView = searchTerm;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            presenter.searchByKeyword(query);
            Toast.makeText(getActivity(), "Searching for "+query, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showSearchListUi(ArrayList<MovieDb> movieDbs) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("searchList", movieDbs);
        Intent intent = new Intent(getActivity(), SearchListActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void showLoadingResultsError() {

    }

    @Override
    public void showLoadingIndicator(boolean show) {
        if(show) {
            logo.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            progressView.start();
        } else {
            progressView.setVisibility(View.GONE);
            progressView.stop();
            logo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoResults() {

    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter);
    }

}
