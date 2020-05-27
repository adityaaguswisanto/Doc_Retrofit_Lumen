package com.adityaagusw.examplelumen.Pagination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class Pagination extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    public Pagination(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItem = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItem + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= getTotalPages()) {
                loadMoreItem();
            }
        }

    }

    protected abstract void loadMoreItem();

    protected abstract int getTotalPages();

    protected abstract boolean isLastPage();

    protected abstract boolean isLoading();

}