package cn.daily.android.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.daily.android.widget.R;

public class DailyCoordinatorBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "DailyBehavior";
    private View mScrolledView;
    private View mScrolledAnchorView;
    private View mFixedView;
    private View mHeaderView;
    private View mPaddingView;
    private int mScrolledWidth = 0;
    private int mVerticalDistance = 0;
    private int mRightSpace = 0;
    private int mMaxVerticalDistance = 0;
    private int mFixedWidth = 0;
    private float mMinWidth = 0;
    private int mConsumedY = 0;
    private int mFixedTop = 0;
    private int mSignDistance = 0;
    private float mScale;
    private int[] mAnchorLocation;

    public DailyCoordinatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        if (mVerticalDistance == 0) {
            View searchIcon = ((View) parent.getParent()).findViewById(R.id.right_view);
            int[] location = new int[2];
            int[] location3 = new int[2];
            searchIcon.getLocationOnScreen(location);
            child.getLocationOnScreen(location3);
            mVerticalDistance = location3[1] - location[1];
        }

        if (mMaxVerticalDistance == 0) {
            int[] location = new int[2];
            mScrolledView.getLocationOnScreen(location);
            int[] location2 = new int[2];
            child.findViewById(R.id.fixed_root).getLocationOnScreen(location2);
            mMaxVerticalDistance = location2[1] - location[1];
        }

        if (mFixedTop == 0) {
            Rect rect = new Rect();
            mFixedView.getGlobalVisibleRect(rect);
            mFixedTop = rect.top;
        }


        if (mAnchorLocation == null) {
            mAnchorLocation = new int[2];
            mScrolledAnchorView.getLocationOnScreen(mAnchorLocation);
            int[] location=new int[2];
            mScrolledView.getLocationOnScreen(location);
            mSignDistance=(mAnchorLocation[0]-(location[0]+mScrolledWidth));
        }


        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    boolean up = true;
    int top = mFixedTop;

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//        Log.e(TAG, "into--[onNestedPreScroll]");


        Rect headRect = new Rect();
        mHeaderView.getGlobalVisibleRect(headRect);
        Rect rect = new Rect();
        mFixedView.getGlobalVisibleRect(rect);

        if (rect.top <= top) {
            up = true;
        } else {
            up = false;
        }

        mConsumedY = mFixedTop - rect.top;
        top = rect.top;
        float height = mMaxVerticalDistance - mConsumedY;
        mScale = height / mMaxVerticalDistance;
        ViewGroup.LayoutParams scrolledAnchorViewLayoutParams = mScrolledAnchorView.getLayoutParams();
        scrolledAnchorViewLayoutParams.width = (int) (mMinWidth - mMinWidth * mScale);
        if (mMinWidth * mScale <= 0) {
            scrolledAnchorViewLayoutParams.width = (int) mMinWidth;
        }
        mScrolledView.setTranslationX(mSignDistance-mSignDistance* mScale);
        mScrolledAnchorView.setLayoutParams(scrolledAnchorViewLayoutParams);

        ViewGroup.LayoutParams scrolledViewLayoutParams = mScrolledView.getLayoutParams();
        scrolledViewLayoutParams.width = (int) (mScrolledWidth * mScale);
        if (scrolledViewLayoutParams.width <= mMinWidth) {
            scrolledViewLayoutParams.width = (int) mMinWidth;
        }
        mScrolledView.setLayoutParams(scrolledViewLayoutParams);
        int[] anchorViewLocation = new int[2];
        mScrolledAnchorView.getLocationOnScreen(anchorViewLocation);
        int[] scrollViewLocation = new int[2];
        mScrolledView.getLocationOnScreen(scrollViewLocation);

        if (anchorViewLocation[1] <= scrollViewLocation[1]) {
            mScrolledView.setVisibility(View.INVISIBLE);
            mScrolledAnchorView.setVisibility(View.VISIBLE);
        } else {
            mScrolledView.setVisibility(View.VISIBLE);
            mScrolledAnchorView.setVisibility(View.INVISIBLE);
        }


        if(Build.VERSION.SDK_INT >= 19){
            addPadding(headRect, rect, scrolledAnchorViewLayoutParams);
        }


    }

    private void addPadding(Rect headRect, Rect rect, ViewGroup.LayoutParams scrolledAnchorViewLayoutParams) {
        if (scrolledAnchorViewLayoutParams.width == mMinWidth && up) {
            ViewGroup.LayoutParams paddingViewLayoutParams = mPaddingView.getLayoutParams();
            paddingViewLayoutParams.height++;

            if (headRect.top < 0) {
                paddingViewLayoutParams.height = 63;
            }

            if (paddingViewLayoutParams.height >= 63) {
                paddingViewLayoutParams.height = 63;
                mPaddingView.setLayoutParams(paddingViewLayoutParams);
            }
        }

        if (!up) {
            ViewGroup.LayoutParams paddingViewLayoutParams = mPaddingView.getLayoutParams();
            paddingViewLayoutParams.height--;
            if (rect.top == mFixedTop) {
                paddingViewLayoutParams.height = 0;
            }
            if (paddingViewLayoutParams.height <= 0) {
                paddingViewLayoutParams.height = 0;
                mPaddingView.setLayoutParams(paddingViewLayoutParams);
            }
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
//        Log.e(TAG, "into--[onLayoutChild]");
        mHeaderView = ((View) parent.getParent()).findViewById(R.id.header_view);
        mPaddingView = ((View) parent.getParent()).findViewById(R.id.padding);
        mScrolledView = ((View) parent.getParent()).findViewById(R.id.scrolled_view);
        if (mScrolledWidth == 0) {
            mScrolledWidth = mScrolledView.getMeasuredWidth();
            ((View) mScrolledView.getParent()).bringToFront();
        }
        mScrolledAnchorView = abl.findViewById(R.id.scrolled_anchor_view);
        if (mRightSpace == 0) {
            mRightSpace = ((View) parent.getParent()).findViewById(R.id.scrolled_right_view).getMeasuredWidth();
        }
        mFixedView = abl.findViewById(R.id.fixed_root);
        if (mFixedWidth == 0) {
            mFixedWidth = mFixedView.getMeasuredWidth();
        }

        mMinWidth = parent.getContext().getResources().getDimension(R.dimen.scroll_view_min_width);

        return super.onLayoutChild(parent, abl, layoutDirection);
    }

}
