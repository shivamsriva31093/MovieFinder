package task.application.com.colette.ui.discover;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.github.florent37.picassopalette.PicassoPalette;
import com.like.LikeButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import task.application.com.colette.R;
import task.application.com.colette.model.local.realm.datamodels.MediaItem;
import task.application.com.colette.ui.base.PresenterCache;
import task.application.com.colette.ui.base.PresenterFactory;
import task.application.com.colette.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.colette.ui.utility.widgets.GeneralTextView;
import task.application.com.colette.util.ActivityUtils;
import task.application.com.colette.util.EndlessScrollListener;
import task.application.com.colette.util.GridLayoutItemDecoration;
import task.application.com.colette.util.Util;
import task.application.com.colette.util.ViewType;

import static task.application.com.colette.util.ViewType.TYPE_FOOTER;
import static task.application.com.colette.util.ViewType.TYPE_HEADER;
import static task.application.com.colette.util.ViewType.TYPE_ITEM;

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
    private ArrayList<? extends MediaBasic> initData = new ArrayList<>();
    private DiscoverActivity.QueryType queryType;
    private int totalPages;
    private int totalResults;
    private EndlessScrollListener rvScrollListener;
    private RecyclerView.LayoutManager layoutManager;
    private PresenterCache<DiscoverPresenter> presenterCache = PresenterCache.getInstance();
    private boolean isDestroyedBySystem;
    private String TAG = RecentMoviesFragment.class.getSimpleName();
    private LayoutManagerType mCurrentLayoutManagerType;
    private OnFragmentInteractionListener onDataLoadListener;

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
            if (getArguments().getParcelableArrayList("data") != null)
                initData = getArguments().getParcelableArrayList("data");
            if (getArguments().getInt("totalPages") != 0)
                totalPages = getArguments().getInt("totalPages");
            totalResults = getArguments().getInt("totalResults");
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
            LikeButton likeButton = (LikeButton)view.findViewById(R.id.favorite);
            likeButton.setEnabled(true);
            toggleFavorite(likeButton, data);
        }
    };

    private void toggleFavorite(LikeButton button, MediaBasic item) {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<MediaItem> res = realm.where(MediaItem.class)
                .equalTo("tmdbId", String.valueOf(item.getId()))
                .findAll();
        if (!res.isEmpty() && res.isValid()) {
            button.setLiked(false);
            presenter.removeMediaFromFavorites(item);
        } else {
            Log.d("liking","true");
            button.setEnabled(true);
            button.setExplodingDotColorsRes(R.color.colorPrimary,R.color.colorPrimaryMid);


            button.setLiked(true);
            button.animate();

            presenter.addMediaToFavorites(item);
        }
    }

    @Override
    public void showTestToast(String msg) {
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        ActivityUtils.showBottomSheetMessage(msg, getActivity(), R.drawable.heart_outline, 1125, null);
    }

    @Override
    public void showNetworkError() {
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
//        builder.setMessage("The connection is lost/slow.").create().show();
        onDataLoadListener.onNoNetworkError(false);
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
        if (initData != null && !initData.isEmpty()) {
            rvAdapter.updateData(initData);
        } else {
            presenter.makeQuery("en", 1, null);
        }
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
                    case TYPE_FOOTER:
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
        try {
            onDataLoadListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            Log.e("error", "Activity DiscoverActivity must implement "
                    +OnFragmentInteractionListener.class.getSimpleName());
            e.printStackTrace();
        }
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
        onDataLoadListener.onDataLoad(true);
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
                    holder.headerTitle.setText(getRelevantHeaderTitleText());
                    holder.headerSubTitle.setText(getRelevantHeaderSubtitleText());
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
            /*if (!imdbRating.get(position, false)) {
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
            }*/
            loadMediaData(holder, position);
        }

        private void updateRowItems(ViewHolder holder, int position) {
            holder.ratingProgressBar.setVisibility(View.GONE);
            if (data.get(position - 1).getImdbRating().equals("IMDb -") ||
                    data.get(position - 1).getImdbRating().equals("Unrated")) {
                holder.ratingParent.setVisibility(View.GONE);
            } else {
                holder.imdbRating.setText(data.get(position - 1).getImdbRating());
                holder.imdbRating.setVisibility(View.VISIBLE);
            }
        }

        private void loadMediaData(ViewHolder holder, int position) {
            if (data != null && !data.isEmpty()) {
                BasicMovieInfo row = (BasicMovieInfo) data.get(position - 1);
                holder.title.setText(row.getTitle());
                holder.title.setTextColor(Color.WHITE);
                //holder.posterCard.setBackgroundResource(R.color.light_gray_inactive_icon);
                holder.imdbRating.setVisibility(View.GONE);
                if (checkFavMediaInDB(row)) {
                    holder.favorite.setLiked(true);
                } else {
                    holder.favorite.setLiked(false);
                }
//                Picasso.with(getActivity()).load(
//                        "https://image.tmdb.org/t/p/w500" + row.getPosterPath())
//                        .error(R.drawable.imgfound)
//                        .placeholder(R.color.light_gray_inactive_icon)
//                        .into(holder.poster);


                Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + row.getPosterPath())
                        .error(R.drawable.imgfound)
                        .into(holder.poster,
                        PicassoPalette.with("https://image.tmdb.org/t/p/w500" + row.getPosterPath(), holder.poster)
//                                .use(PicassoPalette.Profile.MUTED_DARK)
//                                .intoBackground(textView)
//                                .intoTextColor(textView)
                                .use(PicassoPalette.Profile.VIBRANT)
                                .intoBackground(holder.posterCard, PicassoPalette.Swatch.RGB)

                        );





                //holder.poster.setImageResource(R.color.light_gray_inactive_icon);
                //holder.title.setText(row.getTitle());


//                Picasso.with(getActivity())
//                        .load("https://image.tmdb.org/t/p/w500" + row.getPosterPath())
//                        .resize(400,400)
//                        .centerCrop()
//                        .into(new Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                               // holder.poster.setImageBitmap(bitmap);
//                                Palette.from(bitmap)
//                                        .generate(new Palette.PaletteAsyncListener() {
//                                            @Override
//                                            public void onGenerated(Palette palette) {
//                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
//                                                if (textSwatch == null) {
//                                                    holder.title.setTextColor(Color.WHITE);
//                                                    holder.posterCard.setBackgroundColor(Color.BLACK);
//                                                    return;
//                                                }
//
//                                                //holder.headerTitle.setBackgroundColor(textSwatch.getBodyTextColor());
//                                            }
//                                        });
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Drawable errorDrawable) {
//
//                                Log.d("scroll","fail");
////                                holder.title.setText(row.getTitle());
//                                holder.title.setTextColor(Color.WHITE);
//                                holder.posterCard.setBackgroundColor(Color.BLACK);
//
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                            }
//                        });
            }
        }

        private boolean checkFavMediaInDB(MediaBasic item) {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<MediaItem> res = realm.where(MediaItem.class)
                    .equalTo("tmdbId", String.valueOf(item.getId()))
                    .findAll();
            return res != null && !res.isEmpty() && res.isValid();
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
            if(isAdded())   getActivity().runOnUiThread(() -> notifyItemChanged(pos + 1, rating[0]));
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
            private CardView posterCard;
            private GeneralTextView title;
            private GeneralTextView headerSubTitle;
            private CircleImageView trailerButton;
            private AVLoadingIndicatorView footerProgressBar;
            private GeneralTextView headerTitle;
            private LikeButton favorite;
            private FrameLayout favoriteParent;
            private GeneralTextView imdbRating;
            private AVLoadingIndicatorView ratingProgressBar;
            private CardView ratingParent;

            private ViewType HOLDER_ID;

            public ViewHolder(View itemView, ViewType holderType) {
                super(itemView);
                switch (holderType) {
                    case TYPE_HEADER:
                        headerTitle = (GeneralTextView) itemView.findViewById(R.id.title_view);
                        headerSubTitle = (GeneralTextView) itemView.findViewById(R.id.title_view_subheader);
                        HOLDER_ID = holderType;
                        break;
                    case TYPE_ITEM:
                        poster = (ImageView) itemView.findViewById(R.id.poster);
                        poster.setClickable(true);
                        title = (GeneralTextView) itemView.findViewById(R.id.title);
                        posterCard = (CardView)itemView.findViewById(R.id.poster_card);
                        //trailerButton = (CircleImageView) itemView.findViewById(R.id.trailer_button);
                        imdbRating = (GeneralTextView) itemView.findViewById(R.id.imdb_rating);
                        ratingProgressBar = (AVLoadingIndicatorView) itemView.findViewById(R.id.ratingProgressBar);
                        favorite = (LikeButton) itemView.findViewById(R.id.favorite);
                        favorite.setEnabled(true);

                        favoriteParent = (FrameLayout) itemView.findViewById(R.id.favorite_parent);
                        ratingParent = (CardView) itemView.findViewById(R.id.rating_parent);
                        poster.setOnClickListener(view ->
                                itemTouchListener.onItemClick(view, getAdapterPosition() - 1, data.get(getAdapterPosition() - 1)));
                        itemView.setOnClickListener(view ->
                                itemTouchListener.onItemClick(view, getAdapterPosition() - 1, data.get(getAdapterPosition() - 1)));
                        favorite.setOnClickListener(view -> {
                            MediaBasic item = data.get(getAdapterPosition() - 1);
                            item.setImdbRating(imdbRating.getText().toString());
                            itemTouchListener.onFavoriteButtonClick(favorite, getAdapterPosition() - 1, item);
                        });

                        HOLDER_ID = holderType;
                        break;
                    case TYPE_FOOTER:
                        footerProgressBar = (AVLoadingIndicatorView) itemView.findViewById(R.id.progressBar);
                        HOLDER_ID = holderType;
                        break;

                }
            }
        }
    }

    private CharSequence getRelevantHeaderTitleText() {
        switch (queryType) {
            case NOW_PLAYING:
                return queryType.getQuery() + " in theatres";
            case POPULAR:
                return queryType.getQuery() + " among audience";
            case TOP_RATED:
                return queryType.getQuery() + " of all time";
            case UPCOMING:
                return queryType.getQuery() + " next week";
            default:
                return "";
        }
    }

    private CharSequence getRelevantHeaderSubtitleText() {
        switch (queryType) {
            case NOW_PLAYING:
                return "Plan your next movie";
            case POPULAR:
                return "Running on top worldwide";
            case TOP_RATED:
                return "All time great movies";
            case UPCOMING:
                return "Coming to theatres near you";
            default:
                return "";
        }
    }



    public interface ItemTouchListener {
        void onItemClick(View view, int position, MediaBasic item);

        void onFavoriteButtonClick(View view, int position, MediaBasic data);
    }

    public interface OnFragmentInteractionListener {
        void onDataLoad(boolean status);

        void onNoNetworkError(boolean isNetworkAvailable);
    }

    private PresenterFactory<DiscoverPresenter> factory = () -> new DiscoverPresenter(RecentMoviesFragment.this, TAG);
}
