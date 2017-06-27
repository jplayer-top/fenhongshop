package com.fanglin.fenhong.microbuyer.microshop.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.TalentInfo;
import com.fanglin.fenhong.microbuyer.base.model.TalentTimeImages;
import com.fanglin.fenhong.microbuyer.base.model.TimeImagesGroup;
import com.fanglin.fhui.CircleImageView;
import com.fanglin.fhui.PinnedHeaderListView.SectionedBaseAdapter;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/8-上午11:49.
 * 功能描述: 使用ListView的形式实现 达人首页的分区
 */
public class TalentPinnedSectionAdapter extends SectionedBaseAdapter {
    /**
     * 第一分区  达人主要信息
     */
    public final static int TYPE_TOP = 0;
    /**
     * 第二分区 TA最爱的时光
     */
    public final static int TYPE_TOP_BELOW = 1;

    /**
     * 内容分区普通视图
     */
    public final static int TYPE_SECTION_ITEM = 2;

    /**
     * 以下为列表内容区域
     */
    /**
     * 内容分区标题
     */
    public final static int TYPE_SECTION_HEADER = 0;
    public final static int TYPE_SECTION_HEADER_NONE = 1;

    private float topAlpha, botAlpha;
    int marginRight;


    LinearLayout.LayoutParams imageParams;
    AbsListView.LayoutParams topParams;

    private Context mContext;
    private TalentInfo talentInfo;
    private List<TimeImagesGroup> list;

