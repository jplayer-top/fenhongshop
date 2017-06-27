package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeCategory;
import com.fanglin.fenhong.microbuyer.dutyfree.listener.FloorViewHolderListener;
import com.fanglin.fenhong.microbuyer.dutyfree.adapter.FloorAdapter;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/17-下午5:07.
 * 功能描述: 横向楼层ViewHolder
 */
public class FloorViewHolder extends RecyclerView.ViewHolder {

    RecyclerView recyclerView;
    FloorAdapter adapter;
    int type;

    public FloorViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

        int height = itemView.getResources().getDimensionPixelOffset(R.dimen.dp_of_42);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        recyclerView.setLayoutParams(params);

        LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        adapter = new FloorAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
    }

    public static View getView(Context context) {
        return View.inflate(context, R.layout.item_recyclerview_container, null);
    }

    public static FloorViewHolder getHolder(Context context) {
        View view = getView(context);
        return new FloorViewHolder(view);
    }

    public static FloorViewHolder getHolderByView(View view) {
        return new FloorViewHolder(view);
    }

    public void refreshView(List<DutyfreeCategory> categories) {
        adapter.setListener(floorListener);
        adapter.setCategories(categories);
    }

    public void changePositon(int index) {
        recyclerView.smoothScrollToPosition(index);
    }

    public void setType(int type) {
        this.type = type;
    }

    private FloorViewHolderListener floorListener;

    public void setFloorListener(FloorViewHolderListener floorListener) {
        this.floorListener = floorListener;
    }
}
