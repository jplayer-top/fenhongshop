package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.BankCard;
import com.fanglin.fenhong.microbuyer.microshop.adapter.BankCardAdapter;
import com.fanglin.fhui.FHDialog;
import com.fanglin.fhui.ListViewinScroll;

import java.util.List;

/**
 * 作者： Created by Plucky on 15-11-1.
 */
public class LayoutBankCardList implements View.OnClickListener {

    private Context mContext;
    private View view;
    FHDialog dlg;
    BankCardAdapter adapter;
    FrameLayout FTop;
    TextView tv_close;
    LinearLayout LAdd;
    ListViewinScroll listView;

    public LayoutBankCardList (Context c) {
        this.mContext = c;
        view = View.inflate (mContext, R.layout.layout_bankcard_list, null);
        int height = BaseFunc.getDisplayMetrics (mContext).heightPixels * 2 / 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams (params);

        FTop = (FrameLayout) view.findViewById (R.id.FTop);
        tv_close = (TextView) view.findViewById (R.id.tv_close);
        LAdd = (LinearLayout) view.findViewById (R.id.LAdd);
        listView = (ListViewinScroll) view.findViewById (R.id.listView);
        tv_close.setOnClickListener (this);
        LAdd.setOnClickListener (this);

        BaseFunc.setFont (FTop);
        BaseFunc.setFont (LAdd);
        adapter = new BankCardAdapter (mContext);
        listView.setAdapter (adapter);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                if (mcb != null) mcb.onSelect (adapter.getItem (position));
                dismiss ();
            }
        });
        adapter.setCallBack (new BankCardAdapter.BCAdapterCallBack () {
            @Override
            public void onEdit (BankCard bankCard) {
                if (mcb != null) mcb.onEdit (bankCard);
            }
        });

        dlg = new FHDialog (mContext);
        dlg.setCanceledOnTouchOutside (true);
        dlg.setBotView (view, 0);
    }

    public void show (List<BankCard> list) {
        adapter.setList (list);
        adapter.notifyDataSetChanged ();
        dlg.show ();
    }

    public void dismiss () {
        dlg.dismiss ();
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.tv_close:
                dismiss ();
                break;
            case R.id.LAdd:
                if (mcb != null) mcb.onAdd ();
                dismiss ();
                break;
        }
    }

    private LBCCallBack mcb;

    public void setCallBack (LBCCallBack cb) {
        this.mcb = cb;
    }

    public interface LBCCallBack {
        void onAdd ();

        void onEdit (BankCard acard);

        void onSelect (BankCard acard);
    }
}
