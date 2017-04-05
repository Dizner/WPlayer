package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.NetListActivity;
import org.jiaoyajing.dizner.wplayer.util.view.ListViewForScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NetMusicFragment extends Fragment {


    @BindView(R.id.tv_xinge)
    TextView tvXinge;
    @BindView(R.id.tv_rege)
    TextView tvRege;
    @BindView(R.id.tv_yaogun)
    TextView tvYaogun;
    @BindView(R.id.tv_jueshi)
    TextView tvJueshi;
    @BindView(R.id.tv_liuxing)
    TextView tvLiuxing;
    @BindView(R.id.tv_oumei)
    TextView tvOumei;
    @BindView(R.id.tv_jingdian)
    TextView tvJingdian;
    @BindView(R.id.tv_qingge)
    TextView tvQingge;
    @BindView(R.id.tv_yingshi)
    TextView tvYingshi;
    @BindView(R.id.tv_wangluo)
    TextView tvWangluo;
    @BindView(R.id.lfv_tuijain)
    ListViewForScrollView lfvTuijain;

    private int type;
    public static NetMusicFragment newInstance() {
        NetMusicFragment my = new NetMusicFragment();
        return my;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netmusic, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.tv_xinge, R.id.tv_rege, R.id.tv_yaogun, R.id.tv_jueshi, R.id.tv_liuxing, R.id.tv_oumei, R.id.tv_jingdian, R.id.tv_qingge, R.id.tv_yingshi, R.id.tv_wangluo})
    public void onClick(View view) {
        //type = 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
        switch (view.getId()) {
            //新歌榜
            case R.id.tv_xinge:
                type = 1;
                break;
            //热歌榜
            case R.id.tv_rege:
                type = 2;
                break;
            //摇滚榜
            case R.id.tv_yaogun:
                type = 11;
                break;
            //爵士
            case R.id.tv_jueshi:
                type = 12;
                break;
            //流行
            case R.id.tv_liuxing:
                type = 16;
                break;
            //欧美
            case R.id.tv_oumei:
                type = 21;
                break;
            //经典
            case R.id.tv_jingdian:
                type = 22;
                break;
            //情歌
            case R.id.tv_qingge:
                type = 23;
                break;
            //影视
            case R.id.tv_yingshi:
                type = 24;
                break;
            //网络
            case R.id.tv_wangluo:
                type = 25;
                break;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), NetListActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
