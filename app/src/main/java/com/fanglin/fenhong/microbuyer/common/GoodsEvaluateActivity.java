package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.EvaSubGoodsEntity;
import com.fanglin.fenhong.microbuyer.base.model.EvaluateAGoods;
import com.fanglin.fenhong.microbuyer.base.model.EvaluatingGoods;
import com.fanglin.fenhong.microbuyer.common.adapter.GoodsEvaluateAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/23.
 * modify by lizhixin on 2016/02/22
 */
public class GoodsEvaluateActivity extends BaseFragmentActivityUI implements EvaluatingGoods.EGModelCallBack, EvaluateAGoods.EAGModelCallBack, GoodsEvaluateAdapter.IGoodsEvaluateSubmit {

    @ViewInject(R.id.recyclerview)
    RecyclerView recyclerView;
    EvaluatingGoods goods;
    private EvaluateAGoods evaluate;
    private String order_id;
    private String[] params;
    private GoodsEvaluateAdapter evaluateAdapter;
    private SpotsDialog sad;
    private boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_goodsevaluate, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            params = getIntent().getStringExtra("VAL").split(",");
            order_id = params[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(order_id)) {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        sad = BaseFunc.getLoadingDlg(mContext, getString(R.string.deposit_requesting));
        setHeadTitle(R.string.pic_evaluate);

        //获取评论商品
        goods = new EvaluatingGoods();
        goods.setModelCallBack(this);

        //提交评价的API请求者
        evaluate = new EvaluateAGoods();
        evaluate.setModelCallBack(this);
        goods.getData(member, order_id);

        evaluateAdapter = new GoodsEvaluateAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(evaluateAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(evaluateAdapter);

        evaluateAdapter.isAppend = (params != null && params.length == 2) ? 0 : 1;//0为评价  1为追评
        evaluateAdapter.setIgesCallback(this);
    }

    @Override
    protected void onResume() {
        isActive = true;
        super.onResume();
    }

    /**
     * 评论回调
     *
     * @param isSuccess true false
     * @param data      评价成功返回新加积分 （追加评价没有积分，应不显示）
     */
    @Override
    public void onEAGEnd(boolean isSuccess, String data) {
        if (sad != null) {
            sad.dismiss();
        }
        if (isSuccess) {
            showHintDialog(data);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        } else if (!TextUtils.equals("-4", data)) {
            BaseFunc.showMsg(mContext, getString(R.string.eva_error));
        }
    }

    /**
     * 显示提交评价成功后的提示
     *
     * @param data 评价获得的积分
     */
    private void showHintDialog(String data) {
        Dialog dialog = new Dialog(mContext, R.style.alert_dialog);
        View view = View.inflate(mContext, R.layout.dialog_simple_transparent_bg, null);
        TextView text = (TextView) view.findViewById(R.id.text);

        if (BaseFunc.isInteger(data) && Double.parseDouble(data) > 0) {
            text.setText(String.format(getString(R.string.evaluate_success_and_point), data));
        } else {
            text.setText(getString(R.string.evaluate_success));
        }

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }

    /*数据获取回调*/
    @Override
    public void onEGData(EvaluatingGoods goods) {
        if (goods != null) {
            evaluateAdapter.setList(goods.order_goods);
            evaluateAdapter.notifyDataSetChanged();
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.op_error));
            finish();
        }
    }

    @Override
    public void onEGError(String errcode) {
        if (!TextUtils.equals("-4", errcode)) {
            BaseFunc.showMsg(mContext, getString(R.string.op_error));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case 0x001:
                try {
                    Uri uri = data.getParcelableExtra("VAL");
                    if (uri != null && !TextUtils.isEmpty(uri.getPath())) upload(uri);
                } catch (Exception e) {
                    //
                }
                break;
        }
    }

    private void upload(Uri uri) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                if (isActive)
                    dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                if (isActive)
                    dlg.dismiss();
                if (isSuccess) {
                    if (BaseFunc.isValidImgUrl(data)) {
                        evaluateAdapter.picAdapters.get(evaluateAdapter.currentSection).list.add(0, data);
                        evaluateAdapter.picAdapters.get(evaluateAdapter.currentSection).notifyDataSetChanged();
                    }
                }
            }
        }).upload();
    }

    /**
     * 提交评价内容
     *
     * @param is_anonymous         是否匿名
     * @param store_desccredit     店铺评分
     * @param store_servicecredit  服务评分
     * @param store_deliverycredit 派送评分
     * @param isAdded              是否为追加评价 true 追评 false 评价
     */
    @Override
    public void onGoodsEvaluateSubmit(int is_anonymous, float store_desccredit, float store_servicecredit, float store_deliverycredit, boolean isAdded) {
        evaluate.member = member;
        evaluate.order_id = order_id;
        if (!isAdded) {
            //直接评价的参数
            evaluate.is_append = 0;
            evaluate.is_anonymous = is_anonymous;
            evaluate.store_desccredit = store_desccredit;
            evaluate.store_servicecredit = store_servicecredit;
            evaluate.store_deliverycredit = store_deliverycredit;
        } else {
            //追评参数
            evaluate.is_append = 1;
        }

        /**
         * 商品评价集合
         */
        List<EvaSubGoodsEntity> list = new ArrayList<>();
        EvaSubGoodsEntity resultEntity;
        for (int i = 0; i < evaluateAdapter.list.size(); i++) {
            resultEntity = new EvaSubGoodsEntity();
            resultEntity.goods_id = evaluateAdapter.list.get(i).goods_id;
            resultEntity.comment = evaluateAdapter.list.get(i).comment;
            if (!isAdded) {
                //直接评价时才传星数
                resultEntity.stars = evaluateAdapter.list.get(i).stars;
            }
            resultEntity.images = BaseFunc.list2QuoteStr(evaluateAdapter.picAdapters.get(i).list.subList(0, evaluateAdapter.picAdapters.get(i).list.size() - 1));
            list.add(resultEntity);
        }
        evaluate.evaluate_goods = list;

        evaluate.evaluate_goods(mContext);//发起请求

        sad.show();
    }

    @Override
    protected void onStop() {
        isActive = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        isActive = false;
        super.onDestroy();
    }

}
