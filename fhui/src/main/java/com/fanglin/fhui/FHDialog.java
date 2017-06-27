package com.fanglin.fhui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by plucky on 15-9-12.
 * modify by lizhiixn on 16-01-13
 */
public class FHDialog extends Dialog {

    private LinearLayout LHold;
    private static final int TRANSLATE_DURATION = 150;
    private static final int ALPHA_DURATION = 150;
    private Context mContext;
    public FrameLayout mview;
    public LinearLayout LAnim;
    private Animation animin, animout;
    public Object tag;

    public FHDialog (Context c) {
        super(c, R.style.FHDialog);
        getWindow ().setWindowAnimations(-1);//去除原有动画
        mContext = c;
        mview = (FrameLayout) View.inflate (mContext, R.layout.layout_fhdailog, null);
        setContentView (mview);
        LHold = (LinearLayout) mview.findViewById (R.id.LHold);
        LAnim = (LinearLayout) mview.findViewById (R.id.LAnim);
        animin = createTranslationInAnimation(0);
        animout = createTranslationOutAnimation(0);

        LAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        /** 点击背景取消对话框功能*/
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        LHold.setOnClickListener(null);

        /** 可取消*/
        setCancelable(true);
        setCanceledOnTouchOutside (true);
    }

    /** 两种形式 0从底部 1从右边 2 fadein*/
    public void setBotView (View v, int style) {
        switch (style) {
            case 0:
            case 1:
                animin = createTranslationInAnimation (style);
                animout = createTranslationOutAnimation (style);
                break;
            case 2:
                animin = createAlphaInAnimation ();
                animout = createAlphaOutAnimation ();
                break;
        }

        LHold.removeAllViews ();
        LHold.addView (v);
    }


    private Animation createTranslationInAnimation (int style) {
        int type = Animation.RELATIVE_TO_SELF;
        TranslateAnimation an;
        if (style == 0) {
            an = new TranslateAnimation (type, 0, type, 0, type, 1, type, 0);
        } else {
            an = new TranslateAnimation (type, 1, type, 0, type, 0, type, 0);
        }

        an.setDuration (TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation () {
        AlphaAnimation an = new AlphaAnimation (0, 1);
        an.setDuration (ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation (int style) {
        int type = Animation.RELATIVE_TO_SELF;
        TranslateAnimation an;
        if (style == 0) {
            an = new TranslateAnimation (type, 0, type, 0, type, 0, type, 1);
        } else {
            an = new TranslateAnimation (type, 0, type, 1, type, 0, type, 0);
        }

        an.setDuration (TRANSLATE_DURATION);
        an.setFillAfter (true);
        return an;
    }

    private Animation createAlphaOutAnimation () {
        AlphaAnimation an = new AlphaAnimation (1, 0);
        an.setDuration (ALPHA_DURATION);
        an.setFillAfter (true);
        return an;
    }

    /**
     * 如果 允许 可以通过点击有效区域之外的背景部分来关闭对话框, 需要使用者来调用此方法
     * @param outSideCancelable -- true 允许点击
     */
    public void setOutSideCancelable(boolean outSideCancelable) {
        if (outSideCancelable) {
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    @Override
    public void show () {
        super.show ();
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService (Context.INPUT_METHOD_SERVICE);
        if (imm.isActive ()) {
            View focusView = ((Activity) mContext).getCurrentFocus ();
            if (focusView != null) {
                imm.hideSoftInputFromWindow (focusView.getWindowToken (), 0);
            }
        }
        LHold.startAnimation (animin);
    }

    @Override
    public void dismiss () {
        LHold.startAnimation (animout);
        new Handler ().postDelayed (new Runnable () {
            @Override
            public void run () {
                mDismiss ();
            }
        }, 200);
    }

    private void mDismiss () {
        super.dismiss ();
    }
}
