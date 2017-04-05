package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.MainActivity;
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

public class LocalListFragment extends Fragment {
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.music_list)
    ListView musiclist;
    @BindView(R.id.tv_list_title)
    TextView tvListTitle;
    private MainActivity mainActivity;
    private MyMusicListAdapter musicListAdapter;
    private List<Mp3Info> mp3Infos;
    private static String tag = "all";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mymusic_list, container, false);
        ButterKnife.bind(this, view);
//        musiclist = (ListView) view.findViewById(R.id.music_list);
        if (getArguments() != null) {
            tag = getArguments().getString("tag");
        }
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
//        setData();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    /**
     * 获取本地歌曲
     */
    private void setData() {
        mp3Infos = Mp3Utils.getMp3Info(mainActivity, tag);
        musicListAdapter = new MyMusicListAdapter(mainActivity, mp3Infos, new MyMusicListAdapter.OnClick() {
            @Override
            public void click(int pos) {
                mainActivity.playService.setMp3Infos(mp3Infos);
                mainActivity.playService.play(pos);
            }
        });
        Log.d("列表长度：", mp3Infos.size() + "");
        if (mp3Infos.size() <= 0) {
            Toast.makeText(mainActivity, "没有扫描到本地歌曲", Toast.LENGTH_LONG).show();
        } else {
            musiclist.setAdapter(musicListAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivity.dos = mainActivity.FRAGMENT_TAG_MAIN;
    }

    @OnClick(R.id.iv_btn_back)
    public void onClick() {
        mainActivity.switchFragment(mainActivity.FRAGMENT_TAG_MAIN);
    }
}
