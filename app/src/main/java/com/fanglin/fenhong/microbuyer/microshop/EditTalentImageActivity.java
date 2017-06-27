package com.fanglin.fenhong.microbuyer.microshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.mapandlocate.baiduloc.BaiduLocateUtil;
import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoodsEvent;
import com.fanglin.fenhong.microbuyer.base.model.EntityOfAddTalentImage;
import com.fanglin.fenhong.microbuyer.base.model.GPSLocation;
import com.fanglin.fenhong.microbuyer.base.model.TalentLocation;
import com.fanglin.fenhong.microbuyer.microshop.adapter.AddTalentImagesAdapter;
import com.fanglin.fenhong.microbuyer.microshop.adapter.TalentTagAdapter;
import com.fanglin.fhlib.ypyun.UpyunUploader;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.spotsdialog.SpotsDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/15-上午9:51.
 * 功能描述:新增或者编辑达人时光页面
 */
public class EditTalentImageActivity extends BaseFragmentActivityUI implements LayoutTalentLoc.LayoutTalentLocListner {
    public static final int REQTAG = 0;
    public static final int REQPICTURE = 1;

    @ViewInject(R.id.etContent)
    EditText etContent;

    @ViewInject(R.id.tagRecyclerView)
    RecyclerView tagRecyclerView;
    TalentTagAdapter tagAdapter;

    @ViewInject(R.id.tvTagCount)
    TextView tvTagCount;
    @ViewInject(R.id.tvLocation)
    TextView tvLocation;

    LayoutTalentLoc layoutTalentLoc;
    FHLocation fhLocation;

    //时光图片
    AddTalentImagesAdapter timesAdapter;
    @ViewInject(R.id.timeRecycler)
    RecyclerView timeRecycler;
    @ViewInject(R.id.tvTimeCount)
    TextView tvTimeCount;

