package com.fanglin.fenhong.microbuyer.base.model;

import com.google.gson.Gson;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/20.
 * 功能描述: 编辑页面传值
 */
public class EditTipsEntity {
    public int type;//
    public String field;//要编辑的字段
    public String content;//已经输入的内容
    public String tips;//提示信息
    public boolean isCompanyInfo;
    public String lastActivity;

    public String getString(){
        return new Gson().toJson(this);
    }
}
