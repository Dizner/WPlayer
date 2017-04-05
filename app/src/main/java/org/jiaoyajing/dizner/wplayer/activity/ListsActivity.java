package org.jiaoyajing.dizner.wplayer.activity;

import android.os.Bundle;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.fragment.AllListFragment;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;

//列表选择
public class ListsActivity extends BaseActiyvity {
    private AllListFragment fragment;
    private int listId;
    private int openType;
    private long songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        openType = getIntent().getIntExtra("openType", 0);
        songId = getIntent().getLongExtra("songId", 0);
        fragment = new AllListFragment();
        Bundle args = new Bundle();
        args.putInt("openType",openType);
        args.putLong("songId",songId);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_list_layout,fragment).commit();
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
}
