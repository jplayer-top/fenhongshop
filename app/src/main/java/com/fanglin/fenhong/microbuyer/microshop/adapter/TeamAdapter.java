package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.model.Team;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 我的团队 list适配器
 * Created by admin on 2015/11/5.
 */
public class TeamAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Team> list;
    private DecimalFormat commissionFmt;

    public TeamAdapter(Context mContext, ArrayList<Team> list) {
        this.mContext = mContext;
        this.list = list;
        commissionFmt = new DecimalFormat("#0.00元");
    }

    public ArrayList<Team> getList() {
        return list;
    }

    public void setList(ArrayList<Team> list) {
        this.list = list;
    }

    public void addList(ArrayList<Team> list) {
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Team getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_team, null);
            new ViewHolder(convertView);
        }

        Team item = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tvPerson.setText(item.getMember_name());
        viewHolder.tvType.setText(item.getDeep());
        int level = item.getLevel();
        if (level == 1) {
            viewHolder.tvType.setTextColor(mContext.getResources().getColor(R.color.onelevel));
        } else if (level == 2) {
            viewHolder.tvType.setTextColor(mContext.getResources().getColor(R.color.blue));
        } else {
            viewHolder.tvType.setTextColor(mContext.getResources().getColor(R.color.threelevel));
        }
        viewHolder.tvMoney.setText(commissionFmt.format(item.getDeduct_money()));

        viewHolder.tvStatus.setText(item.getVerifyDesc());
        viewHolder.tvStatus.setSelected(item.isVerified());

        return convertView;
    }

    private class ViewHolder {
        private TextView tvPerson, tvType, tvStatus, tvMoney;

        public ViewHolder(View view) {
            tvPerson = (TextView) view.findViewById(R.id.tvPerson);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvMoney = (TextView) view.findViewById(R.id.tvMoney);
            view.setTag(this);
        }
    }
}
