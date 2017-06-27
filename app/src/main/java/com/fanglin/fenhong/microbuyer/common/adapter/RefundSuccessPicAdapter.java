package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;

import java.util.ArrayList;

/**
 * 退款成功页面图片适配器
 * Created by lizhixin on 2015/11/25.
 */
public class RefundSuccessPicAdapter extends RecyclerView.Adapter<RefundSuccessPicAdapter.RefundPicViewHolder> {

    private Context context;
    public ArrayList<String> list;

    public RefundSuccessPicAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public RefundPicViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (context, R.layout.item_add_pic, null);
        return new RefundPicViewHolder (view);
    }

    @Override
    public void onBindViewHolder (RefundPicViewHolder holder, final int position) {

            new FHImageViewUtil (holder.imageView).setImageURI(list.get(position), FHImageViewUtil.SHOWTYPE.DEFAULT);
            holder.imageView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    if (mcb != null) mcb.onPicView(list.get(position));
                }
            });

    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.size ();
    }

    private LookPicCallBackListener mcb;

    public void setOnPicItemClickListener (LookPicCallBackListener cb) {
        this.mcb = cb;
    }

    public interface LookPicCallBackListener {

        void onPicView(String picUrl);//点击已有图片查看大图
    }

    class RefundPicViewHolder extends ViewHolder {

        private ImageView imageView;

        public RefundPicViewHolder (View view) {
            super (view);
            imageView = (ImageView) view.findViewById (R.id.fiv);
        }
    }
}
