package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/12-上午11:47.
 * 功能描述: 达人时光按月分组
 */
public class TimeImagesGroup {
    private String tMonth;//月份
    private String tYear;//年份
    private List<TalentTimeImages> images;//时光图片

    public TimeImagesGroup() {
        //析构时初始化数组
        images = new ArrayList<>();
    }

    public String gettMonth() {
        return tMonth;
    }

    public void settMonth(String tMonth) {
        this.tMonth = tMonth;
    }

    public String gettYear() {
        return tYear;
    }

    public void settYear(String tYear) {
        this.tYear = tYear;
    }

    public List<TalentTimeImages> getImages() {
        return images;
    }

    public static List<TimeImagesGroup> parseImagesArrayByMonth(List<TalentTimeImages> list) {
        List<TimeImagesGroup> timeImagesGroups = new ArrayList<>();
        if (list != null && list.size() > 0) {
            String lastMonth = null, lastYear = null;
            for (TalentTimeImages item : list) {
                /**
                 * 按年月分组  如果是同一组 则加入分组中
                 */

                if (!TextUtils.equals(item.gettYear(), lastYear) || !TextUtils.equals(item.gettMonth(), lastMonth)) {
                    /**
                     * 只要年月不同,则创建一个分组
                     */
                    TimeImagesGroup grp = new TimeImagesGroup();
                    lastMonth = item.gettMonth();
                    lastYear = item.gettYear();
                    grp.settYear(lastYear);
                    grp.settMonth(lastMonth);
                    grp.getImages().add(item);
                    timeImagesGroups.add(grp);
                } else {
                    /**
                     * 如果年月相同 则添加进上一个数组中
                     */
                    if (timeImagesGroups.size() > 0) {
                        int lastIndex = timeImagesGroups.size() - 1;
                        TimeImagesGroup aGrp = timeImagesGroups.get(lastIndex);
                        aGrp.getImages().add(item);
                    }
                }
            }
        }
        return timeImagesGroups;
    }
}
