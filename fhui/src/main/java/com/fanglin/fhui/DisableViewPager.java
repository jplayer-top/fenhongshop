package com.fanglin.fhui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*@category可设置是否可左右滑动的ViewPager
 * @author Plucky
 * */
public class DisableViewPager extends ViewPager {

	private boolean enabled;

	public DisableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = false;
	}

	public DisableViewPager(Context context) {
		super(context);
		this.enabled = false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

}
