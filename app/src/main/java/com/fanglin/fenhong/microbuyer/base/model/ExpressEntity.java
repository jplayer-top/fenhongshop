package com.fanglin.fenhong.microbuyer.base.model;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 快递公司实体类
 * Created by admin on 2015/11/24.
 */
public class ExpressEntity {

    @Id
    private String express_id;//作为DB的主键
    private String express_name;
    private String express_code;

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public String getExpress_code() {
        return express_code;
    }

    public void setExpress_code(String express_code) {
        this.express_code = express_code;
    }
}
