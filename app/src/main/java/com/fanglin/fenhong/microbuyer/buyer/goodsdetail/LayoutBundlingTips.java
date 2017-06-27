package com.fanglin.fenhong.microbuyer.buyer.goodsdetail;

import android.text.TextUtils;
import android.view.View;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.base.model.NativeUrlEntity;
import com.fanglin.fenhong.microbuyer.buyer.GoodsPromBundleListActivity;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.microshop.LayoutPopup;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/12/15-下午12:00.
 * 功能描述: 购买时套装弹窗提示
 */
public class LayoutBundlingTips extends LayoutPopup {

    private int goods_source;
    private List<GoodsBundling> bundling;//优惠套装

    public LayoutBundlingTips(BaseFragmentActivity activity) {
        super(activity);
    }

    public void setBundling(List<GoodsBundling> bundling) {
        this.bundling = bundling;
    }

    public void setGoods_source(int goods_source) {
        this.goods_source = goods_source;
    }

    @Override
    public void onConfirm(View v, String confirmUrl) {
        dismiss();
        if (!TextUtils.isEmpty(confirmUrl)) {
            NativeUrlEntity entity = BaseFunc.isNative(confirmUrl);
            if (entity != null) {
                if (entity.activityClass == GoodsPromBundleListActivity.class) {
                    BaseFunc.gotoGoodsBundle(activity, bundling, goods_source);
                } else {
                    BaseFunc.urlRedirect(activity, entity);
                }
            } else {
                BaseFunc.gotoActivity(activity, FHBrowserActivity.class, confirmUrl);
            }
        }

    }

    @Override
    public void onSubmit(View v) {

    }
}
