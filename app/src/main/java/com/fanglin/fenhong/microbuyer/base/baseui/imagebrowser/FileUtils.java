package com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * SD文件操作类
 *
 * @author WangYunxiu
 */
public class FileUtils {

    public static String DOWNLOAD_PATH = "/FHAPP/download";
    public static String CACHE_PATH = "/fhapp_cache";

    /**
     * 保存文件
     *
     * @param bm Bitmap
     * @param fileName String
     * @throws IOException Exception
     */
    public static void saveBitmapToSD(Bitmap bm, String pathName, String fileName) throws IOException {
        File dirFile = new File(pathName);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(pathName + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static void saveNetBitmapToSD(String imgurl, String pathName, String fileName, final OnNetBitmapSaveListener onNetBitmapSaveListener, final boolean ispng) throws Exception {
        final String imgurl_f = imgurl;
        final String pathName_f = pathName;
        final String fileName_f = fileName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onNetBitmapSaveListener != null) {
                    onNetBitmapSaveListener.onStart();
                }
                try {
                    URL url = new URL(imgurl_f);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.connect();
                    InputStream inputStream = con.getInputStream();
                    Bitmap mbitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    File dirFile = new File(pathName_f);
                    if (!dirFile.exists()) {
                        dirFile.mkdir();
                    }
                    File myCaptureFile = new File(pathName_f + "/" + fileName_f);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    if (ispng) mbitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    else mbitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                    if (onNetBitmapSaveListener != null) {
                        onNetBitmapSaveListener.onFinished(pathName_f + "/" + fileName_f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (onNetBitmapSaveListener != null) {
                        onNetBitmapSaveListener.onFaild(e);
                    }
                }
            }
        }).start();
    }

    public interface OnNetBitmapSaveListener {
        void onStart();

        void onFinished(String filename);

        void onFaild(Exception e);
    }

    /**
     * 是否存在内存卡判断
     *
     * @return
     */
    public static boolean existSDcard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else return false;
    }

    public static String getDataPath(Context context) {
        File file;
        if (existSDcard()) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }
        if (file == null) return "";
        return file.getPath();
    }

    public static String getCachePath(Context context) {
        String path = getDataPath(context) + CACHE_PATH + "/";
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        return path;
    }

    public static String getImageDownloadPath() {
        if (FileUtils.existSDcard()) {
            String path = Environment.getExternalStorageDirectory() + DOWNLOAD_PATH + "/";
            File file = new File(path);
            if (!file.exists()) file.mkdirs();
            return path;
        } else {
            return "";
        }
    }

    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    public static void cleanCache(Context context) {
        deleteFilesByDirectory(new File(getCachePath(context)));
    }


    public static boolean isConnnected(Context c) {
        if (c != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void BrowserOpenL(Context context, List<String> image_list, String curimg) {
        String img_list = "";
        for (int i = 0; i < image_list.size(); i++) {
            img_list += image_list.get(i);
            if (i != image_list.size() - 1) img_list += "#";
        }
        BrowserOpenS(context, img_list, curimg);
    }

    public static void BrowserOpenS(Context context, String imglist, String curimg) {
        Intent intent = new Intent();
        intent.setClass(context, ImageBrowserActivity.class);
        intent.putExtra("imglist", imglist);
        intent.putExtra("image", curimg);
        context.startActivity(intent);
    }


    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
