package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.MutiItemDecoration;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavClassifyEntity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.adapter.NationalPavClassifyAdapter;
import java.util.List;

/**
 * 国家馆 分类 子布局
 * Created by lizhixin on 2015/11/30.
 */
public class LayoutNationalPavCls implements View.OnClickListener {
    private ViewGroup view;
    private NationalPavClassifyAdapter adapter;
    private LinearLayout LSpliter;
    private TextView tvSpliter;
    private List<NationalPavClassifyEntity> originList, twoRowList;
    private String resourceTags;
    private Activity mContext;

    public LayoutNationalPavCls(Activity mContext, String activity_id) {
        this.mContext = mContext;
        view = (ViewGroup) View.inflate(mContext, R.layout.layout_son_national_pav_cls_and_adv, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        tvSpliter = (TextView) view.findViewById(R.id.tvSpliter);
        LSpliter = (LinearLayout) view.findViewById(R.id.LSpliter);
        LSpliter.setVisibility(View.VISIBLE);

        AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 4);
        layoutManager.setDividerHeight(2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new MutiItemDecoration(MutiItemDecoration.Type.ALL, 2));

        adapter = new NationalPavClassifyAdapter(mContext, activity_id);
        recyclerView.setAdapter(adapter);

        LSpliter.setOnClickListener(this);
    }

    public void setDatas(List<NationalPavClassifyEntity> list, ShareData shareData, String resource_tags) {
        if (list != null) {
            adapter.setList(list, resource_tags);
            adapter.notifyDataSetChanged();
        }
        if (shareData != null) {
            adapter.setShareData(shareData);
        }

        this.resourceTags = resource_tags;

        /**
         * 一行4个，如果c>8
         */
        int c = adapter.getItemCount();
        if (c > 8 && list != null) {
            originList = list;
            twoRowList = list.subList(0, 8);

            refreshExpand();
            LSpliter.setVisibility(View.VISIBLE);
        } else {
            LSpliter.setVisibility(View.GONE);
        }
    }

    public View getView() {
        return view;
    }

    private boolean expanded = false;

    @Override
    public void onClick(View v) {
        expanded = !expanded;
        refreshExpand();
    }

    private void refreshExpand() {
        if (expanded) {
            tvSpliter.setText(mContext.getString(R.string.lbl_disexpand));
            tvSpliter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_triangle_gray, 0);
            adapter.setList(originList, resourceTags);
            adapter.notifyDataSetChanged();
        } else {
            tvSpliter.setText(mContext.getString(R.string.all_class));
            tvSpliter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_triangle_gray_down, 0);
            adapter.setList(twoRowList, resourceTags);
            adapter.notifyDataSetChanged();
        }
    }
}
