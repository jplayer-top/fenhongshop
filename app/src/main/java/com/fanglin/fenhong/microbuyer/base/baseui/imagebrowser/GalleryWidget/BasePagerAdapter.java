package com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.GalleryWidget;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Class wraps URLs to adapter, then it instantiates <b>UrlTouchImageView</b> objects to paging up through them.
 */
public class BasePagerAdapter extends PagerAdapter {

    protected final List<String> mResources;
    protected final Context mContext;
    protected int mCurrentPosition = -1;
    protected OnItemChangeListener mOnItemChangeListener;

    public BasePagerAdapter(Context context, List<String> resources) {
        this.mResources = resources;
        this.mContext = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (mCurrentPosition == position) return;

        mCurrentPosition = position;
        if (mOnItemChangeListener != null) mOnItemChangeListener.onItemChange(mCurrentPosition);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mResources == null) return 0;
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(ViewGroup arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(ViewGroup arg0) {
    }

    public void setOnItemChangeListener(OnItemChangeListener listener) {
        mOnItemChangeListener = listener;
    }

    public interface OnItemChangeListener {
        void onItemChange(int currentPosition);

        void onItemClick(int currentPosition);
    }
}
