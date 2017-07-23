package task.application.com.moviefinder.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.moviefinder.util.EndlessScrollListener;
import task.application.com.moviefinder.util.GridLayoutItemDecoration;
import task.application.com.moviefinder.util.Util;
import task.application.com.moviefinder.util.ViewType;

import static task.application.com.moviefinder.util.ViewType.TYPE_FOOTER;
import static task.application.com.moviefinder.util.ViewType.TYPE_ITEM;

public class RecentMoviesFragment extends Fragment implements DiscoverContract.View {

    private static final String SAVED_LIST = "saved_list";
    public static final String QUERY_TYPE = "queryType";
    private static final int SPAN_COUNT = 2;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private DiscoverContract.Presenter presenter;
    private ProgressBar progressBar;
    private List<? extends MediaBasic> savedList = Collections.EMPTY_LIST;
    private DiscoverActivity.QueryType queryType;
    private int totalPages;
    private int totalResults;
    private EndlessScrollListener rvScrollListener;
    private RecyclerView.LayoutManager layoutManager;
    private DiscoverPresenterCache presenterCache = DiscoverPresenterCache.getInstance();
    private boolean isDestroyedBySystem;
    private String TAG = RecentMoviesFragment.class.getSimpleName();
    private LayoutManagerType mCurrentLayoutManagerType;

    public enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

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
            TAG += queryType;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recent_movies, container, false);
        initViews(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.setQueryType(queryType);
        presenter.makeQuery("en", 1, null);
    }

    ItemTouchListener itemTouchListener = (view, position, item) -> {
        Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
        Bundle bundle = new Bundle();
        MediaBasic media = new MediaBasic();
        media.setId(item.getId());
        bundle.putParcelable("clickedItem", media);
        bundle.putSerializable("filtering_type", MediaType.MOVIES);
        intent.putExtra("searchItem", bundle);
        startActivity(intent);
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            savedList = savedInstanceState.getParcelableArrayList(SAVED_LIST);
            queryType = (DiscoverActivity.QueryType) savedInstanceState.getSerializable(QUERY_TYPE);
            TAG = savedInstanceState.getString("tag");
            totalResults = savedInstanceState.getInt("totalItems");
            totalPages = savedInstanceState.getInt("totalPages");
            presenterCache.getPresenter(TAG, factory);
        }
    }

    private void initViews(View rootView, Bundle savedInstanceState) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        setUpRecView(rootView, savedInstanceState);
    }

    private void setUpRecView(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.media_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridLayoutItemDecoration(2, 1, true));
        rvScrollListener = new EndlessScrollListener(layoutManager, 2) {
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
                Log.d("test", "cur page is " + page);
                loadNextPageFromApi(page);
            }
        };
        recyclerView.addOnScrollListener(rvScrollListener);
        rvAdapter = new RVAdapter(new ArrayList<>(), itemTouchListener);
        recyclerView.setAdapter(rvAdapter);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT) {
                    @Override
                    public boolean supportsPredictiveItemAnimations() {
                        return true;
                    }
                };
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                layoutManager = new LinearLayoutManager(getActivity()) {
                    @Override
                    public boolean supportsPredictiveItemAnimations() {
                        return true;
                    }
                };
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                layoutManager = new LinearLayoutManager(getActivity()) {
                    @Override
                    public boolean supportsPredictiveItemAnimations() {
                        return true;
                    }
                };
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }

    private void loadNextPageFromApi(int page) {
        rvAdapter.addFooter();
        presenter.loadNextPage(page);
    }

    @Override
    public void updateNewItems(List<? extends MediaBasic> data) {
        rvAdapter.addDataItems(data);
        int curPage = rvScrollListener.getCurPage();
        rvScrollListener.setCurPage(curPage + 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyedBySystem = false;
//        rvScrollListener.setPrevTotalCount(0);
//        rvScrollListener.setCurPage(2);
//        presenter.setQueryType(queryType);
//        presenter.makeQuery("en", 1, null);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isDestroyedBySystem) {
            presenterCache.removePresenter(TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        outState.putParcelableArrayList(SAVED_LIST, (ArrayList<? extends Parcelable>) rvAdapter.getCurrentData());
        outState.putSerializable(QUERY_TYPE, queryType);
        outState.putInt("totalItems", totalResults);
        outState.putInt("totalPages", totalPages);
        outState.putString("tag", TAG);
        outState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
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

    @Override
    public void setEndlessScrollLoading(boolean status) {
        rvScrollListener.setLoading(status);
        rvAdapter.removeFooter();
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<MediaBasic> data = new ArrayList<>();
        private boolean isLoaderRemoved = true;
        private ItemTouchListener itemTouchListener;

        public RVAdapter(List<? extends MediaBasic> data, ItemTouchListener listener) {
            this.data.addAll(data);
            this.itemTouchListener = listener;
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
                return TYPE_FOOTER.ordinal();
            return TYPE_ITEM.ordinal();
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
                        itemView.setOnClickListener(view ->
                                itemTouchListener.onItemClick(view, getAdapterPosition(), data.get(getAdapterPosition())));
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

    public interface ItemTouchListener {
        void onItemClick(View view, int position, MediaBasic item);
    }

    private DiscoverPresenterFactory factory = new DiscoverPresenterFactory() {
        @NonNull
        @Override
        public DiscoverPresenter createPresenter() {
            return new DiscoverPresenter(RecentMoviesFragment.this, TAG);
        }
    };


}
