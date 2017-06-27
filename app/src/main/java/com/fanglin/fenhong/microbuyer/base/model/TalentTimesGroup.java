package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/6/22-上午11:58.
 * 功能描述: 达人时光按月分组
 */
public class TalentTimesGroup extends APIUtil {
    private String tDay;//日
    private String tMonth;//月份
    private String tYear;//年份
    private List<TalentImagesDetail> times;//时光

    public TalentTimesGroup() {
        //析构时初始化数组
        times = new ArrayList<>();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {
                List<TalentImagesDetail> list;
                if (isSuccess) {
                    try {
                        list = new Gson().fromJson(data, new TypeToken<List<TalentImagesDetail>>() {
                        }.getType());
                    } catch (Exception e) {
                        list = null;
                    }
                } else {
                    list = null;
                }

                if (callBack != null) {
                    callBack.onTalentTimesList(list);
                }
            }
        });
    }

    /**
     * 获取达人时光列表 (post)
     *
     * @param member    mid （可选，登录必传） token （可选，登录必传）
     * @param talent_id 达人id
     * @param curpage   当前页/第几次请求 num 分页数量/每次请求数量
     */
    public void getList(Member member, String talent_id, int curpage) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("talent_id", talent_id);
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM_10));
        params.addBodyParameter("curpage", String.valueOf(curpage));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_TALENT_TIME_LIST, params);
    }


    /**
     * CallBack
     */
    public interface TalentTimesGroupCallBack {
        void onTalentTimesList(List<TalentImagesDetail> list);
    }

    private TalentTimesGroupCallBack callBack;

    public void setModelCallBack(TalentTimesGroupCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * getter&setter&handle the array
     */

    public String gettDay() {
        return tDay;
    }

    public void settDay(String tDay) {
        this.tDay = tDay;
    }

    public String gettMonth() {
        return tMonth;
    }

    public String gettMonthZh() {
        return tMonth + "月";
    }

    public void settMonth(String tMonth) {
        this.tMonth = tMonth;
    }

    public String gettYear() {
        return tYear;
    }

    public void settYear(String tYear) {
        this.tYear = tYear;
    }

    public List<TalentImagesDetail> getTimes() {
        return times;
    }

    public static List<TalentTimesGroup> parseTimesArrayByDay(List<TalentImagesDetail> list) {
        List<TalentTimesGroup> timesGroup = new ArrayList<>();
        if (list != null && list.size() > 0) {
            String lastMonth = null, lastYear = null, lastDay = null;
            for (TalentImagesDetail item : list) {
                /**
                 * 按年月分组  如果是同一组 则加入分组中
                 */

                if (!TextUtils.equals(item.gettYear(), lastYear) || !TextUtils.equals(item.gettMonth(), lastMonth) || !TextUtils.equals(item.gettDay(), lastDay)) {
                    /**
                     * 只要年月不同,则创建一个分组
                     */
                    TalentTimesGroup grp = new TalentTimesGroup();
                    lastMonth = item.gettMonth();
                    lastYear = item.gettYear();
                    lastDay = item.gettDay();

                    grp.settYear(lastYear);
                    grp.settMonth(lastMonth);
                    grp.settDay(lastDay);
                    grp.getTimes().add(item);
                    timesGroup.add(grp);
                } else {
                    /**
                     * 如果年月相同 则添加进上一个数组中
                     */
                    if (timesGroup.size() > 0) {
                        int lastIndex = timesGroup.size() - 1;
                        TalentTimesGroup aGrp = timesGroup.get(lastIndex);
                        aGrp.getTimes().add(item);
                    }
                }
            }
        }
        return timesGroup;
    }
}
