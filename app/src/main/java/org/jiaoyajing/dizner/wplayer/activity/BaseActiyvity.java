package org.jiaoyajing.dizner.wplayer.activity;


import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.service.PlayServer;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseActiyvity extends FragmentActivity {
    public PlayServer playService;
    private boolean isBound = false;
    private OnBindSuccess onBind;
    private static List<BaseActiyvity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
//		startupDateUi();
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playService = null;
            isBound = false;
            Log.d("service", "解除绑定成功");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayServer.PlayBinder playBinder = (PlayServer.PlayBinder) service;
            playService = playBinder.getPlayServer();

            playService.setMusicUpdateListener(musicUpdateListener);
//			playService.startEs();
//			musicUpdateListener.onChange(playService.getCurrentPosition());
            playService.startupDateUi();
            if (onBind != null) {
                onBind.bindSuccess();
            }
            Log.d("service", "绑定成功");
        }
    };


    public interface OnBindSuccess {
        void bindSuccess();
    }

    private PlayServer.MusicUpdateListener musicUpdateListener = new PlayServer.MusicUpdateListener() {

        @Override
        public void onPublish(int progress, Mp3Info mp3) {
            for (BaseActiyvity actiyvity : list) {
                actiyvity.publish(progress, mp3);
            }
//            publish(progress, mp3);
        }

        @Override
        public void onChange(int position) {
            for (BaseActiyvity actiyvity : list) {
                actiyvity.change(position);
            }
            change(position);
        }

        @Override
        public void onPublish(long timer) {
            for (BaseActiyvity actiyvity : list) {
                actiyvity.upTime(timer);
            }
            upTime(timer);
        }
    };

    public abstract void publish(int progress, Mp3Info mp3);

    public abstract void upTime(long timer);

    public abstract void change(int position);

    public abstract void getSelf(List<BaseActiyvity> list);
    public abstract void rmSelf(List<BaseActiyvity> list);

    public void bindPlayService(OnBindSuccess onBind) {
        this.onBind = onBind;
        getSelf(list);
        Log.d("Activity数量", list.size() + "");
        if (!isBound) {
            Intent intent = new Intent(this, PlayServer.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    public void bindPlayService() {
        getSelf(list);
        Log.d("Activity数量", list.size() + "");
        if (!isBound) {
            Intent intent = new Intent(this, PlayServer.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    public void unbindPlayService() {//
        rmSelf(list);
        if (isBound) {
            unbindService(conn);
            if (playService != null) {

                Log.d("service", "解除绑定成功");
            }
            isBound = false;
        }
    }

    public void hideSoftKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        for (BaseActiyvity actiyvity : list) {
//            actiyvity = null;
//        }
//        list = null;
    }
}
