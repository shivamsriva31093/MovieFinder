package task.application.com.moviefinder.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * Created by sHIVAM on 7/1/2017.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int visibleItemsThreshold = 4;
    private int prevTotalCount = 0;
    private int curPage = 1;
    RecyclerView.LayoutManager layoutManager;
    private boolean isLoading = false;

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
        int threshold = getVisibleThreshold();
        if (threshold > visibleItemsThreshold)
            visibleItemsThreshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy <= 0) return;
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = getFirstVisibleItemPos();
        if (isLoading) {
            if (totalItemCount > prevTotalCount) {
                isLoading = false;
                prevTotalCount = totalItemCount;
            }
        }
        if (!isLoading && !isLastPage(curPage)) {
//            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                    && firstVisibleItemPosition >= 0) {
//                loadMore();
//            }
            if ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleItemsThreshold)) {
                isLoading = true;
                curPage += 1;
                loadMore(curPage);
                Log.d("test", "method load more called " + curPage);
            }
        }
    }

    public abstract boolean isLastPage(int page);

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

    public abstract void loadMore(int page);

    public int getVisibleThreshold() {
        return visibleItemsThreshold;
    }
}
