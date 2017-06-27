package com.tencent.xingepush;

/**
 * 信鸽消息解析
 * Created by Plucky on 2015/8/28.
 */
public class XgPushMessage {
    public String title;
    public String content;
    public long timestamp;

    /*在CustomContent 中*/
    public int activity;
    public String img;
    public String url;
}
