package com.fanglin.fenhong.microbuyer.base.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： Created by Plucky on 15-9-27.
 * 快递100数据模型
 */
public class Express100 {
    public String nu;// 	物流单号
    public String com;// 	物流公司编号
    public String status;// 查询结果状态 (0：物流单暂无结果， 1：查询成功， 2：接口出现异常)
    public String message;//物流消息
    public List<ExpData> data;
    public String state;//快递单当前的状态;

    public String getStateDesc() {
        String res = "";

        int state_temp = -1;
        if (!TextUtils.isEmpty(this.state) && TextUtils.isDigitsOnly(this.state)) {
            state_temp = Integer.parseInt(this.state);
        }

        switch (state_temp) {
            case -1:
                res = "卖家正通知快递公司揽件";
                break;
            case 0:
                res = "货物处于运输过程中";
                break;
            case 1:
                res = "货物已由快递公司揽收";
                break;
            case 2:
                res = "货物寄送过程出了问题";
                break;
            case 3:
                res = "收件人已签收";
                break;
            case 4:
                res = "货物货品已签收";
                break;
            case 5:
                res = "快递员正在派件";
                break;
            case 6:
                res = "货品退回中";
                break;
        }
        if (TextUtils.isEmpty(res)) {
            res = message;
        }
        return res;
    }
    
    /**　
     0：在途，即货物处于运输过程中；
     1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
     2：疑难，货物寄送过程出了问题；
     3：签收，收件人已签收；
     4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
     5：派件，即快递正在进行同城派件；
     6：退回，货物正处于退回发件人的途中；*/


    public Express100 getTest() {
        Express100 e = new Express100();
        e.nu = "s";
        e.com = "s";
        e.status = "s";
        e.message = "s";

        e.data = new ArrayList<>();

        ExpData data = new ExpData();
        data.time = "20125622";
        data.context = "sddfsdfas";
        e.data.add(data);

        data = new ExpData();
        data.time = "20125622";
        data.context = "sddfsdfas";
        e.data.add(data);

        e.state = "0";

        return e;
    }

    /** 每一条信息*/
    public class ExpData {
        public String time;// 	每条跟踪信息的时间
        public String context;// 	每条跟综信息的描述
    }
}


