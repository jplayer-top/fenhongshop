package com.fanglin.fenhong.microbuyer.base.event;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.model.EntityOfAddTalentImage;
import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;
import com.fanglin.fhlib.other.FHLog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-下午3:57.
 * 功能描述: 关联商品操作本地数据库EventBus事件
 */
public class LinkGoodsEvent {
    private boolean isAdd;//关联 !取消关联
    private int refreshStates = -1;

    private LinkGoods goods;//
    private EntityOfAddTalentImage mTalentImage;

    /**
     * ------ 方案一 ------
     */
    public LinkGoodsEvent(boolean isAdd, LinkGoods goods) {
        this.isAdd = isAdd;
        this.goods = goods;
        opListGoodsByOne(isAdd, goods);
    }

    public LinkGoods getGoods() {
        return goods;
    }

    private EntityOfAddTalentImage getTalentImageByGoods() {
        if (goods != null) {
            EntityOfAddTalentImage talentImage = new EntityOfAddTalentImage();
            talentImage.setGoods_id(goods.getGoods_id());
            talentImage.setImage(goods.getGoods_image());
            talentImage.setTimes(false);
            return talentImage;
        } else {
            return null;
        }
    }

    /**
     * ------ 方案二 ------
     */
    public LinkGoodsEvent(boolean isAdd, EntityOfAddTalentImage talentImage) {
        this.isAdd = isAdd;
        this.mTalentImage = talentImage;
    }

    public LinkGoodsEvent(int refreshStates) {
        this.refreshStates = refreshStates;
    }

    public int getRefreshStates() {
        return refreshStates;
    }

    public EntityOfAddTalentImage getmTalentImage() {
        if (mTalentImage != null) {
            mTalentImage.setTimes(true);
            return mTalentImage;
        } else {
            return getTalentImageByGoods();
        }

    }

    /**
     * 是否为添加
     *
     * @return boolean
     */
    public boolean isAdd() {
        return isAdd;
    }


    /**
     * 本地存储 关联或取消关联商品
     *
     * @param add       是否是添加
     * @param linkGoods 商品
     */
    public static void opListGoodsByOne(boolean add, LinkGoods linkGoods) {
        if (linkGoods != null) {
            try {
                DbUtils dbUtils = FHApp.getInstance().fhdb;
                if (add) {
                    dbUtils.saveOrUpdate(linkGoods);
                } else {
                    dbUtils.delete(linkGoods);
                }
            } catch (Exception e) {
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    /**
     * 通过id移除本地关联商品
     *
     * @param goodsId string
     */
    public static void deleteListGoodsById(String goodsId) {
        try {
            DbUtils dbUtils = FHApp.getInstance().fhdb;
            dbUtils.deleteById(LinkGoods.class, goodsId);
        } catch (Exception e) {
            FHLog.d("Plucky", e.getMessage());
        }
    }

    /**
     * 通过id串删除
     *
     * @param idstr string
     */
    public static void deleteListGoodsByIds(String... idstr) {
        if (idstr == null || idstr.length == 0) return;
        try {
            DbUtils dbUtils = FHApp.getInstance().fhdb;
            dbUtils.delete(LinkGoods.class, WhereBuilder.b("goods_id", "in", idstr));
        } catch (Exception e) {
            FHLog.d("Plucky", e.getMessage());
        }
    }

    /**
     * 成批本地存储 主要用于图片关联商品用
     *
     * @param add       boolean
     * @param goodsList 商品列表
     */
    public static void opListGoodsByAll(boolean add, List<LinkGoods> goodsList) {
        if (goodsList != null && goodsList.size() > 0) {
            try {
                DbUtils dbUtils = FHApp.getInstance().fhdb;
                if (add) {
                    dbUtils.saveOrUpdateAll(goodsList);
                } else {
                    dbUtils.deleteAll(goodsList);
                }
            } catch (Exception e) {
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    /**
     * 获取已关联的商品
     *
     * @return list
     */
    public static List<LinkGoods> getLinkedGoods() {
        try {
            DbUtils dbUtils = FHApp.getInstance().fhdb;
            return dbUtils.findAll(LinkGoods.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过id查找已关联商品
     *
     * @param goodsId String
     * @return 用于判断是否关联状态
     */
    public static LinkGoods getGoodsById(String goodsId) {
        try {
            DbUtils dbUtils = FHApp.getInstance().fhdb;
            return dbUtils.findById(LinkGoods.class, goodsId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将本地存储的数据结构转换为横向滚动数据源
     *
     * @return list
     */
    public static List<EntityOfAddTalentImage> getListByLocal() {
        List<EntityOfAddTalentImage> list = new ArrayList<>();
        List<LinkGoods> linkgoods = getLinkedGoods();
        if (linkgoods != null && linkgoods.size() > 0) {
            for (LinkGoods link : linkgoods) {
                EntityOfAddTalentImage talentImage = new EntityOfAddTalentImage();
                talentImage.setGoods_id(link.getGoods_id());
                talentImage.setImage(link.getGoods_image());
                talentImage.setTimes(!TextUtils.isEmpty(link.getTime_image_id_local()));
                list.add(talentImage);
            }
        }
        return list;
    }

    /**
     * 获取本地缓存关联商品的数量
     *
     * @return int
     */
    public static int getLocalCount() {
        List<LinkGoods> goods = getLinkedGoods();
        if (goods != null) {
            return goods.size();
        } else {
            return 0;
        }
    }

    /**
     * 清空本地关联商品本地缓存
     *
     * @return boolean
     */
    public static boolean emptyLocal() {
        try {
            DbUtils dbUtils = FHApp.getInstance().fhdb;
            dbUtils.deleteAll(LinkGoods.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatLinkedStr(int count, int max) {
        return "查看已关联商品 " + count + "/" + max;
    }
}
