package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.StoreCls;
import com.fanglin.fenhong.microbuyer.merchant.adapter.StoreClsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/21.
 */
public class SelectStoreClsActivity extends BaseFragmentActivityUI {

    @ViewInject(R.id.lv)
    ListView lv;

    StoreClsAdapter adapter;
    List<StoreCls> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_select_simple, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            String val = getIntent().getStringExtra("VAL");
            list = new Gson().fromJson(val, new TypeToken<List<StoreCls>>() {
            }.getType());
        } catch (Exception e) {
            //
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.select_merchantin_shopcls);
        adapter = new StoreClsAdapter(mContext);
        adapter.setList(list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("VAL", new Gson().toJson(adapter.getItem(position)));
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }


}
