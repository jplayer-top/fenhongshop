package cn.sharesdk.fhshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.sharesdk.R;
import cn.sharesdk.fhshare.model.FHShareItem;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享 文字+图标 适配器
 * Created by lizhixin on 2015/12/10.
 */
public class FHShareAdapter extends RecyclerView.Adapter<FHShareAdapter.ShareViewHolder> {

    private Context mContext;
    private List<FHShareItem> list;
    private Typeface typeface;

    public FHShareAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<FHShareItem> list) {
        this.list = list;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(mContext, R.layout.item_fh_share, null);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShareViewHolder viewHolder, int i) {
        FHShareItem entity = list.get(i);
        if (entity != null) {
            viewHolder.tvName.setText(entity.getName());

            /*viewHolder.tvIcon.setText(mContext.getString(entity.getIcon()));
            viewHolder.tvIcon.setTextColor(mContext.getResources().getColor(entity.getIconColor()));*/

            viewHolder.imageView.setImageResource(entity.getIcon());

            viewHolder.imageView.setOnClickListener(entity.getListener());//直接拿已设置的listener来用
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    private void setFont(TextView view) {
        if (typeface != null) {
            view.setTypeface(typeface);
        }
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;//文字
        //private TextView tvIcon;//图标
        private ImageView imageView;

        public ShareViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_share_name);
            //this.tvIcon = (TextView) itemView.findViewById(R.id.tv_share_icon);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_share);
        }
    }
}
