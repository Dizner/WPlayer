package org.jiaoyajing.dizner.wplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jiaoyajing.dizner.wplayer.ContartMothad;
import org.jiaoyajing.dizner.wplayer.R;
import org.jiaoyajing.dizner.wplayer.activity.ListsActivity;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyMusicListAdapter extends BaseAdapter {
    private Context context;
    private List<Mp3Info> mp3Infos;
    private OnClick click;
    public MyMusicListAdapter(Activity context, List<Mp3Info> mp3Infos,OnClick click) {
        this.context = context;
        this.click = click;
        this.mp3Infos = mp3Infos;

    }

    public void setMp3Infos(ArrayList<Mp3Info> mp3Infos) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        final ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.local_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        final Mp3Info mp3Info = mp3Infos.get(position);
        Glide.with(context).load(mp3Info.getSongPic()).into(holder.muimg);
//        x.image().bind(holder.muimg,mp3Info.getSongPic());
        holder.sname.setText(mp3Info.getTitle());
        holder.sauthor.setText(mp3Info.getArtist());
        holder.cbBtnLike.setChecked(mp3Info.isLike());
        holder.cbBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp3Info.isLike()) {
                    mp3Info.setLike(true);
                    ContartMothad.addLike((int) mp3Info.getId(), new ContartMothad.OnaddLikeSuccess() {
                        @Override
                        public void onSuccess(int songId) {
                            Toast.makeText(context,songId+"添加收藏成功",Toast.LENGTH_SHORT).show();
                            holder.cbBtnLike.setChecked(true);
                            notifyDataSetChanged();
                        }
                    });
                }else {
                    mp3Info.setLike(false);
                    ContartMothad.removeLike((int) mp3Info.getId(), new ContartMothad.OnaddLikeSuccess() {
                        @Override
                        public void onSuccess(int songId) {
                            holder.cbBtnLike.setChecked(false);
                            Toast.makeText(context,songId+"取消收藏成功",Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        holder.RelativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.click(position);
            }
        });
        holder.add2list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ListsActivity.class).putExtra("openType",1).putExtra("songId",mp3Info.getId()));
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.muimg)
        ImageView muimg;
        @BindView(R.id.iv_btn_add2list)
        ImageView add2list;
        @BindView(R.id.sname)
        TextView sname;
        @BindView(R.id.sauthor)
        TextView sauthor;
        @BindView(R.id.cb_btn_like)
        CheckBox cbBtnLike;
        @BindView(R.id.RelativeLayout1)
        RelativeLayout RelativeLayout1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface OnClick{
        void click(int pos);
    }
}
