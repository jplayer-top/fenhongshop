package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/10.
 * 活动楼层
 */

public class ActivityFloor {
    public String floor_name;//
    public String floor_logo;//
    public List<ActivityGoods> floor_goods;//
    public boolean isSelected;

    public static int getScrollPosition (List<ActivityFloor> list, int position) {
        if (list == null) return 0;
        if (position > 0) {
            int c = 0;
            for (int i = 0; i < position; i++) {
                List<ActivityGoods> fg = list.get (i).floor_goods;
                if (fg != null) {
                    c += fg.size ();
                }
            }
            c += position;
            return c;
        } else {
            return 0;
        }
    }
}