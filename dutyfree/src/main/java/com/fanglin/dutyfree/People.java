package com.fanglin.dutyfree;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 青岛芳林信息
 * Created by Plucky on 2017/1/4.
 * 功能描述:测试DB
 */
@Table(name = "People")
public class People {
    @Column(name = "name")
    private String name;
    @Column(name = "id", isId = true)
    private String id;
    @Column(name = "desc")
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
