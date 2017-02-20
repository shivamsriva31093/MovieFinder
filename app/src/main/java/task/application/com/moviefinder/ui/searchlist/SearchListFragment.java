package task.application.com.moviefinder.ui.searchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import info.movito.themoviedbapi.model.MovieDb;
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

    private ArrayList<MovieDb> resultList;
    private OnReplaceFragmentListener listener;

    private SearchListContract.Presenter presenter;
    private FrameLayout recyclerViewParent;
    private RecyclerView recyclerView;
    private RelativeLayout fragmentContainer;
    private SearchListAdapter recyclerViewAdapter;
    private NewtonCradleLoading progressView;


    public static SearchListFragment newInstance() {
        return new SearchListFragment();
    }

    public static SearchListFragment newInstance(ArrayList<MovieDb> movieDbs) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH_LIST, movieDbs);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if(!savedInstanceState.isEmpty()) {
                resultList = (ArrayList<MovieDb>) savedInstanceState.getSerializable(SEARCH_LIST);
            }
        } else if (getArguments() != null && !getArguments().isEmpty()) {
            resultList = (ArrayList<MovieDb>) getArguments().getSerializable(SEARCH_LIST);
        }
        fragmentContainer = (RelativeLayout) getActivity().findViewById(R.id.activity_search_list);
        progressView = (NewtonCradleLoading) fragmentContainer.findViewById(R.id.progressView);
        recyclerViewAdapter = new SearchListAdapter(getActivity(), resultList, itemClickListener);
    }

    SearchItemClickListener itemClickListener = new SearchItemClickListener() {
        @Override
        public void onItemClick(View view, MovieDb item) {
            presenter.onSearchItemClick(item);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchlist, container, false);
        recyclerViewParent = (FrameLayout) rootView.findViewById(R.id.recView_parent);
        recyclerView = (RecyclerView) recyclerViewParent.findViewById(R.id.recView);
//        recyclerView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(event.getAction() == KeyEvent.ACTION_DOWN
//                        && keyCode == KeyEvent.KEYCODE_BACK) {
//                    recyclerViewAdapter.replaceData(recyclerViewAdapter.oldData);
//                }
//                return false;
//            }
//        });
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
    public void showSearchList(ArrayList<MovieDb> movieDbs) {
//        recyclerViewAdapter.replaceData(movieDbs);
        listener.replaceFragment(movieDbs);
    }

    @Override
    public void showItemDetailsUi(MovieDb item) {
        Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CLICKED_ITEM, item);
        intent.putExtra(SEARCH_ITEM, bundle);
        startActivity(intent);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        if(show) {
            recyclerViewParent.setVisibility(View.GONE);
            fragmentContainer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            progressView.setVisibility(View.VISIBLE);
            progressView.start();
        } else {
            progressView.setVisibility(View.GONE);
            progressView.stop();
            fragmentContainer.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            recyclerViewParent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoResults() {

    }

    @Override
    public void showLoadingResultsError() {

    }

    @Override
    public void setPresenter(SearchListContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SEARCH_LIST, recyclerViewAdapter.getCurrentData());
        super.onSaveInstanceState(outState);
    }


    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{
        private Context context;
        private ArrayList<MovieDb> data;
        private ArrayList<MovieDb> oldData;
        private SearchItemClickListener listener;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        public SearchListAdapter(Context context, ArrayList<MovieDb> data, SearchItemClickListener listener) {
            this.context = context;
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
                holder.year.setText(data.get(position - 1).getReleaseDate());
                holder.type.setText(data.get(position - 1).getTagline());
                holder.title.setText(data.get(position - 1).getTitle());
                String url = data.get(position - 1).getBackdropPath();
                if (url==null) {

                    holder.imageView.setImageResource(R.drawable.clapperboard1);
                } else {
                    Picasso.with(context)
                            .load("https://image.tmdb.org/t/p/w500"+url)
                            .placeholder(R.drawable.clapperboard1).into(holder.imageView);
                }

            } else {
                 holder.header.setText(data.size() + " results returned");
            }
        }

        public void replaceData(ArrayList<MovieDb> movieDbs) {
            this.oldData = data;
            this.data = Util.checkNotNull(movieDbs);
            notifyDataSetChanged();
        }

        public ArrayList<MovieDb> getCurrentData() {
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
            private TextView type;
            private TextView header;
            private CircleImageView imageView;
            private int HOLDER_ID;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                if (viewType == TYPE_ITEM) {
                    title = (TextView) itemView.findViewById(R.id.title);
                    type = (TextView) itemView.findViewById(R.id.type);
                    year = (TextView) itemView.findViewById(R.id.year);
                    imageView = (CircleImageView) itemView.findViewById(R.id.item_image);

                    HOLDER_ID = 1;
                } else {
                    header = (TextView) itemView.findViewById(R.id.header);
                    HOLDER_ID = 0;
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(v, data.get(getAdapterPosition()-1));
                    }
                });
            }
        }
    }


    public interface SearchItemClickListener {
        void onItemClick(View view, MovieDb item);
    }

    public interface OnReplaceFragmentListener {
        void replaceFragment(ArrayList<MovieDb> movieDbs);
    }

}
