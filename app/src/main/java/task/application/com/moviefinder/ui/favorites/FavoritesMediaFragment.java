package task.application.com.moviefinder.ui.favorites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;
import task.application.com.moviefinder.ApplicationClass;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.local.realm.datamodels.MediaItem;
import task.application.com.moviefinder.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.moviefinder.ui.utility.realmrecview.RealmRecViewAdapter;
import task.application.com.moviefinder.util.Util;


public class FavoritesMediaFragment extends Fragment implements FavoritesMediaContract.View {


    private static String FILTER = "";
    private OnFragmentInteractionListener mListener;
    private RecyclerView recView;
    private Realm realm;
    private RecViewAdapter adapter;
    private FavoritesMediaContract.Presenter presenter;

    private boolean isMultiSelect;
    private ActionMode actionMode;

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
        if (presenter != null)
            presenter.fetchDataFromRealm(FILTER);
    }

    ItemTouchListener itemTouchListener = new ItemTouchListener() {
        @Override

        public void onItemClick(View view, int position, MediaItem item) {
            if (!isMultiSelect)
                presenter.showMediaDetails(item);
            else {
                toggleSelection(position);

            }
        }

        @Override
        public boolean onItemLongClick(View view, int position, MediaItem item) {
            if (actionMode != null) {
                toggleSelection(position);
                return true;
            }
            isMultiSelect = true;
            actionMode = getActivity().startActionMode(actionCallback);
            animateLayoutShiftDown();
            toggleSelection(position);
            return true;
        }
    };

    private void animateLayoutShiftDown() {

    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        if (adapter.getSelectedItemCount() == 0) {
            actionMode.finish();
            return;
        }
        String title = ApplicationClass.getInstance().getString(R.string.selected_count,
                adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    private ActionMode.Callback actionCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.rec_view_contextual_menu, menu);
            CheckBox selectAll = (CheckBox) menu.findItem(R.id.select_all).getActionView();
            selectAll.setButtonDrawable(R.drawable.cab_checkbox_selector);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedItems();
                    mode.finish();
                    return true;
                case R.id.select_all:
                    item.setChecked(true);
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            isMultiSelect = false;
            adapter.clearSelections();
        }
    };

    private void deleteSelectedItems() {
        final List<Integer> items = adapter.getSelectedItems();
        presenter.deleteDataFromRealm(items, adapter.getData().createSnapshot());
    }

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

    @Override
    public void showItemDetailsUI(MediaItem item) {
        Intent intent = new Intent(getActivity(), SearchItemDetailActivity.class);
        Bundle bundle = new Bundle();
        MediaBasic media = new MediaBasic();
        media.setId(Integer.valueOf(item.getTmdbId()));
        bundle.putParcelable("clickedItem", media);
        bundle.putSerializable("filtering_type", item.getMediaType().equals("Tv") ? MediaType.TV : MediaType.MOVIES);
        intent.putExtra("searchItem", bundle);
        startActivity(intent);
    }

    private class RecViewAdapter extends RealmRecViewAdapter<MediaItem, RecViewAdapter.ViewHolder> {

        private ItemTouchListener listener;
        private SparseBooleanArray selectedItems;

        public RecViewAdapter(@Nullable OrderedRealmCollection<MediaItem> data, ItemTouchListener listener) {
            this(data, true);
            this.listener = listener;
            selectedItems = new SparseBooleanArray();
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
            if (data == null) return;
            if (selectedItems.get(position, false))
                holder.itemView.setAlpha(0.5f);
            else
                holder.itemView.setAlpha(1f);
            holder.backdrop.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + data.get(position).getBackDrop())
                    .error(R.drawable.movie)
                    .placeholder(R.drawable.movie)
                        .into(holder.backdrop);
            holder.title.setText(getData().get(position).getTitle());

        }

        public void toggleSelection(int position) {
            if (selectedItems.get(position, false))
                selectedItems.delete(position);
            else
                selectedItems.put(position, true);
            notifyItemChanged(position);
        }

        public int getSelectedItemCount() {
            return selectedItems.size();
        }

        public void clearSelections() {
            selectedItems.clear();
            notifyDataSetChanged();
        }

        public List<Integer> getSelectedItems() {
            List<Integer> itemIndices = new ArrayList<>();
            for (int i = 0; i < selectedItems.size(); i++)
                itemIndices.add(selectedItems.keyAt(i));
            return itemIndices;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView backdrop;
            private TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                backdrop = (ImageView) itemView.findViewById(R.id.backdrop);
                title = (TextView) itemView.findViewById(R.id.title);
                itemView.setOnClickListener(view ->

                        listener.onItemClick(view, getAdapterPosition(), getItem(getAdapterPosition())));
                itemView.setOnLongClickListener(view ->
                        listener.onItemLongClick(view, getAdapterPosition(), getItem(getAdapterPosition())));
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    interface ItemTouchListener {
        void onItemClick(View view, int position, MediaItem item);
        boolean onItemLongClick(View view, int position, MediaItem item);
    }

}
