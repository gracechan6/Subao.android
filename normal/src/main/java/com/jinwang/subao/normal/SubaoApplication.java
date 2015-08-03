package com.jinwang.subao.normal;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by michael on 15/7/31.
 */
public class SubaoApplication extends Application {
    private AsyncHttpClient sharedHttpClient;

    /**
     *
     * @return
     */
    public AsyncHttpClient getSharedHttpClient()
    {
        return sharedHttpClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedHttpClient = new AsyncHttpClient();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
