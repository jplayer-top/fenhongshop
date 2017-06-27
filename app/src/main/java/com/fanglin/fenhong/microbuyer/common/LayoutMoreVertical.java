package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.StoreHomeInfo;
import com.fanglin.fenhong.microbuyer.buyer.SearchActivity;

import cn.sharesdk.fhshare.model.FHShareItem;

/**
 * 更多选项弹框
 * 作者： Created by Plucky on 15-10-18.
 * modify by lizhixin on 2015/12/25.
 */
public class LayoutMoreVertical implements View.OnClickListener {
    private Context mContext;
    private View attachView;
    TextView tv_msg_num;
    LinearLayout LContent, ll_msg, ll_share, ll_home, ll_search, lJuBao, lDelete, lContact;
    private PopupWindow pw;

    private ShareData shareData;
    private boolean isMsgShow = true;//设置消息中心选项是否显示, 默认显示
    private boolean isHomeShow = true;//设置回首页选项是否显示, 默认显示
    private boolean isSearchShow = true;//设置搜索选项是否显示, 默认显示
    private int msgNum;//消息个数
    private boolean isMiddle = false;
    private boolean isJubao, isDeleted;
    private int screenWidth, layoutWidth, topHeight;

    private String contactUrl;

    public LayoutMoreVertical(View attachView) {
        this.attachView = attachView;
        this.mContext = attachView.getContext();
        screenWidth = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        layoutWidth = BaseFunc.dp2px(mContext, 128);
        topHeight = BaseFunc.dp2px(mContext, 50);

        View view = View.inflate(mContext, R.layout.layout_more_vertical, null);
        tv_msg_num = (TextView) view.findViewById(R.id.tv_msg_num);

        LContent = (LinearLayout) view.findViewById(R.id.LContent);
        ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);
        ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
        ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);

        lJuBao = (LinearLayout) view.findViewById(R.id.lJuBao);
        lDelete = (LinearLayout) view.findViewById(R.id.lDelete);

        lContact = (LinearLayout) view.findViewById(R.id.lContact);

        ll_msg.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        lJuBao.setOnClickListener(this);
        lDelete.setOnClickListener(this);
        lContact.setOnClickListener(this);

        pw = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.update();
    }

    /**
     * 设定页面分享数据
     */
    public void setShareData(ShareData shareData) {
        this.shareData = shareData;
    }

    /**
     * 设置消息中心选项是否显示
     */
    public void setIsMsgShow(boolean isMsgShow) {
        this.isMsgShow = isMsgShow;
    }

    /**
     * 设置回首页选项是否显示
     */
    public void setIsHomeShow(boolean isHomeShow) {
        this.isHomeShow = isHomeShow;
    }

    /**
     * 设置搜索选项是否显示
     */
    public void setIsSearchShow(boolean isSearchShow) {
        this.isSearchShow = isSearchShow;
    }

    /**
     * 设置消息数量
     */
    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }


    /**
     * 联系客服Url
     *
     * @param contactUrl url
     */
    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public ShareData getShareData() {
        return shareData;
    }

    /**
     * 设置背景图片是否为居中的图片
     *
     * @param isMiddle true false
     */
    public void setMiddle(boolean isMiddle) {
        this.isMiddle = isMiddle;
    }

    public void setJubao(boolean jubao) {
        isJubao = jubao;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void show() {
        ll_home.setVisibility(isHomeShow ? View.VISIBLE : View.GONE);
        ll_search.setVisibility(isSearchShow ? View.VISIBLE : View.GONE);
        ll_share.setVisibility(shareData == null ? View.GONE : View.VISIBLE);

        lContact.setVisibility(BaseFunc.isValidUrl(contactUrl) ? View.VISIBLE : View.GONE);

        lJuBao.setVisibility(isJubao ? View.VISIBLE : View.GONE);
        lDelete.setVisibility(isDeleted ? View.VISIBLE : View.GONE);

        if (isMsgShow) {
            ll_msg.setVisibility(View.VISIBLE);
            //如果显示消息的话先判断消息数量
            if (msgNum > 0) {
                tv_msg_num.setVisibility(View.VISIBLE);
            } else {
                tv_msg_num.setVisibility(View.GONE);
            }
        } else {
            ll_msg.setVisibility(View.GONE);
        }


        LContent.setBackgroundResource(isMiddle ? R.drawable.bg_msg_middle_more : R.drawable.bg_msg_more);

        int[] loc = new int[2];
        attachView.getLocationOnScreen(loc);
        int dx = isMiddle ? (40 + loc[0] - layoutWidth / 2) : (screenWidth - layoutWidth - 20);
        int dy = loc[1] + (loc[1] > topHeight ? 0 : topHeight);

        pw.showAtLocation(attachView, Gravity.NO_GRAVITY, dx, dy);
    }

    public void dismiss() {
        pw.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                dismiss();
                BaseFunc.gotoHome(mContext, 0);
                break;
            case R.id.ll_share:
                if (shareData.storeInfo != null) {
                    shareData.cutLastOne = true;
                    FHShareItem item = new FHShareItem();
                    item.setName("二维码");
                    item.setIcon(R.drawable.fhshare_qrcode);
                    item.setListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutShopQrcode layoutShopQrcode = new LayoutShopQrcode(mContext);
                            layoutShopQrcode.refreshView(shareData.storeInfo);
                            layoutShopQrcode.show();
                        }
                    });
                    ShareData.fhShareByItems((BaseFragmentActivity) mContext, shareData, null, item);
                } else {
                    ShareData.fhShare((BaseFragmentActivity) mContext, shareData, null);
                }
                break;
            case R.id.ll_msg:
                BaseFunc.gotoActivity(mContext, MsgCenterActivity.class, null);
                break;
            case R.id.ll_search:
                BaseFunc.gotoActivity(mContext, SearchActivity.class, null);
                break;
            case R.id.lJuBao:
                if (listener != null) {
                    listener.onJubao();
                }
                break;
            case R.id.lDelete:
                if (listener != null) {
                    listener.onDelete();
                }
                break;
            case R.id.lContact:
                BaseFunc.urlClick(mContext, contactUrl);
                break;
        }
        pw.dismiss();
    }

    private LayoutMoreVerticalListener listener;

    public void setListener(LayoutMoreVerticalListener listener) {
        this.listener = listener;
    }

    public interface LayoutMoreVerticalListener {
        void onDelete();

        void onJubao();
    }

}
