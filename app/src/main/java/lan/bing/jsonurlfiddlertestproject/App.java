package lan.bing.jsonurlfiddlertestproject;

import android.app.Application;

import lan.bing.utils.AppUtil;

/**
 * Created by 520 on 2017/1/19.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.initGlobal(getApplicationContext());
    }
}
