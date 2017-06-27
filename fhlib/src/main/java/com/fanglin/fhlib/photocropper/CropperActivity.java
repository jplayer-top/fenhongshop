package com.fanglin.fhlib.photocropper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanglin.fhlib.other.FHLib;
import com.fanglin.fhlib.R;
import com.fanglin.fhlib.other.FHLog;

/**
 * Created by plucky on 15-10-1.
 * #
 */
public class CropperActivity extends BasePhotoCropActivity implements View.OnClickListener {


    CropParams mCropParams;
    Animation animIn, animOut;
    LinearLayout LBOT;
    TextView tv_photo, tv_camera, tv_close;
    View VOutSide;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        LBOT = (LinearLayout) findViewById(R.id.LBOT);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        tv_camera = (TextView) findViewById(R.id.tv_camera);
        tv_close = (TextView) findViewById(R.id.tv_close);
        VOutSide = findViewById(R.id.VOutSide);

        tv_photo.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        VOutSide.setOnClickListener(this);

        mCropParams = new CropParams(this);//初始化裁剪参数

        String strCrop = getIntent().getStringExtra("CROP");
        if (TextUtils.equals("true", strCrop)) {
            mCropParams.crop = "true";
        } else {
            mCropParams.crop = "false";
        }

        mCropParams.outputX = getIntent().getIntExtra("OUTPUTX", 600);
        mCropParams.outputY = getIntent().getIntExtra("OUTPUTY", 600);

        mCropParams.aspectX = getIntent().getIntExtra("ASPECTX", 1);
        mCropParams.aspectY = getIntent().getIntExtra("ASPECTY", 1);

        initView();
    }

    private void initView() {
        animIn = FHLib.createTranslationInAnimation(0);
        animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LBOT.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LBOT.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        LBOT.startAnimation(animIn);
    }

    @Override
    public void onBackPressed() {
        animOut = FHLib.createTranslationOutAnimation(0);
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent ii = new Intent();
                if (uri != null) {
                    ii.putExtra("VAL", uri);
                    setResult(RESULT_OK, ii);
                } else {
                    setResult(RESULT_CANCELED, ii);
                }
                CropperActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        LBOT.startAnimation(animOut);
    }

    @Override
    public void onFailed(String message) {
        super.onFailed(message);
        uri = null;
        FHLog.e("lizx->onCropFailed", message);
        Toast.makeText(this, "裁剪失败", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void onCancel() {
        super.onCancel();
        uri = null;
        Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        super.onPhotoCropped(uri);
        this.uri = uri;
        Toast.makeText(this, "裁剪成功", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onClick(View v) {
        mCropParams.refreshUri();
        if (v.getId() == R.id.tv_photo) {
            CropHelper.clearCachedCropFile(mCropParams.uri);
            startActivityForResult(CropHelper.buildGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);
            return;
        }

        if (v.getId() == R.id.tv_camera) {
            Intent intent = CropHelper.buildCameraIntent(mCropParams);
            startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
            return;
        }

        if (v.getId() == R.id.tv_close || v.getId() == R.id.VOutSide) {
            uri = null;
            onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        if (mCropParams != null) CropHelper.clearCachedCropFile(mCropParams.uri);
        super.onDestroy();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        super.handleIntent(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCompressed(Uri uri) {
        super.onCompressed(uri);
    }
}
