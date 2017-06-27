package com.fanglin.fenhong.microbuyer.common;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 作者： Created by Plucky on 15-10-4.
 */
public class MsgCenterActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.tvIcon1)
    TextView tvIcon1;
    @ViewInject(R.id.tvIcon2)
    TextView tvIcon2;
    @ViewInject(R.id.tvIcon3)
    TextView tvIcon3;
    @ViewInject(R.id.tvIcon4)
    TextView tvIcon4;
    @ViewInject(R.id.tvIcon5)
    TextView tvIcon5;
    @ViewInject(R.id.tvIcon6)
    TextView tvIcon6;
    @ViewInject(R.id.tvIcon7)
    TextView tvIcon7;
    @ViewInject(R.id.tvIcon8)
    TextView tvIcon8;
    @ViewInject(R.id.tvIcon9)
    TextView tvIcon9;

    @ViewInject(R.id.tv_num_activity)
    TextView tv_num_activity;
    @ViewInject(R.id.tv_num_goods)
    TextView tv_num_goods;
    @ViewInject(R.id.tv_num_delivery)
    TextView tv_num_delivery;
    @ViewInject(R.id.tv_num_income)
    TextView tv_num_income;
    @ViewInject(R.id.tv_num_sys)
    TextView tv_num_sys;
    @ViewInject(R.id.tv_num_team)
    TextView tv_num_team;
    @ViewInject(R.id.tv_msg17_num)
    TextView tv_msg17_num;

    @ViewInject(R.id.tv_last_12)
    TextView tv_last_12;
    @ViewInject(R.id.tv_last_13)
    TextView tv_last_13;
    @ViewInject(R.id.tv_last_14)
    TextView tv_last_14;
    @ViewInject(R.id.tv_last_15)
    TextView tv_last_15;
    @ViewInject(R.id.tv_last_9)
    TextView tv_last_9;
    @ViewInject(R.id.tv_last_16)
    TextView tv_last_16;
    @ViewInject(R.id.tv_last_17)
    TextView tv_last_17;

    @ViewInject(R.id.tvMsg18Num)
    TextView tvMsg18Num;
    @ViewInject(R.id.tvLast18)
    TextView tvLast18;
    @ViewInject(R.id.vFans)
    View vFans;
    @ViewInject(R.id.LMSG19)
    LinearLayout LMSG19;
    @ViewInject(R.id.tvMsg19Num)
    TextView tvMsg19Num;
    @ViewInject(R.id.tvLast19)
    TextView tvLast19;

    Typeface iconFontExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_msgcenter, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        iconFontExtra = BaseFunc.geticonFontTypeExtra(mContext);
        setHeadTitle(R.string.msgcenter);
        tvIcon1.setTypeface(iconfont);
        tvIcon2.setTypeface(iconfont);
        tvIcon3.setTypeface(iconfont);
        tvIcon4.setTypeface(iconfont);
        tvIcon5.setTypeface(iconfont);
        tvIcon6.setTypeface(iconfont);
        tvIcon7.setTypeface(iconfont);
        tvIcon8.setTypeface(iconFontExtra);
        tvIcon9.setTypeface(iconFontExtra);
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        LayoutMoreVertical layoutMoreVertical = new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setShareData(null);
        layoutMoreVertical.setIsMsgShow(false);
        layoutMoreVertical.show();
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
        RefreshNum();
    }

    @OnClick(value = {R.id.LGoodsActivity, R.id.LOrder, R.id.LDelivery,
            R.id.LIncome, R.id.Lsys, R.id.LTeam, R.id.LMSG17,
            R.id.LMSG18, R.id.LMSG19})
    public void onViewClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.LGoodsActivity:
                bundle.putString("TITLE", getString(R.string.goods_activity));
                bundle.putInt("TYPE", 12);
                FHCache.onMsgClick(this, 12);
                break;
            case R.id.LOrder:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.goods_notice));
                bundle.putInt("TYPE", 13);
                FHCache.onMsgClick(this, 13);
                break;
            case R.id.LDelivery:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.delivery_notice));
                bundle.putInt("TYPE", 14);
                FHCache.onMsgClick(this, 14);
                break;
            case R.id.LIncome:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.income));
                bundle.putInt("TYPE", 15);
                FHCache.onMsgClick(this, 15);
                break;
            case R.id.Lsys:
                bundle.putString("TITLE", getString(R.string.sys_notice));
                bundle.putInt("TYPE", 9);
                FHCache.onMsgClick(this, 9);
                break;
            case R.id.LTeam:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.team));
                bundle.putInt("TYPE", 16);
                FHCache.onMsgClick(this, 16);
                break;
            case R.id.LMSG17:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.msg17_notice));
                bundle.putInt("TYPE", 17);
                FHCache.onMsgClick(this, 17);
                break;
            case R.id.LMSG18:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.msg_18_title));
                bundle.putInt("TYPE", 18);
                FHCache.onMsgClick(this, 18);
                break;
            case R.id.LMSG19:
                if (member == null) {
                    BaseFunc.gotoLogin(this);
                    return;
                }
                bundle.putString("TITLE", getString(R.string.msg_19_title));
                bundle.putInt("TYPE", 19);
                FHCache.onMsgClick(this, 19);
                break;
        }

        BaseFunc.gotoActivityBundle(this, MsgListActivity.class, bundle);
    }

    private void RefreshNum() {
        if (msgnum != null) {
            if (msgnum.msg17_num > 0) {
                tv_msg17_num.setVisibility(View.VISIBLE);
                tv_msg17_num.setText(String.valueOf(msgnum.msg17_num));
            } else {
                tv_msg17_num.setVisibility(View.INVISIBLE);
            }
            tv_last_17.setText(msgnum.get_msg17_msg(mContext));

            if (msgnum.team_num > 0) {
                tv_num_team.setVisibility(View.VISIBLE);
                tv_num_team.setText(String.valueOf(msgnum.team_num));
            } else {
                tv_num_team.setVisibility(View.INVISIBLE);
            }
            tv_last_16.setText(msgnum.get_team_msg(mContext));

            if (msgnum.sys_msg_num > 0) {
                tv_num_sys.setVisibility(View.VISIBLE);
                tv_num_sys.setText(String.valueOf(msgnum.sys_msg_num));
            } else {
                tv_num_sys.setVisibility(View.INVISIBLE);
            }
            tv_last_9.setText(msgnum.get_sys_msg(mContext));

            if (msgnum.income_num > 0) {
                tv_num_income.setVisibility(View.VISIBLE);
                tv_num_income.setText(String.valueOf(msgnum.income_num));
            } else {
                tv_num_income.setVisibility(View.INVISIBLE);
            }
            tv_last_15.setText(msgnum.get_income_msg(mContext));

            if (msgnum.delivery_notice_num > 0) {
                tv_num_delivery.setVisibility(View.VISIBLE);
                tv_num_delivery.setText(String.valueOf(msgnum.delivery_notice_num));
            } else {
                tv_num_delivery.setVisibility(View.INVISIBLE);
            }
            tv_last_14.setText(msgnum.get_delivery_notice_msg(mContext));

            if (msgnum.order_notice_num > 0) {
                tv_num_goods.setVisibility(View.VISIBLE);
                tv_num_goods.setText(String.valueOf(msgnum.order_notice_num));
            } else {
                tv_num_goods.setVisibility(View.INVISIBLE);
            }
            tv_last_13.setText(msgnum.get_order_notice_msg(mContext));

            if (msgnum.goods_activity_num > 0) {
                tv_num_activity.setVisibility(View.VISIBLE);
                tv_num_activity.setText(String.valueOf(msgnum.goods_activity_num));
            } else {
                tv_num_activity.setVisibility(View.INVISIBLE);
            }
            tv_last_12.setText(msgnum.get_goods_activity_msg(mContext));

            if (msgnum.msg18Num > 0) {
                tvMsg18Num.setText(String.valueOf(msgnum.msg18Num));
                tvMsg18Num.setVisibility(View.VISIBLE);
            } else {
                tvMsg18Num.setVisibility(View.INVISIBLE);
            }
            tvLast18.setText(msgnum.getMsg18Msg(mContext));

            if (msgnum.msg19Num > 0) {
                tvMsg19Num.setText(String.valueOf(msgnum.msg19Num));
                tvMsg19Num.setVisibility(View.VISIBLE);
            } else {
                tvMsg19Num.setVisibility(View.INVISIBLE);
            }
            tvLast19.setText(msgnum.getMsg19Msg(mContext));

        } else {
            tv_msg17_num.setVisibility(View.INVISIBLE);
            tv_msg17_num.setText("0");
            tv_num_team.setVisibility(View.INVISIBLE);
            tv_num_team.setText("0");
            tv_num_sys.setVisibility(View.INVISIBLE);
            tv_num_sys.setText("0");
            tv_num_income.setVisibility(View.INVISIBLE);
            tv_num_income.setText("0");
            tv_num_delivery.setVisibility(View.INVISIBLE);
            tv_num_delivery.setText("0");
            tv_num_goods.setVisibility(View.INVISIBLE);
            tv_num_goods.setText("0");
            tv_num_activity.setVisibility(View.INVISIBLE);
            tv_num_activity.setText("0");

            tvMsg18Num.setText("0");
            tvMsg18Num.setVisibility(View.INVISIBLE);
            tvMsg19Num.setText("0");
            tvMsg19Num.setVisibility(View.INVISIBLE);
        }

        if (member != null && member.talent_id > 0) {
            LMSG19.setVisibility(View.VISIBLE);
            vFans.setVisibility(View.VISIBLE);
        } else {
            LMSG19.setVisibility(View.GONE);
            vFans.setVisibility(View.GONE);
        }
    }
}
