package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 商品促销 满额优惠 实体类
 * Created by lizhixin on 2016/1/5.
 */
public class GoodsDtlPromMansong {

    public String title;
    public String remark;//描述
    public long start_time;
    public long end_time;
    public List<GoodsDtlPromMansongRules> rules;

}
