package com.fanglin.fenhong.microbuyer.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fhui.custom.Custom3LinesLayout;

import java.util.Random;

public class TestCustomViewActivity extends Activity implements View.OnClickListener {

    Custom3LinesLayout custom3LinesView;
    View vLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_view);
        initView();
    }

    private void initView() {
        vLine = findViewById(R.id.vLine);
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        custom3LinesView = (Custom3LinesLayout) findViewById(R.id.custom3LinesView);
        custom3LinesView.setImageDotClickListener(new Custom3LinesLayout.OnImageDotClickListener() {
            @Override
            public void onImageDotClick() {
                Toast.makeText(TestCustomViewActivity.this, "hello custom view", Toast.LENGTH_SHORT).show();
            }
        });

//        custom3LinesView.setTextSizeAll((int) getResources().getDimension(R.dimen.text_size_20));//改变文字大小
//        custom3LinesView.setTextColorAll(getResources().getColor(R.color.blue1D81FC));//改变文字颜色
//        custom3LinesView.setBackgroundResource(R.color.transparent_4c);//改变背景

        custom3LinesView.setPosition(300, 20);//设置在父控件中的位置
    }

    boolean isLeft = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                isLeft = !isLeft;
                turnDirection(isLeft);
                custom3LinesView.setDirection(isLeft);
                break;
            case R.id.btn1:
                custom3LinesView.setTextTop("来了地方上电饭锅" + new Random().nextInt(10000));
                break;
            case R.id.btn2:
                custom3LinesView.setTextMiddle("来了中间" + new Random().nextInt(10000));
                break;
            case R.id.btn3:
                custom3LinesView.setTextBottom("来了底部" + new Random().nextInt(10000));
                break;
            default:
                break;
        }
    }

    private void turnDirection(boolean left) {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation animation;
        if (left) {
            animation = new TranslateAnimation(type, 0, type, -1, type, 0, type, 0);
        } else {
            animation = new TranslateAnimation(type, -1, type, 0, type, 0, type, 0);
        }

        animation.setDuration(1);
        animation.setFillAfter(true);
        vLine.startAnimation(animation);
    }

}
