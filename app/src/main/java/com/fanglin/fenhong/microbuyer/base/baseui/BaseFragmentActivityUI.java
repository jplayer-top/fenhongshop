package com.fanglin.fenhong.microbuyer.base.baseui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fhlib.other.FHLog;
import com.kennyc.view.MultiStateView;

/**
 * 作者： Created by Plucky on 2015/7/6.
 * <p/>
 * 基类FragmentActivity
 */
public class BaseFragmentActivityUI extends BaseFragmentActivity {


    private View msgDot;
    private TextView tvMore;
    private ImageView ivMore;
    public TextView tvHead;
    private TextView tvExtra;
    public View vSpliter;

    public LinearLayout LHold;
    private MultiStateView multiStateView;
    public View vBottomLine;
    public boolean skipChk = false;

    private boolean hasParseed = false;
    public boolean redTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (redTop) {
            setContentView(R.layout.activity_base_red_top);
        } else {
            setContentView(R.layout.activity_base);
        }

        LHold = (LinearLayout) findViewById(R.id.LHold);
        multiStateView = (MultiStateView) findViewById(R.id.multiStateView);

        vSpliter = findViewById(R.id.vSpliter);
        View comtop = findViewById(R.id.comtop);

        ImageView ivBack = (ImageView) comtop.findViewById(R.id.ivBack);
        tvHead = (TextView) comtop.findViewById(R.id.tvHead);
        tvMore = (TextView) comtop.findViewById(R.id.tvMore);
        ivMore = (ImageView) comtop.findViewById(R.id.ivMore);
        tvExtra = (TextView) comtop.findViewById(R.id.tvExtra);

        msgDot = comtop.findViewById(R.id.msgDot);
        vBottomLine = comtop.findViewById(R.id.vBottomLine);

        ivBack.setOnClickListener(l);
        tvMore.setOnClickListener(l);
        ivMore.setOnClickListener(l);
        tvExtra.setOnClickListener(l);

        // 默认状态
        ivMore.setVisibility(View.INVISIBLE);
        tvMore.setVisibility(View.INVISIBLE);
        msgDot.setVisibility(View.INVISIBLE);
        tvExtra.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置标题
     */
    public void setHeadTitle(int resStr) {
        String title = getString(resStr);
        tvHead.setText(title);
    }

    /**
     * 改变View的状态
     *
     * @param viewStatus MultiStateView.VIEW_STATE_UNKNOWN
     *                   MultiStateView.VIEW_STATE_CONTENT
     *                   MultiStateView.VIEW_STATE_ERROR
     *                   MultiStateView.VIEW_STATE_EMPTY
     *                   MultiStateView.VIEW_STATE_LOADING
     *                   Added By Plucky
     */
    public void refreshViewStatus(int viewStatus) {
        multiStateView.setViewState(viewStatus);
    }

    /**
     * 设置标题
     */
    public void setHeadTitle(String title) {
        tvHead.setText(title);
    }

    /**
     * 启用更多图片
     */
    public void enableIvMore(int resid) {
        if (resid > 0) {
            ivMore.setImageResource(resid);
        }
        ivMore.setVisibility(View.VISIBLE);
        tvMore.setVisibility(View.INVISIBLE);
    }

    /**
     * 启用更多文字
     */
    public void enableTvMore(int resStr, boolean isIconFont) {
        String str = getString(resStr);
        tvMore.setText(str);
        if (TextUtils.isEmpty(str)) {
            tvMore.setTypeface(null);
        } else {
            if (isIconFont) {
                tvMore.setTypeface(iconfont);
            } else {
                tvMore.setTypeface(null);
            }
        }

        ivMore.setVisibility(View.INVISIBLE);
        tvMore.setVisibility(View.VISIBLE);
    }

    public void enableTvMore(int resStr, boolean isIconFont, int resColor) {
        enableTvMore(resStr, isIconFont);
        tvMore.setTextColor(getResources().getColor(resColor));
    }

    /**
     * 启用额外的顶部菜单
     *
     * @param resStr     int
     * @param isIconFont true,false
     */
    public void enableTvExtra(int resStr, boolean isIconFont) {
        String str = getString(resStr);
        tvExtra.setText(str);
        if (TextUtils.isEmpty(str)) {
            tvExtra.setTypeface(null);
        } else {
            if (isIconFont) {
                tvExtra.setTypeface(iconfont);
            } else {
                tvExtra.setTypeface(null);
            }
        }

        tvExtra.setVisibility(View.VISIBLE);
    }

    /**
     * 控制TvExtra是否显示
     *
     * @param visible true false
     */
    public void setVisibleOfTvExtra(boolean visible) {
        tvExtra.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 启用消息红点
     *
     * @param visible true false
     */
    public void enableMsgDot(boolean visible) {
        msgDot.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 控制更多文字 显示与否
     *
     * @param visible true false
     */
    public void setVisibleOfTvMore(boolean visible) {
        tvMore.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void onBackClick() {
        finish();
    }

    public void ontvMoreClick() {

    }

    public void onivMoreClick() {

    }

    public void onTvExtraClick() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (member == null && !skipChk) {
            BaseFunc.gotoLogin(mContext);
            finish();
        }
        parseChildHeight();
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivBack:
                    onBackClick();
                    break;
                case R.id.tvMore:
                    ontvMoreClick();
                    break;
                case R.id.ivMore:
                    onivMoreClick();
                    break;
                case R.id.tvExtra:
                    onTvExtraClick();
                    break;
                default:
                    break;
            }
        }

    };

    private void parseChildHeight() {
        if (hasParseed) return;
        try {
            View child = LHold.getChildAt(0);
            if (child instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) child;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                vg.setLayoutParams(params);
                hasParseed = true;
            }
        } catch (Exception e) {
            FHLog.d("BaseFragmentActivityUI", "height parse error");
        }
    }

    public void onEmptyView(int emptyStrRes, int emptyImgRes, View.OnClickListener clickListener) {
        View emptyView = multiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        if (emptyView != null) {
            TextView tvEmpty = (TextView) emptyView.findViewById(R.id.tvEmpty);
            ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.ivEmpty);
            if (emptyStrRes > 0 && tvEmpty != null) {
                tvEmpty.setText(emptyStrRes);
            }
            if (emptyImgRes > 0 && ivEmpty != null) {
                ivEmpty.setImageResource(emptyImgRes);
            }
            emptyView.setOnClickListener(clickListener);
        }
    }
}
