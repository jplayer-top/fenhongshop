package cn.sharesdk.onekeyshare;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Plucky on 2015/7/24.
 * Plucky
 */
public class ShareUtilsByLocal {


    /**
     * 分享多图+文字至朋友圈
     *
     * @param mContext Context
     * @param title    String
     * @param content  String
     * @param files    File
     */
    public static void shareMultiplePictureToTimeLine(Context mContext,
                                                      String title, String content, File... files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", content);// 至关重要

        ArrayList<Uri> imageUris = new ArrayList<>();
        for (File f : files) {
            if (Uri.fromFile(f) != null) {
                imageUris.add(Uri.fromFile(f));
            }
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        mContext.startActivity(intent);
        // 结束
        ((Activity) mContext).finish();
    }
}
