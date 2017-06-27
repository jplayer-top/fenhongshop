package com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanglin.fenhong.microbuyer.R;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.GalleryWidget.BasePagerAdapter;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.GalleryWidget.GalleryViewPager;
import com.fanglin.fenhong.microbuyer.base.baseui.imagebrowser.GalleryWidget.UrlPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageBrowserActivity extends Activity {

    private TextView tv_pagecounter;// 计数器
    private ArrayList<String> array_url = new ArrayList<>();
    private int currentIndex = 0;// 当前页面序号


    private void initParam() {
        Intent intent = getIntent();
        String imglist = intent.getStringExtra("imglist");// 获取需要显示的图片列表
        String curImg = intent.getStringExtra("image");// 当前显示的图片

        if (imglist == null)
            imglist = "";

        String[] astr_tmp = imglist.split("#");

        if (curImg != null) {
            if (!TextUtils.isEmpty(imglist)) {
                // 如果当前图片包含在图片列表中
                for (int i = 0; i < astr_tmp.length; i++) {
                    if (curImg.trim().equals(astr_tmp[i].trim())) {
                        currentIndex = i;// 当前点击的图片在图片列表中的位置
                    }
                    array_url.clear();
                    for (int j = 0; j < astr_tmp.length; j++) {
                        array_url.add(astr_tmp[j]);
                    }

                    if (imglist.contains(curImg)) {
                        if (curImg.equals(astr_tmp[i].trim())) {
                            currentIndex = i;// 当前点击的图片在图片列表中的位置
                        }
                        array_url.clear();
                        for (int j = 0; j < astr_tmp.length; j++) {
                            array_url.add(astr_tmp[j]);
                        }
                    } else {
                        currentIndex = 1;
                        array_url.clear();
                        for (int j = 0; j < astr_tmp.length; j++) {
                            array_url.add(astr_tmp[j]);
                        }

                    }
                }
            } else {
                array_url.clear();
                array_url.add(curImg);
                currentIndex = 0;
            }
        } else {
            currentIndex = 0;
            array_url.clear();
            for (int j = 0; j < astr_tmp.length; j++) {
                array_url.add(astr_tmp[j]);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fhui_imagebrowser);
        tv_pagecounter = (TextView) findViewById(R.id.tv_pagecounter);
        ImageView iv_save = (ImageView) findViewById(R.id.ib_iv_save);
        iv_save.setOnClickListener(new MClickListener());
        initParam();

        String[] urls = new String[array_url.size()];
        for (int i = 0; i < array_url.size(); i++) {
            urls[i] = array_url.get(i);
        }
        List<String> items = new ArrayList<String>();
        Collections.addAll(items, urls);

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                currentIndex = currentPosition;
                tv_pagecounter.setText(currentPosition + 1 + "/"
                        + array_url.size());
            }

            @Override
            public void onItemClick(int currentPosition) {
                finish();
            }
        });

        GalleryViewPager mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(currentIndex);// 初始化显示图片位置

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 保存按钮响应
     */
    class MClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ib_iv_save) {
                if (FileUtils.existSDcard()) {
                    // 存在SD卡，开始储存文件
                    try {
                        String filename = DateUtil.getCurTimeMuleString()
                                + ".jpg";
                        FileUtils.saveNetBitmapToSD(
                                array_url.get(currentIndex),
                                FileUtils.getImageDownloadPath(), filename, null, array_url.get(currentIndex).endsWith(".png"));
                        Toast.makeText(ImageBrowserActivity.this, "已保存:" + FileUtils.getImageDownloadPath() + filename, Toast.LENGTH_SHORT).show();
                        scanFileAsync(ImageBrowserActivity.this, FileUtils.getImageDownloadPath() + filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ImageBrowserActivity.this, "保存失败!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ImageBrowserActivity.this, "SD卡不存在", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void scanFileAsync(Context ctx, String filePath) {
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(Uri.fromFile(new File(filePath)));
            ctx.sendBroadcast(scanIntent);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
