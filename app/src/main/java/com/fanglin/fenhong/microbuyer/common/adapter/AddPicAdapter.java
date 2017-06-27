package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fhui.flowlayout.FlowLayout;
import com.fanglin.fhui.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/23.
 */
public class AddPicAdapter extends TagAdapter {

    public ArrayList<String> imgs = new ArrayList<> ();
    final LayoutInflater mInflater;

    public static ArrayList<String> getDefault () {
        ArrayList<String> list = new ArrayList<> ();
         /* 初始化 列表*/
        list.add ("ADD");//种子图片用来作为添加图片按钮
        return list;
    }

    public AddPicAdapter (Context c, ArrayList<String> imgs) {
        super (imgs);
        this.imgs = imgs;
        mInflater = LayoutInflater.from (c);
    }

    public void addPic (String pic) {
        if (imgs == null || imgs.size () < 1) return;
        if (BaseFunc.isValidImgUrl (pic)) {
            imgs.add (0, pic);//加在最前面
        }
    }

    public void removePic (int index) {
        if (index < 0 || index == imgs.size () - 1) return;
        if (imgs != null && index < imgs.size ()) {
            imgs.remove (index);
        }
    }

    public String getImgs () {
        if (imgs == null || imgs.size () == 0) return null;
        List<String> lst = imgs;
        lst.remove (lst.size () - 1);
        return BaseFunc.list2QuoteStr (lst);
    }

    public List<String> getImageList () {
        if (imgs == null || imgs.size () == 0) return null;
        List<String> lst = imgs;
        lst.remove (lst.size () - 1);
        return lst;
    }

    @Override
    public View getView (FlowLayout parent, final int position, Object o) {
        final String val = String.valueOf (o);
        View view = mInflater.inflate (R.layout.item_add_pic, parent, false);
        FrameLayout FICON = (FrameLayout) view.findViewById (R.id.FICON);
        BaseFunc.setFont (FICON);
        ImageView fiv = (ImageView) view.findViewById (R.id.fiv);
        TextView tv_close = (TextView) view.findViewById (R.id.tv_close);
        if (TextUtils.equals ("ADD", val)) {
            fiv.setImageResource (R.drawable.add_a_picture);
            tv_close.setVisibility (View.INVISIBLE);
            fiv.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    if (mcb != null) mcb.onAddClick ();
                }
            });
        } else {
            new FHImageViewUtil (fiv).setImageURI (val, FHImageViewUtil.SHOWTYPE.DEFAULT);
            tv_close.setVisibility (View.VISIBLE);
            if (BaseFunc.isValidImgUrl (val)) {
                fiv.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        if (mcb != null) mcb.onPicView (val);
                    }
                });
            } else {
                fiv.setOnClickListener (null);
            }
        }

        tv_close.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) mcb.onDelClick (position);
            }
        });
        return view;
    }

    @Override
    public void notifyDataChanged () {
        super.notifyDataChanged ();
    }

    private AddPicCallBack mcb;

    public void setCallBack (AddPicCallBack cb) {
        this.mcb = cb;
    }

    public interface AddPicCallBack {
        void onAddClick ();

        void onDelClick (int index);

        void onPicView (String picUrl);
    }
}
