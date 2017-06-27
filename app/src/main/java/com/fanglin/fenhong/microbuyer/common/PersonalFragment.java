package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.MainActivity;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.FHLoginEvent;
import com.fanglin.fenhong.microbuyer.base.model.AppInfo;
import com.fanglin.fenhong.microbuyer.base.model.PersonalNum;
import com.fanglin.fenhong.microbuyer.buyer.AddressActivity;
import com.fanglin.fenhong.microbuyer.buyer.FavoritesActivity;
import com.fanglin.fenhong.microbuyer.buyer.LoginActivity;
import com.fanglin.fenhong.microbuyer.buyer.ProfileActivity;
import com.fanglin.fenhong.microbuyer.microshop.BalanceActivity;
import com.fanglin.fenhong.microbuyer.microshop.BankCardListActivity;
import com.fanglin.fenhong.microbuyer.microshop.BonusActivity;
import com.fanglin.fenhong.microbuyer.microshop.CommissionActivity;
import com.fanglin.fenhong.microbuyer.microshop.FansListActivity;
import com.fanglin.fenhong.microbuyer.microshop.LayoutBonus;
import com.fanglin.fenhong.microbuyer.microshop.ShareListActivity;
import com.fanglin.fenhong.microbuyer.microshop.TalentActivity;
import com.fanglin.fenhong.microbuyer.microshop.TalentTimesActivity;
import com.fanglin.fenhong.microbuyer.microshop.TeamActivity;
import com.fanglin.fhlib.other.PreferenceUtils;
import com.fanglin.fhui.CircleImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * 作者： Created by Plucky on 2015/8/18.
 * 我的页面
 * modify by lizhixin on 2016/04/22
 */
public class PersonalFragment extends BaseFragment {
    View v;

    @ViewInject(R.id.scrollView)
    ScrollView scrollView;
    @ViewInject(R.id.tvMsgNum)
    TextView tvMsgNum;
    @ViewInject(R.id.circleImageView)
    CircleImageView circleImageView;
    @ViewInject(R.id.LChkin)
    LinearLayout LChkin;
    @ViewInject(R.id.tvNick)
    TextView tvNick;
    @ViewInject(R.id.tvUserName)
    TextView tvUserName;
    @ViewInject(R.id.LIcon)
    LinearLayout LIcon;

    @ViewInject(R.id.tvNumGoodsConllect)
    TextView tvNumGoodsConllect;
    @ViewInject(R.id.tvNumStoresConllect)
    TextView tvNumStoresConllect;
    @ViewInject(R.id.tvNumMicroshopConllect)
    TextView tvNumMicroshopConllect;
    @ViewInject(R.id.tvNumCollectionDr)
    TextView tvNumCollectionDr;//收藏达人
    @ViewInject(R.id.tvNumCollectionTime)
    TextView tvNumCollectionTime;//收藏时光

    @ViewInject(R.id.tvNumBrand)
    TextView tvNumBrand;

    @ViewInject(R.id.ivZoomBg)
    ImageView ivZoomBg;

    @ViewInject(R.id.LContent)
    LinearLayout LContent;
    @ViewInject(R.id.tvNumPay)
    TextView tvNumPay;
    @ViewInject(R.id.tvNumRelease)
    TextView tvNumRelease;
    @ViewInject(R.id.tvNumDelivery)
    TextView tvNumDelivery;
    @ViewInject(R.id.tvNumEvaluate)
    TextView tvNumEvaluate;
    @ViewInject(R.id.tvNumServices)
    TextView tvNumServices;


    @ViewInject(R.id.tvBalance)
    TextView tvBalance;
    @ViewInject(R.id.tvCoupon)
    TextView tvCoupon;
    @ViewInject(R.id.tvCouponRed)
    TextView tvCouponRed;
    @ViewInject(R.id.tvPoints)
    TextView tvPoints;
    @ViewInject(R.id.tvBankCard)
    TextView tvBankCard;

    @ViewInject(R.id.LMicroshopManage)
    LinearLayout LMicroshopManage;
    @ViewInject(R.id.tvReferee)
    TextView tvReferee;

    @ViewInject(R.id.LTalent)
    LinearLayout LTalent;

    @ViewInject(R.id.tvBot)
    TextView tvBot;

    @ViewInject(R.id.tvBadgeTips)
    TextView tvBadgeTips;
    @ViewInject(R.id.tvBadge)
    TextView tvBadge;
    @ViewInject(R.id.tvShopType)
    TextView tvShopType;


