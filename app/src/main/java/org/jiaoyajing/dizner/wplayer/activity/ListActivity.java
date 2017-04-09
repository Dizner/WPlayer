package org.jiaoyajing.dizner.wplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.fragment.PListFragment;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActivity extends BaseActiyvity {
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
    private PListFragment fragment;
    private int listId;
    private int openType;
    private int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        listId = getIntent().getIntExtra("listId", 0);
        fragment = new PListFragment();
        Bundle args = new Bundle();
        args.putInt("listId", listId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_list_layout, fragment).commit();
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
    protected void onResume() {
        super.onResume();
        bindPlayService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPlayService();
    }

    @OnClick({R.id.songimg, R.id.prebtu, R.id.startbtu, R.id.nextbtu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
