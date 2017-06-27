package com.wxpay;

public class WxOrderEntity {
    public String subject;
    private String body;
    public String price;
    public String tradeno;

    public void setBody(String body)
    {
        this.body=body;
    }
    public String getBody()
    {
        return body;
    }
}
