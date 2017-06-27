package com.fanglin.fenhong.microbuyer.base.model;

import android.os.Bundle;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/3/27-下午1:08.
 * 功能描述:
 */
public class NativeUrlEntity {
    public static final int TYPE_CALL = 1;//电话
    public int type;
    public Class<?> activityClass;
    public String val;//单值 model转string时会使用
    public Bundle bundle;//多指 key-value 如果使用bundle则不使用val
}
