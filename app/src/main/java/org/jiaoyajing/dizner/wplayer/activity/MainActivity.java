package org.jiaoyajing.dizner.wplayer.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.adapter.MainPagerAdapter;
import org.jiaoyajing.dizner.wplayer.fragment.AllListFragment;
import org.jiaoyajing.dizner.wplayer.fragment.HomeFragment;
import org.jiaoyajing.dizner.wplayer.fragment.LocalListFragment;
import org.jiaoyajing.dizner.wplayer.fragment.MainFragment;
import org.jiaoyajing.dizner.wplayer.fragment.NetMusicFragment;
import org.jiaoyajing.dizner.wplayer.fragment.SearchFragment;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.Mp3Utils;
import org.jiaoyajing.dizner.wplayer.util.ThemeImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActiyvity implements ThemeImpl {

//    @BindView(R.id.tl_title)
//    TabLayout tabs;
//    @BindView(R.id.vp_fragment)
//    ViewPager pager;

    private List<Fragment> fragmentList;
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private final Handler handler = new Handler();
    private static updateTime up;
    //    @BindView(R.id.tv_btn_menu)
//    TextView tvBtnMenu;
//    @BindView(R.id.ll_btn_search)
//    LinearLayout llBtnSearch;
    @BindView(R.id.songimg)
    ImageView songimg;
    @BindView(R.id.musicname)
    TextView musicname;
    @BindView(R.id.authorname)
    TextView authorname;
    @BindView(R.id.prebtu)
    ImageView prebtu;
    @BindView(R.id.startbtu)
    ImageView startbtu;
    @BindView(R.id.nextbtu)
    ImageView nextbtu;
    @BindView(R.id.colors)
    RelativeLayout colors;

    //    public static MainActivity instance = null;
    private List<Mp3Info> mp3Infos;
    private MainPagerAdapter adapter;
    private NotificationCompat.Builder builder = null;
    private Notification notification = null;
    private RemoteViews contentView;
    private NotificationManager manager = null;
    private Drawable oldBackground = null;
    private int currentColor = 0xFF666666;
    private HomeFragment homeFragment;
    private NetMusicFragment netMusicFragment;
    private List<Fragment> list;
    private SlidingMenu menu;
    private int position;
    public String dos;
    private FragmentTransaction transaction;
    public String FRAGMENT_TAG_MAIN = "main";
    public String FRAGMENT_TAG_LOCALLOST = "locallist";
    public String FRAGMENT_TAG_SEARCH = "search";
    public String FRAGMENT_TAG_LIKE_LIST = "like_list";
    public String FRAGMENT_TAG_HISTORY_LIST = "history";
    public String FRAGMENT_TAG_ALLLIST_LIST = "alllist_list";
    private static long time;
    private MainFragment mainFragment;
//    private int FRAGMENT_TAG_LOCALLOST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();

        Log.d("", (Mp3Utils.getMp3Info(this, "all").size() < 0) + "");
        if (Mp3Utils.getMp3Info(this, "all").size() <= 0) {
            Mp3Utils.scanMp3Info(MainActivity.this, new Mp3Utils.OnScanOverLinister() {
                @Override
                public void onScanOver(int count) {
                    Log.d("扫描数量", count + "");
                    Log.d("扫描数量2", Mp3Utils.getMp3Info(MainActivity.this).size() + "");
                    bindPlayService();

                }
            });
        } else {
            bindPlayService();
        }
//        initNotificationBtn();
    }


    private void initData() {
        mp3Infos = new ArrayList<>();
        mp3Infos.addAll(mp3Infos = Mp3Utils.getMp3Info(this));
    }

    private void initView() {
        fragmentList = new ArrayList<>();
        Animation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        songimg.setAnimation(an);

//        homeFragment = HomeFragment.newInstance();
//        netMusicFragment = NetMusicFragment.newInstance();
        mainFragment = new MainFragment();
        up = mainFragment;
        LocalListFragment localListFragment = new LocalListFragment();
        LocalListFragment likeListFragment = new LocalListFragment();
        Bundle argslike = new Bundle();
        argslike.putString("tag", "like");
        likeListFragment.setArguments(argslike);
        LocalListFragment historyListFragment = new LocalListFragment();
        Bundle argshistory = new Bundle();
        argshistory.putString("tag", "history");
        historyListFragment.setArguments(argshistory);
        SearchFragment searchFragment = new SearchFragment();
        AllListFragment allListFragment = new AllListFragment();
        fragmentMap.put(FRAGMENT_TAG_MAIN, mainFragment);
        fragmentMap.put(FRAGMENT_TAG_LOCALLOST, localListFragment);
        fragmentMap.put(FRAGMENT_TAG_SEARCH, searchFragment);
        fragmentMap.put(FRAGMENT_TAG_LIKE_LIST, likeListFragment);
        fragmentMap.put(FRAGMENT_TAG_HISTORY_LIST, historyListFragment);
        fragmentMap.put(FRAGMENT_TAG_ALLLIST_LIST, allListFragment);
//        fragmentList.add(mainFragment);
//        fragmentList.add(localListFragment);
//        fragmentList.add(searchFragment);
//        fragmentList.add(likeListFragment);
//        fragmentList.add(historyListFragment);
//        fragmentList.add(allListFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.ll_tmp, fragmentMap.get(FRAGMENT_TAG_MAIN)).commit();
//        list = new ArrayList<>();
//        list.add(homeFragment);
//        list.add(netMusicFragment);
//        adapter = new MainPagerAdapter(getSupportFragmentManager(), list);
//        pager.setAdapter(adapter);
//        tabs.setupWithViewPager(pager);
//        tabs.setTabsFromPagerAdapter(adapter);
//        menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.LEFT);
//        // 设置触摸屏幕的模式
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//
//        // 设置滑动菜单视图的宽度
//        menu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 5 * 1);
//        // 设置渐入渐出效果的值
//        menu.setFadeDegree(0.35f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        //为侧滑菜单设置布局
//        menu.setMenu(R.layout.activity_play);

    }


    private Handler myhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this != null) {
                switch (msg.what) {
                    case 1:
                        Mp3Info obj = (Mp3Info) msg.obj;
                        musicname.setText(obj.getTitle());
                        authorname.setText(obj.getArtist());
//                        Bundle args = new Bundle();
//                        args.putLong("time",time);
//                        mainFragment.setArguments(args);
                        if (MainActivity.this != null) {
                            Glide.with(MainActivity.this).load(obj.getSongPic()).into(songimg);
                        }
                        if (playService.isPlaying()) {
                            startbtu.setImageResource(R.mipmap.puse_01);
                        } else {
                            startbtu.setImageResource(R.mipmap.start_01);
                        }
                        break;
                    case 2:
                        long obj1 = (long) msg.obj;
                        up.update(obj1);
                        break;
                }
            }
        }
    };

    @Override
    public void publish(int progress, Mp3Info mp3) {
        // 更新进度条
        Log.d("Main进度", progress + "");
        Message msg = myhandler.obtainMessage(1);
        msg.arg1 = progress;
        msg.obj = mp3;
        myhandler.sendMessage(msg);
    }

    @Override
    public void upTime(long timer) {
        Log.d("倒计时进度", timer + "");
//        time = timer;
        Message msg = myhandler.obtainMessage(2);
        msg.obj = timer;
        myhandler.sendMessage(msg);
    }

    @Override
    public void change(int position) {
        //更新播放位置
        Log.d("播放位置", String.valueOf(position));
//        changeUIStatusOnPlay(position);
    }

    @Override
    public BaseActiyvity getSelf() {
        return this;
    }

    @OnClick({R.id.prebtu, R.id.startbtu, R.id.nextbtu, R.id.songimg})
    public void onClick(View view) {
        switch (view.getId()) {
            //上一曲
            case R.id.prebtu:
                playService.prev();
                ThemeImpl theme = this;
                theme.changeTheme(THEME_MODE_NIGHT);
                break;
            //开始／puse_01
            case R.id.startbtu: {
                if (playService != null) {
                    if (playService.isPlaying()) {
                        startbtu.setImageResource(R.mipmap.start_01);
                        playService.pause();
                    } else {
                        //播放
                        if (playService.isPause()) {
                            startbtu.setImageResource(R.mipmap.puse_01);
                            playService.start();
                        } else {
                            playService.play(0);
                        }
                    }
                }

            }

            break;
            //下一曲
            case R.id.nextbtu:
                playService.next();
                break;
            //下一曲
            case R.id.songimg:
                Intent intent = new Intent(this, PlayActivity.class);
//			intent.putExtra("isPause", isPause);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        bindPlayService();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("main", "销毁");
        unbindPlayService();
//        instance = null;
    }

//    public void changeUIStatusOnPlay(int position) {
//        if (position >= 0 && position <= mp3Infos.size()) {
//            Mp3Info mp3Info = playService.getMp3Infos().get(position);
//            Log.d("播放标题", mp3Info.getTitle());
//            musicname.setText(mp3Info.getTitle());
//            authorname.setText(mp3Info.getArtist());
//            Glide.with(this).load(mp3Info.getSongPic()).into(songimg);
////            contentView.setTextViewText(R.id.nname, mp3Info.getTitle());
////            contentView.setTextViewText(R.id.nsauthor, mp3Info.getArtist());
//            if (playService.isPlaying()) {
//                startbtu.setImageResource(R.mipmap.puse_01);
////				contentView.setImageViewResource(R.id.nstartbtn, R.drawable.puse_01);
//            } else {
//                startbtu.setImageResource(R.mipmap.start_01);
////				contentView.setImageViewResource(R.id.nstartbtn, R.drawable.start_1);
//            }
//            this.position = position;
//
//        }
//    }

    public void switchFragment(String tag) {
//        if (pos != dos) {
//            if (!fragmentList.get(pos).isAdded()) {
//                getSupportFragmentManager().beginTransaction().hide(fragmentList.get(dos))
//                        .add(R.id.ll_tmp, fragmentList.get(pos)).show(fragmentList.get(pos)).commit();
//            } else {
//                getSupportFragmentManager().beginTransaction().hide(fragmentList.get(dos))
//                        .show(fragmentList.get(pos)).commit();
//            }
//            dos = pos;
//        }
        Log.d("是否", "_______" + dos);
        if (!dos.equals(tag)) {
            transaction = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            // 定义当前碎片与即将加载的碎片

            Fragment fromFragment = fragmentMap.get(dos);
            Fragment toFragment = fragmentMap.get(tag);
            Log.d("是否", toFragment.isAdded() + "");
            if (!toFragment.isAdded()) {
                // 如果该碎片还没有被添加到事务中，则新添加到事务
                transaction.hide(fromFragment).add(R.id.ll_tmp,
                        toFragment);
            } else {
                // 如果该碎片已经被添加到事务中，则从事务中取出该碎片进行显示即可。无需销毁再重新创建。
                transaction.hide(fromFragment).show(toFragment);
            }// 提交执行过的事务
            transaction.addToBackStack(null);
            transaction.commit();
            dos = tag;
        }


//        if (dos != pos) {
//            transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.ll_tmp,fragmentList.get(pos));
//            transaction.addToBackStack(null);
//            transaction.commit();
//            dos = pos;
//            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                if (event.getAction()== KeyEvent.ACTION_DOWN) {
////                    switchFragment(0);
//                }
//                break;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void changeTheme(int themeMode) {

    }

    public interface updateTime {
        void update(long time);
    }
}

