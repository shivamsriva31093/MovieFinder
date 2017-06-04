package task.application.com.moviefinder.ui.favorites;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;
import task.application.com.moviefinder.ui.utility.realmrecview.RealmRecViewAdapter;
import task.application.com.moviefinder.util.Util;


public class FavoritesMediaFragment extends Fragment implements FavoritesMediaContract.View {


    private static String FILTER = "";
    private OnFragmentInteractionListener mListener;
    private RecyclerView recView;
    private Realm realm;
    private RecViewAdapter adapter;
    private FavoritesMediaContract.Presenter presenter;

    public FavoritesMediaFragment() {
        // Required empty public constructor
    }


    public static FavoritesMediaFragment newInstance(Bundle args) {
        FavoritesMediaFragment fragment = new FavoritesMediaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("FILTER"))
            FILTER = getArguments().getString("FILTER");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites_tv, container, false);
        recView = (RecyclerView) rootView.findViewById(R.id.fav_rec_view);
        setUpRecyclerView();
        return rootView;
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recView.setLayoutManager(layoutManager);
        adapter = new RecViewAdapter(null, itemTouchListener);
        recView.setAdapter(adapter);
        presenter.fetchDataFromRealm(FILTER);
    }

    ItemTouchListener itemTouchListener = (view, item) -> {

    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void setPresenter(FavoritesMediaContract.Presenter presenter) {
        this.presenter = Util.checkNotNull(presenter, "View presenter can't be null: " + presenter.getClass());
    }

    @Override
    public void updateListAdapter(RealmResults<MediaItem> res) {
        adapter.updateData(res);
    }

    private class RecViewAdapter extends RealmRecViewAdapter<MediaItem, RecViewAdapter.ViewHolder> {

        private ItemTouchListener listener;

        public RecViewAdapter(@Nullable OrderedRealmCollection<MediaItem> data, ItemTouchListener listener) {
            this(data, true);
            this.listener = listener;
        }

        public RecViewAdapter(@Nullable OrderedRealmCollection<MediaItem> data, boolean autoUpdate) {
            super(data, autoUpdate);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fav_rec_view_items, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderedRealmCollection<MediaItem> data = getData();
            if (data != null) {
                Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + data.get(position).getBackDrop())
                        .error(R.drawable.fav_media_placeholder)
                        .placeholder(R.drawable.fav_media_placeholder)
                        .into(holder.backdrop);
                holder.title.setText(getData().get(position).getTitle());
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView backdrop;
            private TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                backdrop = (ImageView) itemView.findViewById(R.id.backdrop);
                title = (TextView) itemView.findViewById(R.id.title);
                itemView.setOnTouchListener((view, motionEvent) -> {
                    listener.onItemTouch(view, getItem(getAdapterPosition()));
                    return true;
                });
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    interface ItemTouchListener {
        void onItemTouch(View view, MediaItem item);
    }

}