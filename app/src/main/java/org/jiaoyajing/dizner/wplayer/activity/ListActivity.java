package org.jiaoyajing.dizner.wplayer.activity;

import android.os.Bundle;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.fragment.PListFragment;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;

public class ListActivity extends BaseActiyvity {
    private PListFragment fragment;
    private int listId;
    private int openType;
    private int songId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listId = getIntent().getIntExtra("listId", 0);
        fragment = new PListFragment();
        Bundle args = new Bundle();
        args.putInt("listId",listId);
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
}
