package com.vincent.socket;

import android.app.Application;

/**
 * 项目名称：SocketAndroidClient
 * 类名：com.vincent.socket
 * 类描述：
 * 创建人：Vincent QQ:1032006226
 * 创建时间：2016/10/14 11:09
 * 修改人：
 * 修改时间：
 * 修改备注：
 * 十月
 */

public class MyApplication extends Application {

    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }

    public static MyApplication getApp() {
        return app;
    }
}
