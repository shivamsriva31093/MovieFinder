package task.application.com.moviefinder.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by sHIVAM on 7/1/2017.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int visibleItemsThreshold = 4;
    private int currentpage;
    private int prevTotalItems = 0;
    private boolean loading = true;
    private int startPageIdx;

    RecyclerView.LayoutManager layoutManager;
    private int defaultNoFooterViewType = -1;
    private int footerViewType = -1;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(LinearLayoutManager layoutManager, int visibleItemsThreshold) {
        this(visibleItemsThreshold);
        this.layoutManager = layoutManager;
    }

    public EndlessScrollListener(GridLayoutManager layoutManager, int visibleItemsThreshold) {
        this(visibleItemsThreshold * layoutManager.getSpanCount());
        this.layoutManager = layoutManager;
    }

    public EndlessScrollListener(StaggeredGridLayoutManager layoutManager, int visibleItemsThreshold) {
        this(visibleItemsThreshold * layoutManager.getSpanCount());
        this.layoutManager = layoutManager;
    }

    public EndlessScrollListener(int visibleItemsThreshold) {
        this(visibleItemsThreshold, 0);
    }

    public EndlessScrollListener(int visibleItemsThreshold, int startPageIdx) {
        init();
        this.visibleItemsThreshold = visibleItemsThreshold;
        this.startPageIdx = startPageIdx;
        this.currentpage = startPageIdx;
    }

    private void init() {
        footerViewType = getFooterViewType(defaultNoFooterViewType);
        int threshold = getVisibleThreshold();
        if (threshold > visibleItemsThreshold)
            visibleItemsThreshold = threshold;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int lastVisibleItem = Integer.MIN_VALUE;
        for (int item : lastVisibleItemPositions) {
            lastVisibleItem = Math.max(item, lastVisibleItem);
        }
        return lastVisibleItem;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int totalItems = adapter.getItemCount();
        int lastVisibleItemPosition = getLastVisibleItemPosition();

        boolean isLoadMore = (lastVisibleItemPosition + visibleItemsThreshold) > totalItems;

        if (isLoadMore) {
            if (isUsingFooterView()) {
                if (!isFooterView(adapter)) {
                    if (totalItems < prevTotalItems) {
                        this.currentpage = this.startPageIdx;
                    } else if (totalItems == prevTotalItems) {
                        currentpage = currentpage == startPageIdx ? startPageIdx : --currentpage;
                    }
                    loading = false;
                }
            } else {
                if (totalItems > prevTotalItems)
                    loading = false;
            }

            if (!loading) {
                prevTotalItems = totalItems;
                currentpage++;
                onLoadMore(currentpage, totalItems, recyclerView);
                loading = true;
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    private int getLastVisibleItemPosition() {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager)
                    .findLastVisibleItemPositions(null);
            return getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return 0;
    }

    public void resetState() {
        this.currentpage = this.startPageIdx;
        this.prevTotalItems = 0;
        this.loading = true;
    }

    public boolean isUsingFooterView() {
        return footerViewType != defaultNoFooterViewType;
    }

    public boolean isFooterView(RecyclerView.Adapter adapter) {
        int pTotalItems = adapter.getItemCount();
        return pTotalItems > 0 &&
                adapter.getItemViewType(pTotalItems - 1) == footerViewType;
    }

    public abstract void onLoadMore(int page, int totalItems, RecyclerView recyclerView);

    public abstract int getFooterViewType(int defaultNoFooterViewType);

    public int getVisibleThreshold() {
        return visibleItemsThreshold;
    }
}
