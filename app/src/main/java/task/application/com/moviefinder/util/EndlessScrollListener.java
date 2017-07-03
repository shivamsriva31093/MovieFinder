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
        init();
        this.visibleItemsThreshold = visibleItemsThreshold;
    }

    private void init() {
        footerViewType = getFooterViewType(defaultNoFooterViewType);
        int threshold = getVisibleThreshold();
        if (threshold > visibleItemsThreshold)
            visibleItemsThreshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = getFirstVisibleItemPos();
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMore();
            }
        }
    }

    public abstract boolean isLoading();

    public abstract boolean isLastPage();

    public abstract int getTotalPages();

    private int getFirstVisibleItemPos() {
        if (layoutManager instanceof LinearLayoutManager)
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        else if (layoutManager instanceof GridLayoutManager)
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return getFirstVisibleItem(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null));
        }
        return 0;
    }

    private int getFirstVisibleItem(int[] firstVisibleItemPositions) {
        int x = Integer.MAX_VALUE;
        for (int item : firstVisibleItemPositions)
            x = Math.min(x, item);
        return x;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    public boolean isUsingFooterView() {
        return footerViewType != defaultNoFooterViewType;
    }

    public boolean isFooterView(RecyclerView.Adapter adapter) {
        int pTotalItems = adapter.getItemCount();
        return pTotalItems > 0 &&
                adapter.getItemViewType(pTotalItems - 1) == footerViewType;
    }

    public abstract void loadMore();

    public abstract int getFooterViewType(int defaultNoFooterViewType);

    public int getVisibleThreshold() {
        return visibleItemsThreshold;
    }
}
