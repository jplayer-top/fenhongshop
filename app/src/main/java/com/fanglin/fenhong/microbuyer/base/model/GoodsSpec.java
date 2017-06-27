package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsSpecAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/22.
 */
public class GoodsSpec {
    public List<GoodsSpecAdapter.SubItem> subItems;
    public String key;
    public String value;

    public List<String> getTags () {
        if (subItems == null) return null;
        List<String> res = new ArrayList<> ();
        for (int i = 0; i < subItems.size (); i++) {
            res.add (subItems.get (i).value);
        }
        return res;
    }
}
