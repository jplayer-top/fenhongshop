package com.fanglin.dutyfree;


import com.fanglin.fhlib.other.TLSSocketFactory;

import org.xutils.http.RequestParams;

/**
 * 青岛芳林信息
 * Created by Plucky on 2017/1/4.
 * 功能描述:基础请求
 */

public class FHRequestParams extends RequestParams {

    public FHRequestParams(String uri) {
        super(uri);
        TLSSocketFactory factory;
        try {
            factory = new TLSSocketFactory();
        } catch (Exception e) {
            factory = null;
        }
        setSslSocketFactory(factory);
        setCacheMaxAge(1000);
        setConnectTimeout(20000);
    }
}
