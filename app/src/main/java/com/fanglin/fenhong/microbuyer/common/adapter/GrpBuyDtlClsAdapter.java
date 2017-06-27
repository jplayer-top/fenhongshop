package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.view.View;
import com.fanglin.fenhong.microbuyer.base.model.GroupBuyCls;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-8.
 */
public class GrpBuyDtlClsAdapter extends HorizonalTagAdapter {


    private List<GroupBuyCls> list;
    private GroupBuyCls parentCls;

    public GrpBuyDtlClsAdapter (Context c) {
        super (c);
    }

    public void setParentCls (GroupBuyCls cls) {
        this.parentCls = cls;
    }

    public void setList (List<GroupBuyCls> lst) {

        /*加入要添加父级分类*/
        if (parentCls != null) {
            if (lst == null || lst.size () == 0) {
                lst = new ArrayList<> ();
            }
            lst.add (0, parentCls);
        }

        this.list = lst;
        setSelected (0);
    }

    public void setSelected (int positon) {
        if (list == null) return;
        for (int i = 0; i < list.size (); i++) {
            list.get (i).isSelected = false;
        }
        if (positon >= list.size ()) return;
        list.get (positon).isSelected = true;
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


    public GroupBuyCls getItem (int position) {
        if (list == null || list.size () == 0) return null;
        return list.get (position);
    }


    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        holder.tv_title.setText (list.get (position).class_name);
        holder.tv_title.setSelected (list.get (position).isSelected);
        holder.vline.setSelected (list.get (position).isSelected);
        holder.LDef.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mlistener != null) {
                    mlistener.onItemClick (position, 0);
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
