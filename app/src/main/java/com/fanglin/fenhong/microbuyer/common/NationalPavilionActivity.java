package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.pulltorefresh.PullToRefreshFHScrollView;
import com.fanglin.fenhong.microbuyer.base.model.NationalPavGoodsEntity;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.base.model.WSNatPavGetActivityDetail;
import com.fanglin.fenhong.microbuyer.base.model.WSNatPavGetActivityGoods;
import com.fanglin.fhui.FHScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.kennyc.view.MultiStateView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import java.util.ArrayList;

/**
 * 国家馆 activity
 * Created by lizhixin on 2015/11/30.
 */
public class NationalPavilionActivity extends BaseFragmentActivityUI implements PullToRefreshBase.OnRefreshListener2 {

    @ViewInject(R.id.iv_top_banner)
    private ImageView ivTopBanner;
    @ViewInject(R.id.ll_classify)
    private LinearLayout llClassify;//分类
    @ViewInject(R.id.ll_adv)
    private LinearLayout llAdv;//广告
    @ViewInject(R.id.ll_goods_list)
    private LinearLayout llGoodsList;//商品列表

    @ViewInject(R.id.ll_filter_header)
    private LinearLayout llFilterHeader;//商品列表排序选项栏
    @ViewInject(R.id.btn)
    Button btn;//返回顶部按钮

    @ViewInject(R.id.pullToRefreshFHScrollView)
    PullToRefreshFHScrollView pullToRefreshFHScrollView;

    @ViewInject(R.id.vSpliterLine)
    View vSpliterLine;

    private LayoutNationalPavCls layoutNationalPavCls;//分类 子模块
    private LayoutNationalPavAdv layoutNationalPavAdv;//广告 子模块
    private LayoutNationalPavGoodsList layoutNationalPavGoodsList;//商品列表 子模块

    private LayoutNavFilter navFilter1;//本页面
    private LayoutNavFilter navFilter2;//商品列表 子模块

    private int curpage = 1;
    private String activity_id;
    private static final String class_id = "0";

    private ShareData shareData;
    private String resource_tags;//统计用

    private LayoutMoreVertical layoutMoreVertical;

    private int w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skipChk = true;//跳过登录检查

        FrameLayout view = (FrameLayout) View.inflate(this, R.layout.activity_national_pavilion02, null);
        LHold.addView(view);

        ViewUtils.inject(this, view);

        activity_id = getIntent().getStringExtra("activity_id");//接收参数

