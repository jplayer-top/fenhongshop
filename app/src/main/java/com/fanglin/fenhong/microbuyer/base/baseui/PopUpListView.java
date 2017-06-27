package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.adapter.PopUpListViewAdapter;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/14.
 */
public class PopUpListView {
    private Context mContext;
    private View attachView;
    private PopupWindow pw;
    private ListView listView;
    PopUpListViewAdapter adapter;
    public boolean autoDismiss = true;
    public int first = 0;

    public PopUpListView(View attachView) {
        this.attachView = attachView;
        this.mContext = attachView.getContext();

        View view = View.inflate(mContext, R.layout.poplistview, null);
        listView = (ListView) view.findViewById(R.id.listView);
        pw = new PopupWindow(view, mContext.getResources().getDimensionPixelSize(R.dimen.dp_of_175), AbsListView.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.update();
        pw.setBackgroundDrawable(new ColorDrawable());

        adapter = new PopUpListViewAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoDismiss) pw.dismiss();
                if (mcb != null) mcb.onItemClick(position, adapter.getItem(position));
            }
        });
    }

    public void setList(List<String> list) {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    public void show() {
        if (adapter.getCount() == 0) return;
        first++;
        int[] location = new int[2];
        attachView.getLocationOnScreen(location);
        int baseY = mContext.getResources().getDimensionPixelSize(R.dimen.dp_of_25);
        int item_height = mContext.getResources().getDimensionPixelSize(R.dimen.dp_of_30);
        int offsetX = location[0];
        int offsetY = location[1] - listView.getMeasuredHeight() - baseY;
        if (first == 1) {
            offsetY = location[1] - adapter.getCount() * item_height - baseY * 2;
        }
        pw.showAtLocation(attachView, Gravity.NO_GRAVITY, offsetX, offsetY);
    }

    public void dismiss() {
        pw.dismiss();
    }

    private PopUpListViewCallBack mcb;

    public void setCallBack(PopUpListViewCallBack cb) {
        this.mcb = cb;
    }

    public interface PopUpListViewCallBack {
        void onItemClick(int index, String txt);
    }
}
