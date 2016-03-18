package uk.ivanc.archimvp.view;

import java.util.List;

import uk.ivanc.archimvp.model.Repository;

/**
 * 主View的抽象.用与显示相关数据
 */
public interface MainMvpView extends MvpView {
    /**
     * 显示代码库
     * @param repositories 代码库集合
     */
    void showRepositories(List<Repository> repositories);

    /**
     * 显示消息
     * @param stringId String的资源文件id
     */
    void showMessage(int stringId);

    /**
     * 显示进度指示器
     */
    void showProgressIndicator();
}
