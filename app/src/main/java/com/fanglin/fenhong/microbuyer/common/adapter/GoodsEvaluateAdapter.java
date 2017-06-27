package com.fanglin.fenhong.microbuyer.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.FileUtils;
import com.fanglin.fenhong.microbuyer.base.baseui.AutoGridLayoutManager;
import com.fanglin.fenhong.microbuyer.base.baseui.FHImageViewUtil;
import com.fanglin.fenhong.microbuyer.base.model.EvaluateAGoods;
import com.fanglin.fenhong.microbuyer.base.model.EvaluatingGoodsEntity;
import com.fanglin.fenhong.microbuyer.common.GoodsEvaluateActivity;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Created by lizhixin on 2016/2/22 16:39.
 * 晒单评价商品列表 adapter
 */
public class GoodsEvaluateAdapter extends SectionedRecyclerViewAdapter<GoodsEvaluateAdapter.HeaderViewHolder, GoodsEvaluateAdapter.ItemViewHolder, GoodsEvaluateAdapter.FooterViewHolder> {
    private Context mContext;
    public List<EvaluatingGoodsEntity> list;//商品数据

    public List<ReturnGoodsPicAdapter> picAdapters;//图片集合

    public int currentSection;//当前处于第几节，用于在activity中做图片数据整合

    public int isAppend;//是否是追加评价  0为评价  1为追评
    private IGoodsEvaluateSubmit igesCallback;

