package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavClassifyEntity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.common.NationalPavilionGoodsListActivity;
import com.google.gson.Gson;
import java.util.List;

/**
 * 国家馆 —— 分类 —— 数据适配
 * Created by lizhixin on 2015/11/27.
 */
public class NationalPavClassifyAdapter extends RecyclerView.Adapter<NationalPavClassifyAdapter.ClassifyViewHolder> {

    private Context context;
    private List<NationalPavClassifyEntity> list;
    private String activity_id;
    private ShareData shareDataBackup;//备用的国家馆首页的数据，如果分类接口没有返回分享数据则使用备用的
    private String resource_tags;//统计用

    public NationalPavClassifyAdapter(Context context, String activity_id) {
        this.context = context;
        this.activity_id = activity_id;
    }

    public void setList(List<NationalPavClassifyEntity> list, String resource_tags) {
        this.resource_tags = resource_tags;
        this.list = list;
    }

    public void setShareData(ShareData shareData) {
        this.shareDataBackup = shareData;
    }

    @Override
    public ClassifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_national_pav_classify, null);
        return new ClassifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassifyViewHolder holder, int position) {
        final NationalPavClassifyEntity classifyEntity = list.get(position);
        if (classifyEntity != null) {
            new FHImageViewUtil(holder.image).setImageURI(classifyEntity.getClass_pic(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            holder.tv.setText(classifyEntity.getClass_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NationalPavilionGoodsListActivity.class);
                    intent.putExtra("activity_id", activity_id);
                    intent.putExtra("class_id", classifyEntity.getClass_id());
                    intent.putExtra("class_name", classifyEntity.getClass_name());
                    intent.putExtra("resource_tags", resource_tags);//统计用

                    ShareData shareData;
                    /** 优先使用返回的数据*/
                    if (!TextUtils.isEmpty(classifyEntity.getShare_title())
                            && !TextUtils.isEmpty(classifyEntity.getShare_desc())
                            && !TextUtils.isEmpty(classifyEntity.getShare_img())) {

                        shareData = new ShareData();
                        shareData.title = classifyEntity.getShare_title();
                        shareData.content = classifyEntity.getShare_desc();
                        shareData.imgs = classifyEntity.getShare_img();
                        shareData.url = String.format(BaseVar.SHARE_ACTIVITY_NAT_CLASS, classifyEntity.getClass_id(), classifyEntity.getEncodeClassName(), activity_id);

                    } else {
                        /** 如果分类接口没有返回分享数据则使用备用的*/
                        shareData = shareDataBackup;
                    }

                    intent.putExtra("share_data", shareData == null ? "" : new Gson().toJson(shareData));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    class ClassifyViewHolder extends ViewHolder {

        private ImageView image;
        private TextView tv;
        private View itemView;

        public ClassifyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.iv_item_classify);
            tv = (TextView) itemView.findViewById(R.id.tv_item_classify);
        }
    }

}
