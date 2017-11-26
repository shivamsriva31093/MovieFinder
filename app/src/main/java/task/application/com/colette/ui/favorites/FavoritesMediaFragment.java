package task.application.com.colette.ui.favorites;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.mediadetails.MediaBasic;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmResults;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.R;
import task.application.com.colette.model.local.realm.datamodels.MediaItem;
import task.application.com.colette.ui.base.PresenterCache;
import task.application.com.colette.ui.base.PresenterFactory;
import task.application.com.colette.ui.itemdetail.SearchItemDetailActivity;
import task.application.com.colette.ui.utility.realmrecview.RealmRecViewAdapter;
import task.application.com.colette.ui.utility.widgets.GeneralTextView;
import task.application.com.colette.util.GridLayoutItemDecoration;
import task.application.com.colette.util.Util;
import task.application.com.colette.util.ViewType;


public class FavoritesMediaFragment extends Fragment implements FavoritesMediaContract.View {

    private static String FILTER = "";
    private final String TAG = FavoritesMediaFragment.this.getTag();
    private static final int PARENT_BOTTOM_MARGIN = 56;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recView;
    private RecViewAdapter adapter;
    private FavoritesMediaContract.Presenter presenter;

    private PresenterFactory<FavoritesPresenter> factory = () ->
            new FavoritesPresenter(FavoritesMediaFragment.this);
    private PresenterCache<FavoritesPresenter> presenterCache =
            PresenterCache.getInstance();
    private boolean isDestroyedBySystem;

