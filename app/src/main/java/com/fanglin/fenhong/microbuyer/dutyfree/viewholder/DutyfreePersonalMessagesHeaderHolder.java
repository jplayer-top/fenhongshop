package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeMessagesBean;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/9.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalMessagesHeaderHolder extends RecyclerView.ViewHolder {
    private static Context mContext;
    private final TextView tvTime;

    public DutyfreePersonalMessagesHeaderHolder(View itemView) {
        super(itemView);
        tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        itemView.setTag(this);
    }

    public static DutyfreePersonalMessagesHeaderHolder getHolder(Context context) {
        mContext = context;
        View itemView = View.inflate(context, R.layout.item_header_time, null);
        return new DutyfreePersonalMessagesHeaderHolder(itemView);
    }

    public void bind2Holder(DutyfreeMessagesBean bean) {
        tvTime.setText(bean.curtime);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mContext, "跳转", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
