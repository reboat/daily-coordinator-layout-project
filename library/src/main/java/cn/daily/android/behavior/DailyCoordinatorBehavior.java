package cn.daily.android.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
    /**
     * 可滑动控件的宽度
     **/
    private int mScrolledWidth = 0;
    /**
     * 可滑动控件距离顶部距离
     */
    private int mVerticalDistance = 0;
    /**
     * 可滑动控件距离右侧距离
     */
    private int mRightSpace = 0;
    /**
     * 可滑动控件总的滑动距离
     */
    private int mMaxVerticalDistance = 0;
    /**
     * 吸顶控件的宽度
     */
    private int mFixedWidth = 0;
    /**
     * 可滑动控件最小宽度
     */
    private float mMinWidth = 0;
    /**
     * 可滑动控件已经滑动的距离
     */
    private int mConsumedY = 0;
    /**
     * 吸顶控件距离顶部的距离
     */
    private int mFixedTop = 0;
    /**
     * 目标控件和签到控件直接的距离
     */
    private int mSignDistance = 0;
    /**
     * 可滑动控件已滑动百分比
     */
    private float mScale;
    /**
     * 目标控件坐标
     */
    private int[] mAnchorLocation;

    /**
     * 背景图标的高度
     */
    private float mHeaderHeight = 0;

    private int mStatusBarHeight = 0;

    public DailyCoordinatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mStatusBarHeight == 0) {
            mStatusBarHeight = getStatusBarHeight(context);
        }
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
            int[] location = new int[2];
            mScrolledView.getLocationOnScreen(location);
            mSignDistance = (mAnchorLocation[0] - (location[0] + mScrolledWidth));
        }


        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
//        Log.e(TAG, "into--[onNestedPreScroll]");

        Rect rect = new Rect();
        mFixedView.getGlobalVisibleRect(rect);

        mConsumedY = mFixedTop - rect.top;
        float height = mMaxVerticalDistance - mConsumedY;
        mScale = height / mMaxVerticalDistance;
        ViewGroup.LayoutParams scrolledAnchorViewLayoutParams = mScrolledAnchorView.getLayoutParams();
        scrolledAnchorViewLayoutParams.width = (int) (mMinWidth - mMinWidth * mScale);
        if (mMinWidth * mScale <= 0) {
            scrolledAnchorViewLayoutParams.width = (int) mMinWidth;
        }
        mScrolledView.setTranslationX(mSignDistance - mSignDistance * mScale);
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


        //透明status bar add padding
        float paddingScale = mConsumedY / mHeaderHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams paddingViewLayoutParams = mPaddingView.getLayoutParams();
            paddingViewLayoutParams.height = (int) (paddingScale * mStatusBarHeight);
        }
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
//        Log.e(TAG, "into--[onLayoutChild]");
        mHeaderView = ((View) parent.getParent()).findViewById(R.id.header_view);
        mPaddingView = ((View) parent.getParent()).findViewById(R.id.padding);
        mScrolledView = ((View) parent.getParent()).findViewById(R.id.scrolled_view);

        if (mHeaderHeight == 0) {
            mHeaderHeight = mHeaderView.getMeasuredHeight();
        }

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


        mMinWidth = parent.getContext().getResources().getDimension(R.dimen.header_scroller_view_min_width);

        return super.onLayoutChild(parent, abl, layoutDirection);
    }

    public int getStatusBarHeight(Context context) {
        int height = 63;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
