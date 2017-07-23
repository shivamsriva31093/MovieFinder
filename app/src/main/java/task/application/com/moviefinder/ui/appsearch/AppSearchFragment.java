package task.application.com.moviefinder.ui.appsearch;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.people.PeopleBasic;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.util.EndlessScrollListener;
import task.application.com.moviefinder.util.Util;
import task.application.com.moviefinder.util.ViewType;

public class AppSearchFragment extends Fragment implements AppSearchContract.View {

    private static final String TAG = AppSearchFragment.class.getSimpleName();
    private static final String QUERY_TYPE = "queryType";
    private static final java.lang.String QUERY = "query";
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String SEARCH_ITEM = "searchItem";
    private static final String SEARCH_LIST = "searchList";

    private AppSearchContract.Presenter presenter;
    private String searchQuery;
    private MediaType searchType;
    private int totalPages;
    private int totalResults;
    private ArrayList<? extends BaseMediaData> resultList = new ArrayList<>();

    private RecyclerView recyclerView;
    private EndlessScrollListener rvScrollListener;
    private SearchListAdapter recyclerViewAdapter;
    private OnFragmentInteractionListener listener;

    public AppSearchFragment() {
    }

    public static AppSearchFragment newInstance(Bundle args) {
        AppSearchFragment fragment = new AppSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && !getArguments().isEmpty()) {
            searchType = (MediaType) getArguments().getSerializable(QUERY_TYPE);
        }
        recyclerViewAdapter = new SearchListAdapter(resultList, searchType, itemClickListener);
    }

    OnItemTouchListener itemClickListener = new OnItemTouchListener() {
        @Override
        public <T extends BaseMediaData> Void onItemClick(View view, T item) {
//            Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(CLICKED_ITEM, item);
//            bundle.putSerializable("filtering_type", searchType);
//            intent.putExtra(SEARCH_ITEM, bundle);
//            startActivity(intent);
            return null;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appsearch, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recView);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        rvScrollListener = new EndlessScrollListener(linearLayoutManager, 2) {


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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchQuery = presenter.getQuery();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(QUERY);
            resultList = savedInstanceState.getParcelableArrayList(SEARCH_LIST);
            totalPages = getArguments().getInt("totalPages");
            totalPages = getArguments().getInt("totalResults");
            searchType = (MediaType) savedInstanceState.getSerializable(QUERY_TYPE);
        }
    }


    @Override
    public void setPresenter(AppSearchContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter);
    }

    @Override
    public void showResultList(List<? extends BaseMediaData> result, int totalPages, int totalResults) {
        this.totalPages = totalPages;
        this.totalResults = totalResults;
        recyclerViewAdapter.replaceData(new ArrayList<>(result));
    }

    @Override
    public void showLoadingIndicator(boolean flag) {
        listener.changeProBarVisibility(flag);
    }

    @Override
    public void showNoResults() {

    }

    private void loadNextPageFromApi(int page) {
        recyclerViewAdapter.addFooter();
        presenter.loadNextPage(page);
    }

    @Override
    public void setEndlessScrollLoading(boolean status) {
        rvScrollListener.setLoading(status);
        recyclerViewAdapter.removeFooter();
    }

    @Override
    public void updateNewItems(List<? extends BaseMediaData> data) {
        recyclerViewAdapter.addDataItems(data);
        int curPage = rvScrollListener.getCurPage();
        rvScrollListener.setCurPage(curPage + 1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SEARCH_LIST, recyclerViewAdapter.getCurrentData());
        outState.putSerializable(QUERY_TYPE, searchType);
        outState.putInt("totalPages", totalPages);
        outState.putInt("totalResults", totalResults);
        outState.putString(QUERY, presenter.getQuery());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            listener = (AppSearchActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement " +
                    "OnReplaceFragmentInterface");
        }
    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
        private ArrayList<BaseMediaData> data = new ArrayList<>();
        private ArrayList<BaseMediaData> oldData = new ArrayList<>();
        private OnItemTouchListener listener;
        private SparseBooleanArray posArray = new SparseBooleanArray();
        private MediaType searchType;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;
        private SparseBooleanArray imdbRating = new SparseBooleanArray();
        private boolean isLoaderRemoved = true;

        public SearchListAdapter(ArrayList<? extends BaseMediaData> data,
                                 MediaType searchType, OnItemTouchListener listener) {
            this.searchType = searchType;
            this.data.addAll(data);
            this.listener = listener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewType holderType = ViewType.values()[viewType];
            switch (holderType) {
                case TYPE_HEADER:
                    return new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.searchlist_header, parent, false)
                            , TYPE_HEADER);
                case TYPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.searchlist_recycler, parent, false),
                            TYPE_ITEM);
                case TYPE_FOOTER:
                    return new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.searchlist_footer, parent, false),
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
//            if (!imdbRating.get(position, false)) {
//                holder.progressBar.setVisibility(View.VISIBLE);
//                presenter.getRatings(searchType, data.get(position - 1), position - 1);
//                imdbRating.put(position, true);
//                posArray.put(position, false);
//                loadMediaData(holder, position);
//            } else {
//
//                if (posArray.get(position)) {
//                    loadMediaData(holder, position);
//                    holder.progressBar.setVisibility(View.GONE);
//                    holder.imdbRating.setText(data.get(position - 1).getImdbRating());
//                    holder.imdbRating.setVisibility(View.VISIBLE);
//                    holder.imdbIcon.setVisibility(View.VISIBLE);
//                    holder.starIcon.setVisibility(View.VISIBLE);
//                }
//            }
            loadMediaData(holder, holder.getAdapterPosition());
        }

        private void loadMediaData(ViewHolder holder, int position) {
            switch (searchType) {
                case MOVIES:
                    loadMovieInfo(holder, (BasicMovieInfo) data.get(position - 1));
                    break;
                case TV:
                    loadTvInfo(holder, (BasicTVInfo) data.get(position - 1));
                    break;
                case PEOPLE:
                    loadPeopleInfo(holder, (PeopleBasic) data.get(position - 1));
            }
            holder.imdbRating.setVisibility(View.GONE);
            holder.imdbIcon.setVisibility(View.GONE);
            holder.starIcon.setVisibility(View.GONE);
        }

        private void loadPeopleInfo(ViewHolder holder, PeopleBasic person) {
            holder.title.setText(person.getName());
            String url = person.getProfilePath();
            if (url == null) {
                holder.imageView.setImageResource(R.drawable.creditplaceholder);
            } else {
                Picasso.with(getActivity())
                        .load("https://image.tmdb.org/t/p/w500" + url)
                        .fit()
                        .placeholder(R.drawable.trailer1)
                        .into(holder.imageView);
            }
        }

        private void loadTvInfo(ViewHolder holder, BasicTVInfo tv) {
            if (!tv.getFirstAirDate().isEmpty())
                holder.year.setText(tv.getFirstAirDate().substring(0, 4));
            holder.title.setText(tv.getOriginalName());
            String url = tv.getPosterPath();
            if (url == null) {
                holder.imageView.setImageResource(R.drawable.trailer1);
            } else {
                Picasso.with(getActivity())
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
                Picasso.with(getActivity())
                        .load("https://image.tmdb.org/t/p/w500" + url)
                        .placeholder(R.drawable.trailer1).into(holder.imageView);
            }
        }

        public void replaceData(ArrayList<? extends BaseMediaData> movieDbs) {
            this.oldData = data;
            this.data.clear();
            this.data.addAll(Util.checkNotNull(movieDbs));
            notifyDataSetChanged();
        }

        public void addDataItems(List<? extends BaseMediaData> newDataItems) {
            removeFooter();
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCB(data, newDataItems));
//            new Handler().post(() -> {
//                diffResult.dispatchUpdatesTo(this);
//                data.addAll(newDataItems);
//            });
            int posStart = data.size() + 1;
            for (BaseMediaData item : newDataItems) {
                data.add(item);
                notifyItemInserted(posStart++);
            }
        }

        public ArrayList<? extends BaseMediaData> getCurrentData() {
            return this.data;
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() + 2 : 0;
        }

        public void setRating(String rating, int pos) {
            rating = rating == null ? "N/A" : rating;
            MediaBasic item = (MediaBasic) data.get(pos);
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
    }


    public interface OnItemTouchListener {

        <T extends BaseMediaData> Void onItemClick(View v, T mediaBasic);
    }

    public interface OnFragmentInteractionListener {
        void changeProBarVisibility(boolean status);
    }


}
