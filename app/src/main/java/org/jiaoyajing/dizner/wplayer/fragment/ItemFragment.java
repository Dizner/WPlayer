package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.NetListActivity;
import org.jiaoyajing.dizner.wplayer.adapter.NetListAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemFragment extends Fragment {

    @BindView(R.id.lv_net_list)
    ListView lvNetList;
    @BindView(R.id.xrv)
    XRefreshView xrv;

    private int type;
    private List<Mp3Info> sList;
    private NetListAdapter adapter;
    private NetListActivity mainActivity;
    private int page = 0;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (NetListActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        if (getArguments() != null) {
            type = getArguments().getInt("type", 1);
        }
        ButterKnife.bind(this, view);
        initView();
        getWebData();
        return view;
    }

    private void initView() {
        sList = new ArrayList<>();
        xrv.setPullLoadEnable(true);
        xrv.setPullRefreshEnable(false);
        xrv.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                getWebData();
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }


    private void getWebData() {
        NetUtils.getTYpeList(type, page, new NetUtils.OnResqutSuccess2() {
            @Override
            public void onSuccess(List<Mp3Info> result) {
                if (result.size() > 0) {
                    sList.addAll(result);
                    setData();
                    page++;
                }
                xrv.stopLoadMore();
            }
        });
    }

    private void setData() {
        Log.d("大小",sList.size()+"");
        adapter = new NetListAdapter(getActivity(), sList);
        lvNetList.setAdapter(adapter);
        lvNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainActivity.playService.setMp3Infos(sList);
                mainActivity.playService.play(i);
                Log.d("点击位置:", i + "");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
//        mainActivity.dos = mainActivity.FRAGMENT_TAG_MAIN;
    }
}