        w = BaseFunc.getDisplayMetrics(mContext).widthPixels;

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            enableMsgDot(true);
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
        } else {
            enableMsgDot(false);
            layoutMoreVertical.setMsgNum(0);
        }
    }

    private void initView() {
        enableIvMore(0);

        layoutMoreVertical=new LayoutMoreVertical(vBottomLine);
        layoutMoreVertical.setIsSearchShow(false);

        //分类 子模块
        layoutNationalPavCls = new LayoutNationalPavCls(this, activity_id);
        llClassify.removeAllViews();
        layoutNationalPavCls.setDatas(null, null, null);
        llClassify.addView(layoutNationalPavCls.getView());

        //广告 子模块
        layoutNationalPavAdv = new LayoutNationalPavAdv(this);
        llAdv.removeAllViews();
        layoutNationalPavAdv.setList(null);
        llAdv.addView(layoutNationalPavAdv.getView());

        //定义两个筛选布局
        navFilter1 = new LayoutNavFilter(this);
        navFilter1.setClickCallBack(new LayoutNavFilter.OnItemClickCallBack() {
            @Override
            public void onItemCLick(int index, int priceIndex) {
                getData(navFilter2, index, priceIndex);
            }
        });
        navFilter2 = new LayoutNavFilter(this);
        navFilter2.setClickCallBack(new LayoutNavFilter.OnItemClickCallBack() {
            @Override
            public void onItemCLick(int index, int priceIndex) {
                getData(navFilter1, index, priceIndex);
            }
        });

        llFilterHeader.addView(navFilter1.getView());//添加一个头部
        llFilterHeader.setVisibility(View.INVISIBLE);//先隐藏

        //商品列表 子模块
        layoutNationalPavGoodsList = new LayoutNationalPavGoodsList(this);
        llGoodsList.removeAllViews();
        layoutNationalPavGoodsList.llFilterHeader.addView(navFilter2.getView());//添加一个头部
        layoutNationalPavGoodsList.setList(null, null);
        llGoodsList.addView(layoutNationalPavGoodsList.getView());

        pullToRefreshFHScrollView.getRefreshableView().setOnScrollListener(new FHScrollView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {

                // 需要验证
                int[] locationInWindow = new int[2];
                llGoodsList.getLocationOnScreen(locationInWindow);//获取控件处于屏幕的位置，x y 坐标
                int[] lineloc = new int[2];
                vBottomLine.getLocationOnScreen(lineloc);

                if (locationInWindow[1] > lineloc[1]) {
                    llFilterHeader.setVisibility(View.INVISIBLE);
                    vSpliterLine.setVisibility(View.INVISIBLE);
                } else {
                    llFilterHeader.setVisibility(View.VISIBLE);
                    vSpliterLine.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTop(int top) {

            }
        });

        pullToRefreshFHScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshFHScrollView.setOnRefreshListener(this);
        pullToRefreshFHScrollView.setScrollingWhileRefreshingEnabled(true);

        onPullDownToRefresh(pullToRefreshFHScrollView);
        refreshViewStatus(MultiStateView.VIEW_STATE_LOADING);

        //回到顶部
        btn.setVisibility(View.GONE);
        btn.setBackgroundResource(R.drawable.back_top);
        pullToRefreshFHScrollView.getRefreshableView().setScrollBtn(btn);
    }

    @Override
    public void onivMoreClick() {
        super.onivMoreClick();
        layoutMoreVertical.show();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        sendRequestToGetActivityDetail(activity_id);
        curpage = 1;
        sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, curpage, 1, 1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        curpage++;
        sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, curpage, 1, 1);
    }

    /**
     * 切换筛选头，刷新商品列表
     */
    private void getData(LayoutNavFilter layoutNavFilter, int index, int priceIndex) {
        layoutNavFilter.changeFiltersBg(index, priceIndex);
        sendRequestToGetGoodsList(activity_id, class_id, BaseVar.REQUESTNUM, 1, index, priceIndex);
    }

    /**
     * 发送请求 刷新 分类 与 广告 与 banner图 与 标题等
     */
    private void sendRequestToGetActivityDetail(String activity_id) {
        WSNatPavGetActivityDetail getActivityDetailHandler = new WSNatPavGetActivityDetail();
        getActivityDetailHandler.setWSGetActivityDetailCallBack(new WSNatPavGetActivityDetail.WSGetActivityDetailCallBack() {
            @Override
            public void onWSGetActivityDetailError(String errcode) {
                refreshViewStatus(MultiStateView.VIEW_STATE_EMPTY);
                pullToRefreshFHScrollView.onRefreshComplete();
                setVisibleOfTvMore(false);
                llAdv.setVisibility(View.GONE);
                llClassify.setVisibility(View.GONE);
            }

            @Override
            public void onWSGetActivityDetailSuccess(WSNatPavGetActivityDetail data) {
                refreshViewStatus(MultiStateView.VIEW_STATE_CONTENT);
                pullToRefreshFHScrollView.onRefreshComplete();
                //开始刷新数据
                if (data != null) {
                    resource_tags = data.resource_tags;
                    //分享
                    setVisibleOfTvMore(true);
                    /** 设置分享格式*/
                    shareData = new ShareData();
                    shareData.title = !TextUtils.isEmpty(data.share_title) ? data.share_title : data.activity_title;
                    String fmt = getString(R.string.share_content_national);
                    shareData.content = !TextUtils.isEmpty(data.share_desc) ? data.share_desc : String.format(fmt, data.activity_title);
                    shareData.imgs = data.share_img;
                    shareData.url = String.format(BaseVar.SHARE_ACTIVITY_NAT, data.activity_id);

                    //标题
                    setHeadTitle(data.activity_title);

                    layoutMoreVertical.setShareData(shareData);

                    //banner 图, 并设置高度
                    int h = w * 350 / 720;
                    new FHImageViewUtil(ivTopBanner).setImageURI(data.activity_banner, FHImageViewUtil.SHOWTYPE.BANNER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
                    ivTopBanner.setLayoutParams(params);

                    if (data.activity_classes != null && data.activity_classes.size() > 0) {
                        //分类 并设置高度
                        layoutNationalPavCls.setDatas(data.activity_classes, shareData, resource_tags);
                    } else {
                        llClassify.setVisibility(View.GONE);
                    }

                    if (data.activity_advs != null && data.activity_advs.size() > 0) {
                        //广告
                        layoutNationalPavAdv.setList(data.activity_advs);
                    } else {
                        llAdv.setVisibility(View.GONE);
                    }

                } else {
                    llAdv.setVisibility(View.GONE);
                    llClassify.setVisibility(View.GONE);
                }
            }
        });
        getActivityDetailHandler.getActivityDetail(member, activity_id);
    }

    /**
     * 发送请求,获取商品列表
     */
    public void sendRequestToGetGoodsList(String activity_id, String class_id, int num, final int curpage, int sort, int order) {
        WSNatPavGetActivityGoods getActivityGoodsHandler = new WSNatPavGetActivityGoods();
        getActivityGoodsHandler.setWSGetActivityGoodsCallBack(new WSNatPavGetActivityGoods.WSGetActivityGoodsCallBack() {
            @Override
            public void onWSGetActivityGoodsError(String errcode) {
                pullToRefreshFHScrollView.onRefreshComplete();
            }

            @Override
            public void onWSGetActivityGoodsSuccess(final ArrayList<NationalPavGoodsEntity> activity_goods) {
                layoutNationalPavGoodsList.llFilterHeader.setVisibility(View.VISIBLE);
                pullToRefreshFHScrollView.onRefreshComplete();
                if (curpage == 1) {
                    layoutNationalPavGoodsList.setList(activity_goods, resource_tags);//设置数据
                } else {
                    layoutNationalPavGoodsList.addList(activity_goods, resource_tags);
                    pullToRefreshFHScrollView.onAppendDateWithDelay(200);
                }
            }
        });
        getActivityGoodsHandler.getActivityGoods(activity_id, class_id, num, curpage, sort, order);
    }


}
