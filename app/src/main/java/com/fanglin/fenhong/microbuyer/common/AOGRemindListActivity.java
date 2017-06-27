package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshRecycleView;
import com.fanglin.fenhong.microbuyer.base.model.GoodsArrivalNotice;
import com.fanglin.fenhong.microbuyer.base.model.WSArrivalNotice;
import com.fanglin.fenhong.microbuyer.common.adapter.AOGRemindListAdapter;
import com.fanglin.fhui.FHHintDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;

/**
 * Created by lizhixin on 2016/04/05
 * 我的提醒 商品列表 activity
 */
public class AOGRemindListActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2, WSArrivalNotice.WSArrivalNoticeCallBack, AOGRemindListAdapter.RemindListener, FHHintDialog.FHHintListener {

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;
    @ViewInject(R.id.ivBack)
    private ImageView ivBack;
    @ViewInject(R.id.tvOption)
    private TextView tvOption;
    @ViewInject(R.id.tvDelete)
    private TextView tvDelete;
    @ViewInject(R.id.cbSelectAll)
    private CheckBox cbSelectAll;

    @ViewInject(R.id.pullToRefreshRecycleView)
    private PullToRefreshRecycleView pullToRefreshRecycleView;

    @ViewInject(R.id.llBottom)
    private LinearLayout llBottom;
    private AOGRemindListAdapter adapter;
    private FHHintDialog deleteDialog;//弹框提示是否删除商品
    private Animation animIn, animOut;
    private WSArrivalNotice wsArrivalNotice;//API请求类
    private int curPage = 1;//当前页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aogremind_list);
        ViewUtils.inject(this);

        ivBack.setOnClickListener(this);
        tvOption.setOnClickListener(this);
        llBottom.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.setCheckBox(true, true);
                } else {
                    adapter.setCheckBox(false, true);
                }
                adapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshRecycleView.getRefreshableView().setLayoutManager(layoutManager);

        adapter = new AOGRemindListAdapter(this, new ArrayList<GoodsArrivalNotice>());
        pullToRefreshRecycleView.getRefreshableView().setAdapter(adapter);
        adapter.setRemindListener(this);

        animIn = AnimationUtils.loadAnimation(this, R.anim.translate_down2up);
        animIn.setAnimationListener(animInListener);
        animOut = AnimationUtils.loadAnimation(this, R.anim.translate_up2down);
        animOut.setAnimationListener(animOutListener);

        wsArrivalNotice = new WSArrivalNotice();
        wsArrivalNotice.setWSArrivalNoticeCallBack(this);

        pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshRecycleView.setOnRefreshListener(this);
        pullToRefreshRecycleView.setScrollingWhileRefreshingEnabled(true);

        //删除提示对话框
        deleteDialog = new FHHintDialog(mContext);
        deleteDialog.setHintListener(this);
        deleteDialog.setTvContent(getString(R.string.confirm_delete_remind));
        deleteDialog.setTvRight(getString(R.string.sure));
        deleteDialog.setTvLeft(getString(R.string.text_cancel));
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPullDownToRefresh(pullToRefreshRecycleView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvOption:
                if (llBottom.getVisibility() == View.GONE) {
                    tvOption.setText(getString(R.string.cancel));
                    llBottom.startAnimation(animIn);
                    adapter.setCheckBox(false, true);
                    cbSelectAll.setChecked(false);//首次显示编辑状态时设置不选中
                } else {
                    tvOption.setText(getString(R.string.edit));
                    llBottom.startAnimation(animOut);
                    adapter.setCheckBox(false, false);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tvDelete:
                batchCancelArrivalNotice();
                break;
            default:
                break;
        }
    }

    /**
     * 批量取消提醒
     */
    private void batchCancelArrivalNotice() {
        if (adapter.getList() != null) {
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < adapter.getList().size(); i++) {
                GoodsArrivalNotice entity = adapter.getList().get(i);
                if (entity.isChecked()) {
                    ids.append(",");
                    ids.append(entity.goods_id);//记录商品Id
                }
            }
            if (ids.toString().length() < 1) {
                BaseFunc.showMsg(mContext, getString(R.string.select_one_at_least));
            } else {
                //弹框提示
                deleteDialog.setConfirmData(ids.toString());
                deleteDialog.show();//组装好goodsID
            }
        }
    }

    /**
     * 点击取消提醒
     */
    @Override
    public void onCancelRemind(String goodsId, String position) {
        cancelArrivalNotice(goodsId);
    }

    /**
     * 取消到货提醒 发送请求
     *
     * @param goodsId 多个id用逗号分隔
     */
    public void cancelArrivalNotice(final String goodsId) {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    BaseFunc.showSelfToast(mContext, getString(R.string.remind_unset_success));
                    for (String id : goodsId.split(",")) {
                        adapter.removeItem(id);
                    }
                    adapter.notifyDataSetChanged();
                    //当数据被全部清空后，没有必要再停留了
                    if (adapter.getList() == null || adapter.getList().size() == 0) {
                        finish();
                    }
                } else {
                    BaseFunc.showMsg(mContext, getString(R.string.option_failure));
                }
            }
        }).cancelArrivalNotice(member.member_id, member.token, goodsId, null);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            finish();
        } else {
            if (llBottom.getVisibility() == View.VISIBLE) {
                tvOption.setText(getString(R.string.edit));
                llBottom.startAnimation(animOut);
            }
            curPage = 1;
            wsArrivalNotice.getList(member, curPage, "1");
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curPage++;
        wsArrivalNotice.getList(member, curPage, "1");
    }

    @Override
    public void onArrivalNoticeError(String errcode) {
        progressBar.setVisibility(View.GONE);
        pullToRefreshRecycleView.onRefreshComplete();
        if (curPage > 1) {
            pullToRefreshRecycleView.showNoMore();
        }else{
            BaseFunc.showMsg(mContext,getString(R.string.no_data));
        }
    }

    @Override
    public void onArrivalNoticeSuccess(ArrayList<GoodsArrivalNotice> data) {
        progressBar.setVisibility(View.GONE);
        pullToRefreshRecycleView.onRefreshComplete();
        if (curPage == 1) {
            pullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
            adapter.setList(data);
        } else {
            adapter.addList(data);
            pullToRefreshRecycleView.onAppendData();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {
        cancelArrivalNotice(deleteDialog.getConfirmData());
    }

    private Animation.AnimationListener animInListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            llBottom.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private Animation.AnimationListener animOutListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            llBottom.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

}
