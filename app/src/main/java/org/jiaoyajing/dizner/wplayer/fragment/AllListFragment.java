package org.jiaoyajing.dizner.wplayer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jiaoyajing.dizner.wplayer.ContartMothad;
import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.ListActivity;
import org.jiaoyajing.dizner.wplayer.activity.MainActivity;
import org.jiaoyajing.dizner.wplayer.adapter.ListsAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.MusicListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部列表
 */
public class AllListFragment extends Fragment {


    @BindView(R.id.iv_btn_back)
    ImageView ivBtnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.all_list)
    ListView allList;

    private List<MusicListBean> list;
    private MainActivity mainActivity;
    private ListsAdapter adapter;
    private int openType;
    private long songId;

    public AllListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_list, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            openType = getArguments().getInt("openType");
            songId = getArguments().getLong("songId");
        }
        if (openType == 0) {
            mainActivity = (MainActivity) getActivity();
        }
        initView();

        return view;
    }

    private void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        list = ContartMothad.getLists();
        adapter = new ListsAdapter(list, getActivity());
        allList.setAdapter(adapter);
        allList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (openType == 0) {
                    startActivity(new Intent(getActivity(), ListActivity.class).putExtra("listId", list.get(i).getId()));
                } else {
//                    Intent intent = new Intent();
//                    intent.putExtra("songId",mp3Infos.get(pos).getId());
//                    getActivity().setResult(getActivity().RESULT_OK,intent);
                    ContartMothad.add2List(songId, (int) list.get(i).getId());
                    getActivity().finish();
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainActivity != null)
            mainActivity.dos = mainActivity.FRAGMENT_TAG_MAIN;
    }

    @OnClick(R.id.iv_btn_back)
    public void onClick() {
        if (mainActivity != null) {
            mainActivity.switchFragment(mainActivity.FRAGMENT_TAG_MAIN);
        } else {
            getActivity().finish();
        }
    }
}
