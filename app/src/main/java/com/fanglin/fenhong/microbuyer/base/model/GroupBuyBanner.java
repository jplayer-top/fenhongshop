package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 2015/10/8.
 */
public class GroupBuyBanner extends APIUtil {
    public String img;
    public String url;


    public GroupBuyBanner () {
        super ();
        setCallBack (new FHAPICallBack () {
            @Override
            public void onStart (String data) {

            }

            @Override
            public void onEnd (boolean isSuccess, String data) {
                if (isSuccess) {
                    try {
                        List<GroupBuyBanner> list = new Gson ().fromJson (data, new TypeToken<List<GroupBuyBanner>> () {
                        }.getType ());
                        if (mcb != null) mcb.GBannerList (convert2Banner (list));
                    } catch (Exception e) {
                        if (mcb != null) mcb.GBannerError ("-1");
                    }
                } else {
                    if (mcb != null) mcb.GBannerError (data);
                }
            }
        });
    }

    public void getbanner () {
        String url = BaseVar.API_GET_GRP_BUY_BANNER;
        execute (HttpRequest.HttpMethod.GET, url, null);
    }

    private GroupBuyBannerModelCallBack mcb;

    public void setModelCallBack (GroupBuyBannerModelCallBack cb) {
        this.mcb = cb;
    }

    public interface GroupBuyBannerModelCallBack {
        void GBannerList (List<Banner> lst);

        void GBannerError (String errcode);
    }

    private List<Banner> convert2Banner (List<GroupBuyBanner> lst) {
        if (lst != null && lst.size () > 0) {
            List<Banner> blist = new ArrayList<> ();
            for (int i = 0; i < lst.size (); i++) {
                Banner b = new Banner ();
                b.index = i;
                b.slide_id = i + "";
                b.slide_title = i + "";
                b.image_url = lst.get (i).img;
                b.link_url = lst.get (i).url;

                blist.add (b);
            }
            return blist;
        } else {
            return null;
        }
    }
}
