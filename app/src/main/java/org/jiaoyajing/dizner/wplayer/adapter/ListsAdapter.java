package org.jiaoyajing.dizner.wplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.javabean.MusicListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dizner on 2017/3/22.
 */

public class ListsAdapter extends BaseAdapter {
    private Context context;
    private List<MusicListBean> list;

    public ListsAdapter(List<MusicListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_lists, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        MusicListBean bean = list.get(i);
        Glide.with(context).load(bean.getImgUrl()).into(holder.ivListImg);
        holder.tvListName.setText(bean.getName());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_list_img)
        ImageView ivListImg;
        @BindView(R.id.tv_list_name)
        TextView tvListName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
