package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.fanglin.fenhong.microbuyer.MainActivity;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.buyer.SearchActivity;

/**
 * 作者： Created by Plucky on 2015/12/18.
 * 更多水平布局
 */
public class LayoutMoreHorizonal implements View.OnClickListener {

    private Context mContext;
    private View attachView;
    private PopupWindow pw;

    private ShareData shareData;

    public LayoutMoreHorizonal (View attachView) {
        this.attachView = attachView;
        this.mContext = attachView.getContext ();

        ViewGroup view = (ViewGroup) View.inflate (mContext, R.layout.layout_more_horizontal, null);
        LinearLayout tv_home = (LinearLayout) view.findViewById (R.id.tv_home);
        LinearLayout tv_search = (LinearLayout) view.findViewById (R.id.tv_search);
        LinearLayout tv_share = (LinearLayout) view.findViewById (R.id.tv_share);
        LinearLayout tv_msg = (LinearLayout) view.findViewById (R.id.tv_msg);
        BaseFunc.setFont (view);
        view.setOnClickListener (this);

        tv_home.setOnClickListener (this);
        tv_share.setOnClickListener (this);
        tv_msg.setOnClickListener (this);
        tv_search.setOnClickListener (this);

        pw = new PopupWindow (view, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        pw.setFocusable (true);
        pw.setOutsideTouchable (true);
        pw.update ();
        pw.setBackgroundDrawable (new ColorDrawable());
        pw.setOnDismissListener (new PopupWindow.OnDismissListener () {
            @Override
            public void onDismiss () {
                if (mcb != null) {
                    mcb.onDismiss ();
                }
            }
        });
    }

    /** 设定页面分享数据*/
    public void setShareData (ShareData shareData) {
        this.shareData = shareData;
    }

    public void show () {
        pw.showAsDropDown (attachView);
        pw.showAsDropDown (attachView, 0, -BaseFunc.dp2px (mContext, 20));
    }

    public void dismiss () {
        pw.dismiss ();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.tv_home:
                dismiss ();
                BaseFunc.gotoHome(mContext,0);
                break;
            case R.id.tv_share:
                ShareData.fhShare ((BaseFragmentActivity) mContext, shareData, null);
                break;
            case R.id.tv_msg:
                BaseFunc.gotoActivity (mContext, MsgCenterActivity.class, null);
                break;
            case R.id.tv_search:
                BaseFunc.gotoActivity (mContext, SearchActivity.class, null);
                break;
        }
        pw.dismiss ();
    }

    private HLayoutMoreCallBack mcb;

    public void setCallBack (HLayoutMoreCallBack cb) {
        this.mcb = cb;
    }

    public interface HLayoutMoreCallBack {
        void onDismiss ();
    }
}