    public TalentPinnedSectionAdapter(Context context) {
        this.mContext = context;
        marginRight = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_20);

        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int iwidth = (width - 20 - 40) / 3;
        imageParams = new LinearLayout.LayoutParams(iwidth, iwidth);

        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        height = height - mContext.getResources().getDimensionPixelOffset(R.dimen.dp_of_48);
        topParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
    }

    public void setAlpha(float topAlpha, float botAlpha) {
        this.topAlpha = topAlpha;
        this.botAlpha = botAlpha;
    }

    public void setTalentInfo(TalentInfo talentInfo) {
        this.talentInfo = talentInfo;
    }

    public void setList(List<TimeImagesGroup> list) {
        this.list = list;
    }

    public void addList(List<TimeImagesGroup> alist) {
        if (alist != null && alist.size() > 0) {
            if (list != null && list.size() > 0) {
                /**
                 * 需要判断上个列表的最后一个和新增列表的第一个 如果月日相同则合并
                 */
                int lastIndex = list.size() - 1;
                TimeImagesGroup lastOne = list.get(lastIndex);
                List<TalentTimeImages> lastTimes = lastOne.getImages();
                TimeImagesGroup firstOne = alist.get(0);
                List<TalentTimeImages> firstTimes = firstOne.getImages();
                if (TextUtils.equals(lastOne.gettMonth(), firstOne.gettMonth()) && TextUtils.equals(lastOne.gettYear(), firstOne.gettYear())) {
                    if (lastTimes != null && lastTimes.size() > 0 && firstTimes != null && firstTimes.size() > 0) {
                        lastTimes.addAll(firstTimes);
                        alist.remove(0);
                        list.addAll(alist);
                    } else {
                        list.addAll(alist);
                    }
                } else {
                    list.addAll(alist);
                }
            }
        }
    }

    public void refreshFocus(boolean hasFocused) {
        if (talentInfo != null) {
            talentInfo.setIs_followed(hasFocused ? "1" : "0");
            notifyDataSetChanged();
        }
    }


    /**
     * 获取分组中的时光
     *
     * @param section int
     * @param index   int  这里的index不是position
     * @return 显示时光图片
     */
    @Override
    public TalentTimeImages getItem(int section, int index) {
        List<TalentTimeImages> images = getGrp(section).getImages();
        if (images != null && images.size() > 0 && index < images.size()) {
            return images.get(index);
        } else {
            return null;
        }
    }

    /**
     * 获取月分组
     *
     * @param section int
     * @return 显示月
     */
    public TimeImagesGroup getGrp(int section) {
        return list.get(section - 2);
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        if (list == null) return 2;
        return list.size() + 2;
    }

    @Override
    public int getCountForSection(int section) {
        if (section == 0) return 1;
        if (section == 1) return 1;
        List<TalentTimeImages> images = getGrp(section).getImages();
        if (images == null) return 0;
        /**
         * 返回行数
         */
        return BaseFunc.getRowCountOfList(images.size(), 3);
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(section, position);
        if (convertView == null) {
            if (viewType == TYPE_TOP) {
                convertView = View.inflate(mContext, R.layout.item_talent_header, null);
                new TopViewHolder(convertView);
            } else if (viewType == TYPE_TOP_BELOW) {
                convertView = View.inflate(mContext, R.layout.item_talent_header_below, null);
                new TopBelowViewHolder(convertView);
            } else {
                convertView = View.inflate(mContext, R.layout.item_talent_of_list, null);
                new SectionItemViewHolder(convertView);
            }
        }
        if (viewType == TYPE_TOP) {
            TopViewHolder topViewHolder = (TopViewHolder) convertView.getTag();
            topViewHolder.vHideTop.setAlpha(topAlpha);
            topViewHolder.vHideBot.setAlpha(botAlpha);

            /**
             * 达人信息赋值
             */

            if (talentInfo != null) {
                new FHImageViewUtil(topViewHolder.circleImageView).setImageURI(talentInfo.getTalent_avatar(), FHImageViewUtil.SHOWTYPE.AVATAR);
                topViewHolder.tvName.setText(talentInfo.getTalent_name());
                topViewHolder.tvDesc.setText(talentInfo.getTalent_intro());
                topViewHolder.tvTimes.setText(talentInfo.getTime_count());
                topViewHolder.tvFans.setText(talentInfo.getFans_count());
                topViewHolder.tvGoods.setText(talentInfo.getGoods_count());
                if (talentInfo.getIs_own() == 1) {
                    topViewHolder.tvEdit.setVisibility(View.VISIBLE);
                    topViewHolder.tvCommission.setVisibility(View.VISIBLE);
                    topViewHolder.LFocus.setVisibility(View.GONE);
                } else {
                    topViewHolder.tvEdit.setVisibility(View.GONE);
                    topViewHolder.tvCommission.setVisibility(View.GONE);
                    topViewHolder.LFocus.setVisibility(View.VISIBLE);
                }

                if (talentInfo.hasFollowed()) {
                    topViewHolder.btnFocus.setText(mContext.getString(R.string.lbl_focused));
                } else {
                    topViewHolder.btnFocus.setText(mContext.getString(R.string.lbl_focus));
                }
                topViewHolder.btnFocus.setSelected(!talentInfo.hasFollowed());

            } else {
                topViewHolder.tvEdit.setVisibility(View.GONE);
                topViewHolder.tvCommission.setVisibility(View.GONE);
                topViewHolder.LFocus.setVisibility(View.VISIBLE);
            }

        } else if (viewType == TYPE_SECTION_ITEM) {
            SectionItemViewHolder sectionItemViewHolder = (SectionItemViewHolder) convertView.getTag();
            if (position == getCountForSection(section) - 1) {
                sectionItemViewHolder.vDivider.setVisibility(View.GONE);
            } else {
                sectionItemViewHolder.vDivider.setVisibility(View.VISIBLE);
            }

            int index0 = BaseFunc.getIndexOfList(position, 0, 3);
            int index1 = BaseFunc.getIndexOfList(position, 1, 3);
            int index2 = BaseFunc.getIndexOfList(position, 2, 3);
            final TalentTimeImages image0 = getItem(section, index0);
            final TalentTimeImages image1 = getItem(section, index1);
            final TalentTimeImages image2 = getItem(section, index2);
            if (image0 != null) {
                sectionItemViewHolder.ivTimes1.setVisibility(View.VISIBLE);
                sectionItemViewHolder.ivTimes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoTalentImageDetail(mContext, image0.getTime_id(), false);
                    }
                });
                new FHImageViewUtil(sectionItemViewHolder.ivTimes1).setImageURI(image0.getTime_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            } else {
                sectionItemViewHolder.ivTimes1.setVisibility(View.INVISIBLE);
            }

            if (image1 != null) {
                sectionItemViewHolder.ivTimes2.setVisibility(View.VISIBLE);
                sectionItemViewHolder.ivTimes2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoTalentImageDetail(mContext, image1.getTime_id(), false);
                    }
                });
                new FHImageViewUtil(sectionItemViewHolder.ivTimes2).setImageURI(image1.getTime_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            } else {
                sectionItemViewHolder.ivTimes2.setVisibility(View.INVISIBLE);
            }

            if (image2 != null) {
                sectionItemViewHolder.ivTimes3.setVisibility(View.VISIBLE);
                sectionItemViewHolder.ivTimes3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseFunc.gotoTalentImageDetail(mContext, image2.getTime_id(), false);
                    }
                });
                new FHImageViewUtil(sectionItemViewHolder.ivTimes3).setImageURI(image2.getTime_image(), FHImageViewUtil.SHOWTYPE.DEFAULT);
            } else {
                sectionItemViewHolder.ivTimes3.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        int viewType = getSectionHeaderViewType(section);
        if (convertView == null) {
            if (viewType == TYPE_SECTION_HEADER_NONE) {
                convertView = View.inflate(mContext, R.layout.item_talent_none, null);
            } else {
                convertView = View.inflate(mContext, R.layout.item_talent_secion_header, null);
                new SectionHeaderViewHolder(convertView);
            }
        }

        if (viewType == TYPE_SECTION_HEADER) {
            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) convertView.getTag();
            TimeImagesGroup grp = getGrp(section);
            if (grp != null) {
                sectionHeaderViewHolder.tvMonth.setText(grp.gettMonth());
                sectionHeaderViewHolder.tvYear.setText(grp.gettYear());
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int section, int position) {
        if (section == 0) {
            return TYPE_TOP;
        } else if (section == 1) {
            return TYPE_TOP_BELOW;
        } else {
            return TYPE_SECTION_ITEM;
        }
    }


    @Override
    public int getSectionHeaderViewType(int section) {
        if (section == 0 || section == 1) return TYPE_SECTION_HEADER_NONE;
        return TYPE_SECTION_HEADER;
    }

    @Override
    public int getSectionHeaderViewTypeCount() {
        return 2;
    }

    /**
     * ViewHolder
     */

    class TopViewHolder {
        View vHideTop, vHideBot;
        LinearLayout LEdit, LFocus;
        TextView tvEdit, tvCommission, tvSlide;
        Button btnFocus;

        CircleImageView circleImageView;
        TextView tvName, tvDesc;
        LinearLayout LTimes, LFans, LGoods;
        TextView tvTimes, tvFans, tvGoods;


        public TopViewHolder(View view) {
            vHideTop = view.findViewById(R.id.vHideTop);
            vHideBot = view.findViewById(R.id.vHideBot);
            LEdit = (LinearLayout) view.findViewById(R.id.LEdit);
            tvEdit = (TextView) view.findViewById(R.id.tvEdit);
            tvCommission = (TextView) view.findViewById(R.id.tvCommission);
            tvSlide = (TextView) view.findViewById(R.id.tvSlide);

            LFocus = (LinearLayout) view.findViewById(R.id.LFocus);
            btnFocus = (Button) view.findViewById(R.id.btnFocus);

            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDesc = (TextView) view.findViewById(R.id.tvDesc);

            LTimes = (LinearLayout) view.findViewById(R.id.LTimes);
            LFans = (LinearLayout) view.findViewById(R.id.LFans);
            LGoods = (LinearLayout) view.findViewById(R.id.LGoods);

            tvTimes = (TextView) view.findViewById(R.id.tvTimes);
            tvFans = (TextView) view.findViewById(R.id.tvFans);
            tvGoods = (TextView) view.findViewById(R.id.tvGoods);

            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEdit(talentInfo);
                    }
                }
            });
            tvCommission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCommission();
                    }
                }
            });
            btnFocus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        boolean hasFocused = talentInfo != null && talentInfo.hasFollowed();
                        String talentId = talentInfo != null ? talentInfo.getTalent_id() : null;
                        listener.onFocus(talentId, hasFocused);
                    }
                }
            });

            LTimes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (talentInfo != null) {
                            listener.onTimes(talentInfo.getTalent_id());
                        }
                    }
                }
            });

            LFans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (talentInfo != null) {
                            listener.onFans(talentInfo.getTalent_id());
                        }
                    }
                }
            });

            LGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (talentInfo != null) {
                            listener.onGoods(talentInfo.getTalent_id());
                        }
                    }
                }
            });

            BaseFunc.setFont(tvSlide);

            view.setLayoutParams(topParams);
            view.setTag(this);
        }
    }

    class TopBelowViewHolder {
        public TopBelowViewHolder(View view) {
            view.setTag(this);
        }
    }

    class SectionHeaderViewHolder {
        TextView tvMonth, tvYear;

        public SectionHeaderViewHolder(View itemView) {
            itemView.setTag(this);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        }
    }

    class SectionItemViewHolder {
        LinearLayout LImagePnl;
        View vDivider;
        ImageView ivTimes1, ivTimes2, ivTimes3;

        public SectionItemViewHolder(View view) {
            LImagePnl = (LinearLayout) view.findViewById(R.id.LImagePnl);
            vDivider = view.findViewById(R.id.vDivider);
            ivTimes1 = (ImageView) view.findViewById(R.id.ivTimes1);
            ivTimes2 = (ImageView) view.findViewById(R.id.ivTimes2);
            ivTimes3 = (ImageView) view.findViewById(R.id.ivTimes3);
            ivTimes1.setLayoutParams(imageParams);
            ivTimes2.setLayoutParams(imageParams);
            ivTimes3.setLayoutParams(imageParams);
            view.setTag(this);
        }
    }

    private TalentPinnedSectionAdapterListener listener;

    public void setListener(TalentPinnedSectionAdapterListener listener) {
        this.listener = listener;
    }

    public interface TalentPinnedSectionAdapterListener {
        void onEdit(TalentInfo talentInfo);

        void onCommission();

        void onFans(String talentId);

        void onGoods(String talentId);

        void onFocus(String talentId, boolean hasFocused);

        void onTimes(String id);
    }
}
