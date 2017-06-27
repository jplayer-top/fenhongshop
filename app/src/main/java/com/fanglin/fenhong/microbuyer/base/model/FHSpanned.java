package com.fanglin.fenhong.microbuyer.base.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import com.fanglin.fenhong.microbuyer.R;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者： Created by Plucky on 2015/12/17.
 * 分红全球购 图标--文字 映射表
 */
public class FHSpanned {

    /** 表情包定义 */
    public static Map<String, Integer> GetFaceMap() {
        Map<String, Integer> mFaceMap = new LinkedHashMap<>();

        mFaceMap.put("[f001]", R.drawable.f001);
        mFaceMap.put("[fwarn]", R.drawable.fwarn);
        mFaceMap.put("[fown]", R.drawable.fown);

        return mFaceMap;
    }

    private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static CharSequence convertNormalStringToSpannableString(
            Context context, String message) {
        String hackTxt;
        if (message.startsWith("[") && message.endsWith("]")) {
            hackTxt = message + " ";
        } else {
            hackTxt = message;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);

        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str2 = localMatcher.group(0);
            int k = localMatcher.start();
            int m = localMatcher.end();
            if (m - k < 8) {
                if (GetFaceMap().containsKey(str2)) {
                    int face = GetFaceMap().get(str2);
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            context.getResources(), face);
                    if (bitmap != null) {
                        ImageSpan localImageSpan = new ImageSpan(context,
                                bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
                        value.setSpan(localImageSpan, k, m,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return value;
    }
}
