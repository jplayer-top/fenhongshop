package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.GoodsComments;
import com.fanglin.fenhong.microbuyer.common.adapter.ReturnGoodsPicAdapter;
import com.fanglin.fhui.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/14.
 * modify by lizhixin on 2016/02/29
 */
public class GoodsCommentsAdapter extends RecyclerView.Adapter<GoodsCommentsAdapter.ViewHolder> {

    private Context mContext;
    private List<GoodsComments> list = new ArrayList<>();

    public GoodsCommentsAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<GoodsComments> list) {
        this.list = list;
    }

    public void addList(List<GoodsComments> lst) {
        if (lst != null && lst.size() > 0) {
            this.list.addAll(lst);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_date.setText(list.get(position).date);
        holder.tv_content.setText(list.get(position).content);
        holder.tv_member_name.setText(list.get(position).getCommentUser());
        holder.rating.setRating(list.get(position).stars);
        holder.rating.setVisibility(View.GONE);
        new FHImageViewUtil(holder.ivComment).setImageURI(list.get(position).member_avatar, FHImageViewUtil.SHOWTYPE.AVATAR);

        if (!TextUtils.isEmpty(list.get(position).images)) {
            //评论图片
            final ReturnGoodsPicAdapter adapter = new ReturnGoodsPicAdapter(mContext, BaseFunc.quoteStrToArrayList(list.get(position).images));
            adapter.showCloseBtn = false;
            AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 3);

            holder.rc_img.setVisibility(View.VISIBLE);
            holder.rc_img.setLayoutManager(layoutManager);
            holder.rc_img.setAdapter(adapter);
            adapter.setOnPictureItemClickListener(new ReturnGoodsPicAdapter.AddPicCallBackListener() {
                @Override
                public void onAddDefault() {
                }

                @Override
                public void onDelItem(int index) {
                }

                @Override
                public void onPicView(String picUrl) {
                    FileUtils.BrowserOpenL(mContext, adapter.list, picUrl);
                }
            });
        } else {
            holder.rc_img.setVisibility(View.GONE);
        }

        //商家解释
        if (TextUtils.isEmpty(list.get(position).seller_explain)) {
            holder.tv_seller_explain.setVisibility(View.GONE);
        } else {
            holder.tv_seller_explain.setVisibility(View.VISIBLE);
            holder.tv_seller_explain.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.seller_explain), list.get(position).seller_explain)));
        }

        ///////////////////////此处开始 追加评价///////////////////////////
        if (list.get(position).append_evaluate != null && list.get(position).append_evaluate.size() > 0) {
            holder.llSecond.setVisibility(View.VISIBLE);
            GoodsComments commentSecond = list.get(position).append_evaluate.get(0);
            //收货 几 天后追评
            if (commentSecond.evaluate_days - 1 > 0) {
                holder.tv_days_second.setText(String.format(mContext.getString(R.string.days_of_evaluate_after_receive_goods), commentSecond.evaluate_days));
            } else {
                holder.tv_days_second.setText(mContext.getString(R.string.add_evaluate_after_receive_goods));
            }
            //追评内容
            holder.tv_content_second.setText(commentSecond.content);
            //商家对追评的解释
            if (TextUtils.isEmpty(commentSecond.seller_explain)) {
                holder.tv_seller_explain_second.setVisibility(View.GONE);
            } else {
                holder.tv_seller_explain_second.setVisibility(View.VISIBLE);
                holder.tv_seller_explain_second.setText(BaseFunc.fromHtml(String.format(mContext.getString(R.string.seller_explain), commentSecond.seller_explain)));
            }
            //追评图片
            if (!TextUtils.isEmpty(commentSecond.images)) {
                final ReturnGoodsPicAdapter adapter = new ReturnGoodsPicAdapter(mContext, BaseFunc.quoteStrToArrayList(commentSecond.images));
                adapter.showCloseBtn = false;
                AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 3);
                holder.rc_img_second.setLayoutManager(layoutManager);
                holder.rc_img_second.setAdapter(adapter);
                holder.rc_img_second.setVisibility(View.VISIBLE);

                adapter.setOnPictureItemClickListener(new ReturnGoodsPicAdapter.AddPicCallBackListener() {
                    @Override
                    public void onAddDefault() {
                    }

                    @Override
                    public void onDelItem(int index) {
                    }

                    @Override
                    public void onPicView(String picUrl) {
                        FileUtils.BrowserOpenL(mContext, adapter.list, picUrl);
                    }
                });
            } else {
                holder.rc_img_second.setVisibility(View.GONE);
            }
        } else {
            holder.llSecond.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_evaluation, null);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_member_name;
        public TextView tv_date;
        public RatingBar rating;
        public CircleImageView ivComment;
        public TextView tv_content, tv_content_second, tv_days_second;
        public RecyclerView rc_img, rc_img_second;
        public TextView tv_seller_explain, tv_seller_explain_second;//商家解释
        public LinearLayout llSecond;

        public ViewHolder(View itemView) {
            super(itemView);
            ivComment = (CircleImageView) itemView.findViewById(R.id.ivComment);
            tv_member_name = (TextView) itemView.findViewById(R.id.tv_member_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_content_second = (TextView) itemView.findViewById(R.id.tv_content_second);
            tv_days_second = (TextView) itemView.findViewById(R.id.tv_days_second);
            rc_img = (RecyclerView) itemView.findViewById(R.id.rc_img);
            rc_img_second = (RecyclerView) itemView.findViewById(R.id.rc_img_second);
            tv_seller_explain = (TextView) itemView.findViewById(R.id.tv_seller_explain);
            tv_seller_explain_second = (TextView) itemView.findViewById(R.id.tv_seller_explain_second);
            llSecond = (LinearLayout) itemView.findViewById(R.id.ll_second);
        }
    }


}
