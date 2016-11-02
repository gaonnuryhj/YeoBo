package com.example.danbilap.project_yeobo;

import android.app.Application;

/**
 * Created by jo-eunseul on 2016. 6. 10..
 */
public class MyApp extends Application {
    private static MyApp app;

    public static MyApp getApp(){
        return app;
    }

    private Login login;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
