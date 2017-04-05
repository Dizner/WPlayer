package org.jiaoyajing.dizner.wplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.adapter.MyAdapter;
import org.jiaoyajing.dizner.wplayer.util.RegisterDiaLog;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends Activity {
    private MyAdapter adapter;
    private ViewPager vpShow;
    private ImageButton imgbtn;
    private List<View> list = new ArrayList<View>();
    private RegisterDiaLog registerDiaLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first);
//		x.Ext.init(getApplication());
        vpShow = (ViewPager) findViewById(R.id.vpShow);
        setData();
    }

    private void setData() {
        ImageView iv1 = new ImageView(FirstActivity.this);
        ImageView iv2 = new ImageView(FirstActivity.this);
        ImageView iv3 = new ImageView(FirstActivity.this);
        registerDiaLog = new RegisterDiaLog(this);
        registerDiaLog.setOnBtnClickListener(new RegisterDiaLog.OnEasyDialogBtnClickListener() {
            @Override
            public void onLiftBtnClilck(View v) {
                registerDiaLog.cencle();
                finish();
            }

            @Override
            public void onRightBtnClick(View v) {

            }

            @Override
            public void onLoging(boolean isRight) {
                if (isRight) {
                    startActivity(new Intent(FirstActivity.this, MainActivity.class));
                    registerDiaLog.cencle();
                    finish();
                } else {
                    Toast.makeText(FirstActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
                }
                hideSoftKey();
            }
        });
        View iv4 = LayoutInflater.from(this).inflate(R.layout.welcom_04, null);
        imgbtn = (ImageButton) iv4.findViewById(R.id.imbtn);
        imgbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                registerDiaLog.show();
            }
        });
        iv1.setScaleType(ScaleType.FIT_XY);
        iv1.setImageResource(R.mipmap.welcome_01);
        list.add(iv1);
        iv2.setScaleType(ScaleType.FIT_XY);
        iv2.setImageResource(R.mipmap.welcome_02);
        list.add(iv2);
        iv3.setScaleType(ScaleType.FIT_XY);
        iv3.setImageResource(R.mipmap.welcome_03);
        list.add(iv3);
        list.add(iv4);
        adapter = new MyAdapter(list);
        vpShow.setAdapter(adapter);
    }

    public void hideSoftKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
