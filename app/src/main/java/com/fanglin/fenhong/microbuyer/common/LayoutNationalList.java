package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.model.WSHomeNationalPavList;
import com.fanglin.fenhong.microbuyer.common.adapter.NationalPavListAdapter;
import java.util.List;

/**
 * 首页 国家馆入口
 * Created by lizhixin on 2015/12/01.
 */
public class LayoutNationalList {

    private Context mContext;
    private ViewGroup view;
    private RecyclerView recyclerView;
    NationalPavListAdapter adapter;

    public LayoutNationalList(Context c) {
        this.mContext = c;
        view = (ViewGroup) View.inflate(mContext, R.layout.layout_national_list, null);
        recyclerView = (RecyclerView) view.findViewById (R.id.recyclerview);

        adapter = new NationalPavListAdapter (mContext);

        AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void setList (List<WSHomeNationalPavList> list) {
        /*list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("http://img2.imgtn.bdimg.com/it/u=194240101,2532182839&fm=21&gp=0.jpg");
        }*/
        adapter.setList (list);
        adapter.notifyDataSetChanged ();
        if (list == null || list.size () == 0) {
            view.setVisibility (View.GONE);
        }
    }

    public View getView () {
        return view;
    }

}
