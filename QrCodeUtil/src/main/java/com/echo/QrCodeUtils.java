package com.echo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.echo.zxing.activity.CaptureActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 2016/11/1-上午10:17.
 * 功能描述: 二维码工具类 基于zxing
 */
public class QrCodeUtils {
    public static final int QR_WIDTH = 200;
    public static final int QR_HEIGHT = 200;
    public static final String QRTEXT = "KEY_QRTEXT";


    /**
     * 创建二维码
     *
     * @param text   String
     * @param width  int
     * @param height int
     * @return Bitmap
     */
    public static Bitmap createQrImage(String text, int width, int height) {
        if (width <= 0) width = QR_WIDTH;
        if (height <= 0) height = QR_HEIGHT;
        try {
            if (TextUtils.isEmpty(text)) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);//去除白边

            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //显示到一个ImageView上面
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 扫描二维码
     *
     * @param activity    Activity
     * @param requestCode int
     */
    public static void qrScan4Result(Activity activity, int requestCode) {
        if (activity == null) return;
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
