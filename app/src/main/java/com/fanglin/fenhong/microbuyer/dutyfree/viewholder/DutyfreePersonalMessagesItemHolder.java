package com.fanglin.fenhong.microbuyer.dutyfree.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.event.DutyfreeVipEvent;
import com.fanglin.fenhong.microbuyer.base.model.dutyfree.DutyfreeMessagesBean;

import de.greenrobot.event.EventBus;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Oblivion on 2017/1/9.
 * 功能描述:
 * Change By Someone (You Should Mark in Here)
 */
public class DutyfreePersonalMessagesItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static Context mContext;
    private TextView tvContent;
    private TextView tvTitle;
    private ImageView ivRead;
    private LinearLayout llLookMore;
    private RelativeLayout rlLookMore;

    public DutyfreePersonalMessagesItemHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        ivRead = (ImageView) itemView.findViewById(R.id.ivRead);
        llLookMore = (LinearLayout) itemView.findViewById(R.id.llLookMore);
        rlLookMore = (RelativeLayout) itemView.findViewById(R.id.rlLookMore);
        llLookMore.setOnClickListener(this);
        itemView.setTag(this);
    }

    public static DutyfreePersonalMessagesItemHolder getHodler(Context context) {
        mContext = context;
        View itemView = View.inflate(context, R.layout.item_dutyfree_messageitem, null);
        return new DutyfreePersonalMessagesItemHolder(itemView);
    }

    public void bindData2ItemHolder(DutyfreeMessagesBean data) {
        this.data = data;
        tvContent.setText(data.msg);
        tvTitle.setText(data.msg_title);
        if (data.is_read.equals("0")) {
            ivRead.setVisibility(View.VISIBLE);
        } else {
            ivRead.setVisibility(View.INVISIBLE);
        }

    }

    private DutyfreeMessagesBean data;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLookMore:
            case R.id.rlLookMore:
                //   Toast.makeText(mContext, "跳转到消息详情", Toast.LENGTH_SHORT).show();
                if (data.is_read.equals("0")) {
                    data.is_read = "1";
                }
                bindData2ItemHolder(data);
                if (!TextUtils.isEmpty(data.msg_url)) {
                    BaseFunc.urlClick(mContext, data.msg_url);
                }
                //返回技术免税消息条目数据
                EventBus.getDefault().post(new DutyfreeVipEvent(true));
                break;
        }
    }
}
