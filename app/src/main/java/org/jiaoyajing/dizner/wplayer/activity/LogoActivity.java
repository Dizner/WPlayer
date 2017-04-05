package org.jiaoyajing.dizner.wplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.service.PlayServer;
import org.jiaoyajing.dizner.wplayer.util.EasyDiaLog;


public class LogoActivity extends Activity {
    private static final int START_LOGO = 1;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private Handler handler = new Handler();
    private SharedPreferences first;
    private SharedPreferences.Editor edit;
    private EasyDiaLog easyDiaLog;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            boolean isFirst = first.getBoolean("isFirst", true);
            Intent intent = new Intent();
            if (isFirst) {
                intent.setClass(LogoActivity.this, FirstActivity.class);
                isFirst = false;
                edit.putBoolean("isFirst", isFirst);
                edit.commit();
                startActivity(intent);
                finish();
            } else {
                easyDiaLog.show();
//                intent.setClass(LogoActivity.this, MainActivity.class);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);
        first = getSharedPreferences("isFirst", MODE_PRIVATE);
        edit = first.edit();
        startService(new Intent(this, PlayServer.class));
        easyDiaLog = new EasyDiaLog(this);
        easyDiaLog.setOnBtnClickListener(new EasyDiaLog.OnEasyDialogBtnClickListener() {
            @Override
            public void onLiftBtnClilck(View v) {
                easyDiaLog.cencle();
                finish();
            }

            @Override
            public void onRightBtnClick(View v) {

            }

            @Override
            public void onLoging(boolean isRight) {
                if (isRight) {
                    startActivity(new Intent(LogoActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LogoActivity.this, "登陆失败，请重试", Toast.LENGTH_SHORT).show();
                }
                hideSoftKey();
            }
        });
        handler.postDelayed(r, AUTO_HIDE_DELAY_MILLIS);
    }

    public void hideSoftKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
