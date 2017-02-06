package task.application.com.moviefinder.ui.searchlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import task.application.com.moviefinder.R;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListFragment extends Fragment {

    public static SearchListFragment newInstance() {
        return new SearchListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchlist, container, false);
        return rootView;
    }
}
