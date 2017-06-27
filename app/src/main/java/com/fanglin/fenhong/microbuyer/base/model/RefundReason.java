package com.fanglin.fenhong.microbuyer.base.model;

/**
 * 退款原因 实体类
 * Created by admin on 2015/11/13.
 */
public class RefundReason {

    private String reason_id;
    private String reason_info;
    private String sort;
    private String update_time;

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason_info() {
        return reason_info;
    }

    public void setReason_info(String reason_info) {
        this.reason_info = reason_info;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
