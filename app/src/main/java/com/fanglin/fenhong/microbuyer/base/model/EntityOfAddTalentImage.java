package com.fanglin.fenhong.microbuyer.base.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-上午10:36.
 * 功能描述: 为了方便提交API时组装数据以及方便适配器数据绑定 创建该类
 */
public class EntityOfAddTalentImage {
    private boolean isEdit;//是否是编辑标签

    /**
     * 关联商品时需要的属性
     */
    private String goods_id;//商品id
    private int sort;//商品排序

    /**
     * 交集部分
     */
    private String image;//是时光图片的字段 可用于共同显示图片用


    /**
     * 时光图片
     */
    private List<TalentImageTag> goods;//关联的商品

    //是否是时光
    private boolean isTimes;

    public boolean isTimes() {
        return isTimes;
    }

    public void setTimes(boolean times) {
        isTimes = times;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<TalentImageTag> getGoods() {
        return goods;
    }

    public void setGoods(List<TalentImageTag> goods) {
        this.goods = goods;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    /**
     * 获取关联图片所关联商品的id
     *
     * @return ids
     */
    public String[] getIdsByGoods() {
        if (goods != null && goods.size() > 0) {
            String[] ids = new String[goods.size()];
            for (int i = 0; i < goods.size(); i++) {
                ids[i] = goods.get(i).getGoods_id();
            }
            return ids;
        } else {
            return null;
        }
    }

    public static List<EntityOfAddTalentImage> getLinkGoodsOfUnImages(List<EntityOfAddTalentImage> all) {
        List<EntityOfAddTalentImage> res = new ArrayList<>();
        if (all != null && all.size() > 0) {
            for (EntityOfAddTalentImage entity : all) {
                if (!entity.isTimes()) {
                    res.add(entity);
                }
            }
        }
        return res;
    }
}
