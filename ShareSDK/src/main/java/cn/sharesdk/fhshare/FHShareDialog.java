package cn.sharesdk.fhshare;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import cn.sharesdk.R;
import cn.sharesdk.fhshare.model.FHShareItem;

/**
 * 分享 对话框
 * Created by lizhixin on 2015/12/9.
 */
public class FHShareDialog extends Dialog {

    private FrameLayout view;
    private LinearLayout LTitle, llBody;
    private RecyclerView recyclerView;
    private FHShareAdapter adapter;

    private Animation animIn;
    private Animation animOut;

    public FHShareDialog(Context context, List<FHShareItem> list) {
        super(context, R.style.FHDialog);
        view = (FrameLayout) View.inflate(context, R.layout.layout_fh_share, null);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window window = getWindow();
        //WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);

        LTitle = (LinearLayout) view.findViewById(R.id.LTitle);
        llBody = (LinearLayout) view.findViewById(R.id.ll_body);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FHShareAdapter(context);
        adapter.setTypeface(Typeface.createFromAsset(context.getAssets(), "iconfont.ttf"));
        adapter.setList(list);
        recyclerView.setAdapter(adapter);

        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        animIn = AnimationUtils.loadAnimation(context, R.anim.fhshare_dialog_in);
        animOut = AnimationUtils.loadAnimation(context, R.anim.fhshare_dialog_out);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void show() {
        super.show();
        llBody.startAnimation(animIn);
    }

    public void close() {
        llBody.startAnimation(animOut);
    }

    /** 判断是否安装了某个应用*/
    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) return true;
        }
        return false;
    }

    /**
     * 设置自定义头部
     */
    public void setTitleView(View view) {
        LTitle.removeAllViews();
        LTitle.addView(view);
    }

}
