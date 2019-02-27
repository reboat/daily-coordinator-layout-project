package cn.daily.android.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import cn.daily.news.biz.core.nav.Nav;

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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.scrolled_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).toPath("/search/index");
            }
        });

        findViewById(R.id.scrolled_right_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(v.getContext()).toPath("/service/index");
            }
        });
    }
}
