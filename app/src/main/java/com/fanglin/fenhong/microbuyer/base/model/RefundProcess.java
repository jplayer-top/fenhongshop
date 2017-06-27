package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.common.PlatformCheckActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsApplyFailActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsApplySuccessActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsRefundSuccessActivity;
import com.fanglin.fenhong.microbuyer.common.ReturnGoodsSubSuccessActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/11/24.
 * 退款、退货时间轴Model
 */

public class RefundProcess {
    public int processid;//表示按钮编号 1-6
    public int refund_type;//申请类型:1:退款 | 2:退货
    public int if_icon;//iconfont 对应的图标
    public int icon_color;//iconfont 对应的颜色 绿色-表示已通过 红色-表示已拒绝 黄色-表示进行中-可操作的步骤
    public int refund_desc;//退款、退货的描述
    public String activity_class;//根据该字段判断进入的页面
    public int refund_state;//当前进度

    public void gotoActivity (Context c, String refund_id, String order_id, String rec_id) {
        if (!TextUtils.isEmpty (activity_class)) {
            Class<?> cls;
            try {
                cls = Class.forName (activity_class);
            } catch (Exception e) {
                cls = null;
            }
            if (cls != null) {
                Intent i = new Intent (c, cls);
                i.putExtra ("ID", refund_id);
                i.putExtra ("ORDER", order_id);
                i.putExtra ("REC", rec_id);
                i.putExtra ("TYPE", refund_type);
                i.putExtra ("STATE", refund_state);
                c.startActivity (i);
            }
        }
    }

    public int[] getColorAndTxt (int refund_state, int refund_type) {
        int[] res = new int[]{0, 0, 0};
        /* 应该都是绿的 正在进行中的 灰色的 */
        switch (processid) {
            case 1://第一个按钮
                if (refund_state >= 1) {
                    res[0] = R.color.color_lv;
                } else {
                    res[0] = R.color.color_bb;
                }
                if (refund_type == 1) {
                    res[1] = R.string.return_goods_process_01_money;
                } else {
                    res[1] = R.string.return_goods_process_01_goods;
                }
                break;
            case 2://第二个按钮
                if (refund_state < 2) {
                    res[1] = R.string.return_goods_process_02_ing;
                    res[0] = R.color.color_huang;
                    activity_class = ReturnGoodsSubSuccessActivity.class.getName ();
                } else {
                    if (refund_state == 2) {
                        res[1] = R.string.return_goods_process_02_refuse;
                        res[0] = R.color.fh_red;
                        activity_class = ReturnGoodsApplyFailActivity.class.getName ();
                    } else {
                        res[1] = R.string.return_goods_process_02_agress;
                        res[0] = R.color.color_lv;
                    }
                }
                break;
            case 3:
                if (refund_state < 4) {
                    //只有当卖家同意退货之后才显示黄色
                    if (refund_state == 3) {
                        res[0] = R.color.color_huang;
                        /*等待买家发货*/
                        activity_class = ReturnGoodsApplySuccessActivity.class.getName ();
                    } else {
                        res[0] = R.color.color_bb;
                    }
                } else {
                    res[0] = R.color.color_lv;
                }
                res[1] = R.string.return_goods_process_03;
                break;
            case 4://卖家收货按钮
                if (refund_state < 5) {
                    if (refund_state == 4) {
                        /*如果买家已发货 变黄 且等待卖家收货*/
                        res[0] = R.color.color_huang;
                        res[1] = R.string.return_goods_process_04_ing;
                        activity_class = ReturnGoodsApplySuccessActivity.class.getName ();
                    } else {
                        res[0] = R.color.color_bb;
                        res[1] = R.string.return_goods_process_04;
                    }
                } else {
                    if (refund_state == 5) {
                        res[1] = R.string.return_goods_process_04_fail;
                        res[0] = R.color.fh_red;
                        activity_class = ReturnGoodsApplySuccessActivity.class.getName ();
                    } else {
                        res[1] = R.string.return_goods_process_04;
                        res[0] = R.color.color_lv;
                    }
                }
                break;
            case 5:
                /*平台审核按钮*/
                if (refund_state > 6) {
                    res[1] = R.string.return_goods_process_05;
                    res[0] = R.color.color_lv;
                } else {
                    if (refund_type == 1) {
                        /*退款*/
                        if (refund_state == 3) {
                            res[0] = R.color.color_huang;
                            res[1] = R.string.return_goods_process_05_ing;
                            activity_class = PlatformCheckActivity.class.getName ();
                        } else {
                            res[0] = R.color.color_bb;
                            res[1] = R.string.return_goods_process_05;
                        }
                    } else {
                        /*退货*/
                        if (refund_state == 6) {
                            res[0] = R.color.color_huang;
                            res[1] = R.string.return_goods_process_05_ing;
                            activity_class = PlatformCheckActivity.class.getName ();
                        } else {
                            res[0] = R.color.color_bb;
                            res[1] = R.string.return_goods_process_05;
                        }
                    }
                }
                break;
            case 6:
                /*退款到帐按钮*/
                if (refund_state == 7) {
                    res[0] = R.color.color_lv;
                    activity_class = ReturnGoodsRefundSuccessActivity.class.getName ();
                    if (refund_type == 1) {
                        res[1] = R.string.return_goods_process_06_money;
                    } else {
                        res[1] = R.string.return_goods_process_06_goods;
                    }
                } else {
                    res[0] = R.color.color_bb;
                    res[1] = R.string.return_goods_process_06;
                }
                break;
        }
        return res;
    }

    public static List<RefundProcess> getList (int refund_type, int refund_state) {
        List<RefundProcess> list = new ArrayList<> ();
        RefundProcess item;
        int[] color_desc;
        if (refund_type == 1) {
            /*退款*/
            item = new RefundProcess ();
            item.processid = 1;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_01;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 2;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = refund_state == 2 ? R.string.if_error : R.string.if_success;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 5;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_05;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 6;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_06;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);
        } else {
            /*退货*/
            item = new RefundProcess ();
            item.processid = 1;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_01;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 2;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = refund_state == 2 ? R.string.if_error : R.string.if_success;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 3;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_03;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 4;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_04;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 5;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_05;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);

            item = new RefundProcess ();
            item.processid = 6;
            item.refund_type = refund_type;
            item.refund_state = refund_state;
            item.if_icon = R.string.if_refund_process_06;
            color_desc = item.getColorAndTxt (refund_state, refund_type);
            item.icon_color = color_desc[0];
            item.refund_desc = color_desc[1];
            list.add (item);
        }
        return list;
    }
}
