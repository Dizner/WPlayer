package org.jiaoyajing.dizner.wplayer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.adapter.NetListAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.javabean.NetMp3Info;
import org.jiaoyajing.dizner.wplayer.util.MyUtils;
import org.jiaoyajing.dizner.wplayer.util.NetUtils;
import org.jiaoyajing.dizner.wplayer.util.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActiyvity {

    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.iv_btn_search)
    ImageView ivBtnSearch;
    @BindView(R.id.ll_input_layout)
    LinearLayout llInputLayout;
    @BindView(R.id.ll_btn_search)
    LinearLayout llBtnSearch;
    @BindView(R.id.lv_net_list)
    ListViewForScrollView lvNetList;
    @BindView(R.id.activity_search)
    RelativeLayout activitySearch;

    private String kw;
    private int page = 0;
    private List<NetMp3Info.SongBean> beanList;
    private NetListAdapter adapter;
    private ArrayList<Mp3Info> sList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        bindPlayService();
        if (MyUtils.isAVD(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("本软件不适合在虚拟机上运行");
            builder.setPositiveButton("exit", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            builder.show();
        }
    }

    @Override
    public void publish(int progress, Mp3Info mp3) {

    }

    @Override
    public void upTime(long timer) {

    }

    @Override
    public void change(int position) {

    }

    @Override
    public BaseActiyvity getSelf() {
        return this;
    }

    @OnClick({R.id.iv_btn_search, R.id.ll_btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            //搜索
            case R.id.iv_btn_search:
                if (TextUtils.isEmpty(etInput.getText())) {
                    Toast.makeText(this,"请输入关键字",Toast.LENGTH_SHORT).show();
                }else {
                    kw = etInput.getText().toString();
                }
                getWebData();
                hideSoftKey();
                break;
            //搜索按钮
            case R.id.ll_btn_search:
                llInputLayout.setVisibility(View.VISIBLE);
                llBtnSearch.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void getWebData() {
        NetUtils.search(kw,new NetUtils.OnResqutSuccess2() {
            @Override
            public void onSuccess(List<Mp3Info> result) {
//                Gson gson = new Gson();
//                NetMp3Info json = gson.fromJson(result, NetMp3Info.class);
//                beanList = json.getSong();
                sList = new ArrayList<>();
                sList.addAll(result);
//                for (NetMp3Info.SongBean bean : beanList) {
//
//                    NetUtils.getMp3(bean.getSongid(), new NetUtils.OnResqutSuccess() {
//                        @Override
//                        public void onSuccess(String result) {
//                            Gson gson = new Gson();
//                            NewMp3 json = gson.fromJson(result, NewMp3.class);
//                            Mp3Info mp3 = new Mp3Info();
//                            NewMp3.DataBean.SongListBean mp3Bean = json.getData().getSongList().get(0);
//                            mp3.setAlbum(mp3Bean.getAlbumName());
//                            mp3.setAlbumId(mp3Bean.getAlbumId());
//                            mp3.setArtist(mp3Bean.getArtistName());
//                            mp3.setDuration(mp3Bean.getTime());
//                            mp3.setId(mp3Bean.getSongId());
//                            mp3.setSize(mp3Bean.getTime()*100);
//                            mp3.setTitle(mp3Bean.getSongName());
//                            mp3.setUrl(mp3Bean.getSongLink());
//                            mp3.setIsMusic(1);
//                            sList.add(mp3);
//                        }
//                    });
//
//                }
//                Log.d("返回：",result);
//                Log.d("大小：",beanList.size()+"");
                setData();
                etInput.clearFocus();
            }
        });
    }

    private void setData() {
//        adapter = new NetListAdapter(SearchActivity.this,beanList);
//        lvNetList.setAdapter(adapter);
        lvNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playService.setMp3Infos(sList);
                playService.play(i);
//                NetUtils.getMp3(beanList.get(i).getSongid(), new NetUtils.OnResqutSuccess() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Gson gson = new Gson();
//                        NewMp3 json = gson.fromJson(result, NewMp3.class);
////                        playService
//                    }
//                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindPlayService();
    }


}
