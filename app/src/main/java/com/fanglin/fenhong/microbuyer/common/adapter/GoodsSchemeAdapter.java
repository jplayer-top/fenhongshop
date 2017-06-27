package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fanglin.fenhong.microbuyer.base.model.GoodsScheme;
import com.fanglin.fenhong.microbuyer.base.model.Member;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/12.
 */
public class GoodsSchemeAdapter extends BaseAdapter {

    private List<GoodsScheme> list;
    public Context mContext;
    public Member member;

    public GoodsSchemeAdapter (Context c) {
        this.mContext = c;
    }

    public void setMember (Member member) {
        this.member = member;
    }

    public void setList (List<GoodsScheme> lst) {
        this.list = lst;
    }

    public void addList (List<GoodsScheme> lst) {
        if (lst != null && lst.size () > 0) {
            list.addAll (lst);
        }
    }

    @Override
    public int getCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public GoodsScheme getItem (int position) {
        return list.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        return null;
    }
}
