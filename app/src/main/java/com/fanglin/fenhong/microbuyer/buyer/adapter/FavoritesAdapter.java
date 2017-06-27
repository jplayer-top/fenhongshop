package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanglin.fenhong.microbuyer.base.model.Favorites;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-16.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavViewHolder> {

    public Context mContext;
    public boolean isShowChk = false;
    public int type = -1;
    private List<Favorites> list = new ArrayList<>();

    public FavoritesAdapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<Favorites> lst) {
        list = lst;
    }

    public void addList(List<Favorites> lst) {
        if (lst != null && lst.size() > 0) {
            list.addAll(lst);
        }
    }

    public Favorites getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(FavViewHolder holder, int position) {

    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSelected(boolean flag) {
        if (getItemCount() == 0) return;
        for (int i = 0; i < getItemCount(); i++) {
            list.get(i).isSelected = flag;
        }
        notifyDataSetChanged();
    }

    public String[] getSelectedIds() {
        String[] res = new String[]{null, "0"};
        if (getItemCount() == 0) return res;
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (list.get(i).isSelected) {
                c++;
                switch (type) {
                    case 0:
                        if (sb.length() == 0) {
                            sb.append(list.get(i).goods_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).goods_id);
                        }
                        break;
                    case 1:
                        if (sb.length() == 0) {
                            sb.append(list.get(i).store_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).store_id);
                        }
                        break;
                    case 2:
                        if (sb.length() == 0) {
                            sb.append(list.get(i).shop_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).shop_id);
                        }
                        break;
                    case 3:
                        if (sb.length() == 0) {
                            sb.append(list.get(i).goods_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).goods_id);
                        }
                        break;
                    case 4://time
                        if (sb.length() == 0) {
                            sb.append(list.get(i).time_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).time_id);
                        }
                        break;
                    case 5://dr
                        if (sb.length() == 0) {
                            sb.append(list.get(i).talent_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).talent_id);
                        }
                        break;
                    case 6://brand
                        if (sb.length() == 0) {
                            sb.append(list.get(i).talent_id);
                        } else {
                            sb.append(",");
                            sb.append(list.get(i).talent_id);
                        }
                        break;
                }
            }
        }
        res[0] = sb.toString();
        res[1] = String.valueOf(c);
        return res;
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        public FavViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FavCallBack mcb;

    public void setCallBack(FavCallBack cb) {
        this.mcb = cb;
    }

    public interface FavCallBack {
        /**
         * int  type: 0_表示商品收藏 1_店铺收藏 2_微店收藏 3_我的足迹 4_收藏时光 5_收藏达人 6_收藏品牌
         * int index: 表示当前行数
         */
        void onItemClick(int type, int index);
    }
}
