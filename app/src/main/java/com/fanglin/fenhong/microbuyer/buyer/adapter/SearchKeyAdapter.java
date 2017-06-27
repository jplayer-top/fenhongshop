package com.fanglin.fenhong.microbuyer.buyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.SearchKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/9/8.
 */
public class SearchKeyAdapter extends RecyclerView.Adapter<SearchKeyAdapter.ViewHolder> {

    private Context mContext;
    private List<SearchKey> list = new ArrayList<> ();

    public SearchKeyAdapter (Context c) {
        this.mContext = c;
    }

    public void setList (List<SearchKey> list) {
        this.list = list;
    }

    public void setListS (List<String> lst) {
        if (lst != null) {
            list = new ArrayList<> ();
            for (int i = 0; i < lst.size (); i++) {
                SearchKey sk = new SearchKey ();
                sk.search_id = i;
                sk.search_keywords = lst.get (i);
                list.add (sk);
            }
        } else {
            list = null;
        }
    }

    @Override
    public int getItemCount () {
        if (list == null) return 0;
        return list.size ();
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        holder.key.setText (list.get (position).search_keywords);

        holder.itemView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mcb != null) {
                    mcb.onItemClick (list.get (position).search_keywords, position);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = View.inflate (mContext, R.layout.item_search, null);
        return new ViewHolder (view);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView key;

        public ViewHolder (View itemView) {
            super (itemView);
            key = (TextView) itemView.findViewById (R.id.key);
        }
    }

    public interface SearchKeyCallBack {
        void onItemClick (String key, int position);
    }

    private SearchKeyCallBack mcb;

    public void setCallBack (SearchKeyCallBack cb) {
        this.mcb = cb;
    }
}
