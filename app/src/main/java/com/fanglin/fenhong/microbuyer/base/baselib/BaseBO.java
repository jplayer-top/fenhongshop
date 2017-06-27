package com.fanglin.fenhong.microbuyer.base.baselib;

import android.text.TextUtils;

import com.fanglin.fenhong.microbuyer.base.model.Address;
import com.fanglin.fenhong.microbuyer.base.model.Member;
import com.fanglin.fhlib.ypyun.api.UpYunUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 基础业务操作类
 * Base Business Operation=BaseBO
 * <p/>
 * Created by Plucky on 2015/7/29.
 */
public class BaseBO extends APIUtil {


    public BaseBO() {
        super();
    }

    /**
     * 复写父类设置回调的方法,让其返回当前实例
     */
    @Override
    public BaseBO setCallBack(FHAPICallBack cb) {
        super.setCallBack(cb);
        return this;
    }

    public BaseBO setNormalRequest(boolean isNormal) {
        this.normalRequest = isNormal;
        return this;
    }


    /**
     * 快递100 请求格式
     */
    public void get_express100(String ecode, String freight_code) {
        String url = String.format(BaseVar.EXPRESS100, ecode, freight_code);
        normalRequest = true;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * API 物流信息请求地址
     */
    public void get_fh_express(String orderId, String mid, String token) {
        String url = BaseVar.API_FH_EXPRESS + "&order_id=" + orderId + "&mid=" + mid + "&token=" + token;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    /**
     * 获取地区级联信息(get)
     * pid 父级分类id (传0或不传返回第一级)
     */
    public void get_area_list(String pid) {
        String url = BaseVar.API_GET_AREA_LIST;
        if (pid != null) {
            url += "&pid=" + pid;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    /**
     * 登录
     */
    public void login(String account, String password) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("account", account);
        params.addBodyParameter("password", UpYunUtils.signature(password));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_LOGIN, params);
    }

    /**
     * 忘记登录密码重置(post)
     * phone 手机号
     * code 手机验证码
     * pwd 新密码
     */
    public void reset_pwd(String phone, String code, String pwd) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("pwd", UpYunUtils.signature(pwd));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_RESET_PWD, params);
    }

