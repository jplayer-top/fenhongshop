package com.wxpay;

public class WXConstants {

    public String APP_ID = "A";

    // 商户号
    public String MCH_ID = "B";

    // API密钥，在商户平台设置
    public String API_KEY = "C";

    private static final String NOTIFY_URL_AFF = "/api/payment/wxpay/notify_url.php";

    private String NOTIFY_URL;

    public WXConstants (String host) {
        NOTIFY_URL = host + NOTIFY_URL_AFF;
    }

    public String getNOTIFY_URL () {
        return NOTIFY_URL;
    }
}
