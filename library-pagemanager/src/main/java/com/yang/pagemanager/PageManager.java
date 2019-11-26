package com.yang.pagemanager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/**
 * Created by yang on 2019/11/26.
 */
public class PageManager {
    public static final int NO_LAYOUT_ID = 0;
    public static int BASE_LOADING_LAYOUT_ID;//= R.layout.pager_loading
    public static int BASE_RETRY_LAYOUT_ID;//= R.layout.pager_error
    public static int BASE_EMPTY_LAYOUT_ID;//= R.layout.pager_empty
    public PageLayout mLoadingAndRetryLayout;

    /**
     * @param appContext
     * @param layoutIdOfEmpty
     * @param layoutIdOfLoading
     * @param layoutIdOfError
     */
    public static void initInApp(Context appContext, int layoutIdOfEmpty, int layoutIdOfLoading, int layoutIdOfError) {
        if (layoutIdOfEmpty > 0) {
            BASE_EMPTY_LAYOUT_ID = layoutIdOfEmpty;
        }
        if (layoutIdOfLoading > 0) {
            BASE_LOADING_LAYOUT_ID = layoutIdOfLoading;
        }
        if (layoutIdOfError > 0) {
            BASE_RETRY_LAYOUT_ID = layoutIdOfError;
        }
    }

    public void showLoading() {
        mLoadingAndRetryLayout.showLoading();
    }

    public void showError() {
        mLoadingAndRetryLayout.showRetry();
    }

    public void showContent() {
        mLoadingAndRetryLayout.showContent();
    }

    public void showEmpty() {
        mLoadingAndRetryLayout.showEmpty();
    }

    public PageListener DEFAULT_LISTENER = new PageListener() {
        @Override
        public void onRetry(View retryView) {

        }
    };

    private PageManager(Object activityOrView, boolean showLoadingFirstIn, PageListener listener) {
        if (listener == null) listener = DEFAULT_LISTENER;

        ViewGroup contentParent = null;
        Context context;
        if (activityOrView instanceof Activity) {
            Activity activity = (Activity) activityOrView;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (activityOrView instanceof Fragment) {

            Fragment fragment = (Fragment) activityOrView;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
            if (contentParent == null) {
                throw new IllegalArgumentException("the fragment must already has a parent ,please do not invoke this in oncreateView,you should use this method in onActivityCreated() or onstart");
            }

            //throw new IllegalArgumentException("the support for fragment has been canceled,please use give me a view object which has a parent");

        } else if (activityOrView instanceof View) {
            View view = (View) activityOrView;
            contentParent = (ViewGroup) (view.getParent());
            if (contentParent == null) {
                throw new IllegalArgumentException("the view must already has a parent ");
            }
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the container's type must be Fragment or Activity or a view ");
        }


        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        View oldContent;
        if (activityOrView instanceof View) {
            oldContent = (View) activityOrView;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        //setup content layout
        PageLayout pageLayout = new PageLayout(context);

        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(pageLayout, index, lp);
        pageLayout.setContentView(oldContent);
        setupLoadingLayout(listener, pageLayout);
        setupRetryLayout(listener, pageLayout);
        setupEmptyLayout(listener, pageLayout);
        final PageListener finalListener = listener;
        pageLayout.getRetryView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalListener != null) {
                    finalListener.onRetry(v);
                }

            }
        });
        pageLayout.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalListener != null) {
                    finalListener.onEmtptyViewClicked(v);
                }
            }
        });
        mLoadingAndRetryLayout = pageLayout;
        //初始状态:loading进去
        if (showLoadingFirstIn) {
            mLoadingAndRetryLayout.showLoading();
        } else {
            mLoadingAndRetryLayout.showContent();
        }

    }

    private void setupEmptyLayout(PageListener listener, PageLayout loadingAndRetryLayout) {
        if (listener.isSetEmptyLayout()) {
            int layoutId = listener.generateEmptyLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setEmptyView(layoutId);
            } else {
                loadingAndRetryLayout.setEmptyView(listener.generateEmptyLayout());
            }
        } else {
            if (BASE_EMPTY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setEmptyView(BASE_EMPTY_LAYOUT_ID);
        }
    }

    private void setupLoadingLayout(PageListener listener, PageLayout loadingAndRetryLayout) {
        if (listener.isSetLoadingLayout()) {
            int layoutId = listener.generateLoadingLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setLoadingView(layoutId);
            } else {
                loadingAndRetryLayout.setLoadingView(listener.generateLoadingLayout());
            }
        } else {
            if (BASE_LOADING_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setLoadingView(BASE_LOADING_LAYOUT_ID);
        }
    }

    private void setupRetryLayout(PageListener listener, PageLayout loadingAndRetryLayout) {
        if (listener.isSetRetryLayout()) {
            int layoutId = listener.generateRetryLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setRetryView(layoutId);
            } else {
                loadingAndRetryLayout.setRetryView(listener.generateRetryLayout());
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setRetryView(BASE_RETRY_LAYOUT_ID);
        }
    }

    public static PageManager generate(Object activityOrView, boolean showLoadingFirstIn, PageListener listener) {
        return new PageManager(activityOrView, showLoadingFirstIn, listener);
    }
}
