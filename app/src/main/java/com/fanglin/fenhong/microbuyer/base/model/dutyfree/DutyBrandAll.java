package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.model.BrandMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/22-下午5:23.
 * 功能描述: 全部品牌列表
 */
public class DutyBrandAll {
    public String title;
    public int index;
    public List<BrandMessage> list;

    public static List<DutyBrandAll> getListByAll(List<BrandMessage> brandList) {
        List<DutyBrandAll> result = new ArrayList<>();
        if (brandList != null && brandList.size() > 0) {
            for (BrandMessage brand : brandList) {
                if (result.size() == 0) {
                    DutyBrandAll all = new DutyBrandAll();
                    all.title = brand.getBrand_initial();
                    all.list = new ArrayList<>();
                    all.list.add(brand);
                    result.add(all);
                } else {
                    int count = result.size();
                    DutyBrandAll last = result.get(count - 1);//取最后一个
                    if (TextUtils.equals(brand.getBrand_initial(), last.title)) {
                        last.list.add(brand);
                    } else {
                        DutyBrandAll all = new DutyBrandAll();
                        all.title = brand.getBrand_initial();
                        all.list = new ArrayList<>();
                        all.list.add(brand);
                        result.add(all);
                    }
                }
            }
        }

        return result;
    }
}
