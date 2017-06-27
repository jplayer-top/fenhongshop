package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.EntityOfAddTalentImage;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageTag;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/16-上午10:05.
 * 功能描述: 添加时光--关联商品--添加时光图片适配器
 */
public class AddTalentImagesAdapter extends RecyclerView.Adapter<AddTalentImagesAdapter.ItemViewHolder> {

    private Context mContext;
    private List<EntityOfAddTalentImage> list;
    private boolean isEditMode;//是否为编辑模式
    private int maxSize;//允许添加的最多图片

    public AddTalentImagesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<EntityOfAddTalentImage> list) {
        this.list = list;
    }

    public List<EntityOfAddTalentImage> getList() {
        return list;
    }

    public void setEditModeAndMaxSize(boolean isEditMode, int maxSize) {
        this.isEditMode = isEditMode;
        this.maxSize = maxSize;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_link_talentimage, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        if (position == 0) {
            //如果是添加图片的按钮
            holder.imageView.setImageResource(R.drawable.icon_addpicture_gray);
            if (isEditMode) {
                if (getItemCount() - 1 < maxSize) {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        } else {
            String aimage = getItem(position).getImage();
            new FHImageViewUtil(holder.imageView).setImageURI(aimage, FHImageViewUtil.SHOWTYPE.DEFAULT);
        }

        if (listener != null) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 1;
        return list.size() + 1;
    }

    public EntityOfAddTalentImage getItem(int position) {
        return list.get(position - 1);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }


    /**
     * 给Item添加点击事件
     */

    private AddTalentImagesAdapterListener listener;

    public void setListener(AddTalentImagesAdapterListener listener) {
        this.listener = listener;
    }

    public interface AddTalentImagesAdapterListener {
        void onItemClick(int position);

        /**
         * 长按事件 第一个是添加按钮
         * 按照position==0 即可判断
         *
         * @param position int
         */
        void onItemLongClick(int position);
    }

    public String getListCountStr() {
        return formatCount(getItemCount() - 1);
    }

    public String formatCount(int count) {
        return count + "/" + maxSize;
    }

    /**
     * 关联商品
     *
     * @param talentImage talentImage
     */
    public void addItem(EntityOfAddTalentImage talentImage) {
        if (talentImage != null && list != null) {
            int sameId = findTheSameEntityIndex(talentImage);
            if (sameId >= 0) {
                //修改sameId元素
                list.set(sameId, talentImage);
            } else {
                list.add(talentImage);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 时光图片会用到
     *
     * @param talentImage entity
     * @return index
     */
    private int findTheSameEntityIndex(EntityOfAddTalentImage talentImage) {
        if (talentImage != null && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                EntityOfAddTalentImage image = list.get(i);
                if (TextUtils.equals(image.getImage(), talentImage.getImage())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 取消关联商品
     *
     * @param talentImage talentImage
     */
    public void removeItem(EntityOfAddTalentImage talentImage) {
        if (talentImage != null && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(list.get(i).getGoods_id(), talentImage.getGoods_id())) {
                    removeItem(i + 1);
                    //跳出循环
                    return;
                }
            }
        }
    }

    /**
     * 通过position 删除
     *
     * @param position int
     */
    public void removeItem(int position) {
        if (list != null) {
            list.remove(position - 1);
            notifyDataSetChanged();
        }
    }

    /**
     * 关联商品列表移除商品的时候 如果该商品是时光所绑定的则移除
     *
     * @param goodsId string
     */
    public void removeGoodsById(String goodsId) {
        if (list == null || list.size() == 0) return;
        for (int i = 0; i < list.size(); i++) {
            EntityOfAddTalentImage image = list.get(i);
            List<TalentImageTag> goods = image.getGoods();
            if (goods != null && goods.size() > 0) {
                int index = -1;
                for (int j = 0; j < goods.size(); j++) {
                    if (TextUtils.equals(goods.get(j).getGoods_id(), goodsId)) {
                        index = j;
                        break;
                    }
                }
                if (index >= 0) {
                    goods.remove(index);
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }
}
