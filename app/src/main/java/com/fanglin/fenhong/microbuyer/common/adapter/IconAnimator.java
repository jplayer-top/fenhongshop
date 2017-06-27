package com.fanglin.fenhong.microbuyer.common.adapter;

import android.view.View;

import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * 作者： Created by Plucky on 2015/10/10.
 */
public class IconAnimator extends HeaderStikkyAnimator {

    private View viewToAnimate;

    public IconAnimator (View viewToAnimate) {
        this.viewToAnimate = viewToAnimate;
    }

    @Override
    public AnimatorBuilder getAnimatorBuilder () {

        float fade = 0.2f; // 20% fade

        AnimatorBuilder animatorBuilder = AnimatorBuilder.create ().applyFade (viewToAnimate, fade);

        return animatorBuilder;
    }
}