    private boolean isMultiSelect;
    private ActionMode actionMode;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout searchBarContainer;
    private NestedScrollView parentLayout;
    private FrameLayout searchBarLeftActionLayout;
    private OnFragmentInteractionListener listener;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            FILTER = savedInstanceState.getString("filter");
            presenter = presenterCache.getPresenter(TAG, factory);
        }
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
//        parentLayout = (NestedScrollView) getActivity().findViewById(R.id.scrollingView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_RippleEffectCustom, true);
        View rootView = inflater.inflate(R.layout.fragment_favorites_tv, container, false);
        recView = (RecyclerView) rootView.findViewById(R.id.fav_rec_view);
        setUpRecyclerView();
        return rootView;
    }

    private void setUpRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                ViewType holderType = ViewType.values()[adapter.getItemViewType(position)];
                switch (holderType) {
                    case TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recView.setLayoutManager(layoutManager);
        recView.setNestedScrollingEnabled(false);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        adapter = new RecViewAdapter(null, itemTouchListener);
        recView.addItemDecoration(new GridLayoutItemDecoration(2, 1, true));
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
            animateSearchBar(-1, View.GONE, position);
            return true;
        }

        @Override
        public void onCheckboxClick(int position, MediaItem item) {
            setSelectedItemCount();
        }

    };

    private void animateSearchBar(final int direction, final int visibility, final int position) {
//        if (searchBarContainer == null) return;
//        searchBarContainer.animate()
//                .setDuration(200)
//                .translationY(direction * searchBarContainer.getHeight())
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        searchBarContainer.setVisibility(visibility);
//                        searchBarContainer.animate().setListener(null);
//                        actionMode = getActivity().startActionMode(actionCallback);
//                        adapter.notifyDataSetChanged();
//                        toggleSelection(position);
//                        animateBotNavChanges(1, View.GONE, true, 0);
//                    }
//                });
        actionMode = getActivity().startActionMode(actionCallback);
        adapter.notifyDataSetChanged();
        toggleSelection(position);
    }

    private void animateBotNavChanges(final int direction, final int visibility, boolean add, final int margin) {
        if (bottomNavigationView == null) return;
        bottomNavigationView.animate()
                .translationY(direction * bottomNavigationView.getHeight())
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
//                        if (add)
//                            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                        else
//                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                        setParentLayoutBottomMargin(margin);
                        bottomNavigationView.setVisibility(visibility);
                        bottomNavigationView.animate().setListener(null);
                    }
                });
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        setSelectedItemCount();
    }

    private void setSelectedItemCount() {
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
            listener.lockOrUnlockNavDrawer(true);
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
//            searchBarContainer.setTranslationY(0);
//            searchBarContainer.setVisibility(View.VISIBLE);
            isMultiSelect = false;
            adapter.clearSelections();
            animateBotNavChanges(0, View.VISIBLE, false, PARENT_BOTTOM_MARGIN);
            listener.lockOrUnlockNavDrawer(false);
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
        if (actionMode != null) {
            actionMode.finish();
        }
        if (!isDestroyedBySystem) {
            presenterCache.removePresenter(TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        isDestroyedBySystem = true;
        outState.putString("filter", FILTER);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyedBySystem = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null)
            presenter.start();
        try {
            listener = (FavoritesMediaActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "must implement " +
                    "OnFragmentInteractionInterface");
        }
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

    @Override
    public void showLoadingIndicator(boolean status) {
        ImageView navDrawerButton = (ImageView) searchBarLeftActionLayout.findViewById(R.id.search_bar_left_action);
//        ProgressBar progressBar = (ProgressBar) searchBarLeftActionLayout.findViewById(R.id.search_bar_progess);
        navDrawerButton.animate()
                .setDuration(300)
                .rotationX(180.0f)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                    }
                });
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
        public int getItemViewType(int position) {
            if (position == 0)
                return ViewType.TYPE_HEADER.ordinal();
            return ViewType.TYPE_ITEM.ordinal();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = null;
            ViewType holderType = ViewType.values()[viewType];
            switch (holderType) {
                case TYPE_HEADER:
                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recentmovies_header, parent, false);
                    break;
                case TYPE_ITEM:
                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fav_rec_view_items, parent, false);
            }
            return new ViewHolder(rootView, holderType);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (holder.HOLDER_ID) {
                case TYPE_HEADER:
                    holder.headerTitle.setText("Your Handpicked " + FILTER);
                    holder.subHeaderTitle.setVisibility(View.GONE);
                    break;
                case TYPE_ITEM:
                    bindRowItems(holder, position);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + 1;
        }

        private void bindRowItems(ViewHolder holder, int position) {
            OrderedRealmCollection<MediaItem> data = getData();
            if (data == null) return;
            holder.checkBox.setVisibility(isMultiSelect ? View.VISIBLE : View.GONE);
            toggleCheckBoxState(isMultiSelect && selectedItems.get(position, false), position, holder);
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + data.get(position - 1).getBackDrop())
                    .error(R.drawable.trailer1)
                    .placeholder(R.drawable.trailer1)
                    .into(holder.backdrop);
            holder.title.setText(getData().get(position - 1).getTitle());
            holder.imdbRating.setText(getData().get(position - 1).getImdbRating());
        }

        public void toggleSelection(int position) {
            if (selectedItems.get(position, false)) {
                selectedItems.delete(position);
            } else {
                selectedItems.put(position, true);
            }
            notifyItemChanged(position);
        }

        public int getSelectedItemCount() {
            return selectedItems.size();
        }

        public void clearSelections() {
            List<Integer> list = getSelectedItems();
            selectedItems.clear();
            notifyDataSetChanged();
        }

        public List<Integer> getSelectedItems() {
            List<Integer> itemIndices = new ArrayList<>();
            for (int i = 0; i < selectedItems.size(); i++)
                itemIndices.add(selectedItems.keyAt(i) - 1);
            return itemIndices;
        }

        public void toggleCheckBoxState(boolean state, int position, ViewHolder holder) {
            holder.checkBox.setChecked(state);
//            holder.backdrop.setScaleType(state ? ImageView.ScaleType.CENTER_INSIDE : ImageView.ScaleType.CENTER_CROP);
            holder.backdrop.setColorFilter(state ? Color.parseColor("#89000000") :
                    Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView backdrop;
            private TextView title;
            private CheckBox checkBox;

            private GeneralTextView headerTitle;
            private GeneralTextView subHeaderTitle;

            private GeneralTextView imdbRating;

            private ViewType HOLDER_ID;

            public ViewHolder(View itemView, ViewType holderType) {
                super(itemView);
                switch (holderType) {
                    case TYPE_HEADER:
                        headerTitle = (GeneralTextView) itemView.findViewById(R.id.title_view);
                        headerTitle.setTextColor(getResources().getColor(R.color.colorPrimaryMid));
                        subHeaderTitle = (GeneralTextView) itemView.findViewById(R.id.title_view_subheader);
                        HOLDER_ID = holderType;
                        break;
                    case TYPE_ITEM:
                        if (itemView instanceof CardView) {
                            CardView cardView = (CardView) itemView.findViewById(R.id.card_view_child);
                            backdrop = (ImageView) itemView.findViewById(R.id.backdrop);
                            title = (TextView) itemView.findViewById(R.id.title);
                            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
                            imdbRating = (GeneralTextView) itemView.findViewById(R.id.imdb_rating);
                            cardView.setOnClickListener(view ->
                                    listener.onItemClick(view, getAdapterPosition(), getItem(getAdapterPosition() - 1)));
                            cardView.setOnLongClickListener(view ->
                                    listener.onItemLongClick(view, getAdapterPosition(), getItem(getAdapterPosition() - 1)));
                            checkBox.setOnClickListener(view -> {
                                toggleSelection(getAdapterPosition());
                                listener.onCheckboxClick(getAdapterPosition(), getItem(getAdapterPosition() - 1));
                            });
                        }
                        HOLDER_ID = holderType;
                        break;
                }
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void lockOrUnlockNavDrawer(boolean status);
    }

    interface ItemTouchListener {
        void onItemClick(View view, int position, MediaItem item);
        boolean onItemLongClick(View view, int position, MediaItem item);

        void onCheckboxClick(int position, MediaItem item);
    }

}
