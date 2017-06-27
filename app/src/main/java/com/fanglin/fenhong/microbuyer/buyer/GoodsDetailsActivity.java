package com.fanglin.fenhong.microbuyer.buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.FHApp;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.PictureView;
import com.fanglin.fenhong.microbuyer.base.baselib.VarInstance;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivity;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.baseui.FHWebView;
import com.fanglin.fenhong.microbuyer.base.event.CartActionEvent;
import com.fanglin.fenhong.microbuyer.base.model.ActivityGoods;
import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.base.model.Area;
import com.fanglin.fenhong.microbuyer.base.model.Banner;
import com.fanglin.fenhong.microbuyer.base.model.BrandGoods;
import com.fanglin.fenhong.microbuyer.base.model.CartCheckEntity;
import com.fanglin.fenhong.microbuyer.base.model.Favorites;
import com.fanglin.fenhong.microbuyer.base.model.GoodsBundling;
import com.fanglin.fenhong.microbuyer.base.model.GoodsComments;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDetail;
import com.fanglin.fenhong.microbuyer.base.model.GoodsDtlReq;
import com.fanglin.fenhong.microbuyer.base.model.ShareData;
import com.fanglin.fenhong.microbuyer.buyer.adapter.GoodsAttrAdpter;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.ActionChangeListener;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.GoodsButtonHandler;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.GoodsRecommentsActivity;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutBundlingTips;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutDeliverRemark;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutGoodsIntro;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutGoodsPromotion;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutGoodsSpec;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutPickAddress;
import com.fanglin.fenhong.microbuyer.buyer.goodsdetail.LayoutTaxfee;
import com.fanglin.fenhong.microbuyer.common.CartActivity;
import com.fanglin.fenhong.microbuyer.common.FHBrowserActivity;
import com.fanglin.fenhong.microbuyer.common.GroupActivity;
import com.fanglin.fenhong.microbuyer.common.LayoutMoreVertical;
import com.fanglin.fenhong.microbuyer.common.StoreActivity;
import com.fanglin.fenhong.microbuyer.common.adapter.HomeNavRecycleGoodsAdapter;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.FHScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Plucky on 16-4-24.
 * 商品详情
 * modify by lizhixin on 2016/04/25 更多按钮与弹框
 */
public class GoodsDetailsActivity extends BaseFragmentActivity implements Favorites.FavModelCallBack, GoodsDtlReq.GoodsDtlModelCallBack, LayoutGoodsSpec.SpecListener, FHScrollView.OnScrollListener, ActionChangeListener, LayoutPickAddress.PickAddressListener, BrandGoods.BrandGoodsModelCallBack, LayoutGoodsPromotion.GoodsPromotionListener {

    @ViewInject(R.id.LLoading)
    LinearLayout LLoading;
    @ViewInject(R.id.progressBar)
    ProgressBar progressBar;
    @ViewInject(R.id.ivError)
    ImageView ivError;

    @ViewInject(R.id.vTopLine)
    View vTopLine;
    @ViewInject(R.id.msgDot)
    View msgDot;
    @ViewInject(R.id.imgMore)
    ImageView imgMore;
    @ViewInject(R.id.tvCartNum)
    TextView tvCartNum;
    @ViewInject(R.id.ivConllect)
    ImageView ivConllect;

