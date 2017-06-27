package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 退货退款申请 图片上传 适配器
 * Created by lizhixin on 2015/11/10.
 */
public class ReturnGoodsPicAdapter extends RecyclerView.Adapter<ReturnGoodsPicAdapter.ReturnGoodsPicViewHolder> {

    private Context context;
    public List<String> list;
    public boolean showCloseBtn = true;//是否显示删除图片按钮

    public ReturnGoodsPicAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public ArrayList<String> getArrayList() {
        ArrayList<String> res = new ArrayList<>();
        res.addAll(list);
        return res;
    }

    @Override
    public ReturnGoodsPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_add_pic, null);
        return new ReturnGoodsPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReturnGoodsPicViewHolder holder, final int position) {

        if ("ADD".equals(list.get(position))) {//默认的添加按钮图片
            holder.imageView.setImageResource(R.drawable.add_pic);
            holder.tvClose.setVisibility(View.INVISIBLE);
            //holder.tvClose
            holder.imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mcb != null) mcb.onAddDefault();
                }
            });

        } else {
            new FHImageViewUtil(holder.imageView).setImageURI(list.get(position), FHImageViewUtil.SHOWTYPE.DEFAULT);
            if (showCloseBtn) {
                holder.tvClose.setVisibility(View.VISIBLE);
            }
            BaseFunc.setFont(holder.tvClose);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mcb != null) mcb.onPicView(list.get(position));
                }
            });
        }

        holder.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcb != null) mcb.onDelItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    private AddPicCallBackListener mcb;

    public void setOnPictureItemClickListener(AddPicCallBackListener cb) {
        this.mcb = cb;
    }

    public interface AddPicCallBackListener {
        void onAddDefault();//点击添加图片按钮

        void onDelItem(int index);//删除某个位置上的图片

        void onPicView(String picUrl);//点击已有图片查看大图
    }

    class ReturnGoodsPicViewHolder extends ViewHolder {

        private ImageView imageView;
        private TextView tvClose;

        public ReturnGoodsPicViewHolder(View view) {
            super(view);
            tvClose = (TextView) view.findViewById(R.id.tv_close);
            imageView = (ImageView) view.findViewById(R.id.fiv);
        }
    }
}
