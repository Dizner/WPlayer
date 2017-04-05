package org.jiaoyajing.dizner.wplayer.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dizner on 2017/3/19.
 */
@Table(name = "music_list")
public class MusicListBean {
    @Column( name = "_id",isId = true,autoGen = true)
    private int id;
    @Column(name = "list_name")
    private String name;
    @Column(name = "img_url")
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

