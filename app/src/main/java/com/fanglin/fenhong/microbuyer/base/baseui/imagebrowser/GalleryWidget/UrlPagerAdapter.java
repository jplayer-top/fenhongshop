package com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.GalleryWidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;

import java.util.List;

public class UrlPagerAdapter extends BasePagerAdapter {
    int screenWidth, picheight;

    public UrlPagerAdapter(Context context, List<String> resources) {
        super(context, resources);
        screenWidth = BaseFunc.getDisplayMetrics(context).widthPixels;
        picheight = screenWidth;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View view = View.inflate(collection.getContext(), R.layout.item_picview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, picheight));
        new FHImageViewUtil(imageView).setImageURI(mResources.get(position), FHImageViewUtil.SHOWTYPE.DEFAULT);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemChangeListener != null) {
                    mOnItemChangeListener.onItemClick(position);
                }
            }
        });
        collection.addView(view, 0);
        return view;
    }
}
