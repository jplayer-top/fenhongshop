package com.fanglin.fenhong.microbuyer.base.model;

import com.fanglin.fenhong.microbuyer.base.baselib.APIUtil;
import com.fanglin.fenhong.microbuyer.base.baselib.BaseVar;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 我的团队列表请求webservice
 * Created by admin on 2015/11/5.
 */
public class WSTeam extends APIUtil {

    public ArrayList<Team> team_list;
    public int all;//总团队人数
    public int verify_count;//已认证数量
    public int first_count;//一级团队总数
    public int second_count;//二级团队总数
    public int third_count;//三级团队总数
    public double first_money;//一级团队的总奖金额
    public double second_money;//二级团队的总奖金额
    public double third_money;//三级团队的总金额

    private WSTeamModelCallBack mcb;

    public WSTeam() {
        super();
        setCallBack(new FHAPICallBack() {
            @Override
            public void onStart(String data) {

            }

            @Override
            public void onEnd(boolean isSuccess, String data) {

                if (isSuccess) {
                    try {
                        WSTeam adata = new Gson().fromJson(data, WSTeam.class);
                        if (mcb != null) mcb.onWSTeamSuccess(adata);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (mcb != null) mcb.onWSTeamError("-1");
                    }
                } else {
                    if (mcb != null) mcb.onWSTeamError(data);
                }
            }
        });
    }

    public void getList(Member m, int start, int type) {
        if (m == null) {
            if (mcb != null) mcb.onWSTeamError("-1");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        params.addBodyParameter("type", String.valueOf(type));//团队类型   0：全部    1：B级团队      2：C级团队
        params.addBodyParameter("start", String.valueOf(start));
        params.addBodyParameter("num", String.valueOf(BaseVar.REQUESTNUM));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_TEAM, params);
    }

    public void setWSTeamModelCallBack(WSTeamModelCallBack mcb) {
        this.mcb = mcb;
    }

    public interface WSTeamModelCallBack {
        void onWSTeamError(String errcode);

        void onWSTeamSuccess(WSTeam data);
    }

    public int getPeopleCount(int type) {
        int res;
        if (type == 0) {
            res = all;
        } else if (type == 1) {
            res = first_count;
        } else if (type == 2) {
            res = second_count;
        } else {
            res = third_count;
        }
        return res;
    }

    public double getCommissionCount(int type) {
        double res;
        if (type == 0) {
            res = first_money + second_money + third_money;
        } else if (type == 1) {
            res = first_money;
        } else if (type == 2) {
            res = second_money;
        } else {
            res = third_money;
        }
        return res;
    }

    public static String getLevelDsc(int type) {
        switch (type) {
            case 0:
                return "全部";
            case 1:
                return "一级";
            case 2:
                return "二级";
            case 3:
                return "三级";
            default:
                return "";
        }
    }

    public String getVerifyCountDesc() {
        return "已认证：" + verify_count + "人";
    }

    public static String getLevelNumLbl(int type) {
        return getLevelDsc(type) + "成员人数:";
    }
}
