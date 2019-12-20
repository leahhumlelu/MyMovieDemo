package com.example.mymoviedemo.ui;

import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollEventListener extends RecyclerView.OnScrollListener {
    private boolean isScrolling = false;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            isScrolling = true;
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount(); // visible item counts
        int totalItemCount = recyclerView.getLayoutManager().getItemCount(); // total item counts
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition(); // how many items have been scrolled out

        if (isScrolling && visibleItemCount + firstVisibleItem == totalItemCount-1) {
            onLoadMore();
            isScrolling = false;
        }
    }

    public abstract void onLoadMore();
}
