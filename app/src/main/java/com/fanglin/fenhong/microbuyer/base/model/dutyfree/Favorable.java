package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.Spanned;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhui.FHTagHandler;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/27.
 * 功能描述;//极速免税VIP买手 商品详情显示
 */

public class Favorable {
    private String favorable_price_desc;//VIP买手 ¥658,
    private String favorable_label;//省90,
    private String favorable_desc;//您暂时还不是VIP买手，开通会员立享VIP买手价,
    private String favorable_img;//http://1991th.com/a.jpg,
    private String normal_price_desc;//普通买手¥568

    public Spanned getFavorablePriceDesc() {
        return BaseFunc.fromHtml(favorable_price_desc,new FHTagHandler());
    }
    public Spanned getNormalPriceDesc() {
        return BaseFunc.fromHtml(normal_price_desc,new FHTagHandler());
    }

    public String getFavorableLabel() {
        return favorable_label;
    }

    public String getFavorableDesc() {
        return favorable_desc;
    }

    public String getFavorableImg() {
        return favorable_img;
    }
}
