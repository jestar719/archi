package uk.ivanc.archimvp.view;

import android.content.Context;

/**
 * View的抽象.
 */
public interface MvpView {
    /**
     * 获取上下文
     * @return 上下文
     */
    Context getContext();
}
