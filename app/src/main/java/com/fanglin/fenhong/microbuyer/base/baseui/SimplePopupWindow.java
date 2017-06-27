package com.fanglin.fenhong.microbuyer.base.baseui;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;

/**
 * 作者： Created by Plucky on 2015/9/7.
 */
public class SimplePopupWindow {
    private View manchor;
    private PopupWindow pw;
    private TextView tv_chg;

    public SimplePopupWindow (View anchor, final String... list) {
        this.manchor = anchor;

        ArrayAdapter<String> adapter = new ArrayAdapter<> (anchor.getContext (), android.R.layout.simple_dropdown_item_1line, list);
        ListView lv = (ListView) View.inflate (anchor.getContext (), R.layout.layout_listview, null);
        lv.setAdapter (adapter);
        lv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                if (mcb != null) {
                    mcb.onItemClick (position);
                }
                if (tv_chg != null) {
                    tv_chg.setText (list[position]);
                }
                dismiss ();
            }
        });
        pw = new PopupWindow (lv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setFocusable (true);
        pw.setOutsideTouchable (true);
        pw.update ();
        pw.setBackgroundDrawable (new ColorDrawable());
    }

    public void show () {
        pw.showAsDropDown (manchor);
    }

    /*设置需要改变的文本输入框*/
    public void setTextView (TextView tv_chg) {
        this.tv_chg = tv_chg;
    }

    public void dismiss () {
        pw.dismiss ();
    }

    private SimplePWCallBack mcb;

    public void setCallBack (SimplePWCallBack cb) {
        this.mcb = cb;
    }

    public interface SimplePWCallBack {
        void onItemClick (int position);
    }
}
