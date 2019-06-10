package cn.daily.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.daily.android.listener.DailyCoordinatorChangeStateListener;


public class DailyCoordinatorLayout extends CoordinatorLayout implements AppBarLayout.OnOffsetChangedListener {
    private DailyCoordinatorChangeStateListener mDailyCoordinatorChangeStateListener;
    private State mCurrentState = State.IDLE;
    private AppBarLayout mAppBarLayout;
    private String mTitle;
    private int mTabMoreVisibility;
    private int mSignVisibility;

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
        inflate(getContext(), R.layout.header_content_layout, this);
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DailyCoordinatorLayout, defStyleAttr, 0);

        int tabLayout = a.getResourceId(R.styleable.DailyCoordinatorLayout_tab, 0);
        if (tabLayout != 0) {
            inflate(getContext(), tabLayout, (ViewGroup) findViewById(R.id.tab_container));
        }
        mTitle = a.getString(R.styleable.DailyCoordinatorLayout_title);
        int visibility = a.getInt(R.styleable.DailyCoordinatorLayout_tab_more_visibility, 1);
        mTabMoreVisibility = visibility == 1 ? VISIBLE : GONE;

        visibility = a.getInt(R.styleable.DailyCoordinatorLayout_sign_visibility, 0);
        mSignVisibility = visibility == 1 ? VISIBLE : GONE;
        a.recycle();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mAppBarLayout.setExpanded(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        findViewById(R.id.tab_more).setVisibility(mTabMoreVisibility);
        findViewById(R.id.header_sign_container).setVisibility(mSignVisibility);
        TextView titleView = findViewById(R.id.title);
        titleView.setText(TextUtils.isEmpty(mTitle) ? "新闻" : mTitle);
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

    public void setTabMoreVisibility(int visibility) {
        mTabMoreVisibility = visibility;
    }

    public void updateTabMoreVisibility(int visibility) {
        findViewById(R.id.tab_more).setVisibility(visibility);
    }


    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
