package com.fanglin.fenhong.microbuyer.dutyfree;

import android.database.Cursor;

import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyAddress;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyCartProduct;
import com.fanglin.fhlib.other.FHLog;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/20-下午4:55.
 * 功能描述: 订单确认页面商品的本地缓存，用于关联商品
 */
public class CartCheckCache {

    /**
     * 清空所有商品
     */
    public static void removeLocalGoodsList() {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                fhApp.fhdb.deleteAll(DutyCartProduct.class);
            } catch (Exception e) {
                //清空本地商品
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    /**
     * 清除本地关联的所有地址
     */
    public static void removeLocalAddress() {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                fhApp.fhdb.deleteAll(DutyAddress.class);
            } catch (Exception e) {
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    /**
     * 添加本地商品缓存
     *
     * @param list List<DutyCartProduct>
     */
    public static void addGoodsList(List<DutyCartProduct> list) {
        if (list == null || list.size() == 0) return;
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                fhApp.fhdb.saveAll(list);
            } catch (Exception e) {
                //将请求回来的商品存入本地缓存
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    /**
     * 读取当前关联的商品
     *
     * @param cartID String
     * @return DutyCartProduct
     */
    public static DutyCartProduct findGoodsById(String cartID) {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            DutyCartProduct product;
            try {
                product = fhApp.fhdb.findById(DutyCartProduct.class, cartID);
            } catch (Exception e) {
                product = null;
                FHLog.d("Plucky", e.getMessage());
            }
            return product;
        } else {
            return null;
        }
    }

    /**
     * 获取当前关联商品可分配数量
     *
     * @param cartID String
     * @return int
     */
    public static int getNumOfGoods(String cartID) {
        DutyCartProduct product = findGoodsById(cartID);
        if (product != null) {
            return product.getProduct_num();
        } else {
            return 0;
        }
    }

    /**
     * 商品绑定地址
     *
     * @param address DutyAddress
     * @param cartID  String
     */
    public static void bindAddrOfGoods(DutyAddress address, String cartID) {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                FHLog.d("Plucky", new Gson().toJson(address));
                address.setIndexID(address.getAddress_id() + "&" + cartID);
                address.setCartID(cartID);
                address.setSelectNum(1);
                fhApp.fhdb.save(address);
            } catch (Exception e) {
                //绑定商品
                FHLog.d("Plucky", e.getMessage());
            }
        }
    }

    //获取商品关联的地址表
    public static List<DutyAddress> getAddrList(String cart_id) {
        List<DutyAddress> addrList;
        try {
            addrList = FHApp.getInstance().fhdb.findAll(Selector.from(DutyAddress.class).where("cartID", "=", cart_id));
            FHLog.d("Plucky", new Gson().toJson(addrList));
        } catch (Exception e) {
            addrList = null;
            FHLog.d("Plucky", e.getMessage());
        }
        return addrList;
    }

    /**
     * 更新绑定地址数量
     *
     * @param address DutyAddress
     * @return boolean
     */
    public static boolean updateBindedAddress(DutyAddress address) {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                fhApp.fhdb.saveOrUpdate(address);
                return true;
            } catch (Exception e) {
                //绑定商品
                FHLog.d("Plucky", e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * 删除绑定地址
     *
     * @param address DutyAddress
     * @return boolean
     */
    public static boolean removeBindedAddress(DutyAddress address) {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                fhApp.fhdb.delete(address);
                return true;
            } catch (Exception e) {
                //绑定商品
                FHLog.d("Plucky", e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * 商品剩余数量
     *
     * @return int
     */
    public static int getLeftNumOfCartId(String cartId, int totalNum) {
        FHApp fhApp = FHApp.getInstance();
        if (fhApp.fhdb != null) {
            try {
                String sql = String.format("select sum(selectNum) as allNum from com_fanglin_fenhong_microbuyer_base_model_dutyfree_DutyAddress where cartID=%1$s", cartId);
                Cursor cursor = fhApp.fhdb.execQuery(sql);
                cursor.moveToFirst();
                int res = cursor.getInt(0);
                cursor.close();
                FHLog.d("Plucky", "num:" + res);
                return totalNum - res;
            } catch (Exception e) {
                FHLog.d("Plucky", e.getMessage());
                return totalNum;
            }
        }
        return 0;
    }
}
