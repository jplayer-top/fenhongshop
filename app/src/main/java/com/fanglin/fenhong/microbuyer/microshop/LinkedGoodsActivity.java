package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoodsEvent;
import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;
import com.fanglin.fenhong.microbuyer.microshop.adapter.LinkGoodsAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-下午6:06.
 * 功能描述: 已关联商品列表
 */
public class LinkedGoodsActivity extends BaseFragmentActivityUI implements LinkGoodsAdapter.LinkGoodsAdapterListener {
    @ViewInject(R.id.listView)
    ListView listView;
    LinkGoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_linkedgoods, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        //关联商品
        List<LinkGoods> localList = LinkGoodsEvent.getLinkedGoods();
        adapter = new LinkGoodsAdapter(mContext, "解除关联");
        adapter.setGoodsList(localList);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        if (localList != null) {
            formatCount(localList.size());
        } else {
            formatCount(0);
        }
    }

    @Override
    public void onBound(int position) {
        int all = LinkGoodsEvent.getLocalCount();
        if (all == 10) {
            BaseFunc.showMsg(mContext, getString(R.string.hint_overlinklimit));
            return;
        }
        LinkGoods aGoods = adapter.getItem(position);
        EventBus.getDefault().post(new LinkGoodsEvent(true, aGoods));
        adapter.notifyDataSetChanged();
        formatCount(all + 1);
    }

    @Override
    public void onUnBound(int position) {
        LinkGoods aGoods = adapter.getItem(position);
        if (!aGoods.isBindedImageOfLocal()) {
            int all = LinkGoodsEvent.getLocalCount();
            EventBus.getDefault().post(new LinkGoodsEvent(false, aGoods));
            adapter.notifyDataSetChanged();
            formatCount(all - 1);
        } else {
            confirmTips();
        }
    }

    private void confirmTips() {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.tips_of_unlink_picgoods));
        fhHintDialog.show();
    }

    public void formatCount(int count) {
        setHeadTitle("已关联 " + count + "/" + 10);
    }
}
