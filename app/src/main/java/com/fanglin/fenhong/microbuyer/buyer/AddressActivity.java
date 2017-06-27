package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.buyer.adapter.AddressAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-20.
 */
public class AddressActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener {
    @ViewInject(R.id.pullToRefreshRecycleView)
    PullToRefreshRecycleView pullToRefreshRecycleView;

    @ViewInject(R.id.Lnoresult)
    LinearLayout Lnoresult;
    private AddressAdapter adapter;
    Class requestAddrActivity;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        View view = View.inflate (mContext, R.layout.activity_address, null);
        LHold.addView (view);
        ViewUtils.inject (this, view);

        try {
            requestAddrActivity = Class.forName (getIntent ().getStringExtra ("VAL"));
        } catch (Exception e) {
            requestAddrActivity = null;
        }
        initView ();
    }

    private void initView () {
        setHeadTitle(R.string.address_edit);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        pullToRefreshRecycleView.getRefreshableView().setLayoutManager (new LinearLayoutManager (mContext));
        adapter = new AddressAdapter (mContext);
        pullToRefreshRecycleView.getRefreshableView().setAdapter (adapter);
        adapter.setCallBack (new AddressAdapter.AddressCallBack () {

            @Override
            public void onItemClick (int position, Address addr) {
                if (requestAddrActivity != null) {
                    Intent i = new Intent (mContext, requestAddrActivity);
                    i.putExtra ("VAL", new Gson ().toJson (addr));
                    setResult (RESULT_OK, i);
                    finish ();
                } else {
                    BaseFunc.gotoActivity (mContext, EditAddressActivity.class, new Gson ().toJson (addr));
                }
            }


            @Override
            public void onEdit (int position, Address addr) {
                BaseFunc.gotoActivity (mContext, EditAddressActivity.class, new Gson ().toJson (addr));
            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getData ();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        onRefresh(pullToRefreshRecycleView);
    }

    private void getData () {
        if (member == null) {
            pullToRefreshRecycleView.onRefreshComplete();
            return;
        }

        new BaseBO ().setCallBack (new APIUtil.FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                pullToRefreshRecycleView.onRefreshComplete();
                if (isSuccess) {
                    List<Address> list;
                    try {
                        list = new Gson ().fromJson (data, new TypeToken<List<Address>> () {
                        }.getType ());
                    } catch (Exception e) {
                        list = null;
                    }
                    if (list != null) {
                        adapter.setList (list);
                        pullToRefreshRecycleView.getRefreshableView().getAdapter ().notifyDataSetChanged ();
                        Lnoresult.setVisibility (View.GONE);
                        pullToRefreshRecycleView.setVisibility (View.VISIBLE);
                    } else {
                        Lnoresult.setVisibility (View.VISIBLE);
                        pullToRefreshRecycleView.setVisibility (View.GONE);
                    }
                } else {
                    Lnoresult.setVisibility (View.VISIBLE);
                    pullToRefreshRecycleView.setVisibility (View.GONE);
                }
            }
        }).get_address (null, member.member_id, member.token);
    }

    @OnClick(value = {R.id.tv_add, R.id.Lnoresult})
    public void onViewClick (View view) {
        switch (view.getId ()) {
            case R.id.tv_add:
                BaseFunc.gotoActivity (this, EditAddressActivity.class, null);
                break;
            case R.id.Lnoresult:
                onRefresh(pullToRefreshRecycleView);
                break;
        }
    }
}
