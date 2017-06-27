package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 首页头部搜索文字提示
 * Created by lizhixin on 2016/2/4.
 */
public class WSDefaultSearchText extends APIUtil {

    public WSDefaultSearchText() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {
            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                if (mcb != null) {
                    if (isSuccess && !TextUtils.isEmpty(data)) {
                        mcb.onDefaultSearchText(data);
                    } else {
                        mcb.onDefaultSearchText("分红全球购");
                    }
                }
            }
        });
    }

    public void getDefaultSearchText() {
        String url = BaseVar.API_DEFAULT_SEARCH_HINT;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    private WSDefaultSearchTextModelCallBack mcb;

    public void setWSDefaultSearchText(WSDefaultSearchTextModelCallBack cb) {
        this.mcb = cb;
    }

    public interface WSDefaultSearchTextModelCallBack {
        void onDefaultSearchText(String str);
    }
}
