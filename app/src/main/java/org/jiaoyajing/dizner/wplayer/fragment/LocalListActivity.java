package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.BaseActiyvity;
import org.jiaoyajing.dizner.wplayer.activity.PlayActivity;
import org.jiaoyajing.dizner.wplayer.adapter.MyMusicListAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.Mp3Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dizner on 2017/3/13.
 */

public class LocalListActivity extends BaseActiyvity {
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.music_list)
    ListView musiclist;
    @BindView(R.id.tv_list_title)
    TextView tvListTitle;
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
    private MyMusicListAdapter musicListAdapter;
    private List<Mp3Info> mp3Infos;
    private static String tag = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymusic_list);
        ButterKnife.bind(this);
        tag = getIntent().getStringExtra("tag");
        switch (tag) {
            case "all":
                tvListTitle.setText("全部列表");
                break;
            case "like":
                tvListTitle.setText("我喜欢");
                break;
            case "history":
                tvListTitle.setText("最近播放");
                break;
        }
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
    public void rmSelf(List<BaseActiyvity> list) {
        list.remove(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        bindPlayService();
        setData();
    }

    /**
     * 获取本地歌曲
     */
    private void setData() {
        mp3Infos = Mp3Utils.getMp3Info(this, tag);
        musicListAdapter = new MyMusicListAdapter(this, mp3Infos, new MyMusicListAdapter.OnClick() {
            @Override
            public void click(int pos) {
                playService.setMp3Infos(mp3Infos);
                playService.play(pos);
            }
        });
        Log.d("列表长度：", mp3Infos.size() + "");
        if (mp3Infos.size() <= 0) {
            Toast.makeText(this, "没有扫描到本地歌曲", Toast.LENGTH_LONG).show();
        } else {
            musiclist.setAdapter(musicListAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPlayService();
    }

    @OnClick({R.id.songimg, R.id.prebtu, R.id.startbtu, R.id.nextbtu,R.id.iv_btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_btn_back:
                finish();
                break;
            //上一曲
            case R.id.prebtu:
                playService.prev();
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
