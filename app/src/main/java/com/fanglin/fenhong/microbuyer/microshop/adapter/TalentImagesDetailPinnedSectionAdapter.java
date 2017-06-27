package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseBO;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.listener.TalentImageClickListener;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.TalentImageComment;
import com.fanglin.fenhong.microbuyer.base.model.TalentImagesDetail;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;

import java.util.List;


/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/12-下午9:50.
 * 功能描述:时光详情适配器
 */
public class TalentImagesDetailPinnedSectionAdapter extends SectionedBaseAdapter {

    public static final int TYPE_HEADER_FOCUS = 0;//关注
    public static final int TYPE_HEADER_NORMAL = 1;//分割线

    public static final int TYPE_ITEM_CONTENT = 0;//时光详情
    public static final int TYPE_ITEM_GOODS = 1;//商品
    public static final int TYPE_ITEM_COMMENT = 2;//评论

    private TalentImagesDetail imagesDetail;
    private List<TalentImageComment> comments;
    private Context mContext;
    private LinearLayout.LayoutParams bannerParams;
    private Member member;

    public TalentImagesDetailPinnedSectionAdapter(Context mContext) {
        this.mContext = mContext;
        int dx = mContext.getResources().getDisplayMetrics().widthPixels;
        bannerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dx);
    }

    public void setData(TalentImagesDetail imagesDetail) {
        this.imagesDetail = imagesDetail;
        if (imagesDetail != null) {
            comments = imagesDetail.getComment();
        }
    }

    public void addList(List<TalentImageComment> list) {
        if (list != null && list.size() > 0) {
            if (comments != null && comments.size() > 0) {
                comments.addAll(list);
            }
        }
    }

    public void refreshMember(Member member) {
        this.member = member;
    }

    //获取关联商品列表
    private List<TalentImagesDetail.TalentGoods> getGoods() {
        if (imagesDetail != null) {
            return imagesDetail.getGoods();
        } else {
            return null;
        }
    }

    //获取关联商品的数量
    private int getGoodsCount() {
        if (getGoods() != null) {
            return getGoods().size();
        }
        return 0;
    }

    //达人信息
    public TalentInfo getTalentInfo() {
        if (imagesDetail != null) return imagesDetail.getTalent();
        return null;
    }

    private String getTalentMemberId() {
        if (imagesDetail != null) return imagesDetail.getTalentMemberId();
        return null;
    }

    public void refreshFocus(boolean hasFollowed) {
        if (getTalentInfo() != null) {
            getTalentInfo().setIs_followed(hasFollowed ? "1" : "0");
            notifyDataSetChanged();
        }
    }

    /**
     * 获取评论列表数量
     *
     * @return int
     */
    private int getCommentsCount() {
        if (comments == null) return 0;
        return comments.size();
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return 3;
    }

    @Override
    public int getCountForSection(int section) {
        if (section == 0) {
            return 1;
        } else if (section == 1) {
            return getGoodsCount();
        } else {
            return getCommentsCount();
        }
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    @Override
    public int getSectionHeaderViewType(int section) {
        if (section == 0) {
            TalentInfo talentInfo = getTalentInfo();
            if (talentInfo != null && talentInfo.getIs_own() == 0) {
                return TYPE_HEADER_FOCUS;
            } else {
                return TYPE_HEADER_NORMAL;
            }
        }

        return TYPE_HEADER_NORMAL;
    }

    @Override
    public int getItemViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int section, int position) {
        if (section == 0) {
            return TYPE_ITEM_CONTENT;
        } else if (section == 1) {
            return TYPE_ITEM_GOODS;
        } else {
            return TYPE_ITEM_COMMENT;
        }
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(section, position);
        if (convertView == null) {
            if (viewType == TYPE_ITEM_CONTENT) {
                convertView = View.inflate(mContext, R.layout.item_talentimagedtl_content, null);
                new ItemContentViewHolder(convertView);
            } else if (viewType == TYPE_ITEM_GOODS) {
                convertView = View.inflate(mContext, R.layout.item_talentimagedtl_goods, null);
                new ItemGoodsViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_talentimagedtl_comment, null);
                new ItemCommentViewHolder(convertView);
            }
        }

        boolean isLastPos = position == getCountForSection(section) - 1;
        if (viewType == TYPE_ITEM_CONTENT) {
            ItemContentViewHolder contentViewHolder = (ItemContentViewHolder) convertView.getTag();
            if (imagesDetail != null) {
                contentViewHolder.tvDesc.setText(imagesDetail.getContent());
                contentViewHolder.tvTime.setText(imagesDetail.getAdd_time_fmt());

                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                contentViewHolder.recyclerView.setLayoutManager(manager);
                TalentTagAdapter adapter = new TalentTagAdapter(mContext);
                adapter.setList(imagesDetail.getTags());
                contentViewHolder.recyclerView.setAdapter(adapter);

                TalentImageClickListener talentImageClickListener = new TalentImageClickListener() {
                    @Override
                    public void onImageClick(ViewGroup view) {
                        if (callBack != null) {
                            callBack.onImageClick(view,section, position);
                        }
                    }
                };

                View bannerView = BaseFunc.getTalentBanner(mContext, imagesDetail.getImages(), talentImageClickListener, imagesDetail.getTalentMemberId());
                if (bannerView != null) {
                    contentViewHolder.LBanner.setVisibility(View.VISIBLE);
                    contentViewHolder.LBanner.removeAllViews();
                    contentViewHolder.LBanner.addView(bannerView);

                } else {
                    contentViewHolder.LBanner.setVisibility(View.GONE);
                }
            }

        } else if (viewType == TYPE_ITEM_GOODS) {
            ItemGoodsViewHolder goodsViewHolder = (ItemGoodsViewHolder) convertView.getTag();
            goodsViewHolder.vLine.setVisibility(isLastPos ? View.GONE : View.VISIBLE);

            List<TalentImagesDetail.TalentGoods> goodsList = getGoods();
            if (goodsList != null && goodsList.size() > 0) {
                final TalentImagesDetail.TalentGoods talentGoods = getGoods().get(position);
                if (talentGoods != null) {
                    goodsViewHolder.tvName.setText(talentGoods.getGoods_name());
                    goodsViewHolder.tvPrice.setText(talentGoods.getGoods_price());
                    new FHImageViewUtil(goodsViewHolder.ivGoodsImage).setImageURI(talentGoods.getGoods_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseFunc.gotoGoodsDetail(mContext, talentGoods.getGoods_id(), null, getTalentMemberId());
                        }
                    });
                }
            }
        } else {
            ItemCommentViewHolder commentViewHolder = (ItemCommentViewHolder) convertView.getTag();
            commentViewHolder.vLine.setVisibility(isLastPos ? View.GONE : View.VISIBLE);
            final TalentImageComment comment = comments.get(position);
            if (comment != null) {
                new FHImageViewUtil(commentViewHolder.circleImageView).setImageURI(comment.getComment_member_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
                commentViewHolder.tvName.setText(comment.getComment_member_name());
                if (comment.isLouzhu()) {
                    commentViewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_louzhu, 0);
                } else {
                    commentViewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                if (comment.isUpped()) {
                    commentViewHolder.ivZan.setSelected(true);
                    commentViewHolder.tvNum.setSelected(true);
                } else {
                    commentViewHolder.ivZan.setSelected(false);
                    commentViewHolder.tvNum.setSelected(false);
                }

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upComment(comment);
                    }
                };

                commentViewHolder.ivZan.setOnClickListener(listener);
                commentViewHolder.tvNum.setOnClickListener(listener);
                commentViewHolder.LZan.setOnClickListener(listener);

                commentViewHolder.tvNum.setText(comment.getUp_count());
                commentViewHolder.tvTime.setText(comment.getComment_time_fmt());

                if (comment.getParent() != null) {
                    String str = String.format(mContext.getString(R.string.fmt_talent_comment), comment.getParent().getComment_member_name(), comment.getComment_message());
                    commentViewHolder.tvComment.setText(BaseFunc.fromHtml(str));
                } else {
                    commentViewHolder.tvComment.setText(comment.getComment_message());
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callBack != null) {
                            callBack.onComment(comment);
                        }
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        int viewType = getSectionHeaderViewType(section);
        if (convertView == null) {
            if (viewType == TYPE_HEADER_FOCUS) {
                convertView = View.inflate(mContext, R.layout.item_talentimagedtl_focus, null);
                new HeaderFocusViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_talent_divider, null);
            }
        }
        if (viewType == TYPE_HEADER_FOCUS) {
            HeaderFocusViewHolder focusViewHolder = (HeaderFocusViewHolder) convertView.getTag();
            final TalentInfo talentInfo = getTalentInfo();
            if (talentInfo != null) {
                convertView.setVisibility(View.VISIBLE);

                focusViewHolder.tvName.setText(talentInfo.getTalent_name());
                new FHImageViewUtil(focusViewHolder.circleImageView).setImageURI(talentInfo.getTalent_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
                String talentType = talentInfo.getTalent_type();
                if (TextUtils.isEmpty(talentType)) {
                    focusViewHolder.tvDesc.setVisibility(View.GONE);
                } else {
                    focusViewHolder.tvDesc.setText(talentType);
                    focusViewHolder.tvDesc.setVisibility(View.VISIBLE);
                }
                if (talentInfo.hasFollowed()) {
                    focusViewHolder.btnFocus.setText(mContext.getString(R.string.lbl_focused));
                } else {
                    focusViewHolder.btnFocus.setText(mContext.getString(R.string.lbl_focus));
                }

                if (callBack != null) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callBack.onTalent(talentInfo.getTalent_id());
                        }
                    });
                    focusViewHolder.btnFocus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callBack.onFocus(talentInfo.hasFollowed());
                        }
                    });
                }
            } else {
                convertView.setVisibility(View.GONE);
            }
        } else {
            //分割线
            View vDivider = convertView.findViewById(R.id.vDivider);
            if (vDivider != null) {
                vDivider.setVisibility(section == 0 ? View.GONE : View.VISIBLE);
            }
        }
        return convertView;
    }

    class HeaderFocusViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvDesc;
        Button btnFocus;

        public HeaderFocusViewHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            btnFocus = (Button) view.findViewById(R.id.btnFocus);
            view.setTag(this);
        }
    }

    class ItemContentViewHolder {
        LinearLayout LBanner;
        TextView tvDesc, tvTime;
        RecyclerView recyclerView;

        public ItemContentViewHolder(View view) {
            LBanner = (LinearLayout) view.findViewById(R.id.LBanner);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            LBanner.setLayoutParams(bannerParams);
            view.setTag(this);
        }
    }

    class ItemGoodsViewHolder {
        ImageView ivGoodsImage;
        TextView tvName, tvPrice;
        View vLine;

        public ItemGoodsViewHolder(View view) {
            ivGoodsImage = (ImageView) view.findViewById(R.id.ivGoodsImage);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            vLine = view.findViewById(R.id.vLine);

            view.setTag(this);
        }
    }

    class ItemCommentViewHolder {
        CircleImageView circleImageView;
        TextView tvName, tvTime, tvComment;
        LinearLayout LZan;
        ImageView ivZan;
        TextView tvNum;
        View vLine;

        public ItemCommentViewHolder(View view) {
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            LZan = (LinearLayout) view.findViewById(R.id.LZan);
            ivZan = (ImageView) view.findViewById(R.id.ivZan);
            tvNum = (TextView) view.findViewById(R.id.tvNum);
            vLine = view.findViewById(R.id.vLine);

            view.setTag(this);
        }
    }

    private TalentImagesDetailAdapterCallBack callBack;

    public void setCallBack(TalentImagesDetailAdapterCallBack callBack) {
        this.callBack = callBack;
    }

    public interface TalentImagesDetailAdapterCallBack {
        void onFocus(boolean isFollowed);

        void onTalent(String talentId);

        void onComment(TalentImageComment comment);

        void onImageClick(ViewGroup view,int section, int position);
    }


    private void upComment(final TalentImageComment comment) {
        if (member == null) {
            BaseFunc.gotoLogin(mContext);
            return;
        }
        //防止数据异常 不允许取消点赞
        if (comment == null || comment.isUpped()) return;

        APIUtil.FHAPICallBack fhapiCallBack = new APIUtil.FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (isSuccess) {
                    if (comment.isUpped()) {
                        comment.setIs_upped("0");
                        comment.opZan(false);//赞减一
                        notifyDataSetChanged();
                    } else {
                        comment.setIs_upped("1");
                        comment.opZan(true);//赞加一
                        notifyDataSetChanged();
                    }
                }
            }
        };
        if (comment.isUpped()) {
            new BaseBO().setCallBack(fhapiCallBack).unZan(member, comment.getComment_id());
        } else {
            new BaseBO().setCallBack(fhapiCallBack).zan(member, comment.getComment_id());
        }

    }
}
