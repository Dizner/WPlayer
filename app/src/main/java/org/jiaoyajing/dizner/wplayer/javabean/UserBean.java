package org.jiaoyajing.dizner.wplayer.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Dizner on 2017/4/4.
 */
@Table(name = "userInfo_table")
public class UserBean {
    @Column(name = "_id",isId = true,autoGen = true)
    public int id;
    @Column(name = "userName")
    public String name;
    @Column(name = "passWord")
    public String pass;

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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