    public GoodsEvaluateAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<EvaluatingGoodsEntity> list) {
        if (list != null) {
            this.list = list;
            //为每个商品添加 增加图片按钮
            picAdapters = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ArrayList<String> temp = new ArrayList<>();
                temp.add("ADD");
                ReturnGoodsPicAdapter adapter = new ReturnGoodsPicAdapter(mContext, temp);
                picAdapters.add(adapter);

                //将上一次评价的内容赋值给 当前编辑区  added By Plucky
                if (isAppend == 0) {
                    this.list.get(i).comment = this.list.get(i).geval_content;
                } else {
                    this.list.get(i).comment = null;
                }
                float itmp = this.list.get(i).geval_scores;
                this.list.get(i).stars = itmp > 0 ? itmp : 5;
            }
        }
    }

    public void setIgesCallback(IGoodsEvaluateSubmit igesCallback) {
        this.igesCallback = igesCallback;
    }

    @Override
    protected int getSectionCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        //最后一节显示店铺评价与提交评价按钮
        return list != null && (list.size() - 1 == section);
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_evaluate_goods, null);
        return new HeaderViewHolder(view);
    }

    @Override
    protected FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_evaluate_goods_footer, null);
        return new FooterViewHolder(view);
    }

    @Override
    protected ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_evaluate_pic, null);
        return new ItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderViewHolder holder, final int section) {
        new FHImageViewUtil(holder.imageView).setImageURI(list.get(section).goods_image, FHImageViewUtil.SHOWTYPE.DEFAULT);
        holder.tvTitle.setText(list.get(section).goods_name);
        BaseFunc.setFont(holder.tv_icon);
        calculateLimit(holder.etContent, holder.tvLimit);

        /**
         *  首次评价与追评的逻辑应该是这样的：fixed By Plucky 2016-3-3 4:40
         *  1、首评的时候 已经评价过的信息的现实控件llFirst 不应该出现 ，因为还没评价过
         *    评分控件llRating应该出现
         *  2、追评的时候相反，要显示第一次评价的信息且不能再次评星
         *  3、不管首评还是追评，输入框始终显示
         */
        if (isAppend == 0) {
            //第一次评价
            holder.llFirst.setVisibility(View.GONE);
            holder.llRating.setVisibility(View.VISIBLE);

            holder.tvCommentFirst.setText("");//为了完美对称
            holder.ratingBarFirst.setRating(5);
        } else {
            //追加评价
            holder.llFirst.setVisibility(View.VISIBLE);
            holder.llRating.setVisibility(View.GONE);

            holder.tvCommentFirst.setText(list.get(section).geval_content);
            holder.ratingBarFirst.setRating(list.get(section).geval_scores);
        }

        //位置信息的更新一定要放在 控件赋值的前面
        holder.watcher.setPosition(section);
        holder.startchanger.setPosition(section);

        /**
         * 控件显示暂存区的数据
         * 在初始化数据源list的时候,已经将服务器上一次保存的评价内容赋值到暂存区
         * 不管如何 暂存区才是用户需要编辑的区域
         * Added By Plucky
         */
        holder.etContent.setText(list.get(section).comment);
        holder.ratingBar.setRating(list.get(section).stars);

        /**
         * 控件在用户操作时 动态改变数据源
         * 注意：onBind 事件只要列表滑动,position会频繁改变，所有用户操作型事件最后
         * 不要放在这个事件中  如TextWatcher、OnRatingBarChangeListener
         * 这时用户操作数据源的变化跟不上position变化、会导致数据源的错乱
         * 另外在OnBind中添加类似的事件是消耗性能的动作
         * Added By Plucky  2016-03-04 11:44
         */
    }

    @Override
    protected void onBindSectionFooterViewHolder(final FooterViewHolder holder, final int section) {
        //if (!TextUtils.isEmpty(list.get(section).geval_content)) {
        if (isAppend == 1) {
            //追加评价时 隐藏店铺评星与匿名勾选
            holder.llBot.setVisibility(View.GONE);
        } else {
            //holder.mCB.setChecked(list.get(section).geval_isanonymous == 1);
            holder.llBot.setVisibility(View.VISIBLE);
            //店铺评星
            holder.rating_desc.setRating(list.get(section).stars_desc);
            holder.rating_service.setRating(list.get(section).stars_service);
            holder.rating_release.setRating(list.get(section).stars_release);
            holder.rating_desc.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    list.get(section).stars_desc = rating;
                }
            });
            holder.rating_service.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    list.get(section).stars_service = rating;
                }
            });
            holder.rating_release.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    list.get(section).stars_release = rating;
                }
            });
        }

        holder.btn_subimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 先检查单个商品是否进行评星+评价
                 */
                boolean passChecked = true;
                for (EvaluatingGoodsEntity entity : list) {
                    if (entity.stars == 0) {
                        BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_evaluate_info_star));
                        passChecked = false;
                        break;
                    }
                    if (TextUtils.isEmpty(entity.comment) || TextUtils.isEmpty(entity.comment.trim())) {
                        BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_evaluate_info_content));
                        passChecked = false;
                        break;
                    }
                }
                if (!passChecked) return;
                /**
                 * 再检查是否贵店铺进行过评价
                 */
                if (holder.rating_desc.getRating() == 0) {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_evaluate_info_star_desc));
                    YoYo.with(Techniques.Shake).duration(700).playOn(holder.rating_desc);
                    return;
                }
                if (holder.rating_service.getRating() == 0) {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_evaluate_info_star_service));
                    YoYo.with(Techniques.Shake).duration(700).playOn(holder.rating_service);
                    return;
                }
                if (holder.rating_release.getRating() == 0) {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_evaluate_info_star_release));
                    YoYo.with(Techniques.Shake).duration(700).playOn(holder.rating_release);
                    return;
                }

                EvaluateAGoods evaluate = new EvaluateAGoods();
                evaluate.is_anonymous = holder.mCB.isChecked() ? 1 : 0;
                evaluate.store_desccredit = holder.rating_desc.getRating();
                evaluate.store_servicecredit = holder.rating_service.getRating();
                evaluate.store_deliverycredit = holder.rating_release.getRating();
                if (igesCallback != null) {
                    igesCallback.onGoodsEvaluateSubmit(
                            holder.mCB.isChecked() ? 1 : 0, holder.rating_desc.getRating(), holder.rating_service.getRating(), holder.rating_release.getRating(),
                            isAppend == 1);//是否是追加评价 true 追加 false 评价
                }
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(ItemViewHolder holder, final int section, int position) {
        currentSection = section;
        AutoGridLayoutManager layoutManager = new AutoGridLayoutManager(mContext, 3);
        holder.recyclerView.setLayoutManager(layoutManager);

        final ReturnGoodsPicAdapter adapter = picAdapters.get(section);
        adapter.setOnPictureItemClickListener(new ReturnGoodsPicAdapter.AddPicCallBackListener() {
            @Override
            public void onAddDefault() {
                currentSection = section;
                if (adapter.getItemCount() < 6) {
                    BaseFunc.selectPictures((GoodsEvaluateActivity) mContext, 0x001, true, 800, 800, 1, 1);
                } else {
                    BaseFunc.showMsg(mContext, mContext.getString(R.string.tips_max_pic));
                }
            }

            @Override
            public void onDelItem(int index) {
                adapter.list.remove(index);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPicView(String picUrl) {
                FileUtils.BrowserOpenL(mContext, adapter.list.subList(0, adapter.getItemCount() - 1), picUrl);
            }
        });
        holder.recyclerView.setAdapter(adapter);
    }

    private void calculateLimit(EditText et, TextView tv) {
        int len = et.length();
        String fmt = String.format(mContext.getString(R.string.evaluate_limit), len);
        tv.setText(fmt);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvTitle, tvLimit, tvCommentFirst, tv_icon;
        private RatingBar ratingBar, ratingBarFirst;
        private EditText etContent;
        private LinearLayout llRating, llFirst;

        private EtTextChanged watcher;//输入框改变
        private RtChangeListener startchanger;//评分控件改变

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.sdv);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating);
            tvLimit = (TextView) itemView.findViewById(R.id.tv_limit);
            etContent = (EditText) itemView.findViewById(R.id.et_content);
            tvCommentFirst = (TextView) itemView.findViewById(R.id.tv_commented_first);//带有first后缀的表示上次评价的内容
            tv_icon = (TextView) itemView.findViewById(R.id.tv_icon);
            llFirst = (LinearLayout) itemView.findViewById(R.id.ll_first);
            ratingBarFirst = (RatingBar) itemView.findViewById(R.id.rb_first);
            llRating = (LinearLayout) itemView.findViewById(R.id.ll_rating);

            watcher = new EtTextChanged();
            watcher.setWidget(etContent, tvLimit);
            etContent.addTextChangedListener(watcher);

            startchanger = new RtChangeListener();
            ratingBar.setOnRatingBarChangeListener(startchanger);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private Button btn_subimt;
        private RatingBar rating_desc;//店铺评价 描述相符
        private RatingBar rating_service;//店铺评价 服务态度
        private RatingBar rating_release;//店铺评价 发货速度
        private CheckBox mCB;
        private LinearLayout llBot;

        public FooterViewHolder(View itemView) {
            super(itemView);
            btn_subimt = (Button) itemView.findViewById(R.id.btn_subimt);
            rating_desc = (RatingBar) itemView.findViewById(R.id.rating_desc);
            rating_service = (RatingBar) itemView.findViewById(R.id.rating_service);
            rating_release = (RatingBar) itemView.findViewById(R.id.rating_release);
            mCB = (CheckBox) itemView.findViewById(R.id.mCB);
            llBot = (LinearLayout) itemView.findViewById(R.id.LBot);
        }
    }

    public interface IGoodsEvaluateSubmit {
        void onGoodsEvaluateSubmit(int is_anonymous, float store_desccredit, float store_servicecredit, float store_deliverycredit, boolean isAdded);
    }


    class EtTextChanged implements TextWatcher {
        private int position;
        private EditText mEt;
        private TextView mTv;

        public EtTextChanged() {
        }

        public void setWidget(EditText et, TextView tv) {
            this.mEt = et;
            this.mTv = tv;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (position >= 0 && position < getSectionCount()) {
                list.get(position).comment = s.toString().trim();
            }

            calculateLimit(mEt, mTv);
        }
    }

    class RtChangeListener implements RatingBar.OnRatingBarChangeListener {
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if (position >= 0 && position < getSectionCount()) {
                list.get(position).stars = rating;
            }
        }
    }

}
