package org.jiaoyajing.dizner.wplayer.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.service.PlayServer;
import org.jiaoyajing.dizner.wplayer.util.LrcView;
import org.jiaoyajing.dizner.wplayer.util.Mp3Utils;
import org.jiaoyajing.dizner.wplayer.util.NetUtils;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends BaseActiyvity implements OnClickListener, OnSeekBarChangeListener, BaseActiyvity.OnBindSuccess {
    @BindView(R.id.songname)
    TextView songname;
    @BindView(R.id.songimags)
    ImageView songimags;
    @BindView(R.id.skb)
    SeekBar sbk;
    @BindView(R.id.sumtime)
    TextView sumtime;
    @BindView(R.id.protime)
    TextView protime;
    @BindView(R.id.state)
    ImageView state;
    @BindView(R.id.nextbtn)
    ImageView nextbtn;
    @BindView(R.id.startbtn)
    ImageView startbtn;
    @BindView(R.id.prebtn)
    ImageView prebtn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lrc_view)
    LrcView lrcView;
    @BindView(R.id.iv_song_bg)
    ImageView ivSongBg;
    private List<Mp3Info> mp3Infos;
    private static final int UPDATE_TIME = 0x1;
    private MyHandler myhandler;
    private RotateAnimation an;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(option);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        initView();
//        setAnimation();
    }

