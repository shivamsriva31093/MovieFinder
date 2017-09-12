package task.application.com.moviefinder.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;
import task.application.com.moviefinder.ui.base.PresenterCache;
import task.application.com.moviefinder.ui.base.PresenterFactory;
import task.application.com.moviefinder.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.moviefinder.ui.utility.GeneralTextView;
import task.application.com.moviefinder.util.EndlessScrollListener;
import task.application.com.moviefinder.util.GridLayoutItemDecoration;
import task.application.com.moviefinder.util.Util;
import task.application.com.moviefinder.util.ViewType;

import static task.application.com.moviefinder.util.ViewType.TYPE_FOOTER;
import static task.application.com.moviefinder.util.ViewType.TYPE_HEADER;
import static task.application.com.moviefinder.util.ViewType.TYPE_ITEM;

public class RecentMoviesFragment extends Fragment implements DiscoverContract.View {

    private static final String SAVED_LIST = "saved_list";
    public static final String QUERY_TYPE = "queryType";
    private static final int SPAN_COUNT = 2;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private DiscoverContract.Presenter presenter;
    private AVLoadingIndicatorView progressBar;
    private List<? extends MediaBasic> savedList = Collections.EMPTY_LIST;
    private DiscoverActivity.QueryType queryType;
    private int totalPages;
    private int totalResults;
    private EndlessScrollListener rvScrollListener;
    private RecyclerView.LayoutManager layoutManager;
    private PresenterCache<DiscoverPresenter> presenterCache =
            PresenterCache.getInstance();
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
    }

    ItemTouchListener itemTouchListener = new ItemTouchListener() {
        @Override
        public void onItemClick(View view, int position, MediaBasic item) {
            Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("clickedItem", item);
            bundle.putSerializable("filtering_type", MediaType.MOVIES);
            intent.putExtra("searchItem", bundle);
            startActivity(intent);
        }

        @Override
        public void onFavoriteButtonClick(View view, int position, MediaBasic data) {
            ImageView button = (ImageView) view;
            toggleFavorite(button, position, button.getTag(), ApplicationClass.getInstance(), data);
        }
    };

    private void toggleFavorite(ImageView button, int position, Object tag, Context context, MediaBasic item) {
        if (tag != null && (int) tag == R.drawable.ic_favorite_border_black_24dp) {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.favorite, null));
            button.setTag(R.drawable.favorite);
            presenter.addMediaToFavorites(item);
        } else {
            button.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp, null));
            button.setTag(R.drawable.ic_favorite_border_black_24dp);
            presenter.removeMediaFromFavorites(item);
        }
    }

    @Override
    public void showTestToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            savedList = savedInstanceState.getParcelableArrayList(SAVED_LIST);
            queryType = (DiscoverActivity.QueryType) savedInstanceState.getSerializable(QUERY_TYPE);
            TAG = savedInstanceState.getString("tag");
            totalResults = savedInstanceState.getInt("totalItems");
            totalPages = savedInstanceState.getInt("totalPages");
            presenter = presenterCache.getPresenter(TAG, factory);
        }
        presenter.setQueryType(queryType);
        presenter.makeQuery("en", 1, null);
    }

    private void initViews(View rootView, Bundle savedInstanceState) {
        progressBar = (AVLoadingIndicatorView) rootView.findViewById(R.id.ratingProgressBar);
        progressBar.setVisibility(View.GONE);
        setUpRecView(rootView, savedInstanceState);
    }

    private void setUpRecView(View rootView, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.media_list);
        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                ViewType holderType = ViewType.values()[rvAdapter.getItemViewType(position)];
                switch (holderType) {
                    case TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        recyclerView.setItemAnimator(animator);

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
    public void setImdbRatings(String rating, int pos) {
        rvAdapter.setRating(new String[]{rating}, pos);
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
        private SparseBooleanArray imdbRating = new SparseBooleanArray();
        private SparseBooleanArray posArray = new SparseBooleanArray();

        public RVAdapter(List<? extends MediaBasic> data, ItemTouchListener listener) {
            this.data.addAll(data);
            this.itemTouchListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewType holderType = ViewType.values()[viewType];
            switch (holderType) {
                case TYPE_HEADER:
                    return new ViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.fragment_recentmovies_header
                                    , parent, false), holderType);
                case TYPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.recentmovies_maincontent, parent, false),
                            holderType);
                case TYPE_FOOTER:
                    return new ViewHolder(LayoutInflater.from(getActivity())
                            .inflate(R.layout.searchlist_footer, parent, false),
                            holderType);
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER.ordinal();
            else if (position == data.size() + 1)
                return TYPE_FOOTER.ordinal();
            return TYPE_ITEM.ordinal();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.HOLDER_ID) {
                case TYPE_HEADER:
                    holder.headerTitle.setText(getRelevantText());
                    break;
                case TYPE_ITEM:
                    showRowItems(holder, holder.getAdapterPosition());
                    break;
                case TYPE_FOOTER:
                    holder.footerProgressBar.setVisibility(isLoaderRemoved ? View.GONE : View.VISIBLE);
                    break;
            }
        }

        private void showRowItems(ViewHolder holder, int position) {
            if (!imdbRating.get(position, false)) {
                holder.ratingProgressBar.setVisibility(View.VISIBLE);
                imdbRating.put(holder.getAdapterPosition(), true);
                posArray.put(holder.getAdapterPosition(), false);
                loadMediaData(holder, holder.getAdapterPosition());
                presenter.getRatings(MediaType.MOVIES, data.get(position - 1), holder.getAdapterPosition() - 1);

            } else {
                if (posArray.get(position)) {
                    loadMediaData(holder, position);
                    updateRowItems(holder, position);
                }
            }
        }

        private void updateRowItems(ViewHolder holder, int position) {
            holder.ratingProgressBar.setVisibility(View.GONE);
            holder.imdbRating.setText(data.get(position - 1).getImdbRating());
            holder.imdbRating.setVisibility(View.VISIBLE);
        }

        private void loadMediaData(ViewHolder holder, int position) {
            if (data != null && !data.isEmpty()) {
                BasicMovieInfo row = (BasicMovieInfo) data.get(position - 1);
                Picasso.with(getActivity()).load(
                        "https://image.tmdb.org/t/p/w500" + row.getPosterPath())
                        .error(R.drawable.imgfound)
                        .placeholder(R.color.colorPrimary)
                        .into(holder.poster);
                holder.title.setText(row.getTitle());
                holder.imdbRating.setVisibility(View.GONE);
                checkFavMediaInDB(holder, position);
            }
        }

        private void checkFavMediaInDB(ViewHolder holder, int position) {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<MediaItem> res = realm.where(MediaItem.class)
                    .equalTo("tmdbId", String.valueOf(data.get(position - 1).getId()))
                    .findAll();
            if (!res.isEmpty() && res.isValid())
                setFavorite(holder, true, position);
        }

        private void setFavorite(ViewHolder holder, boolean status, int position) {
            if (status) {
                holder.favorite.setImageDrawable(ApplicationClass.getInstance()
                        .getResources().getDrawable(R.drawable.favorite));
                holder.favorite.setTag(R.drawable.favorite);
            }
        }

        @Override
        public int getItemCount() {
            return data.isEmpty() ? 0 : data.size() + 2;
        }

        public void setRating(final String[] rating, int pos) {
            rating[0] = rating[0] == null ? "N/A" : rating[0];
            MediaBasic item = data.get(pos);
            if(rating[0].contentEquals("N/A")){
                rating[0] = "Unrated";
            }
            else{
                rating[0] = "IMDb "+rating[0];
            }
            item.setImdbRating(rating[0]);
            posArray.put(pos + 1, true);
            getActivity().runOnUiThread(() -> notifyItemChanged(pos + 1, rating[0]));
        }

        public void updateData(List<? extends MediaBasic> list) {
            this.data.clear();
            this.data.addAll(list);
            notifyDataSetChanged();
        }

        public void addDataItems(List<? extends MediaBasic> newDataItems) {
            removeFooter();
            int posStart = data.size() + 1;
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
            new Handler().post(() -> notifyItemChanged(data.size() + 1));
        }

        public void addFooter() {
            isLoaderRemoved = false;
            new Handler().post(() -> notifyItemChanged(data.size() + 1));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView poster;
            private GeneralTextView title;
            private CircleImageView trailerButton;
            private ProgressBar footerProgressBar;
            private GeneralTextView headerTitle;
            private ImageView favorite;
            private FrameLayout favoriteParent;
            private GeneralTextView imdbRating;
            private AVLoadingIndicatorView ratingProgressBar;

            private ViewType HOLDER_ID;

            public ViewHolder(View itemView, ViewType holderType) {
                super(itemView);
                switch (holderType) {
                    case TYPE_HEADER:
                        headerTitle = (GeneralTextView) itemView.findViewById(R.id.title_view);
                        HOLDER_ID = holderType;
                        break;
                    case TYPE_ITEM:
                        poster = (ImageView) itemView.findViewById(R.id.poster);
                        poster.setClickable(true);
                        title = (GeneralTextView) itemView.findViewById(R.id.title);
                        trailerButton = (CircleImageView) itemView.findViewById(R.id.trailer_button);
                        imdbRating = (GeneralTextView) itemView.findViewById(R.id.imdb_rating);
                        ratingProgressBar = (AVLoadingIndicatorView) itemView.findViewById(R.id.ratingProgressBar);
                        favorite = (ImageView) itemView.findViewById(R.id.favorite);
                        favoriteParent = (FrameLayout) itemView.findViewById(R.id.favorite_parent);
                        poster.setOnClickListener(view ->
                                itemTouchListener.onItemClick(view, getAdapterPosition() - 1, data.get(getAdapterPosition() - 1)));
                        itemView.setOnClickListener(view ->
                                itemTouchListener.onItemClick(view, getAdapterPosition() - 1, data.get(getAdapterPosition() - 1)));
                        favoriteParent.setOnClickListener(view -> {
                            MediaBasic item = data.get(getAdapterPosition() - 1);
                            item.setImdbRating(imdbRating.getText().toString());
                            itemTouchListener.onFavoriteButtonClick(favorite, getAdapterPosition() - 1, item);
                        });
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

    private CharSequence getRelevantText() {
        switch (queryType) {
            case NOW_PLAYING:
                return queryType.getQuery() + " in Theatres";
            case POPULAR:
                return queryType.getQuery() + " among Audience";
            case TOP_RATED:
                return queryType.getQuery() + " by Movie goers";
            case UPCOMING:
                return queryType.getQuery() + " next week";
            default:
                return "";
        }
    }

    public interface ItemTouchListener {
        void onItemClick(View view, int position, MediaBasic item);

        void onFavoriteButtonClick(View view, int position, MediaBasic data);
    }

    private PresenterFactory<DiscoverPresenter> factory = () -> new DiscoverPresenter(RecentMoviesFragment.this, TAG);
}
