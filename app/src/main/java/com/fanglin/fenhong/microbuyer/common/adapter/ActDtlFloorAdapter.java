package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;

import com.fanglin.fenhong.microbuyer.base.model.ActivityFloor;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-8 11:02.
 *  品牌馆详情页
 */
public class ActDtlFloorAdapter extends HorizonalTagAdapter {

    private List<ActivityFloor> list;

    public ActDtlFloorAdapter (Context c) {
        super (c);
    }

    public void setList (List<ActivityFloor> lst) {
        this.list = lst;
        setSelected (0);
    }

    public void setSelected (int position) {
        if (list == null) return;
        for (int i = 0; i < list.size (); i++) {
            list.get (i).isSelected = false;
        }
        if (position >= list.size ()) return;
        list.get (position).isSelected = true;
    }

    public int getSelectedPosition () {
        if (list == null) return 0;
        for (int i = 0; i < list.size (); i++) {
            if (list.get (i).isSelected) {
                return i;
            }
        }
        return 0;
    }

    public ActivityFloor getItem (int position) {
        if (list == null || list.size () == 0) return null;
        return list.get (position);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        holder.tv_title.setText (list.get (position).floor_name);
        holder.tv_title.setSelected (list.get (position).isSelected);
        holder.vline.setSelected (list.get (position).isSelected);
        holder.LDef.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mlistener != null) {
                    int goods_pos = ActivityFloor.getScrollPosition (list, position);
                    mlistener.onItemClick (position, goods_pos);
                }
            }
        });
    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.size ();
    }
}
