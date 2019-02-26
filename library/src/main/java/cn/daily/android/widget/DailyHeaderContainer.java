package cn.daily.android.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class DailyHeaderContainer extends FrameLayout {

    public DailyHeaderContainer( Context context) {
        super(context);
        init(context,null,0);
    }

    public DailyHeaderContainer( Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public DailyHeaderContainer( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context,R.layout.search_layout,this);
    }
}