    /**
     * 个人中心首页(post)
     * mid 会员id
     * token 登录令牌
     */
    public void ucenter(String mid, String token) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_UCENTER, params);
    }

    /**
     * 获取商品分类(get)
     * pid 父级分类id (传0获取一级分类，传值>0获取某个分类的子分类，不传获取全部分类)
     * area  （0 国内 ；1 海外； 2 全部）
     */
    public void get_goods_class(String pid, String area) {
        String url = BaseVar.API_GET_GOODS_CLS;

        if (pid != null) {
            url += "&pid=" + pid;
        }

        if (area != null) {
            url += "&area=" + area;
        }

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * 获取地区运费(get)
     * area_id    地区id    （从商品详情接口获取）
     * transport_id    运费模板id    （从商品详情接口获取）
     */
    public void get_area_freight(String area_id, int transport_id) {
        String url = BaseVar.API_GET_AREA_FREIGHT;
        if (area_id != null) {
            url += "&area_id=" + area_id;
        }
        url += "&transport_id=" + transport_id;

        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    /**
     * 获取商品评论列表(get)
     * goods_id    商品id
     * num         分页数量/每次请求数量
     * curpage     当前页/第几次请求 （第一页对应数字1）
     * type        评论类型 （可选参数， 1 好评 2 中评 3 差评 4 有图  不传 全部）
     */
    public void get_goods_comments(String goods_id, int curpage, String type) {
        String url = BaseVar.API_GET_GOODS_COMMENTS + "&goods_id=" + goods_id;
        url += "&num=" + BaseVar.REQUESTNUM;
        url += "&curpage=" + curpage;

        if (type != null) {
            url += "&type=" + type;
        }
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    /**
     * 添加浏览历史(post)
     * token 登录令牌
     * mid 会员id
     * id 商品id
     */
    public void add_browse(String token, String mid, String id) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("token", token);
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("id", id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_BROWSE, params);
    }


    /**
     * 删除浏览历史(post)
     * token 登录令牌
     * mid 会员id
     * delall 删除所有浏览历史标志 可选（ 0 默认 | 1 ）
     * id 商品id（删除单条记录 "id" 删除多条记录 "id1,id2,id3",delall=1 时可不传 ）
     */
    public void delete_browse(String token, String mid, String delall, String id) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("token", token);
        params.addBodyParameter("mid", mid);
        if (delall != null) {
            params.addBodyParameter("delall", delall);
        }

        if (id != null) {
            params.addBodyParameter("id", id);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_BROWSE, params);
    }


    /**
     * 添加搜索记录(post)
     * token 登录令牌
     * mid 会员id
     * keywords 搜索关键词
     */
    public void add_search_history(String token, String mid, String keywords) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("token", token);
        params.addBodyParameter("mid", mid);
        if (!TextUtils.isEmpty(keywords)) {
            keywords = keywords.trim();
            params.addBodyParameter("keywords", keywords);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_SEARCH_HISTORY, params);
    }

    /**
     * 获取搜索记录(post)
     * token 登录令牌
     * mid 会员id
     */
    public void get_search_history_list(String token, String mid) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("token", token);
        params.addBodyParameter("mid", mid);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_SEARCH_HISTORY, params);
    }

    /**
     * 获取热搜词(get)
     */
    public void get_hot_search() {
        String url = BaseVar.API_GET_HOT_SEARCH;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    /**
     * 删除搜索历史(post)
     * token 登录令牌
     * mid 会员id
     * delall 删除所有搜索历史 可选（ 0 默认 | 1 ）
     * id 商品id（删除单条记录 "id" 删除多条记录 "id1,id2,id3",delall=1 时可不传 ）
     */
    public void delete_search_history(String token, String mid, String delall, String id) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("token", token);
        params.addBodyParameter("mid", mid);
        if (delall != null) {
            params.addBodyParameter("delall", delall);
        }

        if (id != null) {
            params.addBodyParameter("id", id);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_SEARCH_HISTORY, params);
    }


    /**
     * 获取购物车商品列表(post)
     * mid : 用户id
     * token:用户令牌
     * type: 类型， 值为 db，cookie
     * gc_area: 0 国内， 1 国外
     */
    public void get_cart_list(String mid, String token, int gc_area) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("type", "db");
        params.addBodyParameter("gc_area", String.valueOf(gc_area));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_CART_LIST, params);
    }


    /**
     * 删除购物车中的商品(post)
     * cart_id : 购物车id
     * mid：用户id
     * token :  登陆令牌
     */
    public void delete_cart(String cart_id, String mid, String token, int gc_area) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("gc_area", String.valueOf(gc_area));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DEL_CART, params);
    }

    /**
     * 更新购物车中商品的数量(post)
     * token :  登陆令牌
     * cart_id : 购物车id
     * quantity : 更新数量
     * mid : 用户id
     */
    public void update_cart(String mid, String token, String cart_id, int gc_area, int quantity) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("gc_area", String.valueOf(gc_area));
        params.addBodyParameter("quantity", String.valueOf(quantity));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_UPDATE_CART, params);
    }

    /**
     * 添加商品到购物车(post)
     * token：用户令牌
     * mid : 用户id
     * goods_id : 商品id 或者 bl_id : 优惠套装id
     * store_id :  会员商铺id  // 可选
     * quantity : 商品数量
     * gc_area: 0 国内， 1 国外 (需要添加Global)
     * bundling_id: 优惠套装的Id
     * resource_tags 统计数据，透明传输
     */
    public void add_cart(String mid, String token, String goods_id, String store_id, int quantity, int gc_area, String bundling_id, String resource_tags) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);

        if (!TextUtils.isEmpty(bundling_id)) {
            //如果有优惠套装ID,则优先使用
            params.addBodyParameter("bl_id", bundling_id);
        } else {
            params.addBodyParameter("goods_id", goods_id);
        }

        if (!TextUtils.isEmpty(store_id)) {
            params.addBodyParameter("store_id", store_id);
        }

        params.addBodyParameter("quantity", String.valueOf(quantity));
        params.addBodyParameter("gc_area", String.valueOf(gc_area));
        params.addBodyParameter("resource_tags", resource_tags);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_CART, params);
    }

    /**
     * 核对购物车信息(post)
     * mid    会员id
     * token    登录令牌
     * cart_info    购物信息    （字符串格式：商品id1|数量,商品id2|数量,...)
     * address_id    收货地址id （选择其他收货地址时，用来重新计算运费，首次调用该接口不需要传）
     * member_store_id    如果会员有店铺id则传递，没有则不传
     * if_cart    是否来自购物车 （1 来自购物车  0 来自立即购买）
     */
    public void check_cart(String mid, String token, String cart_info, String member_store_id, int if_cart, String address_id, int area) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("cart_info", cart_info);

        if (member_store_id != null) {
            params.addBodyParameter("member_store_id", member_store_id);
        }

        if (address_id != null) {
            params.addBodyParameter("address_id", address_id);
        }

        params.addBodyParameter("if_cart", String.valueOf(if_cart));
        params.addBodyParameter("area", String.valueOf(area));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CHECK_CART, params);
    }


    /**
     * 获取收货地址(post)
     * mid    会员id
     * address_id    收货地址id（可选，不传则返回会员所有地址）
     * token :  登陆令牌
     */
    public void get_address(String address_id, String mid, String token) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        if (address_id != null) {
            params.addBodyParameter("address_id", address_id);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_ADDRESS, params);
    }

    /**
     * 设置收货地址(post)
     * 如果只传address_id,mid,is_default字段，则表示要将收货地址设置为默认，否则下列字段需要全部提交才能进行新增或更新
     * <p/>
     * address_id 地址id （传为修改，不传为新增）
     * mid 会员id
     * token 令牌
     * <p/>
     * name    收货人姓名
     * cert_name    身份证姓名 （国内订单选填）
     * cert_num    身份证号码 （国内订单选填）
     * area_id 区县id
     * city_id 城市id
     * area_info 地区信息
     * address 街道信息
     * mobile 手机号
     * is_default 是否默认收货地址
     */
    public void set_address(String mid, String token, Address addr) {
        if (addr == null) return;
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);

        if (addr.address != null) {
            params.addBodyParameter("address", addr.address);
        }
        if (addr.address_id != null) {
            params.addBodyParameter("address_id", addr.address_id);
        }
        if (addr.area_id != null) {
            params.addBodyParameter("area_id", addr.area_id);
        }
        if (addr.area_info != null) {
            params.addBodyParameter("area_info", addr.area_info);
        }
        if (addr.cert_name != null) {
            params.addBodyParameter("cert_name", addr.cert_name);
        }
        if (addr.cert_num != null) {
            params.addBodyParameter("cert_num", addr.cert_num);
        }
        if (addr.city_id != null) {
            params.addBodyParameter("city_id", addr.city_id);
        }
        if (addr.is_default != null) {
            params.addBodyParameter("is_default", addr.is_default);
        }
        if (addr.mob_phone != null) {
            params.addBodyParameter("mob_phone", addr.mob_phone);
        }

        if (addr.mobile != null) {
            params.addBodyParameter("mobile", addr.mobile);
        }

        if (addr.name != null) {
            params.addBodyParameter("name", addr.name);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_SET_ADDRESS, params);
    }

    /**
     * 删除收货地址(post)
     * mid    会员id
     * address_id 地址id
     * token :  登陆令牌
     */
    public void del_address(String address_id, String mid, String token) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("address_id", address_id);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DEL_ADDRESS, params);
    }

    /**
     * 提交订单(post)
     * mid    会员id
     * token    登录令牌
     * ifcart    是否来自购物车（1 来自购物车   0 来自立即购买）
     * address_id    收货地址id
     * cart_info    商品信息字符串  "商品id|数量,商品id|数量,..." （从核对购物车接口获取）
     * vat_hash    加密字段1 （来自核对购物车信息接口）
     * offpay_hash    加密字段2 （来自核对购物车信息接口）
     * offpay_hash_batch    加密字段3 （来自核对购物车信息接口）
     * coupon_id    使用的现金券id （可选）
     * pay_pwd    支付密码 （使用余额时传递）
     * resource_tags  统计用 -- lizhixin
     */
    public void submit(String mid,
                       String token,
                       String address_id,
                       int if_cart,
                       String cart_info,
                       String vat_hash,
                       String offpay_hash,
                       String offpay_hash_batch,
                       int area,
                       String coupon_id,
                       String pay_pwd,
                       String seckilling_goods,
                       String resource_tags) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("address_id", address_id);
        params.addBodyParameter("if_cart", String.valueOf(if_cart));
        params.addBodyParameter("cart_info", cart_info);
        params.addBodyParameter("area", String.valueOf(area));

        if (!TextUtils.isEmpty(vat_hash)) {
            params.addBodyParameter("vat_hash", vat_hash);
        }
        if (!TextUtils.isEmpty(offpay_hash)) {
            params.addBodyParameter("offpay_hash", offpay_hash);
        }

        if (!TextUtils.isEmpty(offpay_hash_batch)) {
            params.addBodyParameter("offpay_hash_batch", offpay_hash_batch);
        }

        if (!TextUtils.isEmpty(coupon_id)) {
            params.addBodyParameter("coupon_id", coupon_id);
        }

        if (!TextUtils.isEmpty(pay_pwd)) {
            params.addBodyParameter("pay_pwd", pay_pwd);
            params.addBodyParameter("pd_pay", "1");
        }

        params.addBodyParameter("seckilling_goods", seckilling_goods);
        params.addBodyParameter("resource_tags", resource_tags);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_SUBMIT, params);
    }

    /**
     * 取消订单(post)
     * mid    会员id
     * token :  登陆令牌
     * order_id 订单id
     * msg 取消原因
     */
    public void cancle_order(String mid, String token, String order_id, String msg) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("msg", msg);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CANCEL_ORDER, params);
    }

    /**
     * 删除订单(放入回收站)(post)
     * mid    会员id
     * token :  登陆令牌
     * order_id 订单id
     */
    public void delete_order(String mid, String token, String order_id) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("order_id", order_id);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_ORDER, params);
    }

    /**
     * 获取订单详情(post)
     * mid    会员id
     * token :  登陆令牌
     * order_id 订单id
     */
    public void get_order_detail(String mid, String token, String order_id) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("order_id", order_id);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ORDER_DTL, params);
    }

    public void get_cart_num(String mid, String token) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_CART_NUM, params);
    }

    /**
     * 商家入驻 线下支付(post)
     * mid    会员id
     * token :  登陆令牌
     * pay_certificate 付款凭证
     * pay_certificate_explain 付款凭证说明
     */
    public void pay_offline(String mid, String token, String pay_certificate, String pay_certificate_explain) {
        RequestParams params = new RequestParams();

        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("pay_certificate", pay_certificate);
        if (pay_certificate_explain != null)
            params.addBodyParameter("pay_certificate_explain", pay_certificate_explain);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_STORE_PAY_OFFLINE, params);
    }

    public void set_paypwd(String mid, String token, String paypwd_new, String paypwd_confirm, String code, String mobile) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("paypwd_new", UpYunUtils.signature(paypwd_new));//MD5加密
        params.addBodyParameter("paypwd_confirm", UpYunUtils.signature(paypwd_confirm));//MD5加密
        params.addBodyParameter("code", code);
        params.addBodyParameter("mobile", mobile);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_SETPAYPWD, params);
    }


    // mid 会员id
    // paypwd 支付密码（md5）
    // token 登录令牌
    public void check_paypwd(Member m, String paypwd) {
        if (m == null) return;
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        params.addBodyParameter("paypwd", UpYunUtils.signature(paypwd));//MD5加密
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CHECK_PAYPWD, params);
    }


    public void withdraw_cash(String mid, String token, String card_id, double amount, String password) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("card_id", card_id);
        params.addBodyParameter("amount", String.valueOf(amount));
        params.addBodyParameter("password", UpYunUtils.signature(password));//MD5加密
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_WITH_DRAW_CASH, params);
    }

    public void getLastVersion() {
        String url = BaseVar.API_GET_LAST_VERSION + "&app=%E5%88%86%E7%BA%A2%E5%85%A8%E7%90%83%E8%B4%AD";//分红全球购
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public void crashReport(Member member, String appinfo, String crashdata) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("appinfo", appinfo);
        params.addBodyParameter("content", crashdata);
        params.addBodyParameter("contact", member != null ? member.member_name : "未登录用户");
        params.addBodyParameter("mobile", member != null ? member.member_mobile : "无");
        params.addBodyParameter("mid", member != null ? member.member_id : "无");

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CRASH_REPORT, params);
    }

    /**
     * 获取银联支付码
     */
    public void genUnionPayTn(String mid, String token, String order_paysn, double order_amount) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("order_paysn", order_paysn);
        params.addBodyParameter("order_amount", String.valueOf(order_amount));

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GET_TN, params);
    }

    /**
     * 添加商品到货/预售提醒
     *
     * @param mid     mid
     * @param token   token
     * @param goodsId goodsId
     * @param type    可选  通知类型 (1: 到货提醒 默认 | 2: 预售提醒)
     */
    public void addArrivalNotice(String mid, String token, String goodsId, String type) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("type", type == null ? "1" : type);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_ARRIVAL_NOTICE, params);
    }

    /**
     * 取消商品到货/预售提醒
     *
     * @param mid     mid
     * @param token   token
     * @param goodsId goodsId
     * @param type    可选  通知类型 (1: 到货提醒 默认 | 2: 预售提醒)
     */
    public void cancelArrivalNotice(String mid, String token, String goodsId, String type) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("goods_id", goodsId);
        params.addBodyParameter("type", type == null ? "1" : type);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CANCEL_ARRIVAL_NOTICE, params);
    }

    public void if_account_exists(String account) {
        String url = BaseVar.API_IF_ACCOUNT_EXISTS;
        url += "&account=" + account;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }

    public void checkCaptcha(String mobile, String captcha) {
        String url = BaseVar.URL_CHECK_CAPTCHA;
        url += "&mobile=" + mobile;
        url += "&captcha=" + captcha;
        execute(HttpRequest.HttpMethod.GET, url, null);
    }


    /**
     * 更新微店信息
     *
     * @param mid        mid
     * @param token      token
     * @param shop_name  店铺名称
     * @param shop_scope 店铺标语
     * @param shop_logo  店铺头像
     */
    public void setMicroshopInfo(String mid,
                                 String token,
                                 String shop_name,
                                 String shop_scope,
                                 String shop_logo) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("token", token);
        params.addBodyParameter("shop_name", shop_name);
        params.addBodyParameter("shop_scope", shop_scope);
        params.addBodyParameter("shop_logo", shop_logo);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_SET_SHOP_INFO, params);
    }

    /**
     * @param member    mid 会员id  token 登录token
     * @param talent_id talent_id 达人id
     * @param type      type 类型 （可选，0：关注 | 1：取消关注，默认0）
     */
    public void followTalent(Member member, String talent_id, String type) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("talent_id", talent_id);
        params.addBodyParameter("type", type);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_FOLLOW_TALENT, params);
    }

    /**
     * 添加收藏(post)
     *
     * @param member mid 会员id  token 登录token
     * @param fid    收藏对象id（商品：goods_id | 店铺：store_id | 微店：shop_id|时光：time_id）
     * @param type   收藏类型（商品：goods | 店铺：shop | 微店：microshop| 时光：time）
     */
    public void addFavorites(Member member, String fid, String type) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("token", member.token);
            params.addBodyParameter("mid", member.member_id);
        }

        params.addBodyParameter("fid", fid);
        params.addBodyParameter("type", type);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_FAVORITES, params);
    }

    /**
     * 删除收藏(post)
     *
     * @param member mid 会员id  token 登录token
     * @param fid    收藏对象id（商品：goods_id | 店铺：store_id | 微店：shop_id|时光：time_id）
     * @param type   收藏类型（商品：goods | 店铺：shop | 微店：microshop| 时光：time）
     */
    public void deleteFavorites(Member member, String fid, String type) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("token", member.token);
            params.addBodyParameter("mid", member.member_id);
        }
        params.addBodyParameter("fid", fid);
        params.addBodyParameter("type", type);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_FAVORITES, params);
    }

    /**
     * 时光评论点赞 (post)
     *
     * @param member     mid 会员id  token 登录token
     * @param comment_id comment_id 评论id
     */
    public void zan(Member member, String comment_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("comment_id", comment_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_UP_COMMENT, params);
    }

    /**
     * 时光评论取消点赞 (post)
     *
     * @param member     mid 会员id  token 登录token
     * @param comment_id comment_id 评论id
     */
    public void unZan(Member member, String comment_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("comment_id", comment_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CANCEL_UP, params);
    }

    /**
     * @param member     mid 会员id token 登录token
     * @param time_id    time_id 时光id
     * @param comment    comment 评论/回复内容
     * @param comment_id comment_id 评论id（可选， 传值表示回复评论，默认为0）
     */
    public void addComment(Member member, String time_id, String comment, String comment_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("time_id", time_id);
        params.addBodyParameter("comment", comment);
        params.addBodyParameter("comment_id", comment_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_COMMENT, params);
    }

    /**
     * 删除时光标签最近选择 (post)
     *
     * @param member   mid 会员id  token 登录token
     * @param del_all  del_all 是否删除全部 0：否 | 1：是 (可选，默认0)
     * @param tag_name tag_name 标签名 (可选，删除单个)
     */
    public void deleteRecentTags(Member member, String del_all, String tag_name) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("del_all", del_all);
        //决定是否单个删除
        if (!TextUtils.isEmpty(tag_name)) {
            params.addBodyParameter("tag_name", tag_name);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_RECENT_TAGS, params);
    }

    /**
     * 添加时光标签 (post)
     *
     * @param member   mid 会员id  token
     * @param tag_name 标签名称
     */
    public void addTag(Member member, String tag_name) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("tag_name", tag_name);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_TAG, params);
    }

    /**
     * 发表时光 (post)
     *
     * @param member  mid 会员id token
     * @param content 时光文章内容
     * @param gps     定位信息（json格式）
     * @param tags    时光标签（json格式）
     * @param images  时光图片（json格式）
     * @param goods   关联商品 （json格式）
     */
    public void addTime(Member member, String content, String gps, String tags, String images, String goods) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        if (!TextUtils.isEmpty(content)) {
            params.addBodyParameter("content", content);
        }
        if (!TextUtils.isEmpty(gps)) {
            params.addBodyParameter("gps", gps);
        }
        if (!TextUtils.isEmpty(tags)) {
            params.addBodyParameter("tags", tags);
        }
        if (!TextUtils.isEmpty(images)) {
            params.addBodyParameter("images", images);
        }
        if (!TextUtils.isEmpty(goods)) {
            params.addBodyParameter("goods", goods);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_ADD_TIME, params);
    }

    /**
     * 删除时光 (post)
     *
     * @param member  mid 会员id  token 登录token
     * @param time_id 时光id
     */
    public void deleteTime(Member member, String time_id) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("time_id", time_id);
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_DELETE_TIME, params);
    }

    /**
     * 修改达人信息 (post)
     *
     * @param member     mid 会员id token 登录token
     * @param avatar     达人头像 （可选）
     * @param background 达人背景图片 （可选）
     * @param name       达人名称（可选）
     * @param intro      达人简介 （可选）
     */
    public void changeTalentInfo(Member member, String avatar, String background, String name, String intro) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        if (!TextUtils.isEmpty(avatar)) {
            params.addBodyParameter("avatar", avatar);
        }
        if (!TextUtils.isEmpty(background)) {
            params.addBodyParameter("background", background);
        }
        if (!TextUtils.isEmpty(name)) {
            params.addBodyParameter("name", name);
        }
        if (!TextUtils.isEmpty(intro)) {
            params.addBodyParameter("intro", intro);
        }
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_CHANGE_TALENT_INFO, params);

    }

    public void jdPay(Member m, String pay_sn) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("mid", m.member_id);
        params.addBodyParameter("token", m.token);
        params.addBodyParameter("payment_code", "jdpay");
        params.addBodyParameter("pay_sn", pay_sn);

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_JDPAY, params);
    }

    public void applyShoper(Member member) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }

        execute(HttpRequest.HttpMethod.POST, BaseVar.API_APPLY_SHOPER, params);
    }

    /**
     * 服务端加密阿里支付信息
     *
     * @param member   Member
     * @param pay_sn   支付单号
     * @param pay_type 10季卡  20年卡
     */
    public void getAlipayInfo(Member member, String pay_sn, int pay_type) {
        RequestParams params = new RequestParams();
        if (member != null) {
            params.addBodyParameter("mid", member.member_id);
            params.addBodyParameter("token", member.token);
        }
        params.addBodyParameter("pay_sn", pay_sn);
        params.addBodyParameter("pay_type", String.valueOf(pay_type));
        execute(HttpRequest.HttpMethod.POST, BaseVar.API_GETALIPAYINFO, params);
    }
}
