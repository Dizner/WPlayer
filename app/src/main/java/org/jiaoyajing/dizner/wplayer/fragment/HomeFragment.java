package org.jiaoyajing.dizner.wplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.jiaoyajing.dizner.wplayer.ContartMothad;
import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.ListActivity;
import org.jiaoyajing.dizner.wplayer.activity.MainActivity;
import org.jiaoyajing.dizner.wplayer.adapter.ListsAdapter;
import org.jiaoyajing.dizner.wplayer.javabean.MusicListBean;
import org.jiaoyajing.dizner.wplayer.util.ThemeImpl;
import org.jiaoyajing.dizner.wplayer.util.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的模块
 */
public class HomeFragment extends Fragment implements OnItemClickListener, ThemeImpl {
    @BindView(R.id.mRollView)
    RollPagerView mRollView;
    @BindView(R.id.ll_btn_local_list)
    LinearLayout llBtnLocalList;
    @BindView(R.id.ll_btn_all_list)
    LinearLayout llBtnAllList;
    @BindView(R.id.ll_btn_like)
    LinearLayout llBtnLike;
    @BindView(R.id.ll_btn_history)
    LinearLayout llBtnHistory;
    @BindView(R.id.iv_local_music)
    ImageView ivLocalMusic;
    @BindView(R.id.tv_local_music)
    TextView tvLocalMusic;
    @BindView(R.id.iv_all_list)
    ImageView ivAllList;
    @BindView(R.id.tv_all_list)
    TextView tvAllList;
    @BindView(R.id.iv_i_like)
    ImageView ivILike;
    @BindView(R.id.tv_i_like)
    TextView tvILike;
    @BindView(R.id.iv_zuijin)
    ImageView ivZuijin;
    @BindView(R.id.tv_zuijin)
    TextView tvZuijin;
    @BindView(R.id.tv_my_list)
    TextView tvMyList;
    @BindView(R.id.iv_new_List)
    ImageView ivNewList;
    @BindView(R.id.tv_new_list)
    TextView tvNewList;
    @BindView(R.id.RelativeLayout1)
    RelativeLayout RelativeLayout1;
    @BindView(R.id.lv_lists)
    ListViewForScrollView lvLists;
    private MainActivity mainActivity;
    private LocalListActivity listFragment;
    private AlertDialog dialog;
    private List<MusicListBean> list;
    private ListsAdapter adapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public static HomeFragment newInstance() {
        HomeFragment my = new HomeFragment();
        return my;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mainActivity.dos = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        ButterKnife.bind(this, view);
        mRollView.setAdapter(new TestLoopAdapter(mRollView));
        listFragment = new LocalListActivity();
        initView();
        setData();
        return view;
    }

    private void initView() {
        LinearLayout view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog, null, false);
        final EditText et = (EditText) view.findViewById(R.id.et_input);
        TextView tvBtnOk = (TextView) view.findViewById(R.id.tv_btn_ok);
        tvBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et.getText())) {
                    ContartMothad.newList(et.getText().toString(), "");
                    dialog.cancel();
                }
            }
        });
        TextView tvBtnCancle = (TextView) view.findViewById(R.id.tv_btn_cancle);
        tvBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void setData() {
        list = new ArrayList<>();
        list = ContartMothad.getLists();
        adapter = new ListsAdapter(list, getActivity());
        lvLists.setFocusable(false);
        lvLists.setAdapter(adapter);
        lvLists.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ListActivity.class).putExtra("listId", list.get(i).getId()));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @OnClick({R.id.ll_btn_local_list, R.id.ll_btn_all_list, R.id.ll_btn_like, R.id.ll_btn_history, R.id.ll_btn_new_list})
    public void onClick(View view) {
        Intent intent = intent = new Intent();;
        switch (view.getId()) {
            //本地列表
            case R.id.ll_btn_local_list:
                intent.setClass(getContext(),LocalListActivity.class);
                intent.putExtra("tag","all");
                startActivity(intent);
                break;
            case R.id.ll_btn_all_list:
                mainActivity.switchFragment(mainActivity.FRAGMENT_TAG_ALLLIST_LIST);
                break;
            //收藏
            case R.id.ll_btn_like:
                intent.setClass(getContext(),LocalListActivity.class);
                intent.putExtra("tag","like");
                startActivity(intent);
                break;
            //播放历史
            case R.id.ll_btn_history:
                intent.setClass(getContext(),LocalListActivity.class);
                intent.putExtra("tag","history");
                startActivity(intent);
                break;
            //新建列表
            case R.id.ll_btn_new_list:
                dialog.show();
                break;
        }
    }

//    ImageView ivLocalMusic;
//    TextView tvLocalMusic;
//    ImageView ivAllList;
//    TextView tvAllList;
//    ImageView ivILike;
//    TextView tvILike;
//    ImageView ivZuijin;
//    TextView tvZuijin;
//    TextView tvMyList;
//    ImageView ivNewList;
//    TextView tvNewList;
//    RelativeLayout RelativeLayout1;

    //主题回调
    @Override
    public void changeTheme(int themeMode) {
        switch (themeMode) {
            //白天模式
            case THEME_MODE_LIGHT:
                tvLocalMusic.setTextColor(getResources().getColor(R.color.black));
                tvAllList.setTextColor(getResources().getColor(R.color.black));
                tvILike.setTextColor(getResources().getColor(R.color.black));
                tvZuijin.setTextColor(getResources().getColor(R.color.black));
                tvMyList.setTextColor(getResources().getColor(R.color.black));
                tvNewList.setTextColor(getResources().getColor(R.color.black));
                RelativeLayout1.setBackgroundResource(R.color.write);
                break;
            //night
            case THEME_MODE_NIGHT:
                tvLocalMusic.setTextColor(getResources().getColor(R.color.write));
                tvAllList.setTextColor(getResources().getColor(R.color.write));
                tvILike.setTextColor(getResources().getColor(R.color.write));
                tvZuijin.setTextColor(getResources().getColor(R.color.write));
                tvMyList.setTextColor(getResources().getColor(R.color.write));
                tvNewList.setTextColor(getResources().getColor(R.color.write));
                RelativeLayout1.setBackgroundResource(R.color.black);
                break;
        }
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.mipmap.img_1,
                R.mipmap.img_2,
                R.mipmap.img_3,
                R.mipmap.img_4
        };

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }

}
