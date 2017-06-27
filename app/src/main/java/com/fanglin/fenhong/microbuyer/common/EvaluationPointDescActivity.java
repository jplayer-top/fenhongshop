package com.fanglin.fenhong.microbuyer.common;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baseui.BaseFragmentActivityUI;

/**
 * Created by lizhixin on 2016/02/29
 * 积分说明页面
 */
public class EvaluationPointDescActivity extends BaseFragmentActivityUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeadTitle(R.string.points_desc);

        View view = View.inflate(this, R.layout.activity_evaluation_point_desc, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        LHold.addView(view);

        String desc = getIntent().getStringExtra("VAL");
        tv.setText(BaseFunc.fromHtml(desc));
    }
}
