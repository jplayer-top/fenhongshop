package com.fanglin.fhui;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

/**
 * 青岛芳林信息
 * Created by Plucky on 2016/12/27.
 * 功能描述: 扩展Html标签，支持 strike、s 标签
 */

public class FHTagHandler implements Html.TagHandler {

    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("strike") || tag.equals("s") || tag.equals("del")) {
            processStrike(opening, output);
        }
    }

    /**
     * 处理下划线
     *
     * @param opening 是否打开(<s/>)或者闭合(<s><s/>)
     * @param output
     */
    private void processStrike(boolean opening, Editable output) {
        int len = output.length();
        if (opening) {
            output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
        } else {
            Object obj = getLast(output, StrikethroughSpan.class);
            int where = output.getSpanStart(obj);

            output.removeSpan(obj);

            if (where != len) {
                output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            for (int i = objs.length; i > 0; i--) {
                if (text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i - 1];
                }
            }
            return null;
        }
    }
}
