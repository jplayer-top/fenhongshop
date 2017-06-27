package com.fanglin.fenhong.microbuyer.merchant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.StoreCategory;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-10-1.
 */
public class JoinStep3Adapter extends RecyclerView.Adapter<JoinViewHolder> {

    private List<StoreCategory> list;
    private Context mContext;

    public JoinStep3Adapter(Context c) {
        this.mContext = c;
    }

    public void setList(List<StoreCategory> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public String[] getIds_Names(int position) {
        String[] res = new String[]{null, null};
        if (list != null) {
            res[0] = list.get(position).id1 + "," + list.get(position).id2 + "," + list.get(position).id3;
            res[1] = list.get(position).name1 + "," + list.get(position).name2 + "," + list.get(position).name3;
        }
        return res;
    }

    public void addItem(StoreCategory item) {
        this.list.add(item);
    }

    public void removeItem(int pos) {
        this.list.remove(pos);
    }

    /*前端UI排除相同的类目*/
    public boolean containsItem(StoreCategory item) {
        if (item == null) {
            return false;
        }
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(list.get(i).id1, item.id1)
                    && TextUtils.equals(list.get(i).id2, item.id2)
                    && TextUtils.equals(list.get(i).id3, item.id3)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBindViewHolder(JoinViewHolder holder, final int position) {
        holder.tv_cls1.setText(list.get(position).name1);
        holder.tv_cls2.setText(list.get(position).name2);
        holder.tv_cls3.setText(list.get(position).name3);

        holder.tv_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (callback != null) {
                    callback.onDelete(position);
                }
            }
        });
    }

    @Override
    public JoinViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_merchant_join, null);
        return new JoinViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private JoinStep3CallBack callback;

    public void setCallback(JoinStep3CallBack cb) {
        this.callback = cb;
    }

    public interface JoinStep3CallBack {
        void onDelete(int position);
    }
}
