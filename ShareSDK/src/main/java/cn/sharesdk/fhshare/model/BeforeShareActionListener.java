package cn.sharesdk.fhshare.model;

/**
 * 分享之前的回调
 * Created by Plucky on 2015/12/17.
 */
public interface BeforeShareActionListener {
    void onbeforeShare(String platform, FHShareData shareData);
}
