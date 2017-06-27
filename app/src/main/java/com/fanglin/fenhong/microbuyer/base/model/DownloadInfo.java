package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/19.
 * 功能描述: 下载信息描述
 */

public class DownloadInfo {
    private String desc;//下载描述
    private List<String> array;//下载文件列表

    public String getDesc() {
        return desc;
    }

    public List getArray() {
        return array;
    }

    public String[] getArrayList(){
        if (array!=null&&array.size()>0){
            String[] arr=new String[array.size()];
            array.toArray(arr);
            return arr;
        }else {
            return null;
        }
    }
}
