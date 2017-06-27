package com.fanglin.fenhong.microbuyer.merchant.joinstep;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Category;
import com.fanglin.fenhong.microbuyer.merchant.adapter.StoreCategoryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/21.
 */
public class SelectStoreCategoryActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.tv_lv_1)
    TextView tv_lv_1;
    @ViewInject(R.id.tv_lv_2)
    TextView tv_lv_2;
    @ViewInject(R.id.tv_lv_3)
    TextView tv_lv_3;
    @ViewInject(R.id.lv)
    ListView lv;

    StoreCategoryAdapter adapter;
    Class laseActivity;
    private int index = 0;
    private String p_id;
    private String[] pid = new String[]{null, null, null, "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_select_storecategory, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            laseActivity = Class.forName(getIntent().getStringExtra("VAL"));
        } catch (Exception e) {
            laseActivity = null;
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.select_merchantin_scope);
        adapter = new StoreCategoryAdapter(mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (index) {
                    case 0:
                        pid[0] = adapter.getItem(position).gc_id;
                        pid[3] = adapter.getItem(position).gc_name;
                        p_id = pid[0];
                        tv_lv_1.setText(adapter.getItem(position).gc_name);
                        break;
                    case 1:
                        pid[1] = adapter.getItem(position).gc_id;
                        pid[4] = adapter.getItem(position).gc_name;
                        p_id = pid[1];
                        tv_lv_2.setText(adapter.getItem(position).gc_name);
                        break;
                    case 2:
                        pid[2] = adapter.getItem(position).gc_id;
                        pid[5] = adapter.getItem(position).gc_name;
                        p_id = pid[2];
                        tv_lv_3.setText(adapter.getItem(position).gc_name);

                        if (laseActivity != null) {
                            Intent i = new Intent(mContext, laseActivity);
                            i.putExtra("VAL", new Gson().toJson(pid));
                            setResult(RESULT_OK, i);
                        }
                        finish();
                        break;
                }
                index++;
                getData();
            }
        });
        getData();
    }

    private void getData() {
        if (index > 2) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                lv.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                lv.setEnabled(true);
                if (isSuccess) {
                    List<Category> list;
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<Category>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                    if (list != null && list.size() > 0) {
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        BaseFunc.showMsg(mContext, getString(R.string.op_error));
                    }
                }
            }
        }).get_goods_class(p_id, "0");
    }

    @Override
    public void onBackPressed() {
        if (index == 0) {
            super.onBackPressed();
            return;
        }
        back();
    }

    @OnClick(value = {R.id.tv_lv_1, R.id.tv_lv_2})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lv_1:
                tv_lv_1.setText("");
                index = 0;
                p_id = null;
                getData();
                break;
            case R.id.tv_lv_2:
                tv_lv_2.setText("");
                index = 1;
                p_id = pid[0];
                getData();
                break;
        }
    }

    private void back() {
        switch (index) {
            case 2:
                tv_lv_2.setText("");
                p_id = pid[0];
                break;
            case 1:
                tv_lv_1.setText("");
                p_id = null;
                break;
        }
        index--;
        adapter.setList(null);
        adapter.notifyDataSetChanged();
        getData();
    }

    @Override
    public void onBackClick() {
        if (index == 0) {
            super.onBackClick();
            return;
        }
        back();
    }
}
