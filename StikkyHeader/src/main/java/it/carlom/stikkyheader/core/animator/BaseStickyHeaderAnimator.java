package it.carlom.stikkyheader.core.animator;

import android.util.Log;
import android.view.View;

import it.carlom.stikkyheader.core.HeaderAnimator;


public class BaseStickyHeaderAnimator extends HeaderAnimator {

    private float mTranslationRatio;

    @Override
    protected void onAnimatorAttached() {
        //nothing to do
    }

    @Override
    protected void onAnimatorReady() {
        //nothing to do
    }

    @Override
    public void onScroll(int scrolledY) {
        int transy = Math.max(scrolledY, getMaxTranslation());
        Log.d("StikkyHeaderListView", "transy:" + transy);
        getHeader().setTranslationY(transy);

        /*
         * Added By Plucky
         * 添加StikkyHeader下再Stikky一层
         */
        Object tag = getHeader().getTag();
        if (tag != null && tag instanceof View) {
            View botStikkyView = (View) tag;
            botStikkyView.setTranslationY(transy + getHeightHeader());
        }

        mTranslationRatio = calculateTranslationRatio(scrolledY);
    }

    public float getTranslationRatio() {
        return mTranslationRatio;
    }

    private float calculateTranslationRatio(int scrolledY) {
        return (float) scrolledY / (float) getMaxTranslation();
    }

}
