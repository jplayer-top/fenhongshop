package cn.sharesdk.fhshare;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import cn.sharesdk.R;
import cn.sharesdk.fhshare.model.FHShareData;
import cn.sharesdk.fhshare.model.PicTag;
import cn.sharesdk.onekeyshare.ShareUtilsByLocal;

/**
 * 多图分享 页面
 * Created by lizhixin on 2015/12/11.
 */
public class FHShareManyPicsActivity extends Activity implements View.OnClickListener {

    private EditText etContent;
    private FHShareManyPicsAdapter adapter;
    private FHShareData item;//分享的数据
    public static final String APPLOGO = "http://www.fenhongshop.com/wap/images/wap/logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fh_share_many_pics);
        initView();
    }

    private void initView() {

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        TextView tvShare = (TextView) findViewById(R.id.tv_share);
        etContent = (EditText) findViewById(R.id.et_content);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        ivBack.setOnClickListener(this);
        tvShare.setOnClickListener(this);

        adapter = new FHShareManyPicsAdapter(this);

        item = (FHShareData) getIntent().getSerializableExtra("FHShareData");

        if (item != null) {
            List<PicTag> list_pics = new ArrayList<>();

            //查看多图分享的数据
            if (item.mul_img != null && item.mul_img.size() > 0) {
                for (int i = 0; i < item.mul_img.size(); i++) {
                    PicTag picTag = new PicTag();
                    picTag.url = item.mul_img.get(i).split("!")[0];//不裁剪
                    list_pics.add(picTag);

                }
            } else {
                PicTag picTag = new PicTag();
                if (!TextUtils.isEmpty(item.imgs) && !item.imgs.contains(",")) {
                    picTag.url = item.imgs.split("!")[0];//不裁剪

                } else {
                    picTag.url = APPLOGO;
                }
                list_pics.add(picTag);

            }

            adapter.setList(list_pics);

            new FHTCNUtils().setCallBack(new FHTCNUtils.tcnCallBack() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd(int status, String data) {
                    String aContent = item.content;
                    if (status == FHTCNUtils.SUCCESS) {
                        aContent += data;
                    } else {
                        aContent += item.url;
                    }
                    etContent.setText(aContent);
                }
            }).getShortUrl(item.url);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivBack) {
            onBackPressed();
        } else if (id == R.id.tv_share) {
            if (item != null) {
                List<String> images = getSelectedPics(adapter.getList());//获取已选择的图片列表
                if (images != null) {
                    File[] files = new File[images.size()];
                    for (int i = 0; i < images.size(); i++) {
                        files[i] = FHShareData.getCacheByUrl(images.get(i));
                    }
                    //判断是否满足分享条件
                    try {
                        if (files.length == 0) {
                            Toast.makeText(FHShareManyPicsActivity.this, "请选择至少一张图片!", Toast.LENGTH_SHORT).show();
                        } else {
                            ShareUtilsByLocal.shareMultiplePictureToTimeLine(this, item.title, etContent.getText().toString(), files);
                        }
                    } catch (Exception e) {
                        Toast.makeText(FHShareManyPicsActivity.this, "请等图片缓冲完成再分享!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private List<String> getSelectedPics(List<PicTag> list) {
        List<String> result = new ArrayList<>();
        for (PicTag picTag : list) {
            if (picTag.isSelected) result.add(picTag.url);
        }
        return result;
    }

}
