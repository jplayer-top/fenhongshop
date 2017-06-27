package com.fanglin.fenhong.microbuyer.base.model.dutyfree;

import com.lidroid.xutils.db.annotation.Id;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/19-下午4:46.
 * 功能描述: 极速免税购物车商品
 */
public class DutyCartProduct extends BaseProduct {

    @Id
    private String cart_id;//主键

    private int is_selected;//1选中 0未选中
    private int product_num;//购物车数量
    private String product_image;

    private String activity_desc;//活动标示

    //前端自定义字段
    private boolean isExpanded;//是否展开
    private List<DutyAddress> addrList;//商品关联的地址

    @Override
    public String getProductImgUrl() {
        return product_image;
    }

    public String getCartId() {
        return cart_id;
    }

    public boolean isSelected() {
        return is_selected == 1;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setSelected(boolean is_selected) {
        this.is_selected = is_selected ? 1 : 0;
    }

    public void setProductNum(int product_num) {
        this.product_num = product_num;
    }

    public String getProNumDesc() {
        return "x" + String.valueOf(product_num);
    }

    public String getProNum4Cart() {
        return String.valueOf(product_num);
    }

    public List<DutyAddress> getAddrList() {
        return addrList;
    }

    public void setAddrList(List<DutyAddress> addrList) {
        this.addrList = addrList;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getActivityDesc() {
        return activity_desc;
    }

    public List<DutyAddress> getAddress4Logic() {
        if (addrList != null && addrList.size() > 0) {
            if (isExpanded()) {
                return addrList;
            } else {
                return addrList.subList(0, 1);
            }
        } else {
            return null;
        }
    }
}
