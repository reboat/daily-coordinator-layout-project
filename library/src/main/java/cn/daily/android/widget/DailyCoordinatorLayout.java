package cn.daily.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;



public class DailyCoordinatorLayout extends FrameLayout {

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

        if(tabLayout!=0){
            inflate(getContext(),tabLayout, (ViewGroup) findViewById(R.id.tab_container));
        }

        if(contentLayout!=0){
            inflate(getContext(),contentLayout, (ViewGroup) findViewById(R.id.content_container));
        }

        a.recycle();
    }
}
