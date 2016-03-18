package uk.ivanc.archimvp.presenter;

public interface Presenter<V> {
    /**
     * 绑定view.
     * @param view 绑定的view
     */
    void attachView(V view);
    /**
     * 解除view的绑定
     */
    void detachView();

}
