package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;
import com.fanglin.fenhong.microbuyer.base.model.EvaluationArticle;

/**
 * Created by lizhixin on 2016/02/22
 * 发表评价引导页
 */
public class EvaluateBeforeActivity extends BaseFragmentActivityUI implements EvaluationArticle.ArticleModelCallBack {

    private String pointsDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //请求积分说明数据
        EvaluationArticle articleHandler = new EvaluationArticle();
        articleHandler.getEvaluateExplain();
        articleHandler.setCallBack(this);

        View view = View.inflate(mContext, R.layout.activity_evaluate_before, null);
        LHold.addView(view);
        String order_id;
        try {
            order_id = getIntent().getStringExtra("VAL");
        } catch (Exception e) {
            order_id = null;
        }

        setHeadTitle(R.string.pic_evaluate);
        enableTvMore(R.string.description, false);

        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(BaseFunc.fromHtml(getString(R.string.publish_comment_explain)));

        Button button = (Button) view.findViewById(R.id.button);
        final String finalOrder_id = order_id;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFunc.gotoActivity(mContext, GoodsEvaluateActivity.class, finalOrder_id + ",0");// ,0 表示是评价　而非追评
                finish();
            }
        });

    }

    @Override
    public void ontvMoreClick() {
        super.ontvMoreClick();
        if (TextUtils.isEmpty(pointsDesc)) {
            BaseFunc.showMsg(this, "暂无积分说明");
        } else {
            BaseFunc.gotoActivity(this, EvaluationPointDescActivity.class, pointsDesc);
        }
    }

    @Override
    public void onDataSuccess(EvaluationArticle entity) {
        if (entity != null && !TextUtils.isEmpty(entity.article_content)) {
            this.pointsDesc = entity.article_content;
        }
    }

    @Override
    public void onDataError(String errcode) {
        this.pointsDesc = null;
    }
}
