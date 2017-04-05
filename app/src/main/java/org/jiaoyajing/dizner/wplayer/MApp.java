package org.jiaoyajing.dizner.wplayer;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Dizner on 2017/3/14.
 */

public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
