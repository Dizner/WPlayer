package org.jiaoyajing.dizner.wplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.util.NetUtils;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetListAdapter extends BaseAdapter {
    private Context context;
    private List<Mp3Info> mp3Infos;

    public NetListAdapter(Context context, List<Mp3Info> mp3Infos) {
        this.context = context;

        this.mp3Infos = mp3Infos;

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mp3Infos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mp3Infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        final ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.music_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final Mp3Info mp3Info = mp3Infos.get(position);
        holder.sname.setText(mp3Info.getTitle());
        holder.sauthor.setText(mp3Info.getArtist());
        final boolean finalIsOpen = false;
        holder.ivBtnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (holder.llBtnLayout.getVisibility() == View.GONE) {
                    holder.llBtnLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.llBtnLayout.setVisibility(View.GONE);
                }

//                Toast.makeText(context, "展开更多", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvBtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetUtils.downLoad(context, mp3Info, new NetUtils.OnResqutSuccess() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(context,"开始下载",Toast.LENGTH_SHORT).show();
                holder.llBtnLayout.setVisibility(View.GONE);
            }
        });
        x.image().bind(holder.muimg, mp3Info.getSongPic());
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.muimg)
        ImageView muimg;
        @BindView(R.id.sname)
        TextView sname;
        @BindView(R.id.sauthor)
        TextView sauthor;
        @BindView(R.id.iv_btn_more)
        ImageView ivBtnMore;
        @BindView(R.id.tv_btn_download)
        TextView tvBtnDownload;
        @BindView(R.id.ll_btn_layout)
        LinearLayout llBtnLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
