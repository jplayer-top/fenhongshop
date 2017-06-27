package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/9/29-下午2:28.
 * 功能描述;//商品说明
 */
public class GoodsIntro {
    private String title;//无忧退货,
    private String description;//本商品不支持七天无忧退货,
    private String desc;//
    private String url;//

    public String getDescription() {
        if (!TextUtils.isEmpty(desc)) {
            return desc;
        }
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
