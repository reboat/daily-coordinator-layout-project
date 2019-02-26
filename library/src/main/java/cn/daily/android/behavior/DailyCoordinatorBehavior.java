package cn.daily.android.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cn.daily.android.widget.R;

public class DailyCoordinatorBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "DailyBehavior";
    private View mHeaderView;
    private View mScrolledView;
    private View mScrolledAnchorView;
    private int mScrolledWidth = 0;
    private int mVerticalDistance = 0;
    private int mRightSpace = 0;
    private int mMaxVerticalDistance = 0;
    private int mFixedWidth =0;

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
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        Rect rect = new Rect();
        mHeaderView.getLocalVisibleRect(rect);
        float height = mMaxVerticalDistance - rect.top;
        float scale = height / mMaxVerticalDistance;


        ViewGroup.LayoutParams params1= mScrolledAnchorView.getLayoutParams();
        params1.width= (int) (100-100*scale);
        Log.e(TAG,"width:"+params1.width);
        mScrolledAnchorView.setLayoutParams(params1);

        ViewGroup.LayoutParams params = mScrolledView.getLayoutParams();
        params.width = (int) (mScrolledWidth * scale);
        if (params.width <= 100) {
            params.width = 100;
        }
        mScrolledView.setLayoutParams(params);
        mScrolledView.setTranslationY(mVerticalDistance - mVerticalDistance * scale);
        mScrolledView.setTranslationX(mRightSpace - mRightSpace * scale);
        if (rect.top < 0) {
            mScrolledAnchorView.setVisibility(View.VISIBLE);
            mScrolledView.setVisibility(View.GONE);
        } else {
            mScrolledAnchorView.setVisibility(View.INVISIBLE);
            mScrolledView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        Log.e(TAG, "into--[onLayoutChild]");
        mHeaderView = ((View) parent.getParent()).findViewById(R.id.header_view);
        if (mMaxVerticalDistance == 0) {
            mMaxVerticalDistance = mHeaderView.getMeasuredHeight();
        }
        mScrolledView = ((View) parent.getParent()).findViewById(R.id.scrolled_view);
        if (mScrolledWidth == 0) {
            mScrolledWidth = mScrolledView.getMeasuredWidth();
            ((View)mScrolledView.getParent()).bringToFront();
        }
        mScrolledAnchorView = abl.findViewById(R.id.scrolled_anchor_view);
        if (mRightSpace == 0) {
            mRightSpace = ((View) parent.getParent()).findViewById(R.id.scrolled_right_view).getMeasuredWidth();
        }
        if(mFixedWidth ==0){
            mFixedWidth =abl.findViewById(R.id.fixed_root).getMeasuredWidth();
        }


        return super.onLayoutChild(parent, abl, layoutDirection);
    }

}
