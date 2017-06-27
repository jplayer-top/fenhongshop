package com.fanglin.fhui;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/8-上午10:34.
 * 功能描述: 自定义EditText
 */
public class FHEditText extends EditText {

    private String limitReg;//限定输入字符的正则表达式

    public FHEditText(Context context) {
        super(context);
        initView(context, null);
    }

    public FHEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FHEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FHEditText);
            if (array != null) {
                limitReg = array.getString(R.styleable.FHEditText_limitInputReg);
                array.recycle();
            }
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        if (connection == null) {
            return null;
        } else {
            return new FHInputConnecttion(connection, false);
        }
    }

    class FHInputConnecttion extends InputConnectionWrapper implements
            InputConnection {

        public FHInputConnecttion(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        /**
         * 对输入的内容进行拦截
         *
         * @param text              CharSequence
         * @param newCursorPosition int
         * @return boolean
         */
        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            if (TextUtils.isEmpty(limitReg)) {
                return super.commitText(text, newCursorPosition);
            } else {
                return text.toString().matches(limitReg) && super.commitText(text, newCursorPosition);
            }
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean setSelection(int start, int end) {
            return super.setSelection(start, end);
        }

    }
}
