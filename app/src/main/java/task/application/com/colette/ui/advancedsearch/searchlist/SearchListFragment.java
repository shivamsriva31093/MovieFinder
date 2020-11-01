package task.application.com.colette.ui.advancedsearch.searchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;
import com.squareup.picasso.Picasso;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.R;
import task.application.com.colette.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.colette.util.EndlessScrollListener;
import task.application.com.colette.util.Util;
import task.application.com.colette.util.ViewType;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListFragment extends Fragment implements SearchListContract.View{


    private static final String TAG = SearchListFragment.class.getName();
    private static final String SEARCH_LIST = "searchList";
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String SEARCH_ITEM = "searchItem";

    private ArrayList<? extends MediaBasic> resultList = new ArrayList<>();
    private OnReplaceFragmentListener listener;

    private SearchListContract.Presenter presenter;
    private FrameLayout recyclerViewParent;
    private RecyclerView recyclerView;
    private RelativeLayout fragmentContainer;
    private SearchListAdapter recyclerViewAdapter;
    private NewtonCradleLoading progressView;
    private MediaType searchMediaType;
    private CoordinatorLayout parentActivityLayout;
    private int totalResults;
    private int totalPages;
    private String searchQuery = "";
    private EndlessScrollListener rvScrollListener;


    public static SearchListFragment newInstance() {
        return new SearchListFragment();
    }

    public static SearchListFragment newInstance(ArrayList<? extends MediaBasic> movieDbs,
                                                 String query,
                                                 MediaType filterType, int totalResults, int totalPages) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_LIST, movieDbs);
        bundle.putSerializable("filtering_type", filterType);
        bundle.putInt("totalItems", totalResults);
        bundle.putInt("totalPages", totalPages);
        bundle.putString("query", query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
            resultList = getArguments().getParcelableArrayList(SEARCH_LIST);
            searchMediaType = (MediaType) getArguments().getSerializable("filtering_type");
            totalResults = getArguments().getInt("totalItems");
            totalPages = getArguments().getInt("totalPages");
            searchQuery = getArguments().getString("query");
        }

        recyclerViewAdapter = new SearchListAdapter(getActivity(), resultList, searchMediaType, itemClickListener);
    }

    SearchItemClickListener itemClickListener = new SearchItemClickListener() {
        @Override
        public <T extends MediaBasic> Void onItemClick(View view, T item) {
            presenter.onSearchItemClick(item);
            return null;
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            resultList = savedInstanceState.getParcelableArrayList(SEARCH_LIST);
            searchMediaType = (MediaType) getArguments().getSerializable("filtering_type");
            totalResults = getArguments().getInt("totalItems");
            totalPages = getArguments().getInt("totalPages");
            searchQuery = getArguments().getString("query");
        }
        parentActivityLayout = (CoordinatorLayout) getActivity().findViewById(R.id.parent_layout);
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.activity_search_list);
        progressView = (NewtonCradleLoading) fragmentContainer.findViewById(R.id.progressView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchlist, container, false);
        recyclerViewParent = (FrameLayout) rootView.findViewById(R.id.recView_parent);
        recyclerView = (RecyclerView) recyclerViewParent.findViewById(R.id.recView);
        presenter.setFilteringType(searchMediaType);
        presenter.setQuery(searchQuery);
        setUpRecyclerView();
        return rootView;
    }

    private void setUpRecyclerView() {
        if (resultList.isEmpty()) {
            showNoResults();
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        rvScrollListener = new EndlessScrollListener(linearLayoutManager, 5) {


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
        };
        recyclerView.addOnScrollListener(rvScrollListener);
    }

    private void loadNextPageFromApi(int page) {
        recyclerViewAdapter.addFooter();
        presenter.searchByKeyword(presenter.getQuery(), page);
    }

    @Override
    public void setEndlessScrollLoading(boolean status) {
        rvScrollListener.setLoading(status);
        recyclerViewAdapter.removeFooter();
    }

    @Override
    public void updateNewItems(List<? extends MediaBasic> data) {
        recyclerViewAdapter.addDataItems(data);
        int curPage = rvScrollListener.getCurPage();
        rvScrollListener.setCurPage(curPage + 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (SearchListActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement " +
                    "OnReplaceFragmentInterface");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void showSearchList(ArrayList<? extends MediaBasic> movieDbs, int totalResults, int totalPages) {
        listener.replaceFragment(movieDbs, presenter.getQuery(), presenter.getFilteringType(), totalResults, totalPages);
    }


    @Override
    public void setImdbRatings(String rating, int pos) {
        recyclerViewAdapter.setRating(rating, pos);
    }

    @Override
    public <T extends MediaBasic> Void showItemDetailsUi(T item) {
        Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CLICKED_ITEM, item);
        bundle.putSerializable("filtering_type", searchMediaType);
        intent.putExtra(SEARCH_ITEM, bundle);
        startActivity(intent);
        return null;
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        Context context = ApplicationClass.getInstance();
        if(show) {
            recyclerViewParent.setVisibility(View.GONE);
            fragmentContainer.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            progressView.setVisibility(View.VISIBLE);
            progressView.start();
        } else {
            progressView.setVisibility(View.GONE);
            progressView.stop();
            fragmentContainer.setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
            recyclerViewParent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoResults() {
        recyclerViewParent.setVisibility(View.GONE);
        ImageView iv = (ImageView) getActivity().findViewById(R.id.no_results);
        iv.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "No results found!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingResultsError() {
        showNoResults();
        Snackbar snackbar = Snackbar.make(parentActivityLayout,
                "Error in connectivity. Please check your net connection and retry", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void setPresenter(SearchListContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SEARCH_LIST, recyclerViewAdapter.getCurrentData());
        outState.putSerializable("filtering_type", searchMediaType);
        outState.putInt("totalItems", totalResults);
        outState.putInt("totalPages", totalPages);
        outState.putString("query", presenter.getQuery());
        super.onSaveInstanceState(outState);
    }


    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{
        private Context context;
        private ArrayList<MediaBasic> data = new ArrayList<>();
        private ArrayList<MediaBasic> oldData = new ArrayList<>();
        private SearchItemClickListener listener;
        private SparseBooleanArray posArray = new SparseBooleanArray();
        private MediaType searchType;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;
        private SparseBooleanArray imdbRating = new SparseBooleanArray();
        private boolean isLoaderRemoved = true;

        public SearchListAdapter(Context context, ArrayList<? extends MediaBasic> data,
                                 MediaType searchType, SearchItemClickListener listener) {
            this.context = context;
            this.searchType = searchType;
            this.data.addAll(data);
            this.listener = listener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewType holderType = ViewType.values()[viewType];
            switch (holderType) {
                case TYPE_HEADER:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchlist_header, parent, false)
                            , TYPE_HEADER);
                case TYPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchlist_recycler, parent, false),
                            TYPE_ITEM);
                case TYPE_FOOTER:
                    return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchlist_footer, parent, false),
                            TYPE_FOOTER);
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return ViewType.TYPE_HEADER.ordinal();
            if (position == data.size() + 1)
                return ViewType.TYPE_FOOTER.ordinal();
            return ViewType.TYPE_ITEM.ordinal();
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.HOLDER_ID) {
                case TYPE_HEADER:
                    holder.header.setText(totalResults + " results returned");
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
                holder.progressBar.setVisibility(View.VISIBLE);
                presenter.getRatings(searchType, data.get(position - 1), position - 1);
                imdbRating.put(position, true);
                posArray.put(position, false);
                loadMediaData(holder, position);
            } else {

                if (posArray.get(position)) {
                    loadMediaData(holder, position);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imdbRating.setText(data.get(position - 1).getImdbRating());
                    holder.imdbRating.setVisibility(View.VISIBLE);
                    holder.imdbIcon.setVisibility(View.VISIBLE);
                    holder.starIcon.setVisibility(View.VISIBLE);
                }
            }
        }

        private void loadMediaData(ViewHolder holder, int position) {
            if (searchType.equals(MediaType.MOVIES)) {
                loadMovieInfo(holder, (BasicMovieInfo) data.get(position - 1));
            } else {
                loadTvInfo(holder, (BasicTVInfo) data.get(position - 1));
            }
            holder.imdbRating.setVisibility(View.GONE);
            holder.imdbIcon.setVisibility(View.GONE);
            holder.starIcon.setVisibility(View.GONE);
        }

        private void loadTvInfo(ViewHolder holder, BasicTVInfo tv) {
            if (!tv.getFirstAirDate().isEmpty())
                holder.year.setText(tv.getFirstAirDate().substring(0, 4));
            holder.title.setText(tv.getOriginalName());
            String url = tv.getPosterPath();
            if (url == null) {
                holder.imageView.setImageResource(R.drawable.trailer1);
            } else {
                Picasso.with(context)
                        .load("https://image.tmdb.org/t/p/w500" + url)
                        .fit()
                        .placeholder(R.drawable.trailer1)
                        .into(holder.imageView);
            }
        }

        private void loadMovieInfo(ViewHolder holder, BasicMovieInfo movie) {
            if (!movie.getReleaseDate().isEmpty())
                holder.year.setText(movie.getReleaseDate().substring(0, 4));
            holder.title.setText(movie.getTitle());
            String url = movie.getBackdropPath();
            if (url == null) {
                holder.imageView.setImageResource(R.drawable.trailer1);
            } else {
                Picasso.with(context)
                        .load("https://image.tmdb.org/t/p/w500" + url)
                        .placeholder(R.drawable.trailer1).into(holder.imageView);
            }
        }

        public void replaceData(ArrayList<? extends MediaBasic> movieDbs) {
            this.oldData = data;
            this.data.clear();
            this.data.addAll(Util.checkNotNull(movieDbs));
            notifyDataSetChanged();
        }

        public void addDataItems(List<? extends MediaBasic> newDataItems) {
            removeFooter();
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCB(data, newDataItems));
//            new Handler().post(() -> {
//                diffResult.dispatchUpdatesTo(this);
//                data.addAll(newDataItems);
//            });
            int posStart = data.size() + 1;
            for (MediaBasic item : newDataItems) {
                data.add(item);
                notifyItemInserted(posStart++);
            }
        }

        public ArrayList<? extends MediaBasic> getCurrentData() {
            return this.data;
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() + 2 : 0;
        }

        public void setRating(String rating, int pos) {
            rating = rating == null ? "N/A" : rating;
            MediaBasic item = data.get(pos);
            if(rating.contentEquals("N/A")){
                rating = "Unrated";
            }
            else{
                rating = "IMDb "+rating;
            }
            item.setImdbRating(rating);
            posArray.put(pos + 1, true);
            new Handler().post(() -> notifyItemChanged(pos + 1));
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
            private TextView title;
            private ImageView icon;
            private TextView year;
            private TextView header;
            private AppCompatImageView imdbIcon;
            private AppCompatImageView starIcon;
            private CircleImageView imageView;
            private TextView imdbRating;
            private ProgressBar progressBar;
            private ProgressBar footerProgressBar;
            private ViewType HOLDER_ID;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                switch (viewType) {
                    case TYPE_ITEM:
                        title = (TextView) itemView.findViewById(R.id.title);
                        year = (TextView) itemView.findViewById(R.id.year);
                        imageView = (CircleImageView) itemView.findViewById(R.id.item_image);
                        imdbRating = (TextView) itemView.findViewById(R.id.imdb_rating);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.rating_progress);
                        imdbIcon = (AppCompatImageView) itemView.findViewById(R.id.imdb_icon);
                        starIcon = (AppCompatImageView) itemView.findViewById(R.id.star_icon);
                        itemView.setOnClickListener(v -> listener.onItemClick(v, data.get(getAdapterPosition() - 1)));
                        HOLDER_ID = ViewType.TYPE_ITEM;
                        break;
                    case TYPE_HEADER:
                        header = (TextView) itemView.findViewById(R.id.header);
                        HOLDER_ID = ViewType.TYPE_HEADER;
                        break;
                    case TYPE_FOOTER:
                        footerProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
                        HOLDER_ID = ViewType.TYPE_FOOTER;
                        break;
                }

            }
        }

        private class DiffUtilCB extends DiffUtil.Callback {
            private List<? extends MediaBasic> oldData;
            private List<? extends MediaBasic> newData;
            public DiffUtilCB(List<? extends MediaBasic> prevData, List<? extends MediaBasic> data) {
                this.oldData = prevData;
                this.newData = data;
            }

            @Override
            public int getOldListSize() {
                return oldData.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldData.get(oldItemPosition).getId() == newData.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                if (oldData.get(oldItemPosition) == null || newData.get(newItemPosition) == null)
                    return false;
                return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return super.getChangePayload(oldItemPosition, newItemPosition);
            }
        }
    }


    public interface SearchItemClickListener {
        <T extends MediaBasic> Void onItemClick(View view, T item);
    }

    public interface OnReplaceFragmentListener {
        void replaceFragment(ArrayList<? extends MediaBasic> movieDbs, String searchQuery, MediaType filterType, int totalResults, int totalPages);
    }

}
