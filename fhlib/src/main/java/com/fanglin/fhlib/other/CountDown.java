package com.fanglin.fhlib.other;

import android.os.Handler;

/**
 * Created by Plucky on 2015/10/7.
 * TODO 即将被删掉
 */
public class CountDown {
    private Handler handler;
    private long current;
    private static final long DEFFRE = 1000;//默认频次为1000ms
    private CountDownListener listener;

    public CountDown(long timestamp) {
        this.current = timestamp;
        handler = new Handler();
    }

    public void start(final CountDownListener listener) {
        this.listener = listener;
        if (listener != null) listener.onChange(current);//为了一开始就显示而不延迟一秒
        innerfunc();
    }

    /*递归至结束*/
    private void innerfunc() {
        if (handler == null) return;
        handler.postDelayed(task, DEFFRE);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (current == 0) {
                if (listener != null) listener.onStop();
                return;
            }
            if (listener != null) listener.onChange(current--);
            innerfunc();
        }
    };

    public void cancel() {
        if (handler != null) {
            handler.removeCallbacks(task);
            task = null;
            handler = null;
        }
    }

    public interface CountDownListener {
        void onChange(long atime);

        void onStop();
    }
}
