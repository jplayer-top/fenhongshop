package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.SequenceModel;
import com.fanglin.fenhong.microbuyer.base.model.SequenceModelData;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.buyer.adapter.SequenceAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.FragmentViewPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/7-上午10:44.
 * 功能描述:
 */
public class MiaoshaActivity extends BaseFragmentActivityUI implements SequenceModel.SeqsCallBack, SequenceModelData.SequenceDataCallBack {

    @ViewInject(R.id.mrecycle)
    RecyclerView mrecycle;
    @ViewInject(R.id.mpager)
    ViewPager mpager;

    @ViewInject(R.id.LDoing)
    LinearLayout LDoing;
    @ViewInject(R.id.pb)
    ProgressBar pb;
    @ViewInject(R.id.tv_msg)
    TextView tv_msg;
    @ViewInject(R.id.LContent)
    LinearLayout LContent;
    @ViewInject(R.id.iv_refresh)
    ImageView iv_refresh;

    SequenceAdapter seqAdapter;
    SequenceModel seqRequest;

    FragmentViewPagerAdapter adapter;//viewpager适配器

    SequenceModelData seqDataRequest;
    private ShareData shareData;//分享数据
    private int lastPos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_miaosha, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        initView();
    }

    private void initView() {
        setHeadTitle(R.string.miaosha);
        enableTvMore(R.string.if_share,true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mrecycle.setLayoutManager(layoutManager);

        seqAdapter = new SequenceAdapter(mContext);
        seqAdapter.setCallBack(new SequenceAdapter.onItemClickInterface() {
            @Override
            public void onItemClick(int position) {
                if (position < seqAdapter.getItemCount())
                    mpager.setCurrentItem(position);
            }
        });
        mrecycle.setAdapter(seqAdapter);

        //场次的网络请求
        seqRequest = new SequenceModel();
        seqRequest.setModelCallBack(this);

        //获取分享数据
        seqDataRequest = new SequenceModelData();
        seqDataRequest.setModelCallBack(this);

        beginRefresh();

        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        mpager.setAdapter(adapter);
        mpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seqAdapter.setSelected(position);
                mrecycle.smoothScrollToPosition(position);
                if (lastPos < position) {
                    // 向前点击
                    // 恰好位于最后一个
                    int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                    mrecycle.smoothScrollBy(100, 0);
                    if (position - 1 == pos) {
                        FHLog.d("Plucky", "后整");
                    }
                } else {
                    // 恰好位于第一个
                    int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
                    mrecycle.smoothScrollBy(-100, 0);
                    if (position + 1 == pos) {
                        FHLog.d("Plucky", "前整");
                    }
                }

                lastPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(value = {R.id.iv_refresh})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
                beginRefresh();
                break;
        }
    }

    public void beginRefresh() {
        LContent.setVisibility(View.GONE);
        LDoing.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        tv_msg.setVisibility(View.VISIBLE);
        tv_msg.setText(getString(R.string.doing));
        iv_refresh.setVisibility(View.GONE);
        seqRequest.getList();

        seqDataRequest.getData();
    }

    private void endRefresh(boolean isSuccess) {
        pb.setVisibility(View.GONE);
        if (isSuccess) {
            LContent.setVisibility(View.VISIBLE);
            LDoing.setVisibility(View.GONE);
            iv_refresh.setVisibility(View.GONE);
            tv_msg.setText(getString(R.string.doing));
        } else {
            LContent.setVisibility(View.GONE);
            LDoing.setVisibility(View.VISIBLE);
            iv_refresh.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(getString(R.string.op_error_do_again));
        }
    }

    @Override
    public void onSeqsList(List<SequenceModel> list) {
        if (list != null && list.size() > 0) {
            endRefresh(true);
            seqAdapter.setList(list);
            seqAdapter.notifyDataSetChanged();
            List frags = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                MiaoshaFragment miaosha = new MiaoshaFragment();
                SequenceModel sm = list.get(i);
                sm.isLast = (i == list.size() - 1);
                miaosha.setSeq(sm);
                frags.add(miaosha);
            }
            adapter.setList(frags);
            mpager.setCurrentItem(seqAdapter.getOnSalePos());
            adapter.notifyDataSetChanged();
        } else {
            endRefresh(false);
        }
    }

    @Override
    protected void onResume() {
        skipChk = true;
        super.onResume();
    }

    public void pull2nextpage() {
        int i = mpager.getCurrentItem() + 1;
        if (i < seqAdapter.getItemCount()) {
            mpager.setCurrentItem(i);
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (shareData != null) {
            ShareData.fhShare(this, shareData, null);
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.invalid_data));
        }
    }

    @Override
    public void onSeqData(SequenceModelData data) {
        if (data != null) {
            shareData = new ShareData();
            shareData.title = data.share_title;
            shareData.content = data.share_desc;
            shareData.imgs = data.share_img;
            shareData.url = data.share_url;

            setHeadTitle(data.seckilling_title);
        }
    }
}
