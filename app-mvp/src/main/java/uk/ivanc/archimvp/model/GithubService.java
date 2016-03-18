package uk.ivanc.archimvp.model;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Url;
import rx.Observable;

public interface GithubService {
    /**
     *根据用户名从github上获取相应的代码库集合
     * @param username 用户名字段
     * @return 提供Repository集合的Observable
     */
    @GET("users/{username}/repos")
    Observable<List<Repository>> publicRepositories(@Path("username") String username);

    /**
     * 根据用户的url从github上获取用户信息
     * @param userUrl 用户的链接
     * @return 提供User的Observerable
     */
    @GET
    Observable<User> userFromUrl(@Url String userUrl);

    /**
     * Retrofit的简单工厂.创建Retrofit连接Githup
     */
    class Factory {
        public static GithubService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GithubService.class);
        }
    }
}
