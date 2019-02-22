package cn.daily.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.daily.android.listener.DailyCoordinatorChangeStateListener;


public class DailyCoordinatorLayout extends FrameLayout implements AppBarLayout.OnOffsetChangedListener {
    private DailyCoordinatorChangeStateListener mDailyCoordinatorChangeStateListener;
    private State mCurrentState = State.IDLE;
    private AppBarLayout mAppBarLayout;

    public DailyCoordinatorLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public DailyCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DailyCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.daily_coordinator_layout, this);
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DailyCoordinatorLayout, defStyleAttr, 0);

        int tabLayout = a.getResourceId(R.styleable.DailyCoordinatorLayout_tab, 0);
        int contentLayout = a.getResourceId(R.styleable.DailyCoordinatorLayout_content, 0);

        if (tabLayout != 0) {
            inflate(getContext(), tabLayout, (ViewGroup) findViewById(R.id.tab_container));
        }

        if (contentLayout != 0) {
            inflate(getContext(), contentLayout, (ViewGroup) findViewById(R.id.content_container));
        }

        a.recycle();

        mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                if (mDailyCoordinatorChangeStateListener != null) {
                    mDailyCoordinatorChangeStateListener.onStateChanged(this, State.EXPANDED);
                }
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                if (mDailyCoordinatorChangeStateListener != null) {
                    mDailyCoordinatorChangeStateListener.onStateChanged(this, State.COLLAPSED);
                }
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                if (mDailyCoordinatorChangeStateListener != null) {
                    mDailyCoordinatorChangeStateListener.onStateChanged(this, State.IDLE);
                }
            }
            mCurrentState = State.IDLE;
        }
    }

    public void setDailyCoordinatorChangeStateListener(DailyCoordinatorChangeStateListener dailyCoordinatorChangeStateListener) {
        mDailyCoordinatorChangeStateListener = dailyCoordinatorChangeStateListener;
    }

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
