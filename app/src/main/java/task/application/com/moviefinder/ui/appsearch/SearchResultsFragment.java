package task.application.com.moviefinder.ui.appsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtmdbwrapper.enums.MediaType;

import task.application.com.moviefinder.R;


public class SearchResultsFragment extends Fragment {

    private static final String TAG = SearchResultsFragment.class.getSimpleName();
    public static final String QUERY_TYPE = "queryType";

    private String query;
    private MediaType queryType;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    public static SearchResultsFragment newInstance() {
        return new SearchResultsFragment();
    }

    public static SearchResultsFragment newInstance(Bundle args) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
            queryType = (MediaType) getArguments().getSerializable(QUERY_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        return rootView;
    }

}
