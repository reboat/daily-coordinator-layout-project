package cn.daily.android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.transition.Visibility;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
    private ViewFlipper mHotWordContainer;
    private OnHeaderResourceChangeListener mOnHeaderResourceChangeListener;

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mUpdateTipView.setSelected(false);
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
        inflate(context, R.layout.header_scroller_layout, this);
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
        mUpdateTipView = findViewById(R.id.scrolled_right_icon_view);
        mHotWordContainer = findViewById(R.id.hot_word_container);
        findViewById(R.id.scrolled_right_view).setOnClickListener(new OnClickListener() {
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
        findViewById(R.id.scrolled_anchor_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Nav.with(getContext()).toPath("/news/SearchActivity");
            }
        });

        updateInfo();
    }

    private void updateInfo() {
        Context context = getContext();
        if (context instanceof OnHeaderResourceChangeListener) {
            mOnHeaderResourceChangeListener = (OnHeaderResourceChangeListener) context;
            mOnHeaderResourceChangeListener.onHeaderResourceChange()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HeaderResource>() {
                        @Override
                        public void accept(HeaderResource headerResource) throws Exception {
                            GlideApp.with(getContext()).load(headerResource.headerBackgroundUrl).into(mHeaderImageView);
                            boolean update = SettingManager.getInstance().getServiceVersion() < headerResource.serviceVersion ? true : false;
                            mUpdateTipView.setSelected(update);
                            mServiceVersion = headerResource.serviceVersion;
                            if (headerResource.hot_word_list != null && headerResource.hot_word_list.size() > 0) {
                                for (int i = 0; i < headerResource.hot_word_list.size(); i++) {
                                    TextView hotWordView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.header_hot_word_item, mHotWordContainer, false);
                                    hotWordView.setText(headerResource.hot_word_list.get(i).content);
                                    mHotWordContainer.addView(hotWordView, i);
                                    mHotWordContainer.setFlipInterval(4000);
                                }
                            } else {
                                TextView hotWordView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.header_hot_word_item, mHotWordContainer, false);
                                mHotWordContainer.addView(hotWordView);
                                mHotWordContainer.setAutoStart(false);
                                mHotWordContainer.stopFlipping();
                            }
                        }
                    });
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateReceiver, new IntentFilter(UPDATE_SERVICE_STATE));
    }

    public ImageView getSignIconView() {
        return findViewById(R.id.header_sign_icon);
    }

    public TextView getSignTextView() {
        return findViewById(R.id.header_sign_text);
    }

    public void setSignVisibility(@Visibility.Mode int visibility) {
        findViewById(R.id.header_sign_container).setVisibility(visibility);
    }

    public void setOnSignClickListener(View.OnClickListener listener) {
        if (listener != null) {
            findViewById(R.id.header_sign_container).setOnClickListener(listener);
        }
    }
}
