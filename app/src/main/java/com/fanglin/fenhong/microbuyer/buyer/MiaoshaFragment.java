package com.fanglin.fenhong.microbuyer.buyer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragment;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshPinnedHeaderListView;
import com.fanglin.fenhong.microbuyer.base.model.SeqGoodsModel;
import com.fanglin.fenhong.microbuyer.base.model.SequenceModel;
import com.fanglin.fenhong.microbuyer.common.adapter.SeqGoodsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/7-上午11:51.
 * 功能描述:秒杀场次页面
 */
public class MiaoshaFragment extends BaseFragment implements SeqGoodsModel.SeqGoodsModelCallBack, PullToRefreshBase.OnRefreshListener2 {

    View view;
    @ViewInject(R.id.pullToRefreshPinnedHeaderListView)
    PullToRefreshPinnedHeaderListView pullToRefreshPinnedHeaderListView;

    private SequenceModel mseq;
    SeqGoodsAdapter adapter;
    View vheader;
    int mwidth;

    private SeqGoodsModel goodsRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(act, R.layout.fragment_miaosha, null);
        ViewUtils.inject(this, view);
        initView();
    }

    public void setSeq(SequenceModel seq) {
        this.mseq = seq;
    }

    private void pull2nextpage() {
        if (act instanceof MiaoshaActivity) {
            ((MiaoshaActivity) act).pull2nextpage();
        }
    }

    private void initView() {
        mwidth = BaseFunc.getDisplayMetrics(act).widthPixels;

        addHeaderView();

        adapter = new SeqGoodsAdapter(act);
        adapter.setMseq(mseq);

        pullToRefreshPinnedHeaderListView.setScrollingWhileRefreshingEnabled(true);
        pullToRefreshPinnedHeaderListView.setOnRefreshListener(this);

        pullToRefreshPinnedHeaderListView.getRefreshableView().setAdapter(adapter);

        if (mseq != null && mseq.isLast) {
            pullToRefreshPinnedHeaderListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            pullToRefreshPinnedHeaderListView.setMode(PullToRefreshBase.Mode.BOTH);
            pullToRefreshPinnedHeaderListView.getLoadingLayoutProxy().setPullLabel(getString(R.string.miaosha_pulltonext));
        }

        //网络请求
        goodsRequest = new SeqGoodsModel();
        goodsRequest.setModelCallBack(this);

        onPullDownToRefresh(pullToRefreshPinnedHeaderListView);
    }

    @Override
    public void onSeqGoodsList(List<SeqGoodsModel> list) {
        pullToRefreshPinnedHeaderListView.onRefreshComplete();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    CountDownTimer countDownTimer;

    private void addHeaderView() {
        if (mseq == null) return;
        if (vheader != null)
            pullToRefreshPinnedHeaderListView.getRefreshableView().removeHeaderView(vheader);

        vheader = View.inflate(act, R.layout.layout_sequence_header, null);
        TextView tv_icon = (TextView) vheader.findViewById(R.id.tv_icon);
        ImageView iv_banner = (ImageView) vheader.findViewById(R.id.iv_banner);
        TextView tv_miaosha_desc = (TextView) vheader.findViewById(R.id.tv_miaosha_desc);
        final TextView tv_lbl = (TextView) vheader.findViewById(R.id.tv_lbl);
        final TextView tv_miaosha_timeleft = (TextView) vheader.findViewById(R.id.tv_miaosha_timeleft);

        int h = mwidth * 330 / 1080;
        LinearLayout.LayoutParams params_img = new LinearLayout.LayoutParams(mwidth, h);
        iv_banner.setLayoutParams(params_img);

        BaseFunc.setFont(tv_icon);
        if (BaseFunc.isValidUrl(mseq.getPicUrl())) {
            iv_banner.setVisibility(View.VISIBLE);
            new FHImageViewUtil(iv_banner).setImageURI(mseq.getPicUrl(), FHImageViewUtil.SHOWTYPE.SEQBANNER);
            iv_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseFunc.urlClick(act, mseq.getClickUrl());
                }
            });
        } else {
            iv_banner.setVisibility(View.GONE);
        }
        tv_miaosha_desc.setText(mseq.getClockTips());
        if (mseq.sequence_state == 1 && mseq.countdown > 0) {
            tv_lbl.setVisibility(View.VISIBLE);
            tv_miaosha_timeleft.setVisibility(View.VISIBLE);

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            countDownTimer = new CountDownTimer(mseq.countdown * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mseq.countdown = millisUntilFinished / 1000;
                    if (millisUntilFinished > 0) {
                        String atmp = BaseFunc.getCNTimeByTimeStamp(millisUntilFinished / 1000);
                        tv_miaosha_timeleft.setText(atmp);
                    } else {
                        tv_lbl.setVisibility(View.INVISIBLE);
                        tv_miaosha_timeleft.setText("");
                    }
                }

                @Override
                public void onFinish() {
                    tv_miaosha_timeleft.setText("");
                    mseq.countdown = 0;
                    tv_lbl.setVisibility(View.INVISIBLE);
                    ((MiaoshaActivity) act).beginRefresh();
                }
            };
            countDownTimer.start();
        } else {
            tv_lbl.setVisibility(View.INVISIBLE);
            tv_miaosha_timeleft.setVisibility(View.INVISIBLE);
        }

        pullToRefreshPinnedHeaderListView.getRefreshableView().addHeaderView(vheader);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (mseq == null) {
            pullToRefreshPinnedHeaderListView.onRefreshComplete();
            return;
        }
        goodsRequest.getGoods(mseq.sequence_id);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                pull2nextpage();
                pullToRefreshPinnedHeaderListView.onRefreshComplete();
            }
        }.sendEmptyMessageDelayed(0, 50);
    }

    @Override
    public void onResume() {
        super.onResume();
        onPullDownToRefresh(pullToRefreshPinnedHeaderListView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view;
    }
}
