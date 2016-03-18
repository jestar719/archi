package uk.ivanc.archimvp.presenter;

import android.util.Log;

import java.util.List;

import retrofit.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import uk.ivanc.archimvp.ArchiApplication;
import uk.ivanc.archimvp.R;
import uk.ivanc.archimvp.model.GithubService;
import uk.ivanc.archimvp.model.Repository;
import uk.ivanc.archimvp.view.MainMvpView;

public class MainPresenter implements Presenter<MainMvpView> {

    public static String TAG = "MainPresenter";

    private MainMvpView mainMvpView;
    private Subscription subscription;
    private List<Repository> repositories;


    @Override
    public void attachView(MainMvpView view) {
        this.mainMvpView = view;
    }

    /**
     * 解除view的绑定,并取消订阅
     */
    @Override
    public void detachView() {
        this.mainMvpView = null;
        if (subscription != null) subscription.unsubscribe();
    }

    /**
     * 根据输入的用户名搜索相应的Githup代码库.
     * 显示搜索结果,如果没搜索到,显示相应的消息
     * 如果出错,显示相应的消息
     * @param usernameEntered 输入的用户名
     */
    public void loadRepositories(String usernameEntered) {
        String username = usernameEntered.trim();
        if (username.isEmpty()) return;
        //显示进度
        mainMvpView.showProgressIndicator();
        //取消之前的订阅(取消之前的任务)
        if (subscription != null) subscription.unsubscribe();
        ArchiApplication application = ArchiApplication.get(mainMvpView.getContext());
        GithubService githubService = application.getGithubService();
        //在io线程中网络加载数据.在主线程中显示.
        subscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "Repos loaded " + repositories);
                        if (!repositories.isEmpty()) {
                            mainMvpView.showRepositories(repositories);
                        } else {
                            mainMvpView.showMessage(R.string.text_empty_repos);
                        }
                    }
                    //连接出错时,如果是404错误.显示用户名没找到.否则显示出了错误
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Error loading GitHub repos ", error);
                        if (isHttp404(error)) {
                            mainMvpView.showMessage(R.string.error_username_not_found);
                        } else {
                            mainMvpView.showMessage(R.string.error_loading_repos);
                        }
                    }

                    @Override
                    public void onNext(List<Repository> repositories) {
                        MainPresenter.this.repositories = repositories;
                    }
                });
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }

}
