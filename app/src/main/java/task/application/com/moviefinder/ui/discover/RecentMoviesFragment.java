package task.application.com.moviefinder.ui.discover;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.GridLayoutItemDecoration;

public class RecentMoviesFragment extends Fragment {


    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private List<MovieDb> mediaData = Collections.EMPTY_LIST;

    public RecentMoviesFragment() {
        // Required empty public constructor
    }

    public static RecentMoviesFragment newInstance(Bundle args) {
        RecentMoviesFragment fragment = new RecentMoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
        }
        new AsyncTask<Object, Object, List<MovieDb>>() {
            @Override
            protected List<MovieDb> doInBackground(Object... voids) {
                info.movito.themoviedbapi.TmdbApi api = new TmdbApi(ApplicationClass.API_KEY);
                info.movito.themoviedbapi.TmdbMovies movies = api.getMovies();
                return movies.getNowPlayingMovies("en", 1).getResults();
            }

            @Override
            protected void onPostExecute(List<MovieDb> movieDbs) {
                if (movieDbs != null) {
                    mediaData = movieDbs;
                    rvAdapter.updateData(mediaData);
                }
                Log.d("test", "called");
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_movies, container, false);
        setUpRecView(rootView);
        return rootView;
    }

    private void setUpRecView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.media_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridLayoutItemDecoration(2, 1, true));
        recyclerView.setNestedScrollingEnabled(false);
        rvAdapter = new RVAdapter(mediaData);
        recyclerView.setAdapter(rvAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<MovieDb> data;

        public RVAdapter(List<MovieDb> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.recentmovies_maincontent, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (data != null && !data.isEmpty()) {
                Picasso.with(getActivity()).load(
                        "https://image.tmdb.org/t/p/w500" + data.get(position).getPosterPath())
                        .into(holder.poster);
                holder.title.setText(data.get(position).getTitle());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void updateData(List<MovieDb> list) {
            this.data = list;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView poster;
            private TextView title;
            private CircleImageView trailerButton;

            public ViewHolder(View itemView) {
                super(itemView);
                poster = (ImageView) itemView.findViewById(R.id.poster);
                title = (TextView) itemView.findViewById(R.id.title);
                trailerButton = (CircleImageView) itemView.findViewById(R.id.trailer_button);
            }
        }
    }

}
