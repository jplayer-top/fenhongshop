package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/4.
 */
public class GoodsCommentData {
    public int good_count = 0;
    public int normal_count = 0;
    public int bad_count = 0;
    public int all_count = 0;
    public int hasimage_count = 0;
    public int star_average = 0;
    public List<GoodsComments> comments;

    public String getGood_Count() {
        String fmt = "好评(%1$d)";
        String good_fmt = String.format(fmt, good_count);
        return good_fmt;
    }

    public String getNormal_count() {
        String fmt = "中评(%1$d)";
        String average_fmt = String.format(fmt, normal_count);
        return average_fmt;
    }

    public String getBad_count() {
        String fmt = "差评(%1$d)";
        String bad_fmt = String.format(fmt, bad_count);
        return bad_fmt;
    }

    public String getAll_count() {
        String fmt = "全部(%1$d)";
        String all_fmt = String.format(fmt, all_count);
        return all_fmt;
    }

    public String getHasimage_count() {
        String fmt = "有图(%1$d)";
        String ims_fmt = String.format(fmt, hasimage_count);
        return ims_fmt;
    }
}
