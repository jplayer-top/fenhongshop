package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.graphics.Color;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/8-下午1:32.
 * 功能描述;//极速免税 消费单
 */
public class DutyfreeConsume extends APIUtil {
    private String consume_id;//2,
    private String consume_name;//提现,
    private String consume_icon;//http://1991th.com/a.jpg,
    private String consume_color;//#CCCCCC,
    private String createtime;//2016-12-06 22:01:02,
    private String consume_money_desc;//-¥5000.00,
    private String consume_label;//出账金额,
    private String consume_money;//5000.00,
    private String left_money;//¥5000.00,
    private String consume_sn;//4908239977000043,
    private String consume_type;//支出


    public String getConsumeColor() {
        return consume_color;
    }

    public String getConsumeIcon() {
        return consume_icon;
    }

    public String getConsumeId() {
        return consume_id;
    }

    public String getConsumeLabel() {
        return consume_label;
    }

    public String getConsumeMoney() {
        return consume_money;
    }

    public String getConsumeMoneyDesc() {
        return consume_money_desc;
    }

    public String getConsumeName() {
        return consume_name;
    }

    public String getConsumeSn() {
        return consume_sn;
    }

    public String getConsumeType() {
        return consume_type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getLeftMoney() {
        return left_money;
    }

    public int getColor() {
        try {
            return Color.parseColor(consume_color);
        } catch (Exception e) {
            return Color.parseColor("#FFFFFF");
        }
    }
}
