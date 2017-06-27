package com.fanglin.fenhong.microbuyer.microshop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoods4ImageTagEvent;
import com.fanglin.fenhong.microbuyer.base.event.LinkGoodsEvent;
import com.fanglin.fenhong.microbuyer.base.model.EntityOfAddTalentImage;
import com.fanglin.fenhong.microbuyer.base.model.LinkGoods;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageTag;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageTagLine;
import com.fanglin.fhui.FHHintDialog;
import com.fanglin.fhui.custom.Custom3LinesLayout;
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
 * author:Created by Plucky on 16/6/17-下午1:33.
 * 功能描述: 给图片添加标签页面
 */
public class EditImageTagActivity extends BaseFragmentActivityUI {
    @ViewInject(R.id.FContainer)
    FrameLayout FContainer;
    @ViewInject(R.id.imageView)
    ImageView imageView;
    @ViewInject(R.id.LTips)
    LinearLayout LTips;
    @ViewInject(R.id.tvMemo)
    TextView tvMemo;

    FrameLayout.LayoutParams params;
    LinearLayout.LayoutParams paramsGrp;

    List<TalentImageTag> tags = new ArrayList<>();
    EntityOfAddTalentImage talentImage;

    List<LinkGoods> tmpBind = new ArrayList<>();
    List<LinkGoods> tmpUnBind = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.activity_editimagetag, null);
        LHold.addView(view);
        ViewUtils.inject(this, view);
        EventBus.getDefault().register(this);

        String imgUrl = getIntent().getStringExtra("IMGURL");
        String entity = getIntent().getStringExtra("ENTITY");
        if (!TextUtils.isEmpty(entity)) {
            try {
                talentImage = new Gson().fromJson(entity, EntityOfAddTalentImage.class);
                talentImage.setEdit(true);
            } catch (Exception e) {
                talentImage = null;
            }
        }

        if (talentImage == null) {
            talentImage = new EntityOfAddTalentImage();
            talentImage.setTimes(true);
            talentImage.setEdit(false);
            talentImage.setImage(imgUrl);
        }


        initView();
    }

    private void initView() {

        setHeadTitle(getString(R.string.lbl_addtags_0));
        enableTvMore(R.string.continues, false, R.color.fh_red);
        int dx = getResources().getDisplayMetrics().widthPixels;
        params = new FrameLayout.LayoutParams(dx, dx);
        imageView.setLayoutParams(params);

        paramsGrp = new LinearLayout.LayoutParams(dx, dx);
        paramsGrp.gravity = Gravity.CENTER;
        FContainer.setLayoutParams(paramsGrp);

        drawView(talentImage);
    }

    private void drawView(EntityOfAddTalentImage entity) {
        if (entity != null) {
            if (entity.isEdit()) {
                new FHImageViewUtil(imageView).setImageURI(entity.getImage(), FHImageViewUtil.SHOWTYPE.TALENTTIME);
                List<TalentImageTag> goods = entity.getGoods();
                if (goods != null && goods.size() > 0) {
                    for (TalentImageTag tag : goods) {
                        tag.setEditingTag(1);//处于编辑状态的标签
                        addTag(tag);
                    }
                } else {
                    LTips.setVisibility(View.VISIBLE);
                }
            } else {
                new FHImageViewUtil(imageView).setImageURI(entity.getImage(), FHImageViewUtil.SHOWTYPE.TALENTTIME);
                LTips.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(value = {R.id.imageView})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                final int all = LinkGoodsEvent.getLocalCount();
                if (all == 10) {
                    BaseFunc.showMsg(mContext, getString(R.string.hint_overlinklimit_of_image));
                    return;
                }
                if (tags != null) {
                    if (tags.size() < 3) {
                        BaseFunc.gotoActivity(mContext, LinkGoodsActivity.class, LinkGoodsActivity.FROMIMAGE);
                    } else {
                        BaseFunc.showMsg(mContext, getString(R.string.hint_imagetag_limit));
                        return;
                    }

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handleImageTagEvent(LinkGoods4ImageTagEvent event) {
        if (event != null) {
            LinkGoods linkGoods = event.getGoods();
            if (event.isAdd()) {
                //添加标签
                addTagByGoods(linkGoods);
                tmpBind.add(linkGoods);
            } else {
                removeTag(linkGoods.getGoods_id());
            }
        }
    }

    private void addTag(final TalentImageTag tag) {
        if (tag != null) {
            final View view = View.inflate(mContext, R.layout.customimagetag, null);
            view.setTag(tag.getGoods_id());

            final Custom3LinesLayout custom3LinesLayout = (Custom3LinesLayout) view.findViewById(R.id.custom3LinesView);
            custom3LinesLayout.setImageDotClickListener(new Custom3LinesLayout.OnImageDotClickListener() {
                @Override
                public void onImageDotClick() {
                    boolean isLeft = custom3LinesLayout.getIsLeft();
                    custom3LinesLayout.setDirection(!isLeft);
                }
            });
            custom3LinesLayout.setImageDotLongClickListener(new Custom3LinesLayout.OnImageDotLongClickListener() {
                @Override
                public void onImageDotLongClick() {
                    FHHintDialog fhHintDialog = new FHHintDialog(mContext);
                    fhHintDialog.setTvContent(getString(R.string.tips_delete_selected_tags));
                    fhHintDialog.setHintListener(new FHHintDialog.FHHintListener() {
                        @Override
                        public void onLeftClick() {

                        }

                        @Override
                        public void onRightClick() {
                            removeTag(tag.getGoods_id());
                        }
                    });
                    fhHintDialog.show();
                }
            });
            custom3LinesLayout.setTextTop(tag.getShort_name());
            custom3LinesLayout.setTextMiddle(tag.getGoods_price());
            custom3LinesLayout.setTextBottom(tag.getGoods_origin());
            TalentImageTagLine line1 = tag.getLine1();
            if (line1 != null) {
                custom3LinesLayout.setDirection(line1.isLeft());
            } else {
                custom3LinesLayout.setDirection(true);
            }

            custom3LinesLayout.setPositionByLocation(tag.getLocation());
            FContainer.addView(view);

            tags.add(tag);

            LTips.setVisibility(View.GONE);
            tvMemo.setVisibility(View.VISIBLE);
        }
    }

    private void addTagByGoods(final LinkGoods linkGoods) {
        if (linkGoods != null) {
            TalentImageTag tag = new TalentImageTag();
            tag.setGoods_id(linkGoods.getGoods_id());
            tag.setShort_name(linkGoods.getShortName());
            tag.setGoods_price(linkGoods.getGoodsPrice());
            tag.setGoods_origin(linkGoods.getGoods_origin());
            tag.setGoods_image(linkGoods.getGoods_image());

            TalentImageTagLine line1 = new TalentImageTagLine();
            line1.setValue(linkGoods.getShortName());
            TalentImageTagLine line2 = new TalentImageTagLine();
            line2.setValue(linkGoods.getGoodsPriceDesc());
            TalentImageTagLine line3 = new TalentImageTagLine();
            line3.setValue(linkGoods.getGoods_origin());

            tag.setLine1(line1);
            tag.setLine2(line2);
            tag.setLine3(line3);

            tag.setEditingTag(0);//新增的标签

            addTag(tag);
        }
    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        refreshTagsPosition();
        talentImage.setGoods(tags);
        EventBus.getDefault().post(new LinkGoodsEvent(true, talentImage));
        finish();
    }


    private void refreshTagsPosition() {
        if (tags != null && tags.size() > 0) {
            for (TalentImageTag tag : tags) {
                View view = FContainer.findViewWithTag(tag.getGoods_id());
                if (view != null) {
                    Custom3LinesLayout custom3LinesView = (Custom3LinesLayout) view.findViewById(R.id.custom3LinesView);
                    if (custom3LinesView != null) {
                        tag.setLocation(custom3LinesView.getLocStrOfLeftTop());
                        boolean isLeft = custom3LinesView.getIsLeft();
                        String direction = TalentImageTagLine.getDirectionByBool(isLeft);

                        TalentImageTagLine line1 = tag.getLine1();
                        TalentImageTagLine line2 = tag.getLine2();
                        TalentImageTagLine line3 = tag.getLine3();
                        if (line1 != null) {
                            line1.setDirection(direction);
                        }
                        if (line2 != null) {
                            line2.setDirection(direction);
                        }
                        if (line3 != null) {
                            line3.setDirection(direction);
                        }
                    }
                }
            }
        }
    }

    private void removeTag(String goodsId) {
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                TalentImageTag tag = tags.get(i);
                if (TextUtils.equals(goodsId, tag.getGoods_id())) {
                    if (tag.getEditingTag() > 0) {
                        //撤销操作时 编辑前的关联商品要还原
                        LinkGoods linkGoods = LinkGoodsEvent.getGoodsById(goodsId);
                        tmpUnBind.add(linkGoods);
                    }

                    LinkGoodsEvent.deleteListGoodsById(goodsId);
                    View view = FContainer.findViewWithTag(goodsId);
                    FContainer.removeView(view);
                    tags.remove(i);
                    return;
                }
            }
        }
    }

    @Override
    public void onBackClick() {
        super.onBackClick();
        //撤销操作
        LinkGoodsEvent.opListGoodsByAll(false, tmpBind);
        LinkGoodsEvent.opListGoodsByAll(true, tmpUnBind);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //撤销操作
        LinkGoodsEvent.opListGoodsByAll(false, tmpBind);
        LinkGoodsEvent.opListGoodsByAll(true, tmpUnBind);
    }
}
