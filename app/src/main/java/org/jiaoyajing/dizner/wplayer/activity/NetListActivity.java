package org.jiaoyajing.dizner.wplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.fragment.ItemFragment;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.ThemeImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetListActivity extends BaseActiyvity implements ThemeImpl {


    @BindView(R.id.ll_tmp)
    LinearLayout llTmp;
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

    private ItemFragment fragment;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netlist);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type",1);
        fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt("type",type);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_tmp,fragment).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        bindPlayService();
    }

    private Handler myhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this != null) {
                switch (msg.what) {
                    case 1:
                        Mp3Info obj = (Mp3Info) msg.obj;
                        musicname.setText(obj.getTitle());
                        authorname.setText(obj.getArtist());
                        Glide.with(getApplicationContext()).load(obj.getSongPic()).into(songimg);
                        if (playService.isPlaying()) {
                            startbtu.setImageResource(R.mipmap.puse_01);
                        } else {
                            startbtu.setImageResource(R.mipmap.start_01);
                        }
                        break;
                    case 2:
                        break;
                }
            }
        }
    };
    @Override
    public void publish(int progress, Mp3Info mp3) {
        Message msg = myhandler.obtainMessage(1);
        msg.arg1 = progress;
        msg.obj = mp3;
        myhandler.sendMessage(msg);
    }

    @Override
    public void upTime(long timer) {

    }

    @Override
    public void change(int position) {

    }


    @Override
    public void getSelf(List<BaseActiyvity> list) {
        list.add(this);
    }


    @Override
    public void changeTheme(int themeMode) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindPlayService();
    }

    @OnClick({R.id.songimg, R.id.prebtu, R.id.startbtu, R.id.nextbtu})
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
                startActivity(intent);
                break;
        }
    }
}

