package com.fanglin.fenhong.microbuyer.common;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.fanglin.fenhong.microbuyer.base.model.UpdateVersion;
import com.fanglin.fhlib.other.FHLog;
import com.fanglin.fhlib.other.OpenFile;
import com.fanglin.fhui.FHDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import java.io.File;

/**
 * 作者： Created by Plucky on 2015/11/1.
 * modify by lizhixin on 2016/03/30 添加强制更新控制的逻辑
 */
public class LayoutUpdate implements View.OnClickListener {
    private UpdateVersion updateVersion;
    private Context mContext;
    public FHDialog dlg;
    private TextView tv_ok;
    private ProgressBar number_progress_bar;
    private HttpUtils http;
    private HttpHandler handler;
    private boolean isForceUpdate;//是否是强制更新APP
    public boolean isCancel = true;//是否点击取消，包括dialog的取消按钮 + 手机后退键两种
    private String localname;
    private final String INSTALL = "安装";

    public LayoutUpdate(Context c, UpdateVersion updateVersion) {
        this.mContext = c;
        this.updateVersion = updateVersion;
        http = new HttpUtils();
        View view = View.inflate(c, R.layout.layout_update, null);
        int w = BaseFunc.getDisplayMetrics(c).widthPixels;
        int h = BaseFunc.getDisplayMetrics(c).heightPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        view.setLayoutParams(params);

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        number_progress_bar = (ProgressBar) view.findViewById(R.id.number_progress_bar);
        String fmt = c.getString(R.string.title_update_fmt);
        if (updateVersion != null) {
            tv_title.setText(String.format(fmt, updateVersion.version_name));
            tv_content.setText(updateVersion.update_log);

            if (BaseFunc.isValidUrl(updateVersion.app_url)) {
                tv_ok.setEnabled(true);
            } else {
                tv_ok.setEnabled(false);
            }
        }

        tv_ok.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        isForceUpdate = (updateVersion != null) && TextUtils.equals(updateVersion.if_compulsory, "1");//是否强制更新 （1 是 0 否）

        localname = String.format(BaseVar.LOCALAPPNAMEFMT, FHLog.getAppPath(), updateVersion != null ? updateVersion.version_code : 10240);

        dlg = new FHDialog(c);
        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //当要求强制更新时，如果户取消更新，需要强制退出APP
                if (isForceUpdate && isCancel && exitAppListener != null) {
                    exitAppListener.onExitApp();
                }
            }
        });

        //强制升级时不允许返回按钮
        dlg.setCancelable(!isForceUpdate);

        dlg.setBotView(view, 2);
    }

    public void show() {
        tv_ok.setText(R.string.update);
        dlg.show();
    }

    public void dismiss() {
        if (handler != null) handler.cancel();
        dlg.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                isCancel = true;
                dismiss();
                break;
            case R.id.tv_ok:
                if (TextUtils.equals(INSTALL, tv_ok.getText().toString())) {
                    File file = new File(localname);
                    if (file.exists()) {
                        OpenFile.Open(mContext, localname);
                    }
                } else {
                    isCancel = false;
                    download();
                }
                break;
        }
    }

    private void download() {
        if (updateVersion == null) return;
        if (!BaseFunc.isValidUrl(updateVersion.app_url)) return;
        /**
         * 这里判断如果已经下载过的话，则删除，重新下载
         * 而不采用提示已经下载然后让用户去自己的目录下找到并手动安装的方式，有的人找不到或者不愿意去找。
         * modify lizhixin on 2016/04/18
         */
        File file = new File(localname);
        if (file.exists()) {
            file.delete();
        }

        tv_ok.setEnabled(false);
        handler = http.download(updateVersion.app_url, localname, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        number_progress_bar.setVisibility(View.VISIBLE);
                        tv_ok.setEnabled(false);
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        if (total > 0) {
                            number_progress_bar.setProgress((int) (current * 100 / total));
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        number_progress_bar.setVisibility(View.GONE);
                        tv_ok.setEnabled(true);
                        tv_ok.setText(INSTALL);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        number_progress_bar.setVisibility(View.GONE);
                        BaseFunc.showMsg(mContext, mContext.getString(R.string.download_error));
                        tv_ok.setEnabled(true);
                    }
                });
    }

    private ExitAppListener exitAppListener;

    public void setExitAppListener(ExitAppListener exitAppListener) {
        this.exitAppListener = exitAppListener;
    }

    public interface ExitAppListener {
        void onExitApp();
    }
}
