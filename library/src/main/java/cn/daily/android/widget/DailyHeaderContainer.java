package cn.daily.android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zjrb.core.common.glide.GlideApp;

import cn.daily.android.listener.OnHeaderResourceChangeListener;
import cn.daily.android.model.HeaderResource;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.nav.Nav;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class DailyHeaderContainer extends FrameLayout {
    public static final String UPDATE_SERVICE_STATE = "update_service_state";

    private ImageView mHeaderImageView;
    private View mUpdateTipView;
    private OnHeaderResourceChangeListener mOnHeaderResourceChangeListener;

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mUpdateTipView.setVisibility(GONE);
        }
    };
    private long mServiceVersion = -1;

    public DailyHeaderContainer(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DailyHeaderContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DailyHeaderContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.search_layout, this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Context context = getContext();
        if (context instanceof OnHeaderResourceChangeListener) {
            mOnHeaderResourceChangeListener = (OnHeaderResourceChangeListener) context;
            mOnHeaderResourceChangeListener.onHeaderResourceChange()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HeaderResource>() {
                        @Override
                        public void accept(HeaderResource headerResource) throws Exception {
                            GlideApp.with(getContext()).load(headerResource.headerBackgroundUrl).into(mHeaderImageView);

                            int visibility=SettingManager.getInstance().getServiceVersion()<headerResource.serviceVersion?VISIBLE:GONE;
                            mUpdateTipView.setVisibility(visibility);
                            mServiceVersion = headerResource.serviceVersion;
                        }
                    });
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateReceiver, new IntentFilter(UPDATE_SERVICE_STATE));
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateReceiver);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeaderImageView = findViewById(R.id.header_view);
        mUpdateTipView = findViewById(R.id.header_service_update_tip);

        mUpdateTipView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(getContext()).toPath("/service");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(UPDATE_SERVICE_STATE));
                if (mServiceVersion != -1) {
                    SettingManager.getInstance().setServiceVersion(mServiceVersion);
                }
            }
        });

        findViewById(R.id.scrolled_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(getContext()).toPath("/news/SearchActivity");
            }
        });

        findViewById(R.id.header_sign).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(getContext()).toPath("/sign/index");
            }
        });
    }
}
