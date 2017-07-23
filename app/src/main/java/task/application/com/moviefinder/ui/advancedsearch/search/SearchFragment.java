package task.application.com.moviefinder.ui.advancedsearch.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.advancedsearch.searchlist.SearchListActivity;
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
    private AppCompatButton searchAction;
    private LinearLayout logoContainer;
    private ImageView logo;
    private RotateLoading progressView;
    private CoordinatorLayout parentActivityLayout;
    private String searchQuery = "";

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivityLayout = (CoordinatorLayout) getActivity().findViewById(R.id.search_activity_coord);
    }

    private void setUpSearchBox(View rootView) {
        searchBox = (TextInputLayout) rootView.findViewById(R.id.searchbox);
        searchTerm = (EditText) rootView.findViewById(R.id.searchterm);
        moreOptions = (Spinner) rootView.findViewById(R.id.optionsMenu);
        searchAction = (AppCompatButton) rootView.findViewById(R.id.search_action);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.searchbox_extra_options,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreOptions.setAdapter(adapter);
        moreOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //searchTerm.setHint("Search "+parent.getSelectedItem().toString());
                if (parent.getSelectedItemId() == 0)
                    presenter.setFilteringType(MediaType.MOVIES);
                else
                    presenter.setFilteringType(MediaType.TV);
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

        searchTerm.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                attemptToSearch();
            }
            return false;
        });

        searchAction.setOnClickListener(v -> attemptToSearch());


    }

    private void attemptToSearch() {
        searchTerm.setError(null);
        final String query = searchTerm.getText().toString();
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
            searchQuery = query;
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
    public void showSearchListUi(ArrayList<? extends MediaBasic> movieDbs, int totalResults, int totalPages) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchList", movieDbs);
        bundle.putSerializable("filtering_type", presenter.getFilteringType());
        bundle.putInt("totalPages", totalPages);
        bundle.putInt("totalItems", totalResults);
        bundle.putString("query", searchQuery);
        Intent intent = new Intent(getActivity(), SearchListActivity.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void showLoadingResultsError() {
        Snackbar snackbar = Snackbar.make(parentActivityLayout,
                "Error in connectivity. Please check your net connection and retry", Snackbar.LENGTH_LONG);
        snackbar.show();
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