    @ViewInject(R.id.scrollView)
    FHScrollView scrollView;
    @ViewInject(R.id.btnBackTop)
    Button btnBackTop;
    @ViewInject(R.id.LContent)
    LinearLayout LContent;
    @ViewInject(R.id.LBanner)
    LinearLayout LBanner;
    @ViewInject(R.id.tvGoodsName)
    TextView tvGoodsName;
    @ViewInject(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @ViewInject(R.id.tvGoodsPromHint)
    TextView tvGoodsPromHint;
    @ViewInject(R.id.tvGoodsMarketprice)
    TextView tvGoodsMarketprice;
    @ViewInject(R.id.tvMarketIcon)
    TextView tvMarketIcon;
    @ViewInject(R.id.LTaxfee)
    LinearLayout LTaxfee;
    @ViewInject(R.id.tvTaxLbl)
    TextView tvTaxLbl;
    @ViewInject(R.id.tvTaxfeeIcon)
    TextView tvTaxfeeIcon;
    @ViewInject(R.id.ivNationFlag)
    ImageView ivNationFlag;
    @ViewInject(R.id.tvNationDesc)
    TextView tvNationDesc;
    @ViewInject(R.id.tvGoodsDesc)
    TextView tvGoodsDesc;
    @ViewInject(R.id.LShare)
    LinearLayout LShare;
    @ViewInject(R.id.tvShareIcon)
    TextView tvShareIcon;
    @ViewInject(R.id.tvShareWin)
    TextView tvShareWin;
    @ViewInject(R.id.LMiaosha)
    LinearLayout LMiaosha;
    @ViewInject(R.id.tvMiaoshaDesc)
    TextView tvMiaoshaDesc;
    @ViewInject(R.id.tvHour)
    TextView tvHour;
    @ViewInject(R.id.tvMinutes)
    TextView tvMinutes;
    @ViewInject(R.id.tvSecond)
    TextView tvSecond;
    @ViewInject(R.id.LSpec)
    LinearLayout LSpec;
    @ViewInject(R.id.tvCurrentSpec)
    TextView tvCurrentSpec;
    @ViewInject(R.id.LPromotion)
    LinearLayout LPromotion;
    @ViewInject(R.id.tvArea)
    TextView tvArea;
    @ViewInject(R.id.tvGoodsFreight)
    TextView tvGoodsFreight;
    @ViewInject(R.id.tvRefundTips)
    TextView tvRefundTips;
    @ViewInject(R.id.LComment)
    LinearLayout LComment;
    @ViewInject(R.id.ivComment)
    CircleImageView ivComment;
    @ViewInject(R.id.tvCommentCount)
    TextView tvCommentCount;
    @ViewInject(R.id.tvCommentUser)
    TextView tvCommentUser;
    @ViewInject(R.id.tvCommentContent)
    TextView tvCommentContent;
    @ViewInject(R.id.ivStoreLogo)
    ImageView ivStoreLogo;
    @ViewInject(R.id.tvStoreName)
    TextView tvStoreName;
    @ViewInject(R.id.tvStoreNotice)
    TextView tvStoreNotice;
    @ViewInject(R.id.tvNoticeClick)
    TextView tvNoticeClick;
    @ViewInject(R.id.ivGlobalService)
    ImageView ivGlobalService;

    @ViewInject(R.id.FWeb)
    FrameLayout FWeb;
    @ViewInject(R.id.FAttr)
    FrameLayout FAttr;
    @ViewInject(R.id.LNotFake)
    LinearLayout LNotFake;
    @ViewInject(R.id.LFake)
    LinearLayout LFake;
    @ViewInject(R.id.FWebFake)
    FrameLayout FWebFake;
    @ViewInject(R.id.FAttrFake)
    FrameLayout FAttrFake;

    @ViewInject(R.id.FWebContent)
    FrameLayout FWebContent;
    @ViewInject(R.id.webView)
    FHWebView webView;
    @ViewInject(R.id.listViewAttr)
    ListView listViewAttr;
    @ViewInject(R.id.LWeb)
    LinearLayout LWeb;
    @ViewInject(R.id.webProgressBar)
    ProgressBar webProgressBar;

    @ViewInject(R.id.tvSaleOutHint)
    TextView tvSaleOutHint;
    @ViewInject(R.id.tvLeft)
    TextView tvLeft;
    @ViewInject(R.id.tvRight)
    TextView tvRight;

    public String goods_id;
    Favorites fav;
    GoodsDtlReq goodsDtlReq;
    GoodsDetail _goodsDtl;
    ShareData shareData;
    int buyNum = 1;
    LayoutMoreVertical layoutMoreVertical;
    LayoutGoodsSpec layoutGoodsSpec;
    LayoutTaxfee layoutTaxfee;
    LayoutBundlingTips layoutBundlingTips;

    DecimalFormat dfPrice;
    CountDownTimer countDownTimer;

    @ViewInject(R.id.tvNotSupportArea)
    TextView tvNotSupportArea;

    //品牌信息+品牌商品 显示区域
    @ViewInject(R.id.LBrandDtl)
    LinearLayout LBrandDtl;
    @ViewInject(R.id.ivBrandLogo)
    ImageView ivBrandLogo;
    @ViewInject(R.id.tvBrandName)
    TextView tvBrandName;
    @ViewInject(R.id.tvBrandGoodsNum)
    TextView tvBrandGoodsNum;
    @ViewInject(R.id.tvBrandDesc)
    TextView tvBrandDesc;
    @ViewInject(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewInject(R.id.LFuli)
    LinearLayout LFuli;
    @ViewInject(R.id.tvMicroLvl)
    TextView tvMicroLvl;
    @ViewInject(R.id.tvMicroWin)
    TextView tvMicroWin;

    LayoutDeliverRemark layoutDeliverRemark;
    LayoutPickAddress layoutPickAddress;
    LayoutGoodsIntro layoutGoodsIntro;
    LayoutGoodsPromotion layoutGoodsPromotion;

    private String resource_tags;//统计数据，透明传输 -- lizhixin
    private String talent_deductid;//达人id 统计哪个达人分享的商品
    public static final int REQREFRESH = 1;
    public double goods_freight;//商品运费
    int screenHeight;
    public final static String ZEROSTR = "00";
    private String fmtShareMoney;

    private BrandGoods brandGoodsReq;
    private HomeNavRecycleGoodsAdapter brandAdapter;
    private boolean hasShowBundlingTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetails);
        ViewUtils.inject(this);

        EventBus.getDefault().register(this);

        goods_id = getIntent().getStringExtra("goodsId");
        resource_tags = getIntent().getStringExtra("resourceTags");
        talent_deductid = getIntent().getStringExtra("talentDeductid");

        if (TextUtils.isEmpty(goods_id)) {
            finish();
        }

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMember();
        if (msgnum != null && msgnum.getTotalNum() > 0) {
            layoutMoreVertical.setMsgNum(msgnum.getTotalNum());
            msgDot.setVisibility(View.VISIBLE);
        } else {
            msgDot.setVisibility(View.GONE);
            layoutMoreVertical.setMsgNum(0);
        }
        if (member != null) {
            refreshAddress();//获取我的地址信息
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQREFRESH) {
            getData(goods_id);//返回界面需重新刷新
        }
    }

    private void initView() {

        fmtShareMoney = getString(R.string.fmt_sharewin);
        BaseFunc.setFont(tvTaxfeeIcon);
        BaseFunc.setFont(tvShareIcon);
        tvStoreNotice.setSingleLine(true);

        tvTaxLbl.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        tvMarketIcon.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        scrollView.setOnScrollListener(this);
        scrollView.setScrollBtn(btnBackTop);

        int w = BaseFunc.getDisplayMetrics(mContext).widthPixels;
        int h = 429 * w / 1125;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        ivGlobalService.setLayoutParams(params);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);//不显示缩放按钮

