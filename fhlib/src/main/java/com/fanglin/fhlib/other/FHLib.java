package com.fanglin.fhlib.other;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 分红全球购基础静态类库
 * Created by Plucky on 2015/8/4.
 */
public class FHLib {
    public static final int TRANSLATE_DURATION = 150;

    public static boolean isMainProcess(Context c) {
        ActivityManager am = ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = c.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 0 网络未连接 1 手机网络 2 WIFI
     */
    public static int isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return 0;
        }

        if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return 2;
        } else {
            return 1;
        }
    }

    public static boolean isEmail(String addr) {
        String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
        return addr.matches(regex);
    }

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";

        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static Animation createTranslationInAnimation(int style) {
        int type = Animation.RELATIVE_TO_SELF;
        TranslateAnimation an;
        if (style == 0) {
            an = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        } else {
            an = new TranslateAnimation(type, 1, type, 0, type, 0, type, 0);
        }

        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    public static Animation createTranslationOutAnimation(int style) {
        int type = Animation.RELATIVE_TO_SELF;
        TranslateAnimation an;
        if (style == 0) {
            an = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        } else {
            an = new TranslateAnimation(type, 0, type, 1, type, 0, type, 0);
        }

        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    /**
     * 判断是否安装了某个应用
     */
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) return true;
        }
        return false;
    }

    public static String getTimeStrByTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date(timestamp * 1000);
        return sdf.format(date);
    }

    public static String getTimeStrByTimestampDay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date(timestamp * 1000);
        return sdf.format(date);
    }

    public static void EnableViewGroup(ViewGroup vg, boolean flag) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View view = vg.getChildAt(i);
            view.setEnabled(flag);
            // 如果该控件本身也是一个视图容器的话,则递归
            if (view instanceof ViewGroup) {
                EnableViewGroup((ViewGroup) view, flag);
            }
        }
    }

    public static String getFriendlyTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;
        String timeStr;
        if (timeGap > 24 * 60 * 60) {
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60) {
            timeStr = timeGap / 60 + "分钟前";
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }

    public static String getFriendlySize(long size) {
        if (size < 0) size = 0;
        DecimalFormat df = new DecimalFormat("#0.00");
        if (size < 1024) {
            return df.format(size) + "B";
        }
        if (size < Math.pow(1024, 2)) {
            return df.format(size / Math.pow(1024, 1)) + "KB";
        }

        if (size < Math.pow(1024, 3)) {
            return df.format(size / Math.pow(1024, 2)) + "MB";
        }

        if (size < Math.pow(1024, 4)) {
            return df.format(size / Math.pow(1024, 3)) + "GB";
        }

        /** 暂时只友好到TB*/
        return df.format(size / Math.pow(1024, 4)) + "TB";
    }

    /**
     * 打开浏览器
     */
    public static void openBrowser(Context c, String Url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        c.startActivity(i);
    }

    /**
     * 判断当前Activity 是不是处于前台
     */
    public static boolean isAppOnForeground(Context c) {
        try {
            ActivityManager mActivityManager = ((ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningTaskInfo> taskinfos = mActivityManager.getRunningTasks(1);
            return taskinfos.size() > 0 && TextUtils.equals(c.getPackageName(), taskinfos.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            return false;
        }
    }

    public static PackageInfo getPkgInfo(Context c) {
        PackageManager pm = c.getPackageManager();
        try {
            return pm.getPackageInfo(c.getPackageName(), 0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取设备编号
     */
    public static String getDeviceID(Context c) {
        final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取剪切板管理器 API 11之后
     */
    public static android.content.ClipboardManager getClipBoardManagerOfContent(Context context) {
        return (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 获取剪切板管理器 API 11之前
     */
    public static android.text.ClipboardManager getClipBoardManagerOfText(Context context) {
        return (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 复制内容
     */
    public static void copy(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (Build.VERSION.SDK_INT > 11) {
                getClipBoardManagerOfContent(context).setPrimaryClip(ClipData.newPlainText(null, str.trim()));
            } else {
                getClipBoardManagerOfText(context).setText(str.trim());
            }
        }
    }

    /**
     * 粘贴接剪切板中的内容
     */
    public static String paste(Context context) {
        if (Build.VERSION.SDK_INT > 11) {
            return getClipBoardManagerOfContent(context).getPrimaryClip().toString();
        } else {
            return getClipBoardManagerOfText(context).getText().toString();
        }
    }


    public static String getMetaData(Context context, String key) {
        String pkgname = context.getPackageName();
        String res;
        try {
            ApplicationInfo appinfo = context.getPackageManager().getApplicationInfo(pkgname, PackageManager.GET_META_DATA);
            res = appinfo.metaData.getString(key);
        } catch (Exception e) {
            res = null;
        }

        return res;
    }

    /**
     * 格式化时间戳
     *
     * @param timeStamp long
     * @param fmt       String  yyyy-MM-dd HH:mm
     * @return String
     */
    public static String formatTimestamp(long timeStamp, String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(TextUtils.isEmpty(fmt) ? "yyyy-MM-dd HH:mm" : fmt, Locale.CHINA);
        Date date = new Date(timeStamp * 1000);
        return sdf.format(date);
    }

    /**
     * 刷新图库
     *
     * @param mContext  Context
     * @param imageFile File
     */
    public static void refreshMedia(Context mContext, File imageFile) {
        if (mContext == null || imageFile == null) return;
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);

    }

    /**
     * 将Layout保存图片至SDCard
     *
     * @param view View
     */
    public static void saveView(final View view, String filepath) {
        if (view != null) {
            final Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(bmp));
            final File file = new File(filepath);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                        FHLib.refreshMedia(view.getContext(), file);
                    } catch (Exception e) {
                        //
                    }
                }
            }).start();
        }
    }

    /**
     * <p>
     * 解压压缩包
     * </p>
     *
     * @param zipFilePath 压缩文件路径
     * @param destDir     压缩包释放目录
     * @throws Exception
     */
    public static void unZip(String zipFilePath, String destDir) throws Exception {
        /**
         * 使用GBK编码可以避免压缩中文文件名乱码
         */
        String CHINESE_CHARSET = "GBK";

        /**
         * 文件读取缓冲区大小
         */
        int CACHE_SIZE = 1024;

        ZipFile zipFile = new ZipFile(zipFilePath);
        Enumeration<?> emu = zipFile.entries();
        BufferedInputStream bis;
        FileOutputStream fos;
        BufferedOutputStream bos;
        File file, parentFile;
        ZipEntry entry;
        byte[] cache = new byte[CACHE_SIZE];
        while (emu.hasMoreElements()) {
            entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(destDir + entry.getName()).mkdirs();
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            file = new File(destDir + entry.getName());
            parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists())) {
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, CACHE_SIZE);
            int nRead;
            while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                fos.write(cache, 0, nRead);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
        zipFile.close();
    }

    /**
     * Bitmap转换成字节数组
     *
     * @param bitmap Bitmap
     * @return byte[]
     */
    public static byte[] bitmap2ByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            bitmap.recycle();
            byte[] bytes = outputStream.toByteArray();
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bytes;
        } else {
            return null;
        }
    }

    public static boolean isJson(String jsonstr){
        if (TextUtils.isEmpty(jsonstr)) return false;
        String regex = "^\\{(.+:.+,*){1,}\\}$";
        return jsonstr.matches(regex);
    }

}
