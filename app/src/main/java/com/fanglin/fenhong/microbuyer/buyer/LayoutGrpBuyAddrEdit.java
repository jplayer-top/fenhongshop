package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Context;
import android.content.Intent;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.common.CartCheckActivity;
import com.fanglin.fhui.FHHintDialog;
import com.google.gson.Gson;

/**
 * 作者： Created by Plucky on 2015/10/22.
 */
public class LayoutGrpBuyAddrEdit implements FHHintDialog.FHHintListener {

    private Context mContext;
    private FHHintDialog dlg;

    public int REQCODE;
    public Address address;

    public LayoutGrpBuyAddrEdit (Context c) {
        mContext = c;
        dlg = new FHHintDialog (mContext);
        dlg.setTvContent(mContext.getString(R.string.hint_buy_global_goods));
        dlg.setHintListener(this);
    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {
        CartCheckActivity act = (CartCheckActivity) mContext;
        Intent i = new Intent (act, EditAddressActivity.class);
        i.putExtra ("EDIT", getClass ().getName ());
        i.putExtra ("VAL", new Gson ().toJson (address));
        act.startActivityForResult (i, REQCODE);
    }

    public void show () {
        dlg.show ();
    }
}
