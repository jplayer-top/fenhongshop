package com.fanglin.fenhong.microbuyer.base.baseui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.fanglin.fenhong.microbuyer.R;

/**
 * WIFI环境不通时的提示UI
 * Created by lizhixin on 2016/5/12 16:15.
 */
public class FHwifiHintPopWindow extends PopupWindow{

    public FHwifiHintPopWindow(final Context mContext) {
        super();
        View view = View.inflate(mContext, R.layout.pop_wifi_hint, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null) {
                    /**
                     * 当某些手机入魅族等不支持无线网络设置页面时，改为跳转至设置页面
                     */
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    ComponentName componentName = intent.resolveActivity(mContext.getPackageManager());
                    if (componentName != null) {
                        mContext.startActivity(intent);
                    } else {
                        intent  = new Intent(Settings.ACTION_SETTINGS);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable());
    }

}