    //关联商品
    AddTalentImagesAdapter goodsAdapter;
    @ViewInject(R.id.goodsRecycler)
    RecyclerView goodsRecycler;
    @ViewInject(R.id.tvGoodsCount)
    TextView tvGoodsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_edit_talentimage, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        setHeadTitle(getString(R.string.title_record_time));
        enableTvMore(R.string.publish, false, R.color.fh_red);
        final LinearLayoutManager tagManager = new LinearLayoutManager(mContext);
        tagManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tagRecyclerView.setLayoutManager(tagManager);
        tagAdapter = new TalentTagAdapter(mContext);
        tagAdapter.setListener(new TalentTagAdapter.TalentTagAdapterListener() {
            @Override
            public void onItemClick(boolean isAddBtn, int position) {
                if (isAddBtn) {
                    BaseFunc.gotoActivity4Result(EditTalentImageActivity.this, TalentTagSearchActivity.class, null, REQTAG);
                }
            }

            @Override
            public void onItemLongClick(boolean isAddBtn, int position) {
                confirmDeleteTags(0, position);
            }
        });
        tagAdapter.setList(new ArrayList<String>());
        tagAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                tvTagCount.setText(tagAdapter.getTagCountStr());
            }
        });
        tagAdapter.setEditModeAndMaxSize(true, 10);
        tagRecyclerView.setAdapter(tagAdapter);
        layoutTalentLoc = new LayoutTalentLoc(mContext);
        layoutTalentLoc.setListner(this);

        //时光图片
        LinearLayoutManager timeManager = new LinearLayoutManager(mContext);
        timeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timeRecycler.setLayoutManager(timeManager);
        timesAdapter = new AddTalentImagesAdapter(mContext);
        timesAdapter.setList(new ArrayList<EntityOfAddTalentImage>());
        timesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                tvTimeCount.setText(timesAdapter.getListCountStr());
            }
        });
        timesAdapter.setEditModeAndMaxSize(true, 5);
        timesAdapter.setListener(new AddTalentImagesAdapter.AddTalentImagesAdapterListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    doCrop();
                } else {
                    BaseFunc.gotoEditImageTagActivity(EditTalentImageActivity.this, null, timesAdapter.getItem(position));
                }
            }

            @Override
            public void onItemLongClick(int position) {
                confirmDeleteTags(1, position);
            }
        });
        timeRecycler.setAdapter(timesAdapter);


        //关联商品
        List<EntityOfAddTalentImage> localList = LinkGoodsEvent.getListByLocal();
        LinearLayoutManager goodsManager = new LinearLayoutManager(mContext);
        goodsManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        goodsRecycler.setLayoutManager(goodsManager);
        goodsAdapter = new AddTalentImagesAdapter(mContext);
        goodsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                tvGoodsCount.setText(goodsAdapter.getListCountStr());
            }
        });
        goodsAdapter.setList(localList);
        goodsAdapter.setEditModeAndMaxSize(true, 10);
        goodsAdapter.setListener(new AddTalentImagesAdapter.AddTalentImagesAdapterListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    BaseFunc.gotoActivity(mContext, LinkGoodsActivity.class, null);
                } else {
                    BaseFunc.gotoActivity(mContext, LinkedGoodsActivity.class, null);
                }
            }

            @Override
            public void onItemLongClick(int position) {
                confirmDeleteTags(2, position);
            }
        });
        tvGoodsCount.setText(goodsAdapter.formatCount(localList.size()));
        goodsRecycler.setAdapter(goodsAdapter);

        //开启定位
        getLoc();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQTAG:
                    String aTag = data.getStringExtra("TAG");
                    tagAdapter.addItem(aTag);
                    break;
                case REQPICTURE:
                    try {
                        Uri uri = data.getParcelableExtra("VAL");
                        upload(uri);
                    } catch (Exception e) {
                        //
                    }
                    break;
            }
        }
    }

    /**
     * 弹出删除确认框
     *
     * @param type     0 标签 1 时光  2 关联商品
     * @param position int
     */
    private void confirmDeleteTags(final int type, final int position) {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.tips_delete_selected_tags));
        fhHintDialog.setTvLeft(getString(R.string.delete));
        fhHintDialog.setTvRight(getString(R.string.dont_delete));
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {
                if (type == 0) {
                    tagAdapter.removeItem(position);
                } else if (type == 1) {
                    String[] ids = timesAdapter.getItem(position).getIdsByGoods();
                    if (ids != null && ids.length > 0) {
                        LinkGoodsEvent.deleteListGoodsByIds(ids);
                        /**
                         * 时光图片会影响到关联商品
                         */
                        List<EntityOfAddTalentImage> localList = LinkGoodsEvent.getListByLocal();
                        goodsAdapter.setList(localList);
                        goodsAdapter.notifyDataSetChanged();
                    }
                    timesAdapter.removeItem(position);
                } else {
                    String goodsId = goodsAdapter.getItem(position).getGoods_id();
                    LinkGoodsEvent.deleteListGoodsById(goodsId);
                    goodsAdapter.removeItem(position);

                    /**
                     * 如果删除的商品是时光里的则需要去掉
                     */
                    timesAdapter.removeGoodsById(goodsId);
                }
            }

            @Override
            public void onRightClick() {

            }
        });
        fhHintDialog.show();
    }

    /**
     * 执行定位
     */
    private void getLoc() {
        tvLocation.setText(getString(R.string.lbl_locating));
        BaiduLocateUtil.getinstance(getApplicationContext()).start();
        BaiduLocateUtil.getinstance(getApplicationContext()).setCallBack(new BaiduLocateUtil.LocationCallBack() {
            @Override
            public void onChange(FHLocation location) {
                if (location != null && mContext != null) {
                    fhApp.resetLocation();
                    FHCache.setLocation2SandBox(mContext, location);
                    FHCache.recordGPS(fhApp, member, location);//记录GPS定位信息
                    BaiduLocateUtil.getinstance(getApplicationContext()).stop();

                    /** 提交定位信息
                     */
                    new GPSLocation().record(fhApp, member);
                    tvLocation.setText(location.getAddrDesc());
                    fhLocation = location;
                }
            }

            @Override
            public void onFailure() {
                fhLocation = null;
                tvLocation.setText(getString(R.string.lbl_locating_failure));
            }
        });
    }

    @Override
    public void onLocation(boolean useLoc, FHLocation location) {
        this.fhLocation = location;
        if (fhLocation != null) {
            tvLocation.setText(fhLocation.getAddrDesc());
        } else {
            tvLocation.setText(getString(R.string.lbl_locating_failure));
        }

    }

    @Override
    public void onReLocation() {
        getLoc();
    }

    @OnClick(value = {R.id.tvLocation})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvLocation:
                layoutTalentLoc.show(fhLocation);
                break;
        }
    }

    /**
     * EventBus处理关联商品\处理添加图片标签
     *
     * @param event event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleLinkGoods(LinkGoodsEvent event) {
        if (event != null) {
            if (event.getRefreshStates() > 0) {
                List<EntityOfAddTalentImage> localList = LinkGoodsEvent.getListByLocal();
                goodsAdapter.setList(localList);
                goodsAdapter.notifyDataSetChanged();
            } else {
                EntityOfAddTalentImage talentImage = event.getmTalentImage();
                if (talentImage != null) {
                    if (talentImage.isTimes()) {
                        if (event.isAdd()) {
                            timesAdapter.addItem(talentImage);
                        } else {
                            timesAdapter.removeItem(talentImage);
                        }

                        /**
                         * 时光图片会影响到关联商品
                         */
                        List<EntityOfAddTalentImage> localList = LinkGoodsEvent.getListByLocal();
                        goodsAdapter.setList(localList);
                        goodsAdapter.notifyDataSetChanged();
                    } else {
                        if (event.isAdd()) {
                            goodsAdapter.addItem(talentImage);
                        } else {
                            goodsAdapter.removeItem(talentImage);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void doCrop() {
        BaseFunc.selectPictures(this, REQPICTURE, true, 1080, 1080, 1, 1);
    }


    private void upload(Uri uri) {
        if (member == null) return;
        final SpotsDialog dlg = BaseFunc.getLoadingDlg(this, getString(R.string.picture_uploading));
        new UpyunUploader(member.member_id).setUploadFile(uri.getPath()).setUpYunCallBack(new UpyunUploader.UpYunCallBack() {
            @Override
            public void startLoading() {
                dlg.show();
            }

            @Override
            public void endLoading(boolean isSuccess, String data) {
                dlg.dismiss();
                if (isSuccess) {
                    BaseFunc.gotoEditImageTagActivity(EditTalentImageActivity.this, data, null);
                }
            }
        }).upload();
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }

        String gps = null;
        if (fhLocation != null) {
            gps = TalentLocation.getJsonStrByFHLocation(fhLocation);
        }

        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(content.trim())) {
            YoYo.with(Techniques.Shake).duration(700).playOn(etContent);
            confirmTips(0);
            return;
        }
        //时光
        List<EntityOfAddTalentImage> times = timesAdapter.getList();
        if (times == null || times.size() == 0) {
            confirmTips(1);
            return;
        }

        //关联商品
        List<EntityOfAddTalentImage> goods = goodsAdapter.getList();
        if (goods == null || goods.size() == 0) {
            confirmTips(2);
            return;
        }

        //后端需要去除所有关联商品中 包含关联图片的商品
        List<EntityOfAddTalentImage> ungoods = EntityOfAddTalentImage.getLinkGoodsOfUnImages(goods);


        //标签
        List<String> tags = tagAdapter.getList();
        if (tags == null || tags.size() == 0) {
            confirmTips(3);
            return;
        }

        new BaseBO().setCallBack(new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }).addTime(member,
                etContent.getText().toString(),
                gps,
                new Gson().toJson(tags),
                new Gson().toJson(times),
                new Gson().toJson(ungoods));

    }

    /**
     * 弹出提示框
     *
     * @param type 0 内容
     *             1 时光图片
     *             2 关联商品
     *             3 标签
     */
    private void confirmTips(final int type) {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        switch (type) {
            case 0:
                fhHintDialog.setTvContent(getString(R.string.tips_talenthome_recordsomething));
                fhHintDialog.setTvLeft(getString(android.R.string.cancel));
                fhHintDialog.setTvRight(getString(android.R.string.ok));
                break;
            case 1:
                fhHintDialog.setTvContent(getString(R.string.tips_talenthome_addtimeimage));
                fhHintDialog.setTvLeft(getString(R.string.lbl_wait));
                fhHintDialog.setTvRight(getString(R.string.lbl_toadd));
                break;
            case 2:
                fhHintDialog.setTvContent(getString(R.string.tips_talenthome_addtimegoods));
                fhHintDialog.setTvLeft(getString(R.string.lbl_wait));
                fhHintDialog.setTvRight(getString(R.string.lbl_toadd));
                break;
            case 3:
                fhHintDialog.setTvContent(getString(R.string.tips_talenthome_addtimetags));
                fhHintDialog.setTvLeft(getString(R.string.lbl_wait));
                fhHintDialog.setTvRight(getString(R.string.lbl_toadd));
                break;
        }
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                switch (type) {
                    case 1:
                        doCrop();
                        break;
                    case 2:
                        BaseFunc.gotoActivity(mContext, LinkGoodsActivity.class, null);
                        break;
                    case 3:
                        BaseFunc.gotoActivity4Result(EditTalentImageActivity.this, TalentTagSearchActivity.class, null, REQTAG);
                        break;
                }
            }
        });
        fhHintDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        //清空本地缓存
        LinkGoodsEvent.emptyLocal();
    }

    private void confirmExit() {
        FHHintDialog fhHintDialog = new FHHintDialog(mContext);
        fhHintDialog.setTvContent(getString(R.string.msg_of_publish_talentimage));
        fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                finish();
            }
        });
        fhHintDialog.show();
    }

    @Override
    public void onBackClick() {
        confirmExit();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }
}
