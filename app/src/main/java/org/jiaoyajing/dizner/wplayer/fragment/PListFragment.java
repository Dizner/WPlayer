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

import org.jiaoyajing.dizner.wplayer.ContartMothad;
import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.ListActivity;
import org.jiaoyajing.dizner.wplayer.adapter.MyMusicListAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.javabean.MusicListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dizner on 2017/3/13.
 */

public class PListFragment extends Fragment {
    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.music_list)
    ListView musiclist;
    @BindView(R.id.tv_list_title)
    TextView tvListTitle;
    private ListActivity mainActivity;
    private MyMusicListAdapter musicListAdapter;
    private List<Mp3Info> mp3Infos;
    private int listId;
    private List<MusicListBean> lists;
    private int openType = 0;
    private int songId;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (ListActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mymusic_list, container, false);
        ButterKnife.bind(this, view);
//        musiclist = (ListView) view.findViewById(R.id.music_list);
        if (getArguments() != null) {
            listId = getArguments().getInt("listId");
        }
        lists = ContartMothad.getLists();
        for (MusicListBean list : lists) {
            if (list.getId() == listId) {
                tvListTitle.setText(list.getName());
            }
        }

        setData();
        return view;
    }

    /**
     * 获取本地歌曲
     */
    private void setData() {
        mp3Infos = ContartMothad.getMp3Infos(listId);
        musicListAdapter = new MyMusicListAdapter(mainActivity, mp3Infos, new MyMusicListAdapter.OnClick() {
            @Override
            public void click(int pos) {
                mainActivity.playService.setMp3Infos(mp3Infos);
                mainActivity.playService.play(pos);

            }
        });
        Log.d("歌曲列表长度：", mp3Infos.size() + "");
        if (mp3Infos.size() <= 0) {
            Toast.makeText(mainActivity, "没有扫描到本地歌曲", Toast.LENGTH_LONG).show();
        } else {
            musiclist.setAdapter(musicListAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @OnClick(R.id.iv_btn_back)
    public void onClick() {
        getActivity().finish();
    }
}
