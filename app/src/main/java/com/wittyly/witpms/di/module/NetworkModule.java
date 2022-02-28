package com.wittyly.witpms.di.module;


import android.content.Context;
import android.support.constraint.solver.Cache;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.model.converter.UserJsonConverter;
import com.wittyly.witpms.repository.CommentRepository;
import com.wittyly.witpms.repository.FeedRepository;
import com.wittyly.witpms.repository.MentionRepository;
import com.wittyly.witpms.repository.UserRepository;
import com.wittyly.witpms.util.Environment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = CacheModule.class)
public class NetworkModule {

    private final Context context;
    private Interceptor authorizationInterceptor;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Retrofit provideRetrofit(final Authorization auth) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(httpLoggingInterceptor);

        if (auth.getAccessToken() != null && !auth.getAccessToken().equals("") && authorizationInterceptor == null) {

            authorizationInterceptor = new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {

                    /*
                     * I took the token in another way
                     * since we cannot access authorization class
                     * inside another thread (Because statement here runs on another thread).
                     * So the best solution is to take the value in a private preferences
                     * rather than initiating another realm instance and query the authorization
                     * again.
                     **/

                    Request newRequest = chain.request()
                            .newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer " + context.getSharedPreferences("private", 0).getString("Token", null))
                            .build();

                    return chain.proceed(newRequest);
                }
            };

            httpClient.addInterceptor(authorizationInterceptor);

        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<User>>(){}.getType(), new UserJsonConverter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Environment.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;

    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public UserRepository provideUserRepository(Retrofit retrofit) {
        return retrofit.create(UserRepository.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public MentionRepository provideMentionRepository(Retrofit retrofit) {
        return retrofit.create(MentionRepository.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public FeedRepository provideFeedRepository(Retrofit retrofit) {
        return retrofit.create(FeedRepository.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public CommentRepository provideCommentRepository(Retrofit retrofit) {
        return retrofit.create(CommentRepository.class);
    }

}
