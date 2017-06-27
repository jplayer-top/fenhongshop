package com.fanglin.fhlib.ypyun;

import android.os.AsyncTask;

import com.fanglin.fhlib.ypyun.api.UpYunException;
import com.fanglin.fhlib.ypyun.api.UpYunUtils;
import com.fanglin.fhlib.ypyun.api.Uploader;

import java.io.File;

/**
 * @category 又拍云上传工具
 * @author plucky
 */
public class UpyunUploader {

    public static final String DEFAULT_DOMAIN = "http://img.fenhongshop.com";// 空间域名
    public static final String TEST_API_KEY = "Wacwz2TsYxfjllh2ixSsMVR5PFY="; // 测试使用的表单api验证密钥
    public static final long EXPIRATION = System.currentTimeMillis () / 1000 + 1000 * 5 * 10; // 过期时间，必须大于当前时间
    public static final String BUCKET_NAME = "fhimg";

    private String SAMPLE_PIC_FILE = "";// 当前上传文件

    private String member_id = "default";

    public UpyunUploader (String mid) {
        this.member_id = mid;
    }

    public void upload () {
        if (SAMPLE_PIC_FILE != null && !SAMPLE_PIC_FILE.equals ("")) {
            new UploadTask ().execute (SAMPLE_PIC_FILE);
        }
    }

    /** 设置上传文件 */
    public UpyunUploader setUploadFile (String uploadFile) {
        this.SAMPLE_PIC_FILE = uploadFile;
        return this;
    }


    public class UploadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute () {
            super.onPreExecute ();
            if (mcb != null) mcb.startLoading ();
        }

        @Override
        protected String doInBackground (String... params) {
            String string = null;
            try {
                // 设置服务器上保存文件的目录和文件名，如果服务器上同目录下已经有同名文件会被自动覆盖的。
                // 上传文件路径规则: /weishop/member_id/yyyyMMdd/
                // 文件名 member_id_current_avatar 自动覆盖
                String SAVE_KEY = File.separator + "weishop" + File.separator + member_id + File.separator + UpYunUtils.FormatCurDate ("yyyyMMdd") + File.separator + Long.toString (System.currentTimeMillis () / 1000) + ".jpg";

                // 取得base64编码后的policy
                String policy = UpYunUtils.makePolicy (SAVE_KEY, EXPIRATION, BUCKET_NAME);

                // 根据表单api签名密钥对policy进行签名
                // 通常我们建议这一步在用户自己的服务器上进行，并通过http请求取得签名后的结果。
                String signature = UpYunUtils.signature (policy + "&" + TEST_API_KEY);

                // 上传文件到对应的bucket中去。
                string = DEFAULT_DOMAIN + Uploader.upload (policy, signature, BUCKET_NAME, params[0]);
            } catch (UpYunException e) {
                e.printStackTrace ();
            }

            return string;
        }

        @Override
        protected void onPostExecute (String result) {
            super.onPostExecute (result);
            if (mcb != null) mcb.endLoading (result != null, result);
        }

    }


    private UpYunCallBack mcb;

    public UpyunUploader setUpYunCallBack (UpYunCallBack cb) {
        this.mcb = cb;
        return this;
    }

    public interface UpYunCallBack {
        void startLoading ();

        void endLoading (boolean isSuccess, String data);
    }
}
