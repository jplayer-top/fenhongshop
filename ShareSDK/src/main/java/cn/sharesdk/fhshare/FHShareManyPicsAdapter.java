package cn.sharesdk.fhshare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import cn.sharesdk.R;
import cn.sharesdk.fhshare.model.FHShareData;
import cn.sharesdk.fhshare.model.PicTag;

/**
 * 多图分享 图片 适配器
 * Created by lizhixin on 2015/12/14.
 */
public class FHShareManyPicsAdapter extends RecyclerView.Adapter<FHShareManyPicsAdapter.PicsViewHolder> {

    private Context mContext;
    private List<PicTag> list;

    public FHShareManyPicsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<PicTag> list) {
        this.list = list;
    }

    @Override
    public PicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_pic_fh_share, null);
        return new PicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PicsViewHolder holder, final int position) {
        FHShareData.displayByUniversal(holder.imageView, list.get(position).url);
        holder.ivChoose.setSelected(true);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ivChoose.isSelected()) {
                    holder.ivChoose.setSelected(false);
                    list.get(position).isSelected = false;
                } else {
                    holder.ivChoose.setSelected(true);
                    list.get(position).isSelected = true;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public List<PicTag> getList() {
        return list;
    }

    class PicsViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView ivChoose;

        public PicsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            ivChoose = (ImageView) itemView.findViewById(R.id.ivChoose);
        }
    }

}
