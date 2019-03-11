package cn.daily.android.listener;

import cn.daily.android.model.HeaderResource;
import io.reactivex.Flowable;

public interface OnHeaderResourceChangeListener {
    Flowable<HeaderResource> onHeaderResourceChange();
}