        //解决锤子手机上按钮无法点击的问题
        if (Build.VERSION.SDK_INT >= 21) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        ws.setUserAgentString(ws.getUserAgentString() + "FHMall_Android");
        webView.setWebViewClient(new FHClient());
        webView.setWebChromeClient(new FHChromeClient());

        //不事先加载图片 提高加载速度
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        dfPrice = new DecimalFormat("#0.00");
        screenHeight = BaseFunc.getDisplayHeight(this);

        layoutMoreVertical = new LayoutMoreVertical(imgMore);//初始化更多选项布局
        layoutMoreVertical.setIsSearchShow(false);

        layoutDeliverRemark = new LayoutDeliverRemark(mContext);
        layoutPickAddress = new LayoutPickAddress(mContext);
        layoutPickAddress.setListener(this);

        layoutGoodsSpec = new LayoutGoodsSpec(this);
        layoutGoodsSpec.setCallBack(this);
        layoutTaxfee = new LayoutTaxfee(this);

        layoutGoodsIntro = new LayoutGoodsIntro(mContext);
        layoutBundlingTips = new LayoutBundlingTips(GoodsDetailsActivity.this);
        layoutGoodsPromotion = new LayoutGoodsPromotion(this);
        layoutGoodsPromotion.setPromotionListener(this);

        changeWebStatus(0);
        setWebContentLayoutParams();

        LLoading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ivError.setVisibility(View.GONE);

        FWebContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }


        });

        goodsDtlReq = new GoodsDtlReq();
        goodsDtlReq.setModelCallBack(this);

        brandGoodsReq = new BrandGoods();
        brandGoodsReq.setModelCall(this);
        brandAdapter = new HomeNavRecycleGoodsAdapter(mContext);
        LinearLayoutManager brandManager = new LinearLayoutManager(mContext);
        brandManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(brandManager);
        recyclerView.setAdapter(brandAdapter);

        fhApp.sendCartActionByLocal(0);


        addBrowse();//添加浏览记录

        fav = new Favorites();
        fav.setModelCallBack(this);

        getData(goods_id);//获取商品信息
    }

    public void getData(String mgoods_id) {
        /** 请求数据*/
        goods_id = mgoods_id;
        goodsDtlReq.get_goods_detail(goods_id, member, talent_deductid);
    }

    @OnClick(value = {
            R.id.ivBack, R.id.FCart, R.id.imgMore,
            R.id.ivKefu, R.id.ivStore, R.id.ivConllect, R.id.tvLeft, R.id.tvRight,
            R.id.LTaxfee, R.id.LShare, R.id.LSpec, R.id.LDelivery, R.id.LStore, R.id.tvIntoBrand,
            R.id.FWeb, R.id.FWebFake, R.id.FAttr, R.id.FAttrFake, R.id.LComment,
            R.id.tvNoticeClick, R.id.tvStoreNotice, R.id.ivError,
            R.id.tvNotSupportArea, R.id.tvRefundTips, R.id.ivGlobalService})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.FCart:
                BaseFunc.gotoActivity4Result(this, CartActivity.class, null, REQREFRESH);
                break;
            case R.id.imgMore:
                layoutMoreVertical.show();
                break;
            /** -----******-----*/
            case R.id.ivKefu:
                exploreBrowser();
                break;
            case R.id.ivStore:
                exploreStore();
                break;
            case R.id.ivConllect:
                doCollect();
                break;
            case R.id.tvLeft:
                onLeftAction();
                break;
            case R.id.ivGlobalService:
                BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, BaseVar.URL_QUALITY);
                break;
            case R.id.tvRight:
                onRightAction();
                break;
            /** -----******-----*/
            case R.id.LTaxfee:
                if (_goodsDtl == null) return;
                layoutTaxfee.show(_goodsDtl, goods_freight, 1);
                break;
            case R.id.LShare:
                ShareData.fhShare(this, shareData, null);
                break;
            case R.id.LSpec:
                showSpec(GoodsButtonHandler.SHOPPINGACTION_BOTH);//规格选择
                break;
            case R.id.LDelivery:
                if (member == null) {
                    BaseFunc.gotoLogin(mContext);
                    return;
                }
                layoutPickAddress.show();
                break;
            case R.id.LStore:
                exploreStore();
                break;
            case R.id.tvIntoBrand:
                if (_goodsDtl == null) return;
                BaseFunc.gotoActivity(mContext, GroupActivity.class, _goodsDtl.brand_id);
                break;
            case R.id.FWeb:
            case R.id.FWebFake:
                changeWebStatus(0);
                break;
            case R.id.FAttr:
            case R.id.FAttrFake:
                changeWebStatus(1);
                break;
            case R.id.LComment:
                if (_goodsDtl == null) return;
                BaseFunc.gotoActivity(mContext, GoodsRecommentsActivity.class, _goodsDtl.goods_id);
                break;
            case R.id.tvNoticeClick:
            case R.id.tvStoreNotice:
                if (tvStoreNotice.getLineCount() == 1) {
                    tvStoreNotice.setSingleLine(false);
                    tvNoticeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_store_notice_up, 0);
                } else {
                    tvStoreNotice.setSingleLine(true);
                    tvNoticeClick.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_store_notice, 0);
                }
                break;
            case R.id.ivError:
                LLoading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                ivError.setVisibility(View.GONE);
                getData(goods_id);//返回界面需重新刷新
                break;
            case R.id.tvNotSupportArea:
                if (_goodsDtl == null || layoutDeliverRemark == null || TextUtils.isEmpty(_goodsDtl.not_deliver_remark)) {
                    return;
                }
                layoutDeliverRemark.setDesc(_goodsDtl.not_deliver_remark);
                layoutDeliverRemark.show();
                break;
            case R.id.tvRefundTips:
                layoutGoodsIntro.show();
                break;
            default:
                break;
        }
    }

    private void showSpec(int actionId) {
        if (_goodsDtl == null) return;
        if (layoutGoodsSpec != null) {
            layoutGoodsSpec.setData(_goodsDtl);
            layoutGoodsSpec.setMember(member);
            layoutGoodsSpec.show(actionId);
        }
    }

    @Override
    public void onGDMCData(GoodsDetail dtl) {
        _goodsDtl = dtl;
        if (_goodsDtl != null) {
            _goodsDtl.resource_tags = resource_tags;
        }
        drawView();
    }

    @Override
    public void onGDMCError(String errcode) {
        _goodsDtl = null;
        drawView();
    }

    public void exploreStore() {
        if (_goodsDtl == null) return;
        BaseFunc.gotoActivity(this, StoreActivity.class, _goodsDtl.store_id);
    }

    /**
     * 百度商桥链接
     */
    private void exploreBrowser() {
        if (_goodsDtl != null && BaseFunc.isValidUrl(_goodsDtl.store_baidusales)) {
            BaseFunc.gotoActivity(mContext, FHBrowserActivity.class, _goodsDtl.store_baidusales);
        } else {
            BaseFunc.showMsg(mContext, getString(R.string.seller_out_of_line));
        }
    }

    private void doCollect() {
        if (member == null) {
            BaseFunc.gotoLogin(this);
            return;
        }
        ivConllect.setSelected(!ivConllect.isSelected());
        if (ivConllect.isSelected()) {
            add_fav();
        } else {
            del_fav();
        }
    }

    private void add_fav() {
        fav.add_favorites(mContext, member, goods_id, "goods");
    }

    private void del_fav() {
        fav.delete_favorites(mContext, member, goods_id, "goods");
    }

    @Override
    public void onFavList(List<Favorites> list) {

    }

    @Override
    public void onFavError(String errcode) {
        ivConllect.setEnabled(true);
        if (TextUtils.equals("0", errcode)) {
            switch (fav.actionNum) {
                case 0:
                    refreshCollect(true);
                    break;
                case 1:
                    refreshCollect(false);
                    break;
            }
        }
    }

    @Override
    public void onFavStart() {
        ivConllect.setEnabled(false);
    }

    public void refreshCollect(boolean isAdd) {
        ivConllect.setSelected(isAdd);
    }

    private void addBrowse() {
        if (goods_id == null) return;
        if (member == null) return;
        //只要会员进入该页面就记录该商品浏览记录
        new BaseBO().add_browse(member.token, member.member_id, goods_id);
    }

    private void drawView() {
        hasShowBundlingTips = false;
        if (_goodsDtl != null) {

            if (member != null) {
                refreshAddress();//获取我的地址信息
            } else {
                parseArea();
            }

            buyNum = 1;
            LLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            ivError.setVisibility(View.GONE);

            //显示微店主身份能省多少钱
            if (_goodsDtl.isShowMicroLvl()) {
                LFuli.setVisibility(View.VISIBLE);
                tvMicroLvl.setText(_goodsDtl.getMicro_shop_name());
                tvMicroWin.setText(_goodsDtl.getSaveMoneyDesc());
            } else {
                LFuli.setVisibility(View.GONE);
            }

            LContent.setVisibility(View.VISIBLE);

            ivConllect.setSelected(_goodsDtl.if_fav == 1);

            layoutGoodsIntro.setList(_goodsDtl.getGoods_intro_info());
            layoutBundlingTips.refreshData(_goodsDtl.getPopupInfo());
            layoutBundlingTips.setGoods_source(_goodsDtl.goods_source);

            /** 分享格式 */
            shareData = new ShareData();

            //如果API返回了分享相关的信息 则使用API返回

            if (!TextUtils.isEmpty(_goodsDtl.share_title)) {
                shareData.title = _goodsDtl.share_title;
            } else {
                shareData.title = _goodsDtl.goods_name;
            }

            if (!TextUtils.isEmpty(_goodsDtl.share_describe)) {
                shareData.content = _goodsDtl.share_describe;
            } else {
                shareData.content = !TextUtils.isEmpty(_goodsDtl.goods_desc) ? _goodsDtl.goods_desc : getString(R.string.share_goods_content);
            }

            shareData.url = String.format(BaseVar.SHARE_GOODS_DTL, _goodsDtl.goods_id);

            shareData.price = _goodsDtl.goods_price;
            shareData.deductMoney = _goodsDtl.getGoods_reward_money();

            List<String> imgs = _goodsDtl.getGoodsImage();
            if (imgs != null && imgs.size() > 0) {
                if (!TextUtils.isEmpty(_goodsDtl.goods_image)) {
                    shareData.imgs = _goodsDtl.goods_image;
                } else {
                    shareData.imgs = _goodsDtl.getAGoodsImage();
                }
                shareData.mul_img = imgs;
            }

            if (!TextUtils.isEmpty(_goodsDtl.share_img)) {
                shareData.imgs = _goodsDtl.share_img;
            }

            if (layoutMoreVertical != null) {
                layoutMoreVertical.setShareData(shareData);
            }

            /** 品牌显示*/
            if (!TextUtils.isEmpty(_goodsDtl.brand_id) && !TextUtils.isEmpty(_goodsDtl.brand_id) && BaseFunc.isValidUrl(_goodsDtl.brand_pic)) {

                brandGoodsReq.getDefaultBrandGoods(_goodsDtl.brand_id);

                LBrandDtl.setVisibility(View.VISIBLE);
                new FHImageViewUtil(ivBrandLogo).setImageURI(_goodsDtl.brand_pic, FHImageViewUtil.SHOWTYPE.DEFAULT);
                tvBrandName.setText(_goodsDtl.brand_name);
                tvBrandDesc.setText(_goodsDtl.brand_intro);
                String brandGoodsNum = String.format(getString(R.string.fmt_brand_goodsnum), String.valueOf(_goodsDtl.brand_goods_storage));
                tvBrandGoodsNum.setText(BaseFunc.fromHtml(brandGoodsNum));
            } else {
                LBrandDtl.setVisibility(View.GONE);
            }


            /**基本信息*/

            LBanner.removeAllViews();
            List<Banner> banners = _goodsDtl.getGoodsImageBanner();
            if (banners != null && banners.size() > 0) {
                PictureView pictureView = new PictureView(this);
                pictureView.setShowType(FHImageViewUtil.SHOWTYPE.DEFAULT);
                LBanner.addView(pictureView.getView(banners));
            }

            tvGoodsName.setText(_goodsDtl.getGoodsNameForDetail(this));
            tvGoodsPrice.setText(dfPrice.format(_goodsDtl.getGoodsPrice4Show()));
            /** 分享奖金*/
            tvShareWin.setText(String.format(fmtShareMoney, _goodsDtl.getGoods_reward_money()));

            if (_goodsDtl.xianshi != null) {
                tvGoodsPromHint.setVisibility(View.VISIBLE);
                tvGoodsPromHint.setText(_goodsDtl.xianshi.getLowerLimitTextOfGoodsDtl());
            } else {
                tvGoodsPromHint.setVisibility(View.GONE);
            }
            tvGoodsMarketprice.setText(dfPrice.format(_goodsDtl.getMarketPrice4Show()));
            tvGoodsMarketprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //中划线

            LTaxfee.setVisibility(_goodsDtl.goods_source > 0 ? View.VISIBLE : View.INVISIBLE);

            tvNationDesc.setText(_goodsDtl.goods_promise);
            new FHImageViewUtil(ivNationFlag).setImageURI(_goodsDtl.nation_flag, FHImageViewUtil.SHOWTYPE.DEFAULT);

            if (!TextUtils.isEmpty(_goodsDtl.goods_desc)) {
                tvGoodsDesc.setVisibility(View.VISIBLE);
                tvGoodsDesc.setText(_goodsDtl.goods_desc);
            } else {
                tvGoodsDesc.setVisibility(View.GONE);
            }

            /** 商品规格*/
            layoutGoodsSpec.setData(_goodsDtl);
            if (TextUtils.isEmpty(_goodsDtl.getCurrentGoodsspec())) {
                //不显示规格选择行
                LSpec.setVisibility(View.GONE);
            } else {
                LSpec.setVisibility(View.VISIBLE);
                if (buyNum > 0) {
                    tvCurrentSpec.setText(String.format(getString(R.string.goods_spec_content), _goodsDtl.getCurrentGoodsspec(), String.valueOf(buyNum)));//当前规格 + 默认1件
                } else {
                    tvCurrentSpec.setText(_goodsDtl.getCurrentGoodsspec());//当前规格 + 默认1件
                }

            }

            /** 默认显示的运费 */
            refreshFreight(_goodsDtl, getString(R.string.all_area), _goodsDtl.goods_freight);

            /** 不支持配送说明 */
            if (TextUtils.isEmpty(_goodsDtl.not_deliver_remark)) {
                tvNotSupportArea.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }


            /** 是否支持7天无理由退货 发票 保障语标签提示 */
            if (!TextUtils.isEmpty(_goodsDtl.getRefundTips(mContext))) {
                tvRefundTips.setVisibility(View.VISIBLE);
                tvRefundTips.setText(_goodsDtl.getRefundTips(mContext));
            } else {
                tvRefundTips.setVisibility(View.GONE);
            }

            /** 评价*/
            List<GoodsComments> comments = _goodsDtl.getGoodsCommects();
            if (comments != null && comments.size() > 0) {
                LComment.setVisibility(View.VISIBLE);
                GoodsComments goodsComments = comments.get(0);
                new FHImageViewUtil(ivComment).setImageURI(goodsComments.member_avatar, FHImageViewUtil.SHOWTYPE.AVATAR);
                tvCommentUser.setText(goodsComments.getCommentUser());
                tvCommentContent.setText(goodsComments.content);
                tvCommentCount.setText(_goodsDtl.getCommentCountDesc());
            } else {
                LComment.setVisibility(View.GONE);
            }

            /** 店铺 */
            new FHImageViewUtil(ivStoreLogo).setImageURI(_goodsDtl.store_logo, FHImageViewUtil.SHOWTYPE.BANNER);
            tvStoreName.setText(_goodsDtl.store_name);
            /**店铺公告*/
            if (!TextUtils.isEmpty(_goodsDtl.store_notice)) {
                tvStoreNotice.setVisibility(View.VISIBLE);
                tvStoreNotice.setText(BaseFunc.fromHtml(_goodsDtl.store_notice));
            } else {
                tvStoreNotice.setVisibility(View.GONE);
            }

            reLoad(_goodsDtl.goods_commonid);
            /** 商品属性*/
            GoodsAttrAdpter attrAdpter = new GoodsAttrAdpter(this);
            attrAdpter.setJson(_goodsDtl.goods_attr);
            listViewAttr.setAdapter(attrAdpter);

            /**促销活动*/
            layoutGoodsPromotion.setDatas(_goodsDtl);
            LPromotion.removeAllViews();
            LPromotion.addView(layoutGoodsPromotion.getViewGroup());

            //更新两个操作按钮的状态
            GoodsButtonHandler.refreshBtnStatus(_goodsDtl.getGoodsSaleState(), TextUtils.equals(_goodsDtl.if_notice, "1"), tvLeft, tvRight, tvSaleOutHint, GoodsButtonHandler.SHOPPINGACTION_BOTH, this);

            /**秒杀*/
            if (_goodsDtl.seckilling_countup > 0 || _goodsDtl.seckilling != null) {

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }

                /** 距秒杀开始 */
                if (_goodsDtl.seckilling_countup > 0) {
                    /** 倒计时处理*/
                    LMiaosha.setVisibility(View.VISIBLE);
                    tvMiaoshaDesc.setText(getString(R.string.miaosha_start));
                    countDownTimer = new CountDownTimer(_goodsDtl.seckilling_countup * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (millisUntilFinished > 0) {
                                long[] hms = BaseFunc.getD_H_M_S(millisUntilFinished / 1000);
                                tvHour.setText(String.valueOf(hms[1]));
                                tvMinutes.setText(String.valueOf(hms[2]));
                                tvSecond.setText(String.valueOf(hms[3]));
                            } else {
                                tvHour.setText(ZEROSTR);
                                tvMinutes.setText(ZEROSTR);
                                tvSecond.setText(ZEROSTR);
                                getData(goods_id);
                            }
                        }

                        @Override
                        public void onFinish() {
                            tvHour.setText(ZEROSTR);
                            tvMinutes.setText(ZEROSTR);
                            tvSecond.setText(ZEROSTR);
                            getData(goods_id);
                        }
                    };
                } else {
                    if (_goodsDtl.seckilling != null) {
                        LMiaosha.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(_goodsDtl.seckilling.share_title)) {
                            shareData.title = _goodsDtl.seckilling.share_title;
                        }
                        if (!TextUtils.isEmpty(_goodsDtl.seckilling.share_desc)) {
                            shareData.content = _goodsDtl.seckilling.share_desc;
                        }
                        if (BaseFunc.isValidUrl(_goodsDtl.seckilling.share_img)) {
                            shareData.imgs = _goodsDtl.seckilling.share_img;
                            shareData.mul_img = null;
                        }
                        tvGoodsPrice.setText(dfPrice.format(_goodsDtl.seckilling.price));
                        tvGoodsMarketprice.setText(dfPrice.format(_goodsDtl.seckilling.origin_price));

                        /** 倒计时处理*/
                        tvMiaoshaDesc.setText(getString(R.string.miaosha_end));
                        countDownTimer = new CountDownTimer(_goodsDtl.seckilling.countdown * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (millisUntilFinished > 0) {
                                    long[] hms = BaseFunc.getD_H_M_S(millisUntilFinished / 1000);
                                    tvHour.setText(String.valueOf(hms[1]));
                                    tvMinutes.setText(String.valueOf(hms[2]));
                                    tvSecond.setText(String.valueOf(hms[3]));
                                } else {
                                    tvHour.setText(ZEROSTR);
                                    tvMinutes.setText(ZEROSTR);
                                    tvSecond.setText(ZEROSTR);
                                    getData(goods_id);
                                }
                            }

                            @Override
                            public void onFinish() {
                                tvHour.setText(ZEROSTR);
                                tvMinutes.setText(ZEROSTR);
                                tvSecond.setText(ZEROSTR);
                                getData(goods_id);
                            }
                        };
                    } else {
                        LMiaosha.setVisibility(View.GONE);
                    }
                }

                if (countDownTimer != null)
                    countDownTimer.start();

            } else {
                LMiaosha.setVisibility(View.GONE);
            }

            refreshMember();

        } else {
            LContent.setVisibility(View.GONE);
            LLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            ivError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBuyNumChanged(int buyNum) {
        if (!TextUtils.isEmpty(tvCurrentSpec.getText())) {
            this.buyNum = buyNum;
            tvCurrentSpec.setText(String.format(mContext.getString(R.string.goods_spec_content), tvCurrentSpec.getText().toString().split(" ")[0], String.valueOf(buyNum)));//当前规格 + 默认1件
        }
    }

    @Override
    public void onBuy(String goodsid, int num) {
        if (_goodsDtl == null) return;
        //如果有弹框提示信息则先弹框 如果弹过一次则不弹了
        if (_goodsDtl.getPopupInfo() != null && !hasShowBundlingTips) {
            layoutBundlingTips.show();
            hasShowBundlingTips = true;
        } else {
            CartCheckEntity entity = new CartCheckEntity();
            entity.setGoodsIdNum(goodsid + "|" + num);
            entity.setGoodsSource(_goodsDtl.goods_source);
            entity.setIfCart(0);
            entity.setResourceTags(_goodsDtl.resource_tags);
            BaseFunc.gotoCartCheckActivity(this, entity, GoodsDetailsActivity.REQREFRESH);
        }
    }
    public void onBrandGoodsList(List<ActivityGoods> list) {
        if (list != null && list.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            brandAdapter.setList(list);
            if (_goodsDtl != null) {
                brandAdapter.setActivityList(_goodsDtl.genBrandActivityList());
            }
            brandAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    //规格选择页面的回调 -- 添加购物车
    @Override
    public void onAdd(String goodsid, int num) {
        //如果有弹框提示信息则先弹框 如果弹过一次则不弹了
        if (_goodsDtl.getPopupInfo() != null && !hasShowBundlingTips) {
            layoutBundlingTips.show();
            hasShowBundlingTips = true;
        } else {
            add2Cart(num);
        }
    }

    @Override
    public void onSelected(String goodsid) {
        getData(goodsid);
    }

    @Override
    public void onAddArrivalNotice() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
        } else {
            addArrivalNotice();
        }
    }

    @Override
    public void onCancelArrivalNotice() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
        } else {
            cancelArrivalNotice();
        }
    }

    @Override
    public void onSeeSimilarGoods() {
        seeSimilarGoods();
    }

    private void getAreaFreight(final Area a) {
        if (_goodsDtl == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    refreshFreight(_goodsDtl, a.area_name, Double.parseDouble(data));
                }
            }
        }).get_area_freight(a.area_id, _goodsDtl.transport_id);
    }

    public void refreshFreight(GoodsDetail _dtl, String to, double freight) {
        this.goods_freight = freight;
        if (_dtl == null) return;

        // 如果不支持配送则显示 Added By Plucky 2016-08-15 13:51
        if (!_dtl.isSupportArea(to)) {
            tvNotSupportArea.setVisibility(View.VISIBLE);
            GoodsButtonHandler.refreshBtnStatus(_goodsDtl.getGoodsSaleState(), TextUtils.equals(_goodsDtl.if_notice, "1"), tvLeft, tvRight, tvSaleOutHint, GoodsButtonHandler.SHOPPINTACTION_NOTSUPPORT_AREA, GoodsDetailsActivity.this);
        } else {
            tvNotSupportArea.setVisibility(View.GONE);
            GoodsButtonHandler.refreshBtnStatus(_goodsDtl.getGoodsSaleState(), TextUtils.equals(_goodsDtl.if_notice, "1"), tvLeft, tvRight, tvSaleOutHint, GoodsButtonHandler.SHOPPINGACTION_BOTH, GoodsDetailsActivity.this);
        }

        CharSequence[] res = _dtl.getAreaXAndFreight(this, to, freight);
        tvArea.setText(res[0]);
        tvGoodsFreight.setText(res[1]);
    }

    /**
     * 设置地区信息
     */
    private void parseArea() {
        /** 不频繁读取沙盒 从FHApp取 */
        FHLocation loc = ((FHApp) getApplication()).getDefLocation();
        if (loc != null) {
            handleLocation(loc);
            //如果得到了位置就不再进行操作了，否则频繁更新地理位置影响效率
            return;
        }

        /** 再定位 */
        BaiduLocateUtil.getinstance(getApplicationContext()).start();
        BaiduLocateUtil.getinstance(getApplicationContext()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(FHLocation location) {
                if (location != null) {
                    ((FHApp) getApplication()).resetLocation();
                    FHCache.setLocation2SandBox(mContext, location);
                    handleLocation(location);
                    BaiduLocateUtil.getinstance(getApplicationContext()).stop();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * Added By Plucky
     *
     * @param location location
     */
    private void handleLocation(FHLocation location) {
        if (location == null || _goodsDtl == null) return;
        Area a = _goodsDtl.getAreaByName(location.province);
        if (a != null) {
            a.area_name = location.getAreaInfo();
            getAreaFreight(a);
        }
    }

    /**
     * 添加地址
     */
    @Override
    public void onAdd() {
        BaseFunc.gotoActivity(this, EditAddressActivity.class, null);
    }

    @Override
    public void onPick(Address address) {
        if (address != null) {
            Area area = new Area();
            area.area_id = address.area_id;
            area.area_name = address.area_info;

            if (_goodsDtl.transport_id > 0) {
                getAreaFreight(area);
            } else {
                refreshFreight(_goodsDtl, area.area_name, _goodsDtl.goods_freight);
            }

            BaseFunc.setAddressDefault(member, address.address_id);
        }
    }

    private void changeWebStatus(int index) {
        if (index == 0) {
            FWeb.setSelected(true);
            FAttr.setSelected(false);
            FWebFake.setSelected(true);
            FAttrFake.setSelected(false);
            LWeb.setVisibility(View.VISIBLE);
            listViewAttr.setVisibility(View.GONE);
        } else {
            FWeb.setSelected(false);
            FAttr.setSelected(true);
            FWebFake.setSelected(false);
            FAttrFake.setSelected(true);

            LWeb.setVisibility(View.GONE);
            listViewAttr.setVisibility(View.VISIBLE);
        }
        //webView.setRequestScroll(false);
    }

    public void reLoad(String goods_commonid) {
        String murl = String.format(BaseVar.GOODSDTL, goods_commonid);
        FHLog.d("Plucky", murl);
        webView.loadUrl(murl);
    }

    private class FHClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webProgressBar.setVisibility(View.GONE);
            webView.loadUrl(BaseVar.FAILEDWEB);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webProgressBar.setVisibility(View.GONE);

            //dom渲染完成再自动加载图片
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            webProgressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }
    }

    private void setWebContentLayoutParams() {
        ViewGroup.LayoutParams params = FWebContent.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        //确保显示一屏
        if (params.height < screenHeight) {
            params.height = screenHeight - getResources().getDimensionPixelOffset(R.dimen.dp_of_175);
        }
        FWebContent.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        displayFake();
    }

    private void displayFake() {
        int[] loc = new int[2];
        FWeb.getLocationOnScreen(loc);
        int[] topLineLoc = new int[2];
        vTopLine.getLocationOnScreen(topLineLoc);
        if (loc[1] <= topLineLoc[1]) {
            LFake.setVisibility(View.VISIBLE);
            LNotFake.setVisibility(View.INVISIBLE);
            webView.setRequestScroll(true);
        } else {
            LFake.setVisibility(View.INVISIBLE);
            LNotFake.setVisibility(View.VISIBLE);
            webView.setRequestScroll(false);
        }
    }

    @Override
    public void onTop(int top) {

    }


    /**
     * 加入购物车
     */
    private void add2Cart(final int num) {
        if (member == null) {
            BaseFunc.gotoLogin(this);
            return;
        }

        if (num == 0 || _goodsDtl == null) return;
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    BaseFunc.getCartNum(member);
                    VarInstance.getInstance().showMsg(R.string.add_success);
                }
            }
        }).add_cart(member.member_id, member.token, _goodsDtl.goods_id, member.store_id, num, _goodsDtl.goods_source, null, _goodsDtl.resource_tags);
    }

    /**
     * 只有微店主才能分享赚钱
     * 秒杀商品不能赚钱
     */
    private void refreshMember() {
        if (member != null && member.if_shoper == 1) {
            if (LMiaosha.getVisibility() == View.VISIBLE) {
                LShare.setVisibility(View.GONE);
            } else {
                if (_goodsDtl != null) {
                    LShare.setVisibility(View.VISIBLE);
                } else {
                    LShare.setVisibility(View.GONE);
                }
            }
        } else {
            LShare.setVisibility(View.GONE);
        }
    }

    /**
     * 添加到货提醒
     */
    private void addArrivalNotice() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                tvLeft.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                BaseFunc.showSelfToast(mContext, getString(R.string.remind_set_success));
                tvLeft.setEnabled(true);
                changeGoodsNotice("1");

                GoodsButtonHandler.refreshBtnStatus(_goodsDtl.getGoodsSaleState(), true, tvLeft, tvRight, tvSaleOutHint, GoodsButtonHandler.SHOPPINGACTION_BOTH, GoodsDetailsActivity.this);
            }
        }).addArrivalNotice(member.member_id, member.token, goods_id, null);
    }

    /**
     * 取消到货提醒
     */
    private void cancelArrivalNotice() {
        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {
                tvLeft.setEnabled(false);
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                BaseFunc.showSelfToast(mContext, getString(R.string.remind_unset_success));
                tvLeft.setEnabled(true);
                changeGoodsNotice("0");
                GoodsButtonHandler.refreshBtnStatus(_goodsDtl.getGoodsSaleState(), false, tvLeft, tvRight, tvSaleOutHint, GoodsButtonHandler.SHOPPINGACTION_BOTH, GoodsDetailsActivity.this);
            }
        }).cancelArrivalNotice(member.member_id, member.token, goods_id, null);
    }

    /**
     * 查看相似
     */
    private void seeSimilarGoods() {
        if (_goodsDtl != null) {
            BaseFunc.gotoGoodsListActivity(this, null, 0, _goodsDtl.gc_id);
        }
    }

    private void changeGoodsNotice(String if_notice) {
        _goodsDtl.if_notice = if_notice;
    }


    private int currentLeft = 0;
    private int currentRight = 0;


    private void onLeftAction() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        if (_goodsDtl == null) return;

        switch (currentLeft) {
            case GoodsButtonHandler.LEFT_ACTION_CART:
                //如果有弹框提示信息则先弹框 如果弹过一次则不弹了
                if (_goodsDtl.getPopupInfo() != null && !hasShowBundlingTips) {
                    layoutBundlingTips.show();
                    hasShowBundlingTips = true;
                } else {
                    //判断如果存在规格 弹出规格选择窗口
                    if (TextUtils.isEmpty(_goodsDtl.getCurrentGoodsspec())) {
                        add2Cart(1);
                    } else {
                        showSpec(GoodsButtonHandler.SHOPPINGACTION_CART);
                    }
                }
                break;
            case GoodsButtonHandler.LEFT_ACTION_NOTICE:
                addArrivalNotice();
                break;
            case GoodsButtonHandler.LEFT_ACTION_CANCELNOTICE:
                cancelArrivalNotice();
                break;
        }
    }

    private void onRightAction() {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        switch (currentRight) {
            case GoodsButtonHandler.RIGHT_ACTION_BUY:
                //如果有弹框提示信息则先弹框 如果弹过一次则不弹了
                if (_goodsDtl.getPopupInfo() != null && !hasShowBundlingTips) {
                    layoutBundlingTips.show();
                    hasShowBundlingTips = true;
                } else {
                    showSpec(GoodsButtonHandler.SHOPPINGACTION_BUY);
                }
                break;
            case GoodsButtonHandler.RIGHT_ACTION_SIMILAR:
                seeSimilarGoods();
                break;
        }
    }

    @Override
    public void onChange(int leftAction, int rightAction) {
        currentLeft = leftAction;
        currentRight = rightAction;
    }

    private class FHChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            webProgressBar.setProgress(newProgress);
            if (newProgress == 100)
                webProgressBar.setVisibility(View.GONE);

        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handlerCartAction(CartActionEvent event) {
        if (event != null) {
            if (event.getCartAction() != null) {
                BaseFunc.setCartNumofTv(tvCartNum, event.getCartAction().getNum());
            } else {
                BaseFunc.setCartNumofTv(tvCartNum, 0);
            }
        }
    }

    private void refreshAddress() {
        if (member == null) return;

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    List<Address> list;
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<Address>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                    layoutPickAddress.setList(list);

                    Address address = layoutPickAddress.getDefaultAddress();
                    // 如果没有地址 则开启定位
                    if (address != null) {
                        onPick(address);//跟选择地址一样 这里相当于自动选择地址计算价格
                        layoutPickAddress.setCurrentAreaInfo(address.area_info);
                    } else {
                        parseArea();
                    }
                } else {
                    parseArea();
                }
            }
        }).get_address(null, member.member_id, member.token);
    }

    @Override
    public void onBundling(List<GoodsBundling> bundling) {
        layoutBundlingTips.setBundling(bundling);
    }
}
