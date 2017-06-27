package com.fanglin.fenhong.microbuyer.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.Area;
import com.fanglin.fenhong.microbuyer.common.adapter.AreaAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/21.
 */
public class SelectAreaActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.tv_province)
    TextView tv_province;
    @ViewInject(R.id.tv_city)
    TextView tv_city;
    @ViewInject(R.id.tv_area)
    TextView tv_area;
    @ViewInject(R.id.lv)
    ListView lv;

    AreaAdapter adapter;
    String laseActivity;
    private int index = 0;
    private String p_id;
    private String[] pid = new String[]{null, null, null, ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_selectarea, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        try {
            laseActivity = getIntent().getStringExtra("VAL");
        } catch (Exception e) {
            laseActivity = null;
        }
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.selecte_a_addr);
        adapter = new AreaAdapter(mContext);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (index) {
                    case 0:
                        pid[0] = adapter.getItem(position).area_id;
                        p_id = pid[0];
                        tv_province.setText(adapter.getItem(position).area_name);
                        break;
                    case 1:
                        pid[1] = adapter.getItem(position).area_id;
                        p_id = pid[1];
                        tv_city.setText(adapter.getItem(position).area_name);
                        break;
                    case 2:
                        pid[2] = adapter.getItem(position).area_id;
                        p_id = pid[2];
                        tv_area.setText(adapter.getItem(position).area_name);

                        pid[3] = tv_province.getText().toString() + " " + tv_city.getText().toString() + " " + tv_area.getText().toString();
                        if (laseActivity == null) {
                            finish();
                            return;
                        }
                        try {
                            Class last = Class.forName(laseActivity);
                            Intent i = new Intent(mContext, last);
                            i.putExtra("VAL", new Gson().toJson(pid));
                            setResult(RESULT_OK, i);
                            finish();
                        } catch (Exception e) {
                            //
                        }
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
                    List<Area> list;
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<Area>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                    if (list != null && list.size() > 0) {
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).get_area_list(p_id);
    }

    @Override
    public void onBackPressed() {
        if (index == 0) {
            super.onBackPressed();
            return;
        }
        back();
    }

    @OnClick(value = {R.id.tv_province, R.id.tv_city})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_province:
                tv_province.setText("");
                index = 0;
                p_id = null;
                getData();
                break;
            case R.id.tv_city:
                tv_city.setText("");
                index = 1;
                p_id = pid[0];
                getData();
                break;
        }
    }

    private void back() {
        switch (index) {
            case 2:
                tv_city.setText("");
                p_id = pid[0];
                break;
            case 1:
                tv_province.setText("");
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
