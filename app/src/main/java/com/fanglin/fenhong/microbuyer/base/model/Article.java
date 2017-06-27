package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-4.
 * 商城文章系统
 * 当ac_id=1时返回系统公告
 */
public class Article extends APIUtil {
    public String article_id;// 索引id
    public String ac_id;// 分类id
    public String article_url;// 跳转链接
    public String article_sort;// 排序
    public String article_title;// 标题
    public String article_time;// 发布时间戳

    public boolean showDlg = false;
    public int actioncode = 0;//0 获取列表 1 获取具体一个对象

    public Article () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    switch (actioncode) {
                        case 0:
                            List<Article> articles;
                            try {
                                articles = new Gson ().fromJson (data, new TypeToken<List<Article>> () {
                                }.getType ());
                            } catch (Exception e) {
                                articles = null;
                            }
                            if (articles != null && articles.size () > 0) {
                                if (mcb != null) mcb.igetList (articles);
                            } else {
                                if (mcb != null) mcb.iError ("-1");
                            }
                            break;
                        case 1:

                            break;
                    }

                } else {
                    if (mcb != null) mcb.iError (data);
                }
            }
        });
    }

    /*
     *  ac_id 传入1 时表示公告系统数据
     *  curpage 从1开始计数
     */
    public void getList (int ac_id, int num, int curpage) {
        String url = BaseVar.API_GET_ARTICLE_LIST + "&ac_id=" + ac_id + "&num=" + num + "&curpage=" + curpage;
        actioncode = 0;
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private ArticleModelCallBack mcb;

    public void setModelCallBack (ArticleModelCallBack cb) {
        this.mcb = cb;
    }

    public interface ArticleModelCallBack {
        void igetList (List<Article> lst);

        void iError (String errcode);
    }

}
