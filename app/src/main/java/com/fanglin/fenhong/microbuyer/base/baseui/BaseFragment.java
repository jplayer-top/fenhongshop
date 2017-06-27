package com.fanglin.fenhong.microbuyer.base.baseui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fanglin.fenhong.microbuyer.base.baselib.BaseFunc;
import com.fanglin.fenhong.microbuyer.base.baselib.FHCache;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fenhong.microbuyer.base.model.MessageNum;

/**
 * 基础FragMent
 * Created by Plucky on 15-9-30.
 */
public class BaseFragment extends Fragment {
    public Member member;
    public String member_id;
    public MessageNum msgnum;
    public BaseFragmentActivity act;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = (BaseFragmentActivity) getActivity();
        refreshBaseData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshBaseData();
    }

    private void refreshBaseData() {
        member = FHCache.getMember(act);
        msgnum = FHCache.getNum(act);
        member_id = member != null ? member.member_id : null;
    }


    public int getChannelId(String url) {
        int mchannel = 0;
        if (BaseFunc.isValidUrl(url)) {
            url = BaseFunc.formatHtml(url);
            Uri uri = Uri.parse(url);
            if (uri != null) {
                String themeTypeStr = uri.getQueryParameter("channel_id");
                if (BaseFunc.isInteger(themeTypeStr))
                    mchannel = Integer.valueOf(themeTypeStr);
            }
        }
        return mchannel;
    }
}
