package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreePersonalMessageBean;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/6.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalMessageHolder extends RecyclerView.ViewHolder {

    private TextView tvPhoneNum;
    private TextView tvTime;
    private TextView tvMoney;
    private static Context mContext;//有可能会出现内存泄漏，后期数据下来不再需要Toast
    private ImageView ivMsg;

    public DutyfreePersonalMessageHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View itemView) {
        tvPhoneNum = (TextView) itemView.findViewById(R.id.tvPhoneNum);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
        ivMsg = (ImageView) itemView.findViewById(R.id.ivMsg);
        itemView.setTag(this);
    }

    public static DutyfreePersonalMessageHolder getHolder(Context context) {
        View view = View.inflate(context, R.layout.item_holder_dutyfreepersonalmessage, null);
        mContext = context;
        return new DutyfreePersonalMessageHolder(view);
    }

    public void bindData2ItemHolder(int position, DutyfreePersonalMessageBean.AwardListBean date) {
        tvMoney.setText(date.pay_money);
        tvPhoneNum.setText(date.phone_num);
        tvTime.setText(date.curtime);
        FHImageViewUtil fhImageViewUtil = new FHImageViewUtil(ivMsg);
        fhImageViewUtil.setImageURI(date.pay_img, FHImageViewUtil.SHOWTYPE.BANNER);
    }
}
