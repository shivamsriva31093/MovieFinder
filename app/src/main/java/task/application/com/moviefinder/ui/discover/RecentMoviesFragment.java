package task.application.com.moviefinder.ui.discover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.EndlessScrollListener;
import task.application.com.moviefinder.util.GridLayoutItemDecoration;
import task.application.com.moviefinder.util.Util;
import task.application.com.moviefinder.util.ViewType;

public class RecentMoviesFragment extends Fragment implements DiscoverContract.View {

    private static final String SAVED_LIST = "saved_list";
    public static final String QUERY_TYPE = "queryType";

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private DiscoverContract.Presenter presenter;
    private ProgressBar progressBar;
    private List<? extends MediaBasic> savedList = Collections.EMPTY_LIST;
    private DiscoverActivity.QueryType queryType;
    private int totalPages;
    private int totalResults;

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
            queryType = (DiscoverActivity.QueryType) getArguments().getSerializable(QUERY_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_movies, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            savedList = savedInstanceState.getParcelableArrayList(SAVED_LIST);
            queryType = (DiscoverActivity.QueryType) savedInstanceState.getSerializable(QUERY_TYPE);
            totalResults = savedInstanceState.getInt("totalItems");
            totalPages = savedInstanceState.getInt("totalPages");
        }
    }

    private void initViews(View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        setUpRecView(rootView);
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
        recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager, 4) {


            @Override
            public boolean isLastPage(int page) {
                return page == getTotalPages();
            }

            @Override
            public int getTotalPages() {
                return totalPages;
            }

            @Override
            public void loadMore(int page) {
                loadNextPageFromApi(page);
            }
        });
    }

    private void loadNextPageFromApi(int page) {
        rvAdapter.addFooter();
        presenter.loadNextPage(page);
    }

    @Override
    public void updateNewItems(List<? extends MediaBasic> data) {
        rvAdapter.addDataItems(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        rvAdapter = new RVAdapter(savedList);
        recyclerView.setAdapter(rvAdapter);
        if (presenter != null) {
            presenter.setQueryType(queryType);
            presenter.makeQuery("en", 1, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_LIST, (ArrayList<? extends Parcelable>) rvAdapter.getCurrentData());
        outState.putSerializable(QUERY_TYPE, queryType);
        outState.putInt("totalItems", totalResults);
        outState.putInt("totalPages", totalPages);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(DiscoverContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter, "DiscoverPresenter is null");
    }

    @Override
    public void showResultList(List<? extends MediaBasic> result, int totalPages, int totalResults) {
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        rvAdapter.updateData(result);
    }

    @Override
    public void showLoadingIndicator(boolean flag) {
        if (flag) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoResults() {
        Snackbar snackbar = Snackbar.make((CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout),
                "Error loading data. Please try again", Snackbar.LENGTH_LONG);
        snackbar.setAction("Retry", view -> {
            presenter.makeQuery("en", 1, null);
        });
        snackbar.show();
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<MediaBasic> data = new ArrayList<>();
        private boolean isLoaderRemoved = true;

        public RVAdapter(List<? extends MediaBasic> data) {
            this.data.addAll(data);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewType holderType = ViewType.values()[viewType];
            switch (holderType) {
                case TYPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.recentmovies_maincontent, parent, false),
                            holderType);
                case TYPE_FOOTER:
                    return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.searchlist_footer, parent, false),
                            holderType);
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == data.size())
                return ViewType.TYPE_FOOTER.ordinal();
            return ViewType.TYPE_ITEM.ordinal();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.HOLDER_ID) {
                case TYPE_ITEM:
                    showRowItems(holder, holder.getAdapterPosition());
                    break;
                case TYPE_FOOTER:
                    holder.footerProgressBar.setVisibility(isLoaderRemoved ? View.GONE : View.VISIBLE);
                    break;
            }
        }

        private void showRowItems(ViewHolder holder, int position) {
            if (data != null && !data.isEmpty()) {
                BasicMovieInfo row = (BasicMovieInfo) data.get(position);
                Picasso.with(getActivity()).load(
                        "https://image.tmdb.org/t/p/w500" + row.getPosterPath())
                        .error(R.drawable.trailer)
                        .placeholder(R.drawable.trailer)
                        .into(holder.poster);
                holder.title.setText(row.getTitle());
            }
        }

        @Override
        public int getItemCount() {
            return data.isEmpty() ? 0 : data.size() + 1;
        }

        public void updateData(List<? extends MediaBasic> list) {
            this.data.clear();
            this.data.addAll(list);
            notifyDataSetChanged();
        }

        public void addDataItems(List<? extends MediaBasic> newDataItems) {
            removeFooter();
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCB(data, newDataItems));
//            new Handler().post(() -> {
//                diffResult.dispatchUpdatesTo(this);
//                data.addAll(newDataItems);
//            });
            int posStart = data.size();
            for (MediaBasic item : newDataItems) {
                data.add(item);
                notifyItemInserted(posStart++);
            }
        }

        public List<? extends MediaBasic> getCurrentData() {
            return this.data;
        }

        public void removeFooter() {
            isLoaderRemoved = true;
            new Handler().post(() -> notifyItemChanged(data.size()));
        }

        public void addFooter() {
            isLoaderRemoved = false;
            new Handler().post(() -> notifyItemChanged(data.size()));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView poster;
            private TextView title;
            private CircleImageView trailerButton;
            private ProgressBar footerProgressBar;

            private ViewType HOLDER_ID;

            public ViewHolder(View itemView, ViewType holderType) {
                super(itemView);
                switch (holderType) {
                    case TYPE_ITEM:
                        poster = (ImageView) itemView.findViewById(R.id.poster);
                        title = (TextView) itemView.findViewById(R.id.title);
                        trailerButton = (CircleImageView) itemView.findViewById(R.id.trailer_button);
                        HOLDER_ID = holderType;
                        break;
                    case TYPE_FOOTER:
                        footerProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
                        HOLDER_ID = holderType;
                        break;

                }
            }
        }
    }

}
