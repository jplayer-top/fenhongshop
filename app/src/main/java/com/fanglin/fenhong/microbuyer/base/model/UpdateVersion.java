package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 作者： Created by Plucky on 15-10-31.
 * App 更新管理模块
 */
public class UpdateVersion {
    public String version_id;// 版本id
    public int version_code;// 版本代号
    public String version_name;// 版本名称
    public String app_name;// 应用名称
    public String app_url;// 应用下载链接
    public String images_url;// 应用预览图（多张用逗号隔开）
    public String app_intro;// 应用介绍
    public String update_log;// 更新日志
    public long add_time;// 版本时间
    public int down_count;// 下载次数
    public String app_platofrm;// 系统平台 （1安卓2苹果）
    public String if_compulsory;// 是否强制更新 （1 是 0 否）

    public static UpdateVersion getTestData() {
        UpdateVersion uv = new UpdateVersion();
        uv.update_log = "1、优化奖金分享记录跟踪\n2、性能优化\n3、新增附近的微店";
        uv.version_name = "2.0";
        uv.version_code = 336;
        uv.app_url = "http://app.fenhongshop.com/version/20150619/5583ee4e565a1.apk";
        return uv;
    }
}
