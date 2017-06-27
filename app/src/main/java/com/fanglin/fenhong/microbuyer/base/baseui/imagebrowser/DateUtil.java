package com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间/日历 处理
 *
 * @author WangYunxiu
 */
public class DateUtil {
    /**
     * 返回当前系统时间String
     *
     * 年-月-日 时:分:秒
     * WangYunxiu
     */
    public static String getCurTimeString() {
        String timeString = "";
        Calendar c = Calendar.getInstance();
        timeString += c.get(Calendar.YEAR);
        timeString += "-";
        timeString += c.get(Calendar.MONTH);
        timeString += "-";
        timeString += c.get(Calendar.DATE);
        timeString += " ";
        timeString += c.get(Calendar.HOUR);
        timeString += ":";
        timeString += c.get(Calendar.MINUTE);
        timeString += ":";
        timeString += c.get(Calendar.SECOND);
        return timeString;
    }

    public static String getCurTimeMuleString() {
        return System.currentTimeMillis() + "";
    }

    /**
     * 时间转换
     *
     * @param time 时间戳 (如果传的是10位，则会处理成13位)
     * @param type 装换类型1、MM-dd HH:mm 2、MM月dd日 HH:mm 3、yyyy-MM-dd HH:mm:ss 4、HH:mm
     *             5、yyyy年MM月dd日 HH:mm 4、HH:mm"
     * @return String
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDate(long time, int type) {
        long t = 1000000000000L;
        if (time - t < 0) {
            time = time * 1000;
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        switch (type) {
            case 1:
                format = new SimpleDateFormat("MM-dd HH:mm");
                break;
            case 2:
                Calendar oldc = Calendar.getInstance();
                oldc.setTimeInMillis(time);
                Calendar newc = Calendar.getInstance();
                if (oldc.get(Calendar.YEAR) < newc.get(Calendar.YEAR)) {
                    format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                } else {
                    format = new SimpleDateFormat("MM月dd日 HH:mm");
                }
                break;
            case 3:
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case 4:
                format = new SimpleDateFormat("HH:mm");
                break;
            case 5:
                format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                break;
            case 6:
                format = new SimpleDateFormat("yyyy年MM月dd日");
                break;
            case 7:
                format = new SimpleDateFormat("yyyy-MM-dd");
                break;
            default:
                break;
        }

        return format.format(time);
    }
}