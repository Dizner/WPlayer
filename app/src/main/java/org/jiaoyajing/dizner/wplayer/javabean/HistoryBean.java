package org.jiaoyajing.dizner.wplayer.javabean;

/**
 * Created by Dizner on 2017/3/20.
 */
//@Table(name = "like_table")
public class HistoryBean {
//    @Column(name = "_id",isId = true)
    private int id;
//    @Column(name = "sid")
    private int sid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
