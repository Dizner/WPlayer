package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.AboutActivity;
import org.jiaoyajing.dizner.wplayer.activity.MainActivity;
import org.jiaoyajing.dizner.wplayer.adapter.MainPagerAdapter;
import org.jiaoyajing.dizner.wplayer.util.DataCleanManager;
import org.jiaoyajing.dizner.wplayer.util.Mp3Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dizner on 2017/3/14.
 */

public class MainFragment extends Fragment implements View.OnClickListener, MainActivity.updateTime {
    @BindView(R.id.tv_btn_menu)
    TextView tvBtnMenu;
    @BindView(R.id.tl_title)
    TabLayout tabs;
    @BindView(R.id.ll_btn_search)
    LinearLayout llBtnSearch;
    @BindView(R.id.vp_fragment)
    ViewPager pager;


    private SlidingMenu menu;
    private HomeFragment homeFragment;
    private NetMusicFragment netMusicFragment;

    private MainPagerAdapter adapter;
    private List<Fragment> list;
    private MainActivity mainActivity;
    private TextView size, time;
    private AlertDialog dialog;
    private long l;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("是否", "____11111___" + mainActivity.dos);
        mainActivity.dos = mainActivity.FRAGMENT_TAG_MAIN;
    }

    @OnClick({R.id.tv_btn_menu, R.id.ll_btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_menu:
                menu.showMenu();
                break;
            case R.id.ll_btn_search:
                mainActivity.switchFragment(mainActivity.FRAGMENT_TAG_SEARCH);
                break;
            //清理缓存
            case R.id.ll_btn_clear:
                DataCleanManager.cleanCache(getActivity());
                Toast.makeText(getContext(), "缓存已清理完成", Toast.LENGTH_SHORT).show();
                try {
                    String size1 = DataCleanManager.getCacheSize(getContext());
                    size.setText(size1);
                } catch (Exception e) {
//            e.printStackTrace();
                }
                break;
            //夜间模式（暂无）
            case R.id.ll_btn_night:
                break;
            //定时关闭
            case R.id.ll_btn_at_time:
                if (mainActivity.playService != null && mainActivity.playService.isPlaying()) {
                    dialog.show();
                } else {
                    Toast.makeText(getContext(), "未播放状态，不能设置", Toast.LENGTH_SHORT).show();
                }
                break;
            //关于
            case R.id.ll_btn_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            //退出
            case R.id.ll_btn_exit:
                exitApp();
                break;
        }
    }


    public void exitApp() {
        mainActivity.unbindPlayService();
        mainActivity.playService.unRegisterRe();
        mainActivity.finish();
        System.exit(0);
    }

    private void initView() {

        homeFragment = HomeFragment.newInstance();
        netMusicFragment = NetMusicFragment.newInstance();
//        fragmentList.add(homeFragment);
        list = new ArrayList<>();
        list.add(homeFragment);
        list.add(netMusicFragment);
        adapter = new MainPagerAdapter(getActivity().getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        tabs.setTabsFromPagerAdapter(adapter);
        menu = new SlidingMenu(getActivity());
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffset(getActivity().getWindowManager().getDefaultDisplay().getWidth() / 4 * 1);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.layout_menu);
//        menu.findViewById()
        LinearLayout btnClear = (LinearLayout) menu.findViewById(R.id.ll_btn_clear);
        btnClear.setOnClickListener(this);
        LinearLayout btnNight = (LinearLayout) menu.findViewById(R.id.ll_btn_night);
        LinearLayout btnAtTime = (LinearLayout) menu.findViewById(R.id.ll_btn_at_time);
        LinearLayout btnAbout = (LinearLayout) menu.findViewById(R.id.ll_btn_about);
        LinearLayout btnExit = (LinearLayout) menu.findViewById(R.id.ll_btn_exit);
        size = (TextView) menu.findViewById(R.id.tv_cache_size);
        time = (TextView) menu.findViewById(R.id.tv_timer);
        try {
            String size1 = DataCleanManager.getCacheSize(getContext());
            size.setText(size1);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        btnNight.setOnClickListener(this);
        btnAtTime.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnExit.setOnClickListener(this);


        //初始化dialoag
        LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog, null, false);
        final EditText et = (EditText) view.findViewById(R.id.et_input);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("请输入定时时间(分钟)");
        TextView tvBtnOk = (TextView) view.findViewById(R.id.tv_btn_ok);
        tvBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et.getText())) {
                    String s = et.getText().toString();
                    l = Long.parseLong(s);
                    if (l <= 90) {
                        time.setText(Mp3Utils.formatTime(Long.parseLong(s) * 60 * 1000));
                        mainActivity.playService.setTimer(Long.parseLong(s) * 60 * 1000);
                        et.setText("");
                        dialog.cancel();
                    } else {
                        Toast.makeText(getActivity(), "最大时长90分钟", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        TextView tvBtnCancle = (TextView) view.findViewById(R.id.tv_btn_cancle);
        tvBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long obj = (long) msg.obj;
                    time.setText(Mp3Utils.formatTime(obj));
                    break;
            }

        }
    };

    @Override
    public void update(long time) {
        Message message = handler.obtainMessage();
        message.obj = time;
        message.what = 1;
        handler.sendMessage(message);
        if (time == 0) {
            exitApp();
        }
    }

}
