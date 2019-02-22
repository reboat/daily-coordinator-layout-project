package cn.daily.android.listener;


import cn.daily.android.widget.DailyCoordinatorLayout;

public interface DailyCoordinatorChangeStateListener {
    void onStateChanged(DailyCoordinatorLayout coordinatorLayout, DailyCoordinatorLayout.State state);
}
