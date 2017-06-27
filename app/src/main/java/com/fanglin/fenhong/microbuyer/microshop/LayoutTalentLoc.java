package com.fanglin.fenhong.microbuyer.microshop;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.R;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/15-下午7:04.
 * 功能描述: 达人发表时光定位操作的控件
 */
public class LayoutTalentLoc implements View.OnClickListener {
    private TextView tvUseLoc;
    private Dialog dialog;
    FHLocation fhLocation;

    public LayoutTalentLoc(Context mContext) {
        View view = View.inflate(mContext, R.layout.layout_talent_location, null);
        TextView tvNotLoc = (TextView) view.findViewById(R.id.tvNotLoc);
        tvUseLoc = (TextView) view.findViewById(R.id.tvUseLoc);
        TextView tvReLoc = (TextView) view.findViewById(R.id.tvReLoc);
        dialog = new Dialog(mContext, R.style.FHDialog);
        dialog.setContentView(view);

        view.findViewById(R.id.LContainer).setOnClickListener(this);
        view.findViewById(R.id.LSubContainer).setOnClickListener(this);
        tvNotLoc.setOnClickListener(this);
        tvUseLoc.setOnClickListener(this);
        tvReLoc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.LSubContainer) {
            dialog.dismiss();
        }
        if (listner != null) {
            switch (v.getId()) {
                case R.id.tvNotLoc:
                    listner.onLocation(false, null);
                    break;
                case R.id.tvUseLoc:
                    listner.onLocation(true, fhLocation);
                    break;
                case R.id.tvReLoc:
                    listner.onReLocation();
                    break;
            }
        }
    }

    public void show(FHLocation fhLocation) {
        this.fhLocation = fhLocation;
        if (fhLocation == null) {
            tvUseLoc.setVisibility(View.GONE);
        } else {
            tvUseLoc.setVisibility(View.VISIBLE);
            tvUseLoc.setText(formatAddrDesc(fhLocation.getAddrDesc()));
        }
        dialog.show();
    }

    public interface LayoutTalentLocListner {
        /**
         * @param useLoc     是否使用定位地址
         * @param fhLocation 如果使用定位地址则显示地址  否则显示 不显示位置
         */
        void onLocation(boolean useLoc, FHLocation fhLocation);

        /**
         * 重新使用地址
         */
        void onReLocation();
    }

    private LayoutTalentLocListner listner;

    public void setListner(LayoutTalentLocListner listner) {
        this.listner = listner;
    }

    private String formatAddrDesc(String addr) {
        return "使用【" + addr + "】";
    }
}
