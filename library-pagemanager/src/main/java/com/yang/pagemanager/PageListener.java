package com.yang.pagemanager;

import android.view.View;

/**
 * Created by yang on 2019/11/26.
 */
public abstract class PageListener {

    public abstract void onRetry(View retryView);

    public void onEmtptyViewClicked(View emptyView) {
    }

    public int generateLoadingLayoutId() {
        return PageManager.NO_LAYOUT_ID;
    }

    public int generateRetryLayoutId() {
        return PageManager.NO_LAYOUT_ID;
    }

    public int generateEmptyLayoutId() {
        return PageManager.NO_LAYOUT_ID;
    }

    public View generateLoadingLayout() {
        return null;
    }

    public View generateRetryLayout() {
        return null;
    }

    public View generateEmptyLayout() {
        return null;
    }

    public boolean isSetLoadingLayout() {
        if (generateLoadingLayoutId() != PageManager.NO_LAYOUT_ID || generateLoadingLayout() != null)
            return true;
        return false;
    }

    public boolean isSetRetryLayout() {
        if (generateRetryLayoutId() != PageManager.NO_LAYOUT_ID || generateRetryLayout() != null)
            return true;
        return false;
    }

    public boolean isSetEmptyLayout() {
        if (generateEmptyLayoutId() != PageManager.NO_LAYOUT_ID || generateEmptyLayout() != null)
            return true;
        return false;
    }


}