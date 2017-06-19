package task.application.com.moviefinder.ui.searchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.androidtmdbwrapper.model.tv.BasicTVInfo;
import com.squareup.picasso.Picasso;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.moviefinder.util.Util;

/**
 * Created by sHIVAM on 2/6/2017.
 */

public class SearchListFragment extends Fragment implements SearchListContract.View{


    private static final String TAG = SearchListFragment.class.getName();
    private static final String SEARCH_LIST = "searchList";
    private static final String CLICKED_ITEM = "clickedItem";
    private static final String SEARCH_ITEM = "searchItem";

    private ArrayList<? extends MediaBasic> resultList;
    private OnReplaceFragmentListener listener;

    private SearchListContract.Presenter presenter;
    private FrameLayout recyclerViewParent;
    private RecyclerView recyclerView;
    private RelativeLayout fragmentContainer;
    private SearchListAdapter recyclerViewAdapter;
    private NewtonCradleLoading progressView;
    private MediaType searchMediaType;
    private CoordinatorLayout parentActivityLayout;


    public static SearchListFragment newInstance() {
        return new SearchListFragment();
    }

    public static SearchListFragment newInstance(ArrayList<? extends MediaBasic> movieDbs, MediaType filterType) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_LIST, movieDbs);
        bundle.putSerializable("filtering_type", filterType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if(!savedInstanceState.isEmpty()) {
                resultList = savedInstanceState.getParcelableArrayList(SEARCH_LIST);
                searchMediaType = (MediaType) getArguments().getSerializable("filtering_type");
            }
        } else if (getArguments() != null && !getArguments().isEmpty()) {
            resultList = getArguments().getParcelableArrayList(SEARCH_LIST);
            searchMediaType = (MediaType) getArguments().getSerializable("filtering_type");
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
        setRetainInstance(true);
        parentActivityLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_content);
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.activity_search_list);
        progressView = (NewtonCradleLoading) fragmentContainer.findViewById(R.id.progressView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchlist, container, false);
        recyclerViewParent = (FrameLayout) rootView.findViewById(R.id.recView_parent);
        recyclerView = (RecyclerView) recyclerViewParent.findViewById(R.id.recView);
        setUpRecyclerView();
        return rootView;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
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
    public void showSearchList(ArrayList<? extends MediaBasic> movieDbs) {
//        recyclerViewAdapter.replaceData(movieDbs);
        listener.replaceFragment(movieDbs, presenter.getFilteringType());
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
        recyclerViewAdapter.replaceData(new ArrayList<>());
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingResultsError() {
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
        super.onSaveInstanceState(outState);
    }


    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{
        private Context context;
        private ArrayList<? extends MediaBasic> data;
        private ArrayList<? extends MediaBasic> oldData;
        private SearchItemClickListener listener;
        private MediaType searchType;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        public SearchListAdapter(Context context, ArrayList<? extends MediaBasic> data, MediaType searchType, SearchItemClickListener listener) {
            this.context = context;
            this.searchType = searchType;
            this.data = data;
            this.listener = listener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_ITEM) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.list_recycler, parent, false);
                return new ViewHolder(itemView, TYPE_ITEM);
            }
            if(viewType == TYPE_HEADER){
                View itemView = LayoutInflater.from(context).inflate(R.layout.list_header, parent, false);
                return new ViewHolder(itemView, TYPE_HEADER);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder.HOLDER_ID == 1) {
                if (searchType.equals(MediaType.MOVIES)) {
                    BasicMovieInfo movie = (BasicMovieInfo) data.get(position - 1);
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
                } else {
                    BasicTVInfo tv = (BasicTVInfo) data.get(position - 1);
                    if (!tv.getFirstAirDate().isEmpty())
                        holder.year.setText(tv.getFirstAirDate().substring(0, 4));
                    holder.title.setText(tv.getOriginalName());
                    String url = tv.getPosterPath();
                    Log.d("tmdb", (url == null) + "");
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

            } else {
                 holder.header.setText(data.size() + " results returned");
            }
        }

        public void replaceData(ArrayList<? extends MediaBasic> movieDbs) {
            this.oldData = data;
            this.data = Util.checkNotNull(movieDbs);
            notifyDataSetChanged();
        }

        public ArrayList<? extends MediaBasic> getCurrentData() {
            return this.data;
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private ImageView icon;
            private TextView year;
            private TextView header;
            private CircleImageView imageView;
            private int HOLDER_ID;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                if (viewType == TYPE_ITEM) {
                    title = (TextView) itemView.findViewById(R.id.title);
                    year = (TextView) itemView.findViewById(R.id.year);
                    imageView = (CircleImageView) itemView.findViewById(R.id.item_image);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(v, data.get(getAdapterPosition() - 1));
                        }
                    });
                    HOLDER_ID = 1;
                } else {
                    header = (TextView) itemView.findViewById(R.id.header);
                    HOLDER_ID = 0;
                }

            }
        }

    }


    public interface SearchItemClickListener {
        <T extends MediaBasic> Void onItemClick(View view, T item);
    }

    public interface OnReplaceFragmentListener {
        void replaceFragment(ArrayList<? extends MediaBasic> movieDbs, MediaType filterType);
    }

}