    MainActivity act;
    LayoutBonus layoutBonus;
    private int shopType;// 微店类型

    LayoutMyQrcode layoutMyQrcode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = (MainActivity) getActivity();
        v = View.inflate(act, R.layout.fragment_personal, null);
        ViewUtils.inject(this, v);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getNum();
        RefreshView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return v;
    }


    private void RefreshView() {
        if (member != null) {
            circleImageView.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            LChkin.setVisibility(View.GONE);
            ivZoomBg.setBackgroundResource(R.drawable.bg_login);

            if (!TextUtils.isEmpty(member.member_nickname)) {
                //有昵称显示昵称 没昵称显示用户名
                tvNick.setVisibility(View.VISIBLE);
                tvUserName.setVisibility(View.GONE);

                tvNick.setText(member.member_nickname);
            } else {
                tvUserName.setVisibility(View.VISIBLE);
                tvNick.setVisibility(View.GONE);

                tvUserName.setText(member.member_name);
            }

            new FHImageViewUtil(circleImageView).setImageURI(member.getMember_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);

            if (member.talent_id > 0) {
                LTalent.setVisibility(View.VISIBLE);
            } else {
                LTalent.setVisibility(View.GONE);
            }

        } else {
            ivZoomBg.setBackgroundResource(R.drawable.bg_not_login);
            circleImageView.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            tvNick.setVisibility(View.GONE);
            LChkin.setVisibility(View.VISIBLE);
            LTalent.setVisibility(View.GONE);

            refreshNum(new PersonalNum());
        }

        if (msgnum != null && msgnum.getTotalNum() > 0) {
            tvMsgNum.setText(String.valueOf(msgnum.getTotalNum()));
            tvMsgNum.setVisibility(View.VISIBLE);
        } else {
            tvMsgNum.setText("0");
            tvMsgNum.setVisibility(View.INVISIBLE);
        }

        AppInfo appInfo = new AppInfo(act);
        tvBot.setText(appInfo.getAppinfo4Debug());
    }

    /**
     * 获取个人中心数字提醒
     */
    private void getNum() {
        if (member == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    parseNum(data);
                }
            }
        }).ucenter(member.member_id, member.token);
    }

    /**
     * 根据缓存和网络请求更新数字
     */
    private void parseNum(String data) {
        try {
            PreferenceUtils.setPrefString(act, BaseVar.UCENTER, data);
            PersonalNum anum = new Gson().fromJson(data, PersonalNum.class);
            refreshNum(anum);
        } catch (Exception e) {
            //
        }
    }

    /**
     * 更新数字
     */
    private void refreshNum(PersonalNum num) {
        if (num != null) {

            tvNumGoodsConllect.setText(num.goods_favorite_count);
            tvNumStoresConllect.setText(num.store_favorite_count);
            tvNumMicroshopConllect.setText(num.microshop_favorite_count);
            tvNumCollectionTime.setText(num.time_favorite_count);
            tvNumCollectionDr.setText(num.talent_favorite_count);

            tvNumBrand.setText(num.brand_favorite_count);

            //我的钱包
            tvBalance.setText(num.get_member_balanceDesc());
            tvCoupon.setText(String.valueOf(num.get_coupon_count()));
            tvPoints.setText(String.valueOf(num.get_member_points()));
            tvBankCard.setText(String.valueOf(num.get_bank_card_count()));

            if (num.get_wait_paid_order() == 0) {
                tvNumPay.setVisibility(View.INVISIBLE);
            } else {
                tvNumPay.setVisibility(View.VISIBLE);
                tvNumPay.setText(String.valueOf(num.wait_paid_order_count));
            }

            if (num.get_wait_send_order() == 0) {
                tvNumRelease.setVisibility(View.INVISIBLE);
            } else {
                tvNumRelease.setVisibility(View.VISIBLE);
                tvNumRelease.setText(String.valueOf(num.wait_send_order_count));
            }

            if (num.get_wait_receive_order() == 0) {
                tvNumDelivery.setVisibility(View.INVISIBLE);
            } else {
                tvNumDelivery.setVisibility(View.VISIBLE);
                tvNumDelivery.setText(String.valueOf(num.wait_receive_order_count));
            }

            if (num.get_wait_evaluate_order() == 0) {
                tvNumEvaluate.setVisibility(View.INVISIBLE);
            } else {
                tvNumEvaluate.setVisibility(View.VISIBLE);
                tvNumEvaluate.setText(String.valueOf(num.wait_evaluate_order_count));
            }

            if (num.get_service_order() == 0) {
                tvNumServices.setVisibility(View.INVISIBLE);
            } else {
                tvNumServices.setVisibility(View.VISIBLE);
                tvNumServices.setText(String.valueOf(num.service_order_count));
            }

            if (!TextUtils.isEmpty(num.batch_id)) {
                layoutBonus.show(num.bag_name);
            }

            if (num.newcoming_coupon_count == 0) {
                tvCouponRed.setVisibility(View.INVISIBLE);
            } else {
                tvCouponRed.setVisibility(View.VISIBLE);
                tvCouponRed.setText(String.valueOf(num.newcoming_coupon_count));
            }

            //分享达人的判断依据是 member_degree
            if (num.member_degree == 1) {
                LMicroshopManage.setVisibility(View.VISIBLE);
                tvReferee.setVisibility(View.VISIBLE);
                tvBadge.setSelected(true);
                tvBadgeTips.setVisibility(View.GONE);

                String sharerName;
                if (!TextUtils.isEmpty(num.sharer_name)) {
                    sharerName = String.format(getString(R.string.fmt_referee), num.sharer_name);
                } else {
                    sharerName = String.format(getString(R.string.fmt_referee), getString(R.string.app_name));
                }
                tvReferee.setText(BaseFunc.fromHtml(sharerName));
            } else {
                tvBadge.setSelected(false);
                LMicroshopManage.setVisibility(View.GONE);
                tvReferee.setVisibility(View.GONE);
                tvBadgeTips.setVisibility(View.VISIBLE);
            }

            shopType = num.shop_type;
            tvShopType.setText(num.getShopTypeName());

            if (num.jump2Url()) {
                BaseFunc.gotoActivity(act, FHBrowserActivity.class, num.jump_url);
            }

            FHCache.setMemberType(act, num.member_type);
            EventBus.getDefault().post(new FHLoginEvent());

            layoutMyQrcode.refreshData(num, member);
        } else {
            /** 数字提醒-- 收藏相关*/
            tvNumGoodsConllect.setText("0");
            tvNumStoresConllect.setText("0");
            tvNumMicroshopConllect.setText("0");

            /** 数字提醒-- 订单相关*/
            tvNumPay.setVisibility(View.INVISIBLE);
            tvNumRelease.setVisibility(View.INVISIBLE);
            tvNumDelivery.setVisibility(View.INVISIBLE);
            tvNumEvaluate.setVisibility(View.INVISIBLE);
            tvNumServices.setVisibility(View.INVISIBLE);

            tvCouponRed.setVisibility(View.INVISIBLE);

            LMicroshopManage.setVisibility(View.GONE);
            tvReferee.setVisibility(View.GONE);
        }
    }

    @OnClick(value = {R.id.ivAlert, R.id.FMsg, R.id.circleImageView, R.id.LChkin, R.id.tvBadgeTips,
            R.id.LGoods, R.id.LShop, R.id.LFavBrand, R.id.LHistory, R.id.LFavShop, R.id.llCollectionDR, R.id.llCollectionTime,
            R.id.LOrder, R.id.LPay, R.id.LRelease, R.id.LDelivery, R.id.LEvaluate, R.id.LServices,
            R.id.LlWallet, R.id.LlBalance, R.id.LlCoupon, R.id.LlPoints, R.id.LBankCard,
            R.id.LQrcode, R.id.LCommission, R.id.LTeam, R.id.LShare, R.id.LBeMicroShop,
            R.id.LHowtoShare, R.id.LAddr, R.id.LSettings, R.id.LAdvice, R.id.LCall,
            R.id.llDR, R.id.llMyHome, R.id.llMyTime, R.id.llMyFans, R.id.llIncome})
    public void onViewClick(View v) {

        switch (v.getId()) {
            case R.id.ivAlert:
                BaseFunc.gotoActivity(act, AOGRemindListActivity.class, null);
                break;
            case R.id.FMsg:
                BaseFunc.gotoActivity(act, MsgCenterActivity.class, null);
                break;
            case R.id.circleImageView:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, ProfileActivity.class, null);
                break;
            case R.id.LChkin:
                BaseFunc.gotoLogin(act);
                break;
            case R.id.tvBadgeTips:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FHBrowserActivity.class, BaseVar.URL_TO_BE_SHARER);
                break;
            case R.id.LGoods:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "0");
                break;
            case R.id.LShop:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "1");
                break;

            case R.id.LFavShop:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "2");
                break;
            case R.id.LFavBrand:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "2");
                break;
            case R.id.LHistory:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "3");
                break;
            case R.id.llCollectionDR:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "3");
                break;
            case R.id.llCollectionTime:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, FavoritesActivity.class, "4");
                break;

            /** =========华丽的分割线=========*/

            case R.id.LOrder:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, OrderActivity.class, "0");
                break;

            case R.id.LPay:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, OrderActivity.class, "10");
                break;

            case R.id.LRelease:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, OrderActivity.class, "20");
                break;

            case R.id.LDelivery:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, OrderActivity.class, "30");
                break;

            case R.id.LEvaluate:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, OrderActivity.class, "100");
                break;
            case R.id.LServices:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, ReturnGoodsListActivity.class, null);
                break;
            /** ==============达人相关============ */
            case R.id.llDR:
            case R.id.llMyHome:
                if (member != null) {
                    BaseFunc.gotoActivity(act, TalentActivity.class, String.valueOf(member.talent_id));
                }
                break;
            case R.id.llMyTime:
                if (member != null) {
                    BaseFunc.gotoActivity(act, TalentTimesActivity.class, String.valueOf(member.talent_id));
                }
                break;
            case R.id.llMyFans:
                if (member != null) {
                    BaseFunc.gotoActivity(act, FansListActivity.class, String.valueOf(member.talent_id));
                }
                break;
            case R.id.llIncome:
                if (member != null) {
                    BaseFunc.gotoActivity(act, CommissionActivity.class, "1");
                }
                break;

            /** =========华丽的分割线=========*/

            case R.id.LlWallet:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, BalanceActivity.class, null);
                break;

            case R.id.LlBalance:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, BalanceActivity.class, null);
                break;

            case R.id.LlCoupon:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                String val = tvCouponRed.getVisibility() == View.VISIBLE ? "A" : null;

                BaseFunc.gotoActivity(act, BonusActivity.class, val);
                break;
            case R.id.LlPoints:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, PointsActivity.class, null);
                break;

            case R.id.LBankCard:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, BankCardListActivity.class, null);
                break;

            /** 二维码 */
            case R.id.LQrcode:
                if (layoutMyQrcode.isHasDraw())
                    layoutMyQrcode.show();
                break;
            case R.id.LCommission:
                BaseFunc.gotoActivity(act, CommissionActivity.class, "0");
                break;
            case R.id.LTeam:
                BaseFunc.gotoActivity(act, TeamActivity.class, null);
                break;
            case R.id.LBeMicroShop:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                if (shopType == 0) {
                    BaseFunc.gotoActivity(act, FHBrowserActivity.class, BaseVar.URL_TO_GET_VDIAN);
                } else {
                    gotoMicroShopWap();
                }
                break;
            case R.id.LShare:
                BaseFunc.gotoActivity(act, ShareListActivity.class, null);
                break;
            case R.id.LHowtoShare:
                BaseFunc.gotoActivity(act, FHBrowserActivity.class, BaseVar.URL_HOW_TO_SHARE);
                break;

            case R.id.LAddr:
                if (member == null) {
                    BaseFunc.gotoLogin(act);
                    return;
                }
                BaseFunc.gotoActivity(act, AddressActivity.class, null);
                break;

            case R.id.LSettings:
                BaseFunc.gotoActivity4Result(this, SettingActivity.class, null, LoginActivity.LOGIN_REQ);
                break;
            case R.id.LAdvice:
                BaseFunc.gotoActivity(act, FeedBackActivity.class, null);
                break;
            case R.id.LCall:
                BaseFunc.Call(act, "4006888506");
                break;
            default:
                break;
        }
    }

    private void gotoMicroShopWap() {
        if (shopType > 0) {
            String vUrl = String.format(BaseVar.URL_EXPLORER_VDIAN, shopType);
            BaseFunc.gotoActivity(act, FHBrowserActivity.class, vUrl);
        }
    }

    private void initView() {
        BaseFunc.setFont(LContent);
        BaseFunc.setFont(LChkin);
        BaseFunc.setFont(LIcon);
        layoutBonus = new LayoutBonus(act);

        layoutMyQrcode = new LayoutMyQrcode(act);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.LOGIN_REQ && resultCode == Activity.RESULT_OK) {
            scrollView.smoothScrollTo(0, 0);
        }
    }
}
