package org.jiaoyajing.dizner.wplayer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.MainActivity;
import org.jiaoyajing.dizner.wplayer.adapter.NetListAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.javabean.NetMp3Info;
import org.jiaoyajing.dizner.wplayer.util.NetUtils;
import org.jiaoyajing.dizner.wplayer.util.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {


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

    private String kw;
    private int page = 0;
    private List<NetMp3Info.SongBean> beanList;
    private NetListAdapter adapter;
    private ArrayList<Mp3Info> sList;
    private MainActivity mainActivity;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        mainActivity.bindPlayService();
        return view;
    }

    @OnClick({R.id.iv_btn_search, R.id.ll_input_layout, R.id.ll_btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            //搜索
            case R.id.iv_btn_search:
                if (TextUtils.isEmpty(etInput.getText())) {
                    Toast.makeText(getActivity(),"请输入关键字",Toast.LENGTH_SHORT).show();
                }else {
                    kw = etInput.getText().toString();
                }
                getWebData();
                mainActivity.hideSoftKey();
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
                Gson gson = new Gson();
//                NetMp3Info json = gson.fromJson(result, NetMp3Info.class);
                sList = new ArrayList<>();
                sList.addAll(result);
                setData();
                etInput.clearFocus();
            }
        });
    }

    private void setData() {
        adapter = new NetListAdapter(getActivity(),sList);
        lvNetList.setAdapter(adapter);
        lvNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainActivity.playService.setMp3Infos(sList);
                mainActivity.playService.play(i);
                Log.d("点击位置:",i+"");
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        mainActivity.dos = mainActivity.FRAGMENT_TAG_MAIN;
    }
}