//    private void setAnimation() {
//        an = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        an.setDuration(20000);
//        an.setRepeatCount(-1);
//        an.setInterpolator(new LinearInterpolator());
//        an.setFillAfter(true);
//        songimags.setAnimation(an);
//
//    }

    private void initView() {
        songname = (TextView) findViewById(R.id.songname);
        sumtime = (TextView) findViewById(R.id.sumtime);
        protime = (TextView) findViewById(R.id.protime);
        sbk = (SeekBar) findViewById(R.id.skb);
        songimags = (ImageView) findViewById(R.id.songimags);
        state = (ImageView) findViewById(R.id.state);
        state.setTag(PlayServer.SUIJI_PLAY);
        nextbtn = (ImageView) findViewById(R.id.nextbtn);
        startbtn = (ImageView) findViewById(R.id.startbtn);
        prebtn = (ImageView) findViewById(R.id.prebtn);
        setLinster();
        mp3Infos = Mp3Utils.getMp3Info(this);
        myhandler = new MyHandler(this);
        sbk.setProgress(0);
        lrcView.setAnimation(lrcView.animate());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("play", "onResume");
        bindPlayService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("play", "onStop");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("play", "onPause");
        unbindPlayService();
    }

    private void setLinster() {
        nextbtn.setOnClickListener(this);
        startbtn.setOnClickListener(this);
        prebtn.setOnClickListener(this);
        state.setOnClickListener(this);
        sbk.setOnSeekBarChangeListener(this);
    }

    @OnClick({R.id.state, R.id.nextbtn, R.id.startbtn, R.id.prebtn, R.id.iv_btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startbtn: {
                if (playService.isPlaying()) {
                    startbtn.setImageResource(R.mipmap.start_01);
                    playService.pause();
                } else {
                    if (playService.isPause()) {
                        startbtn.setImageResource(R.mipmap.puse_01);
                        playService.start();
                    } else {
                        playService.play(0);
                    }
                }
            }
            break;
            case R.id.nextbtn:
                playService.next();
                break;
            case R.id.prebtn:
                playService.prev();
                break;
            case R.id.iv_btn_back:
                finish();
                break;
            case R.id.state:
                int mode = (Integer) state.getTag();
                switch (mode) {
                    case PlayServer.XUNHUAN_PLAY:
                        state.setImageResource(R.mipmap.state_01sj);
                        state.setTag(PlayServer.SUIJI_PLAY);
                        playService.setPlay_mode(PlayServer.SUIJI_PLAY);
                        break;
                    case PlayServer.SUIJI_PLAY:
                        state.setImageResource(R.mipmap.state_04dq);
                        state.setTag(PlayServer.DANQU_PLAY);
                        playService.setPlay_mode(PlayServer.DANQU_PLAY);
                        break;
                    case PlayServer.DANQU_PLAY:
                        state.setImageResource(R.mipmap.state_03xh);
                        state.setTag(PlayServer.XUNHUAN_PLAY);
                        playService.setPlay_mode(PlayServer.XUNHUAN_PLAY);
                        break;
                }
                break;
        }
    }

    private void setLrc(Mp3Info mp3Info) {
        NetUtils.getLrc(this, mp3Info.getLrcUrl(), String.valueOf(mp3Info.getId()), new NetUtils.OnResqutSuccess() {
            @Override
            public void onSuccess(String result) {
                Log.d("歌词：", result);
                try {
                    lrcView.setLrcPath(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void bindSuccess() {
        playService.setLrcView(lrcView);
    }

    class MyHandler extends Handler {
        private PlayActivity playActivity;

        public MyHandler(PlayActivity playActivity) {
            this.playActivity = playActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (playActivity != null) {
                switch (msg.what) {
                    case UPDATE_TIME:
                        protime.setText(Mp3Utils.formatTime(msg.arg1));
                        Mp3Info obj = (Mp3Info) msg.obj;
                        sbk.setMax((int) obj.getDuration());
                        songname.setText(obj.getTitle());
                        sumtime.setText(Mp3Utils.formatTime(obj.getDuration()));
                        if (first) {
                            x.image().bind(songimags, obj.getSongPic());
                            x.image().bind(ivSongBg, obj.getSongPic());
                            playService.setLrcView(lrcView);
                            setLrc(obj);
                        }
                        lrcView.changeCurrent(msg.arg1);
                        first = false;
                        if (playService.isPlaying()) {
                            startbtn.setImageResource(R.mipmap.puse_01);
//				contentView.setImageViewResource(R.id.nstartbtn, R.drawable.puse_01);
                        } else {
                            startbtn.setImageResource(R.mipmap.start_01);
//				contentView.setImageViewResource(R.id.nstartbtn, R.drawable.start_1);
                        }
                        break;
                    case 2:
//                        long obj1 = (long) msg.obj;
//
//                        Log.d("play倒计时进度2", obj1 + "  " + (MainActivity.instance != null));
//                        if (MainActivity.instance != null)
//                            MainActivity.instance.upTime(obj1);
                        break;
                }
            }
        }
    }

    @Override
    public void publish(int progress, Mp3Info mp3) {
        Message msg = myhandler.obtainMessage(UPDATE_TIME);
        msg.arg1 = progress;
        msg.obj = mp3;
        myhandler.sendMessage(msg);
        sbk.setProgress(progress);
//        if (MainActivity.instance!=null) {
//            MainActivity.instance.publish(progress,mp3);
//        }
    }

    @Override
    public void upTime(long timer) {
        Log.d("play倒计时进度", timer + "");
        Message msg = myhandler.obtainMessage(2);
        msg.obj = timer;
        myhandler.sendMessage(msg);
    }

    @Override
    public void change(int position) {

        first = true;

        Mp3Info mp3Info = playService.getMp3Infos().get(position);
        songname.setText(mp3Info.getTitle());
        sumtime.setText(Mp3Utils.formatTime(mp3Info.getDuration()));
        sbk.setProgress(0);
        sbk.setMax((int) mp3Info.getDuration());
        if (this.playService.isPlaying()) {
            startbtn.setBackgroundResource(R.mipmap.puse_01);
        } else {
            startbtn.setBackgroundResource(R.mipmap.start_01);
        }
        switch (playService.getPlay_mode()) {
            case PlayServer.XUNHUAN_PLAY:
                state.setImageResource(R.mipmap.state_03xh);
                state.setTag(PlayServer.XUNHUAN_PLAY);
                break;
            case PlayServer.DANQU_PLAY:
                state.setImageResource(R.mipmap.state_04dq);
                state.setTag(PlayServer.DANQU_PLAY);
                break;
            case PlayServer.SUIJI_PLAY:
                state.setImageResource(R.mipmap.state_01sj);
                state.setTag(PlayServer.SUIJI_PLAY);
                break;
            default:
                break;
        }
//		}
    }

    @Override
    public BaseActiyvity getSelf() {
        return this;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser) {
//			playService.pause();
            playService.seekTo(progress);
//			playService.start();
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MainActivity.instance = null;
    }
}
