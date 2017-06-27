package com.fanglin.fhlib.other;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.fanglin.fhlib.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by plucky on 15-11-2.
 * 以下是原作者 只是MyLog 实在太Low
 * Change by Oblivion --->add s
 */

/**
 * Android开发调试日志工具类[支持保存到SD卡]<br>
 * <br>
 * <p/>
 * 需要一些权限: <br>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> <br>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" /><br>
 *
 * @author PMTOAM
 */
public class FHLog {
    public static final String APPPATH = "FHApp";
    public static final String LOGPATH = APPPATH + "/log/";

    public static boolean isSaveDebugInfo = true;// 是否保存调试日志
    public static boolean isSaveCrashInfo = true;// 是否保存报错日志

    public static void v(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, "--> " + msg);
        }
    }

    public static void d(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "--> " + msg);
        }
    }

    public static void i(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, "--> " + msg);
        }
    }

    public static void w(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, "--> " + msg);
        }
    }

    /**
     * add System.out.println(msg);
     * @param msg
     */
    public static void s(final String msg) {
        if (BuildConfig.DEBUG) {
            System.out.println(msg);
        }
    }

    /**
     * 调试日志，便于开发跟踪。
     *
     * @param tag String
     * @param msg String
     */
    public static void e(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "--> " + msg);
        }

        if (isSaveDebugInfo) {
            new Thread() {
                public void run() {
                    write(time() + tag + " --> " + msg + "\n");
                }
            }.start();
        }
    }

    /**
     * 记录调试日志,非打印错误,友盟会记录上传e的信息
     *
     * @param tag String
     * @param msg String
     */
    public static void record(final String tag, final String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "--> " + msg);
        }

        if (isSaveDebugInfo) {
            new Thread() {
                public void run() {
                    write(time() + tag + " --> " + msg + "\n");
                }
            }.start();
        }
    }

    /**
     * try catch 时使用，上线产品可上传反馈。
     *
     * @param tag String
     * @param tr  String
     */
    public static void e(final String tag, final Throwable tr) {
        if (isSaveCrashInfo) {
            new Thread() {
                public void run() {
                    write(time() + tag + " [CRASH] --> " + getStackTraceString(tr) + "\n");
                }

                ;
            }.start();
        }
    }

    /**
     * 获取捕捉到的异常的字符串
     *
     * @param tr Throwable
     * @return String
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 标识每条日志产生的时间
     *
     * @return String
     */
    private static String time() {
        return "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis())) + "] ";
    }

    /**
     * 以年月日作为日志文件名称
     *
     * @return String
     */
    private static String date() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(System.currentTimeMillis()));
    }

    /**
     * 保存到日志文件
     *
     * @param content String
     */
    public static synchronized void write(String content) {
        try {
            FileWriter writer = new FileWriter(getFile(), true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志文件路径
     *
     * @return String
     */
    public static String getFile() {
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + LOGPATH);
        if (!cacheDir.exists()) cacheDir.mkdir();

        File filePath = new File(cacheDir + File.separator + date() + ".txt");

        return filePath.toString();
    }

    public static String getAppPath() {
        return getAppPath("");
    }

    public static String getAppPath(String basePath) {
        String apath = TextUtils.isEmpty(basePath) ? APPPATH : basePath;
        File sdDir = null;

        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            sdDir = Environment.getExternalStorageDirectory();

        File cacheDir = new File(sdDir + File.separator + apath);
        if (!cacheDir.exists()) cacheDir.mkdir();

        return cacheDir.toString();
    }
}
