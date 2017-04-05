package org.jiaoyajing.dizner.wplayer;

import android.util.Log;

import org.jiaoyajing.dizner.wplayer.javabean.LinkedTable;
import org.jiaoyajing.dizner.wplayer.javabean.Mp3Info;
import org.jiaoyajing.dizner.wplayer.javabean.MusicListBean;
import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dizner on 2017/3/20.
 */

public class ContartMothad {
    private static DbManager db;

    /**
     * 记录历史
     *
     * @param songId
     * @param successs
     */
    public static void addHistory(int songId, OnaddLikeSuccess successs) {
        db = x.getDb(new DbManager.DaoConfig());
        try {
//            db.replace(bean);
            KeyValue pairs = new KeyValue("history", true);
            int id = db.update(Mp3Info.class, WhereBuilder.b("_id", "=", songId), pairs);
            if (id > 0) {
                successs.onSuccess(songId);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空历史
     *
     * @param successs
     */
    public static void clearHistory(OnaddLikeSuccess successs) {
        db = x.getDb(new DbManager.DaoConfig());
        try {
            KeyValue pairs = new KeyValue("history", false);
            int id = db.update(Mp3Info.class, WhereBuilder.b("1", "=", "1"), pairs);
            if (id > 0) {
                successs.onSuccess(1);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏
     *
     * @param songId
     * @param successs
     */
    public static void addLike(int songId, OnaddLikeSuccess successs) {
        db = x.getDb(new DbManager.DaoConfig());
//        HistoryBean bean = new HistoryBean();
//        bean.setSid(songId);
        try {
//            db.replace(bean);
            KeyValue pairs = new KeyValue("isLike", true);
            int id = db.update(Mp3Info.class, WhereBuilder.b("_id", "=", songId), pairs);
            if (id > 0) {
                successs.onSuccess(songId);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除收藏
     *
     * @param songId
     * @param successs
     */
    public static void removeLike(int songId, OnaddLikeSuccess successs) {
        db = x.getDb(new DbManager.DaoConfig());
        try {
//            db.replace(bean);
            KeyValue pairs = new KeyValue("isLike", false);
            int id = db.update(Mp3Info.class, WhereBuilder.b("_id", "=", songId), pairs);
            if (id > 0) {
                successs.onSuccess(songId);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public interface OnaddLikeSuccess {
        void onSuccess(int songId);
    }

    /**
     * 新建列表
     *
     * @param listName
     * @param imgUrl
     */
    public static void newList(String listName, String imgUrl) {
        db = x.getDb(new DbManager.DaoConfig());
        MusicListBean bean = new MusicListBean();
        bean.setName(listName);
        bean.setImgUrl(imgUrl);

        try {
            db.replace(bean);
            Log.d("新建列表", "未出错");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取全部列表
     *
     * @return
     */
    public static List<MusicListBean> getLists() {
        db = x.getDb(new DbManager.DaoConfig());
        List<MusicListBean> listBeen;
        try {
            listBeen = db.selector(MusicListBean.class).findAll();
            Log.d("获取列表集合", (listBeen == null ? 0 : listBeen.size()) + "");
            return listBeen;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加到指定列表
     *
     * @param songId
     * @param listId
     */
    public static void add2List(long songId, int listId) {
        db = x.getDb(new DbManager.DaoConfig());
        LinkedTable table = new LinkedTable();
        table.setSongId(songId);
        table.setListId(listId);
        try {
            db.replace(table);
            Log.d("添加到列表：",songId +" 添加到 "+listId);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单个歌曲
     *
     * @param songId
     */
    private static List<Mp3Info> getMp3(long songId) {
        db = x.getDb(new DbManager.DaoConfig());
        try {
            List<Mp3Info> id = db.selector(Mp3Info.class).where(WhereBuilder.b("_id", "=", songId)).findAll();
            Log.d("根据ID获取Mp3",(id==null?0:id.size())+"");
            Log.d("搜索ID",songId+"");
            return id;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定列表id的歌曲
     *
     * @param listId
     */
    public static List<Mp3Info> getMp3Infos(int listId) {
        db = x.getDb(new DbManager.DaoConfig());
        List<Mp3Info> mp3InfoList = new ArrayList<>();
        try {
            List<LinkedTable> tableList = db.selector(LinkedTable.class).where(WhereBuilder.b("listId", "=", listId)).findAll();
            if (tableList != null) {
                Log.d("用列表Id获取关系",(tableList == null?0:tableList.size())+"");
                for (LinkedTable table : tableList) {
                    mp3InfoList.addAll(getMp3(table.getSongId()));
                    Log.d("查询到的ID",table.getSongId()+"");
                }
            }
            Log.d("用列表Id获取歌曲",(mp3InfoList == null?0:mp3InfoList.size())+"");
        } catch (DbException e) {
            e.printStackTrace();
        }

        return mp3InfoList;
    }

    /**
     * 从列表删除
     *
     * @param songId
     * @param listId
     * @return
     */
    public static int removeOfList(int songId, int listId) {
        db = x.getDb(new DbManager.DaoConfig());
        try {
            int delete = db.delete(LinkedTable.class, WhereBuilder.b("listId", "=", listId).and(WhereBuilder.b("songId", "=", songId)));
            return delete;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
