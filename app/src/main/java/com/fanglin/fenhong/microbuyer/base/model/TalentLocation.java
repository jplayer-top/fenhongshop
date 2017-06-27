package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.mapandlocate.baiduloc.FHLocation;
import com.google.gson.Gson;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/20-下午10:32.
 * 功能描述:
 */
public class TalentLocation {
    public double x;//37.568809, 纬度
    public double y;//121.339006, 经度
    public String city;//青岛市,
    public String county;//市南区,
    public String address;//在青岛软件园7号楼

    public static String getJsonStrByFHLocation(FHLocation fhLocation) {
        if (fhLocation != null) {
            TalentLocation talentLocation = new TalentLocation();
            talentLocation.x = fhLocation.longitude;
            talentLocation.y = fhLocation.latitude;
            talentLocation.city = fhLocation.city;
            talentLocation.county = fhLocation.street;
            talentLocation.address = fhLocation.address;
            return new Gson().toJson(talentLocation);
        }
        return null;
    }
}
