package com.fanglin.fhlib.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/10/28-下午2:56.
 * 功能描述: 保存界面至SDCard
 */
public class SaveViewUtil {

    public static final int SUCCESS = 0x001;
    public static final int ERROR = 0x002;

    private Bitmap bitmap;
    private File imageFile;
    private Context mContext;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    FHLib.refreshMedia(mContext, imageFile);
                    if (listener != null) {
                        listener.onSaveSuccess(imageFile);
                    }
                    break;
                case ERROR:
                    if (listener != null) {
                        listener.onSaveError();
                    }
                    break;
            }
        }
    };

    public SaveViewUtil(View view, String filepath) {

        if (view != null) {
            mContext = view.getContext();
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(bitmap));
        }

        if (!TextUtils.isEmpty(filepath)) {
            imageFile = new File(filepath);
        }
    }

    public void save() {
        if (bitmap == null || imageFile == null) {
            handler.sendEmptyMessage(ERROR);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(imageFile));
                    handler.sendEmptyMessage(SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(ERROR);
                }
            }
        }).start();
    }

    private SaveViewListener listener;

    public SaveViewUtil setListener(SaveViewListener listener) {
        this.listener = listener;
        return this;
    }

    public interface SaveViewListener {
        void onSaveSuccess(File file);

        void onSaveError();
    }
}
