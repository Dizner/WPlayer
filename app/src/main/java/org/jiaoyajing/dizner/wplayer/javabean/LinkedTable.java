package org.jiaoyajing.dizner.wplayer.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dizner on 2017/3/19.
 * 此表是管理单个歌曲与列表的多对多关系
 */
@Table(name = "linked")
public class LinkedTable {
    @Column(name = "_id",isId = true)
    private int id;
    @Column(name = "songId")
    private long songId;
    @Column(name = "listId")
    private int listId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}
