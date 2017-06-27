package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.model.ActivityFloor;
import com.fanglin.fenhong.microbuyer.common.adapter.ActDtlFloorAdapter;
import com.fanglin.fenhong.microbuyer.common.adapter.HorizonalTagAdapter;

import java.util.List;

/**
 * 作者： Created by Plucky on 2015/12/18.
 * 主题馆楼层指示条
 */
public class LayoutActDtlSectionContent {

    private View view;
    RecyclerView aclass;
    ActDtlFloorAdapter floorAdapter;
    LinearLayoutManager lm;
    private View vLine;

    public LayoutActDtlSectionContent(final Context mContext) {
        view = View.inflate(mContext, R.layout.layout_actdtl_section_content, null);
        aclass = (RecyclerView) view.findViewById(R.id.aclass);
        vLine = view.findViewById(R.id.vLine);
        TextView btn_next = (TextView) view.findViewById(R.id.btn_next);
        lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        aclass.setLayoutManager(lm);
        floorAdapter = new ActDtlFloorAdapter(mContext);
        floorAdapter.setListener(new HorizonalTagAdapter.HorizonalTagListener() {
            @Override
            public void onItemClick(int position, int goods_position) {
                if (mcb != null) {
                    mcb.onItemClick(position, goods_position);
                }
            }
        });
        aclass.setAdapter(floorAdapter);
        BaseFunc.setFont(btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) {
                    mcb.onMoreClick();
                }
            }
        });
    }

    public View getView(List<ActivityFloor> floorlist) {
        floorAdapter.setList(floorlist);
        floorAdapter.notifyDataSetChanged();
        return view;
    }

    public void doItemClick(int position) {
        floorAdapter.setSelected(position);
        aclass.smoothScrollToPosition(position);
        floorAdapter.notifyDataSetChanged();
    }

    public void doMoreClick() {
        if (floorAdapter.getItemCount() == 0) {
            return;
        }
        int index = lm.findLastCompletelyVisibleItemPosition();
        index++;
        if (index >= floorAdapter.getItemCount()) {
            index = floorAdapter.getItemCount() - 1;
        }
        aclass.smoothScrollToPosition(index);
    }

    private SctDtlSectionCallBack mcb;

    public View getvLine() {
        return vLine;
    }

    public void setCallBack(SctDtlSectionCallBack cb) {
        this.mcb = cb;
    }

    public interface SctDtlSectionCallBack {
        void onItemClick(int position, int goods_position);

        void onMoreClick();
    }
}
